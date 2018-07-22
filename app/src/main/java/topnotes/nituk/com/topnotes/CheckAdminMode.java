package topnotes.nituk.com.topnotes;

import com.google.firebase.auth.FirebaseAuth;

public  class CheckAdminMode {
    private static final String sohanEmail="sohan.cse16@nituk.ac.in",
            amitEmail="amitkishorraturi.cse16@nituk.ac.in",
            arvindEmail="arvind7799.cse16@nituk.ac.in";

    public static boolean isAdminMode(){
        String userEmail=FirebaseAuth.getInstance().getCurrentUser().getEmail();
        if(userEmail.equals(sohanEmail)||userEmail.equals(amitEmail)
                ||userEmail.equals(arvindEmail)){
            return  true;
        }
        else
            return  false;
    }
}
