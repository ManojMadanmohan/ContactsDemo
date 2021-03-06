package manoj.jek.go.com.contactsdemo.ui.activities;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;

import butterknife.BindView;
import butterknife.ButterKnife;
import manoj.jek.go.com.contactsdemo.R;
import manoj.jek.go.com.contactsdemo.ui.features.ContactsFeature;
import manoj.jek.go.com.contactsdemo.ui.models.Contact;
import manoj.jek.go.com.contactsdemo.ui.network.Utils;
import rx.Single;
import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class NewContactActivity extends AppCompatActivity {

    @BindView(R.id.new_contact_pic)
    ImageView _pictureView;
    @BindView(R.id.new_contact_pic_layout)
    View _pictureLayout;
    @BindView(R.id.new_contact_first_name)
    TextInputLayout _firstNameView;
    @BindView(R.id.new_contact_last_name)
    TextInputLayout _lastNameView;
    @BindView(R.id.new_contact_email)
    TextInputLayout _emailView;
    @BindView(R.id.new_contact_number)
    TextInputLayout _phoneView;
    @BindView(R.id.new_contact_save)
    Button _saveButton;
    @BindView(R.id.error_view)
    View _errorView;
    @BindView(R.id.retry)
    Button _retryButton;
    @BindView(R.id.new_contact_layout)
    View _contactLayout;

    private Uri _imageUri;
    private Bitmap _bitmap;

    private String _firstName;
    private String _lastName;
    private String _email;
    private String _phoneNumber;

    private static final int PICTURE_REQUEST_CODE = 101;

    private enum PHOTO_OPTIONS {
        CAMERA, GALLERY;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_contact);
        ButterKnife.bind(this);
        setListeners();
        setActionBar();
    }

    private void setActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setListeners(){
        addPictureListener();
        addSaveButtonListener();
    }

    private void addPictureListener() {
        _pictureLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImageIntent();
            }
        });
    }

    private void addSaveButtonListener() {
        _saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()) {
                    saveContact();
                }
            }
        });
    }

    private void openImageIntent() {

        // Determine Uri of camera image to save.
        setTempPicOutputUri();

        // Camera.
        final List<Intent> cameraIntents = new ArrayList<Intent>();
        final Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        final PackageManager packageManager = getPackageManager();
        final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for(ResolveInfo res : listCam) {
            final String packageName = res.activityInfo.packageName;
            final Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(packageName);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, _imageUri);
            cameraIntents.add(intent);
        }

        // Filesystem.
        final Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

        // Chooser of filesystem options.
        final Intent chooserIntent = Intent.createChooser(galleryIntent, "Select Source");

        // Add the camera options.
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[cameraIntents.size()]));
        startActivityForResult(chooserIntent, PICTURE_REQUEST_CODE);
    }

    private void setTempPicOutputUri() {
        final File root = new File(Environment.getExternalStorageDirectory() + File.separator + "MyDir" + File.separator);
        root.mkdirs();
        final String fname = "temp"+ UUID.randomUUID().toString().substring(0,4)+".jpg";
        final File sdImageMainDirectory = new File(root, fname);
        _imageUri = Uri.fromFile(sdImageMainDirectory);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICTURE_REQUEST_CODE) {
            final boolean isCamera;
            if (data == null) {
                isCamera = true;
            } else {
                final String action = data.getAction();
                if (action == null) {
                    isCamera = false;
                } else {
                    isCamera = action.equals(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                }
            }
            Uri currentUri = _imageUri;
            if (!isCamera) {
                currentUri = data.getData();
            }
            setTempPicOutputUri();
            Crop.of(currentUri, _imageUri).asSquare().start(this);
        } else if (requestCode == Crop.REQUEST_CROP && resultCode == RESULT_OK) {
            loadImage();
        }
    }

    private void loadImage() {
        Glide.with(this).load(_imageUri).asBitmap().into(new BitmapImageViewTarget(_pictureView) {
            @Override
            protected void setResource(Bitmap resource) {
                _bitmap = resource;
                super.setResource(resource);
            }
        });
    }

    private boolean validate() {
        setFeilds();
        return validateFirstName() &&
                validateLastName() &&
                validatePhone() &&
                validatePicture();
    }

    private boolean validatePicture() {
        if(_bitmap == null) {
            Toast.makeText(this,"Please upload a picture",Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void setFeilds() {
        _firstName = _firstNameView.getEditText().getText().toString();
        _lastName = _lastNameView.getEditText().getText().toString();
        _phoneNumber = _phoneView.getEditText().getText().toString();
        _email = _emailView.getEditText().getText().toString();
        if(_email == null) { _email = ""; }
    }

    private boolean validateFirstName() {
        if(_firstName == null || _firstName.length() < 3) {
            _firstNameView.setError("Please set proper first name > 3 charecters");
            return false;
        } else {
            _firstNameView.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateLastName() {
        if(_lastName == null || _lastName.length() < 3) {
            _lastNameView.setError("Please set proper last name > 3 charecters");
            return false;
        } else {
            _lastNameView.setErrorEnabled(false);
            return true;
        }
    }


    private boolean validatePhone() {
        if(!isValidPhoneNumber()) {
            _phoneView.setError("Please set proper phone number");
            return false;
        } else {
            _phoneView.setErrorEnabled(false);
            return true;
        }
    }

    private boolean isValidPhoneNumber() {
        //In PROD, construct and use a valid Regex after consultation from a domain expert who can explain the kind of phone numbers expected
        if(_phoneNumber == null || _phoneNumber.length() < 10 || _phoneNumber.length() > 14) {
            return false;
        }
        String regexStr = "^[0-9\\-\\+]*$";
        if(!_phoneNumber.matches(regexStr)) {
            return false;
        }
        return true;
    }

    private void saveContact() {
        Toast.makeText(this, "Make call to save this contact!", Toast.LENGTH_LONG).show();
        final Contact contact = new Contact((int)(Math.random()*1000), _firstName, _lastName, _email, _phoneNumber, "http://google.com", false);
        Single<Contact> single = Single.fromCallable(new Callable<Contact>() {

            @Override
            public Contact call() throws Exception {
                Contact finalContact = ContactsFeature.getInstance().addContact(contact);
                return finalContact;
            }
        });
        _contactLayout.setVisibility(View.GONE);
        findViewById(R.id.progress_wheel).setVisibility(View.VISIBLE);
        single.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<Contact>() {
                    @Override
                    public void onSuccess(Contact value) {
                        Toast.makeText(NewContactActivity.this, "Successfully uploaded!", Toast.LENGTH_LONG).show();
                        findViewById(R.id.progress_wheel).setVisibility(View.GONE);
                        ContactInfoActivity.launch(value, NewContactActivity.this, _pictureView, _firstNameView.getEditText());
                        finish();
                    }

                    @Override
                    public void onError(Throwable error) {
                        error.printStackTrace();
                        findViewById(R.id.progress_wheel).setVisibility(View.GONE);
                        showErrorView();
                    }
                });
    }

    private void showErrorView() {
        _errorView.setVisibility(View.VISIBLE);
        _contactLayout.setVisibility(View.GONE);
        _retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _contactLayout.setVisibility(View.VISIBLE);
                _errorView.setVisibility(View.GONE);
            }
        });
    }


}
