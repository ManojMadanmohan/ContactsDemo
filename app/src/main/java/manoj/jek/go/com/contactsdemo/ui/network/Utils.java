package manoj.jek.go.com.contactsdemo.ui.network;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;

import manoj.jek.go.com.contactsdemo.ui.models.Contact;
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
        if(firstName == null && lastName == null)
        {
            return " - ";
        }
        String formattedFirstName = firstName;
        String formattedLastName = lastName;
        if(formattedFirstName.length() > 1) formattedFirstName = formattedFirstName.substring(0,1).toUpperCase() + formattedFirstName.substring(1);
        if(formattedLastName.length() > 1) formattedLastName = formattedLastName.substring(0,1).toUpperCase() + formattedLastName.substring(1);
        return formattedFirstName + " " + formattedLastName;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static boolean isLollipopOrAbove() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return true;
        } else {
            return false;
        }
    }

    public static SpannableStringBuilder colorPartialString(String parentText, String match, int color) {
        int startPos =  parentText.toLowerCase().indexOf(match.toLowerCase());
        return colorPartialString(parentText, startPos, match.length(), color);
    }

    public static SpannableStringBuilder colorPartialString(String text, int startPos, int length, int color) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        SpannableString spannable = new SpannableString(text);
        spannable.setSpan(new ForegroundColorSpan(color), startPos, startPos + length, 0);
        builder.append(spannable);
        return builder;
    }
}
