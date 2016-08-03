package manoj.jek.go.com.contactsdemo.ui.db;

import java.util.List;

import manoj.jek.go.com.contactsdemo.ui.models.Contact;

public interface ILocalContactFeature {

    public void addContact(Contact contact);

    public List<Contact> fetchContacts();

}
