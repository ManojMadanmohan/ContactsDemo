package manoj.jek.go.com.contactsdemo.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.bumptech.glide.Glide;

import java.util.List;
import java.util.concurrent.Callable;

import manoj.jek.go.com.contactsdemo.R;
import manoj.jek.go.com.contactsdemo.ui.models.Contact;
import manoj.jek.go.com.contactsdemo.ui.network.Utils;
import rx.Single;
import rx.SingleSubscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ContactInfoActivity extends AppCompatActivity {

    private Contact _contact;
    private Single<Contact> _single;
    private Subscription _subscription;

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
        _contact = (Contact) getIntent().getParcelableExtra(KEY_CONTACT_EXTRA);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        loadContact(_contact.getRemoteId());
    }

    private void loadContact(final int id) {
        _single = Single.fromCallable(new Callable<Contact>() {
            @Override
            public Contact call() throws Exception {
                if(Utils.isNetworkAvailable(ContactInfoActivity.this)) {
                    Contact contact = Utils.getContactsService().getContact(String.valueOf(id)).execute().body();
                    contact.save();
                    return contact;
                } else {
                    return new Select().from(Contact.class).where("remoteId = ?",_contact.getRemoteId()).executeSingle();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        _subscription = _single.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<Contact>() {
                    @Override
                    public void onSuccess(Contact value) {
                        _contact = value;
                        setupView();
                    }

                    @Override
                    public void onError(Throwable error) {
                        //handle
                    }
                });
    }

    private void setupView() {
        findViewById(R.id.progress_wheel).setVisibility(View.GONE);
        findViewById(R.id.contact_info_layout).setVisibility(View.VISIBLE);
        setTextViewText(R.id.contact_info_name, Utils.capitalizeName(_contact.getFirstName(), _contact.getLastName()));
        setTextViewText(R.id.contact_info_email, _contact.getEmail());
        setTextViewText(R.id.contact_info_phone, _contact.getNumber());
        Glide.with(this).load(_contact.getProfilePictureUrl()).placeholder(R.drawable.contacts_placeholder)
        .into((ImageView) findViewById(R.id.contact_info_pic));
    }

    private void setTextViewText(int resourceId, String text) {
        TextView textView = (TextView) findViewById(resourceId);
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
