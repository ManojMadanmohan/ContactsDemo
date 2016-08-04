package manoj.jek.go.com.contactsdemo.ui.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by manoj on 5/8/16.
 */

public class ContactsSqLiteHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "contacts";
    public static final int DATABASE_VERSION = 0;

    public static final String TABLE_NAME = "contacts";

    public ContactsSqLiteHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table "+TABLE_NAME+
                " ( id text not null primary key, first_name text not null, last_name text not null, email text not null, phone_number text not null, profile_pic text not null, favorite boolean );");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        onCreate(sqLiteDatabase);
    }
}
