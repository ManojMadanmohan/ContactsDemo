package manoj.jek.go.com.contactsdemo.ui.network;

public class Utils {

    public static ContactsService getContactsService()
    {
        return ContactsService.retrofit.create(ContactsService.class);
    }
}
