package manoj.jek.go.com.contactsdemo.ui.features;

import android.content.Context;

import com.activeandroid.query.Select;

import java.io.IOException;
import java.util.List;

import manoj.jek.go.com.contactsdemo.ui.models.Contact;
import manoj.jek.go.com.contactsdemo.ui.network.Utils;

public class ContactsFeature {

    private static ContactsFeature _contactsFeature;

    private ContactsFeature() {

    }

    public static ContactsFeature getInstance() {
        if(_contactsFeature == null) {
            _contactsFeature = new ContactsFeature();
        }
        return _contactsFeature;
    }

    public List<Contact> fetchAllContactsSync(Context context) throws IOException {
        if(Utils.isNetworkAvailable(context)) {
            return fetchAllContactsFromServer();
        } else {
            return new Select().from(Contact.class).execute();
        }
    }

    public Contact fetchContactSync(Context context, int contactId) throws IOException {
        if(Utils.isNetworkAvailable(context)) {
            return fetchContactFromServer(contactId);
        } else {
            return new Select().from(Contact.class).where("remoteId = ?",contactId).executeSingle();
        }
    }

    public Contact fetchContactFromServer(int id) throws IOException {
        Contact contact = Utils.getContactsService().getContact(String.valueOf(id)).execute().body();
        contact.save();
        return contact;
    }

    public List<Contact> fetchAllContactsFromServer() throws IOException {
        List<Contact> contacts = Utils.getContactsService().getAllContacts().execute().body();
        for(Contact contact: contacts) {
            contact.save();
        }
        return contacts;
    }

    public Contact addContact(Contact contact) throws IOException {
        Contact addedContact = Utils.getContactsService().addContact(contact).execute().body();
        addedContact.save();
        return addedContact;
    }

    public Contact updateContact(Contact contact) throws IOException {
        Contact updatedContact = Utils.getContactsService().updateContact(String.valueOf(contact.getRemoteId()), contact)
                .execute().body();
        updatedContact.save();
        return updatedContact;
    }


}
