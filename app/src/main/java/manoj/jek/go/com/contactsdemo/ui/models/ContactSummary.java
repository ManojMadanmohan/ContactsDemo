package manoj.jek.go.com.contactsdemo.ui.models;

public class ContactSummary {
    private String first_name;
    private String last_name;
    private String profile_pic;
    private int id;

    public ContactSummary(String fName, String lName, String profilePic, int id)
    {
        first_name = fName;
        last_name = lName;
        profile_pic = profilePic;
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public int getId() {
        return id;
    }
}
