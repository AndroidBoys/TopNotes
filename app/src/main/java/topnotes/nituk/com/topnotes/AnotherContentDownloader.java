package topnotes.nituk.com.topnotes;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

public class AnotherContentDownloader {
   private Context mContext;
   private DownloadManager mDownloadManager;
   private long mDownloadReference;
   private BroadcastReceiver recieverDownloadComplete;
   private BroadcastReceiver receiverNotificationClicked;

    AnotherContentDownloader(Context context)
    {
        mContext = context;
        mDownloadManager=(DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);


    }
    public void downloadFile(String url,String title,int subject,int type)
    {
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        // configure notification
        request.setDescription("My download")
                .setTitle(title);

        // set the download path
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOCUMENTS+"/TopNotes/"+mContext.getResources().getStringArray(R.array.subjectList)[subject]+
                        "/"+ mContext.getResources().getStringArray(R.array.categoryList)[type],title+".jpg");

        Log.i("destination",Environment.DIRECTORY_DOCUMENTS+"/TopNotes/"+mContext.getResources().getStringArray(R.array.subjectList)[subject]+
                "/"+ mContext.getResources().getStringArray(R.array.categoryList)[type]);

        // enque for download
        mDownloadReference=mDownloadManager.enqueue(request);

    }
}
