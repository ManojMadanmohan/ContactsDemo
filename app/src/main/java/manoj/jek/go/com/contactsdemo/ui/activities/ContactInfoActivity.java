package manoj.jek.go.com.contactsdemo.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.bumptech.glide.Glide;

import java.util.concurrent.Callable;

import butterknife.BindView;
import butterknife.ButterKnife;
import manoj.jek.go.com.contactsdemo.R;
import manoj.jek.go.com.contactsdemo.ui.features.ContactsFeature;
import manoj.jek.go.com.contactsdemo.ui.models.Contact;
import manoj.jek.go.com.contactsdemo.ui.network.Utils;
import rx.Observable;
import rx.Single;
import rx.SingleSubscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ContactInfoActivity extends AppCompatActivity {

    private Contact _contact;
    private Single<Contact> _fetchObservable;
    private Single<Contact> _updateObservable;
    private Subscription _subscription;

    @BindView(R.id.contact_info_name)
    TextView _nameView;
    @BindView(R.id.contact_info_phone)
    TextView _numberView;
    @BindView(R.id.contact_info_email)
    TextView _emailView;
    @BindView(R.id.contact_info_pic)
    ImageView _pictureView;
    @BindView(R.id.contact_info_fav)
    ImageView _favIcon;
    @BindView(R.id.contact_info_call_icon)
    ImageView _phoneIcon;
    @BindView(R.id.contact_info_sms_icon)
    ImageView _smsIcon;

    public static final String KEY_CONTACT_EXTRA = "contact";

    public static void launch(Contact contact, Context context) {
        Intent intent = new Intent(context,ContactInfoActivity.class);
        intent.putExtra(KEY_CONTACT_EXTRA, contact);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_info);
        ButterKnife.bind(this);
        _contact = (Contact) getIntent().getParcelableExtra(KEY_CONTACT_EXTRA);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        createSingle(_contact.getRemoteId());
    }

    private void createSingle(final int id) {
        _fetchObservable = Single.fromCallable(new Callable<Contact>() {
            @Override
            public Contact call() throws Exception {
                return ContactsFeature.getInstance().fetchContactSync(ContactInfoActivity.this, id);
            }
        });
    }

    private Single<Contact> createUpdateSingle(final Contact contact) {
        return Single.fromCallable(new Callable<Contact>() {
            @Override
            public Contact call() throws Exception {
                return ContactsFeature.getInstance().updateContact(contact);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        _subscription = _fetchObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<Contact>() {
                    @Override
                    public void onSuccess(Contact value) {
                        _contact = value;
                        setupView();
                    }

                    @Override
                    public void onError(Throwable error) {
                        Toast.makeText(ContactInfoActivity.this, "Something went wrong",Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void setupView() {
        findViewById(R.id.progress_wheel).setVisibility(View.GONE);
        findViewById(R.id.contact_info_layout).setVisibility(View.VISIBLE);
        setTextViewText(_nameView, Utils.capitalizeName(_contact.getFirstName(), _contact.getLastName()));
        setTextViewText(_emailView, _contact.getEmail());
        setTextViewText(_numberView, _contact.getNumber());
        _favIcon.setImageResource(_contact.getIsFavorite() ? R.drawable.ic_favorite_black_24px : R.drawable.ic_favorite_border_black_24px);
        setListeners();
        Glide.with(this).load(_contact.getProfilePictureUrl()).placeholder(R.drawable.contacts_placeholder)
        .into( _pictureView);
    }

    private void setListeners() {
        _phoneIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+_contact.getNumber()));
                startActivity(callIntent);
            }
        });
        _emailView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.setType("text/plain");
                emailIntent.putExtra(Intent.EXTRA_EMAIL  , new String[]{_contact.getEmail()});
                startActivity(emailIntent);
            }
        });
        _favIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _favIcon.setImageResource(R.drawable.ic_favorite_black_24px);
                Toast.makeText(ContactInfoActivity.this, "updating contact...", Toast.LENGTH_LONG).show();
                _contact.makeFavorite();
                _updateObservable = createUpdateSingle(_contact);
                _updateObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new SingleSubscriber<Contact>() {
                            @Override
                            public void onSuccess(Contact value) {
                                _contact = value;
                                setupView();
                                Toast.makeText(ContactInfoActivity.this, "Updated contact successfully", Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onError(Throwable error) {
                                Toast.makeText(ContactInfoActivity.this, "Could not update contact, Please try again later", Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });
        _pictureView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //zoom picture, LATER
            }
        });
        _smsIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.putExtra("address"  , _contact.getNumber());
                sendIntent.setType("vnd.android-dir/mms-sms");
                startActivity(sendIntent);
            }
        });
    }

    private void setTextViewText(TextView textView, String text) {
        if(text == null)
        {
            text = " - ";
        }
        textView.setText(text);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
