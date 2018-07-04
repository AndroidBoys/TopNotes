package topnotes.nituk.com.topnotes;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkCheck {

    public static boolean isNetworkAvailable(Context context) {


        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();//it will give the info related to the network
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
