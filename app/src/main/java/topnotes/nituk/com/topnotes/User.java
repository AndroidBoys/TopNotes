package topnotes.nituk.com.topnotes;

// The class object represents the current user
public class User {
   private String name;
   private String rn;
   private  String email;
   private String imageUrl;
   private static User mCurrentUser;

    private User(String name, String rn, String email, String imageUrl) {
        this.name = name;
        this.rn = rn;
        this.email = email;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRn() {
        return rn;
    }

    public void setRn(String rn) {
        this.rn = rn;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public static User getUser(String name, String rn, String email, String imageUrl)
    {
        if(mCurrentUser==null)
        {
            mCurrentUser= new User(name, rn, email, imageUrl);
        }
        return mCurrentUser;
    }
    // overloaded version
    public static User getUser()
    {
        if(mCurrentUser!=null)
        {
            return mCurrentUser;
        }
        return null;
    }
}
