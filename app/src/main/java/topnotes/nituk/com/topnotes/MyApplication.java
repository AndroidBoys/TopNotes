package topnotes.nituk.com.topnotes;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import androidx.annotation.NonNull;

public class MyApplication extends Application {

    public static MyApplication thisApp;
    public static List<Long> downloadList;
    public static List<String> subjectNames;
    public static List<String> subjectNamesToken;
    public static List<String> spinnerSubjectList;

    @Override
    public void onCreate() {
        super.onCreate();
        thisApp = this;
        downloadList = new ArrayList<>();
        }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public static MyApplication getApp()
    {
        return thisApp;
    }

    public static List<Long> getDownloadList()
    {
        return downloadList;
    }

}
