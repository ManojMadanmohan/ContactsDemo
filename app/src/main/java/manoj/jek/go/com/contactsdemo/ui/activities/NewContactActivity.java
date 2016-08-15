package manoj.jek.go.com.contactsdemo.ui.activities;

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

    }

    private boolean validate() {
        return false;
    }
    
    private void saveContact() {

    }



}
