package manoj.jek.go.com.contactsdemo.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import manoj.jek.go.com.contactsdemo.R;
import manoj.jek.go.com.contactsdemo.ui.models.Contact;
import manoj.jek.go.com.contactsdemo.ui.network.Utils;

public class ContactInfoActivity extends AppCompatActivity {

    private Contact _contact;

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
        setupView();
    }

    private void setupView() {
        setTextViewText(R.id.contact_info_name, Utils.capitalizeName(_contact.getFirstName(), _contact.getLastName()));
        setTextViewText(R.id.contact_info_email, _contact.getEmail());
        setTextViewText(R.id.contact_info_phone, _contact.getNumber());
        Glide.with(this).load(_contact.getProfilePictureUrl()).placeholder(R.drawable.contacts_placeholder)
        .into((ImageView) findViewById(R.id.contact_info_pic));
    }

    private void setTextViewText(int resourceId, String text) {
        TextView textView = (TextView) findViewById(resourceId);
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
