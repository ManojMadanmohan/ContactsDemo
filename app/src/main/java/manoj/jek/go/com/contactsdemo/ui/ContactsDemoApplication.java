package manoj.jek.go.com.contactsdemo.ui;

import android.app.Application;
import com.activeandroid.ActiveAndroid;

public class ContactsDemoApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        ActiveAndroid.initialize(this);
    }
}
