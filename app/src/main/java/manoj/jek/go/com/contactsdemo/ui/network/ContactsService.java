package manoj.jek.go.com.contactsdemo.ui.network;

import java.util.List;
import java.util.logging.Level;

import manoj.jek.go.com.contactsdemo.ui.models.Contact;
import manoj.jek.go.com.contactsdemo.ui.models.ContactSummary;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface ContactsService {

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://gojek-contacts-app.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(Utils.getBuilder().build())
            .build();

    @GET("contacts.json")
    Call<List<Contact>> getAllContacts();

    @GET("contacts/{id}.json")
    Call<Contact> getContact(@Path("id") String id);

    @POST("contacts.json")
    Call<Contact> addContact(@Body Contact contact);

    @PUT("contacts/{id}.json")
    Call<Contact> updateContact(@Path("id") String id, @Body Contact contact);
}
