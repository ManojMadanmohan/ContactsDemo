package manoj.jek.go.com.contactsdemo.ui.models;

public class Contact implements Comparable{

    private String first_name;
    private String last_name;
    private String phone_number;
    private String _profile_pic;
    private String email;
    private int id;
    private boolean _isFavorite;

    public Contact(int id, String firstName, String lastName, String email,
                   String number, String profilePicUrl, boolean isFav) {
        this.email = email;
        this.id = id;
        first_name = firstName;
        last_name = lastName;
        this.phone_number = number;
        _profile_pic = profilePicUrl;
        _isFavorite = isFav;
    }

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

    public int getId() {
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
}
