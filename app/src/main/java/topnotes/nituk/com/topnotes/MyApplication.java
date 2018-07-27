package topnotes.nituk.com.topnotes;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

public class MyApplication extends Application {

    public static MyApplication thisApp;
    public static List<Long> downloadList;
    @Override
    public void onCreate() {
        super.onCreate();
        thisApp=this;
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
