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
}
