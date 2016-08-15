package manoj.jek.go.com.contactsdemo.ui.activities;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import manoj.jek.go.com.contactsdemo.R;

public class NewContactActivity extends AppCompatActivity {

    @BindView(R.id.new_contact_pic)
    ImageView _pictureView;
    @BindView(R.id.new_contact_pic_layout)
    View _pictureLayout;
    @BindView(R.id.new_contact_first_name)
    TextView _firstNameView;
    @BindView(R.id.new_contact_last_name)
    TextView _lastNameView;
    @BindView(R.id.new_contact_email)
    TextView _emailView;
    @BindView(R.id.new_contact_number)
    TextView _phoneView;
    @BindView(R.id.new_contact_save)
    Button _saveButton;

    private enum PHOTO_OPTIONS {
        CAMERA, GALLERY;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_contact);
        ButterKnife.bind(this);
        setListeners();
    }

    private void setListeners(){
        addPictureListener();
        addSaveButtonListener();
    }

    private void addPictureListener() {
        _pictureLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImageChooserDialog();
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

    private void showImageChooserDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final CharSequence [] arr = new CharSequence[] {PHOTO_OPTIONS.CAMERA.name(), PHOTO_OPTIONS.GALLERY.name()};
        builder.setItems(arr, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                firePhotoPickerIntent(arr[i].toString());
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setTitle("Upload image");
        dialog.setMessage("Take an image or choose an image from the gallery");
    }

    private void firePhotoPickerIntent(String optionName) {
        PHOTO_OPTIONS option = PHOTO_OPTIONS.valueOf(optionName);
        if(option.equals(PHOTO_OPTIONS.CAMERA)) {

        } else {

        }
    }

    private boolean validate() {
        return false;
    }

    private void saveContact() {

    }



}
