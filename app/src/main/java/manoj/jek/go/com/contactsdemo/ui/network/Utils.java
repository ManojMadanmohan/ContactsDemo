package manoj.jek.go.com.contactsdemo.ui.network;


import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class Utils {

    public static ContactsService getContactsService()
    {
        return ContactsService.retrofit.create(ContactsService.class);
    }

    public static OkHttpClient.Builder getBuilder()
    {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        // add your other interceptors …

        // add logging as last interceptor
        httpClient.addInterceptor(logging);  //
        return httpClient;
    }

    public static String capitalizeName(String firstName, String lastName)
    {
        String rawName = firstName + " " + lastName;
        String output = "";
        for(String part: rawName.split(" "))
        {
            part = part.substring(0,1).toUpperCase() + part.substring(1, part.length()).toLowerCase();
            output = output + part + " ";
        }
        return output;
    }
}
