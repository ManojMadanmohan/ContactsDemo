package manoj.jek.go.com.contactsdemo.ui.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.gson.annotations.Expose;

@Table(name = "Contacts")
public class Contact extends Model implements Comparable, Parcelable{

    @Expose
    @Column(name="fname")
    private String first_name;
    @Expose
    @Column(name="lname")
    private String last_name;
    @Expose
    @Column(name="phone")
    private String phone_number;
    @Expose
    @Column(name="picture")
    private String _profile_pic;
    @Expose
    @Column(name="email")
    private String email;
    @Expose
    @Column(name="remoteId", index = true, unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private int id;
    @Expose
    @Column(name="favorite")
    private boolean _isFavorite;

    //No- args constructor needed for GSON
    public Contact() {
        super();
    }

    public Contact(int id, String firstName, String lastName, String email,
                   String number, String profilePicUrl, boolean isFav) {
        super();
        this.email = email;
        this.id = id;
        first_name = firstName;
        last_name = lastName;
        this.phone_number = number;
        _profile_pic = profilePicUrl;
        _isFavorite = isFav;
    }

    protected Contact(Parcel in) {
        first_name = in.readString();
        last_name = in.readString();
        phone_number = in.readString();
        _profile_pic = in.readString();
        email = in.readString();
        id = in.readInt();
        _isFavorite = in.readByte() != 0;
    }

    public static final Creator<Contact> CREATOR = new Creator<Contact>() {
        @Override
        public Contact createFromParcel(Parcel in) {
            return new Contact(in);
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };

    public String getFirstName() {
        return first_name;
    }

    public String getLastName() {
        return last_name;
    }

    public String getNumber() {
        return phone_number;
    }

    public String getProfilePictureUrl() {
        return _profile_pic;
    }

    public String getEmail() {
        return email;
    }

    public void makeFavorite() {
        _isFavorite = true;
    }

    public boolean  getIsFavorite() {
        return _isFavorite;
    }

    public int getRemoteId() {
        return id;
    }

    @Override
    public int compareTo(Object o) {
        if(o == null || !(o instanceof Contact) ) {
            return -1;
        }
        Contact that = (Contact) o;
        if(this.getIsFavorite() && !that.getIsFavorite()) {
            return -1;
        } else if(!this.getIsFavorite() && that.getIsFavorite()) {
            return 1;
        } else {
            String thisName = (this.getFirstName() + this.getLastName()).toLowerCase();
            String thatName = (that.getFirstName() + that.getLastName()).toLowerCase();
            return thisName.compareTo(thatName);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(first_name);
        parcel.writeString(last_name);
        parcel.writeString(phone_number);
        parcel.writeString(_profile_pic);
        parcel.writeString(email);
        parcel.writeInt(id);
        parcel.writeByte((byte) (_isFavorite ? 1 : 0));
    }
}
