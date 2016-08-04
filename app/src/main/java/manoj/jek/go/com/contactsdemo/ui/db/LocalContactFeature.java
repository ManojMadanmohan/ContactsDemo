package manoj.jek.go.com.contactsdemo.ui.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import manoj.jek.go.com.contactsdemo.ui.models.Contact;

public class LocalContactFeature implements ILocalContactFeature {

    private ContactsSqLiteHelper _dbHelper;

    public LocalContactFeature(Context context) {
        _dbHelper = new ContactsSqLiteHelper(context);
    }

    @Override
    public void addContact(Contact contact) {
        SQLiteDatabase database = _dbHelper.getWritableDatabase();
        database.insert(ContactsSqLiteHelper.TABLE_NAME, null, getContentValues(contact));
        database.close();
    }

    @Override
    public List<Contact> fetchContacts() {
        return null;
    }

    private ContentValues getContentValues(Contact contact)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", contact.getId());
        contentValues.put("first_name", contact.getFirstName());
        contentValues.put("last_name", contact.getLastName());
        contentValues.put("email", contact.getEmail());
        contentValues.put("phone_number", contact.getNumber());
        contentValues.put("profile_pic", contact.getProfilePictureUrl());
        contentValues.put("favorite", contact.getIsFavorite());
        return contentValues;
    }
}
