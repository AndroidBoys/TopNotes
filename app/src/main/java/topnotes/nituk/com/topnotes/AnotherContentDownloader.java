package topnotes.nituk.com.topnotes;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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
        //since download manager takes a uri not a url
        Uri uri = Uri.parse(url);//converting url into uri
        incrementDownloadCounter(subject,type);
//        String mimeType = mContext.getContentResolver().getType(uri);
       // Log.i("mimeType:",mimeType);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        // configure notification
        request.setDescription("My download")
                .setTitle(title);

        // set the download path
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOCUMENTS+"/TopNotes/"+mContext.getResources().getStringArray(R.array.subjectList)[subject]+
                        "/"+ mContext.getResources().getStringArray(R.array.categoryList)[type],title+".pdf");
        //In the above code we can make some changes. Instead of appending .pdf with title we should not append anything.

       request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);



        Log.i("destination",Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getPath()+"/TopNotes/"+mContext.getResources().getStringArray(R.array.subjectList)[subject]+
                "/"+ mContext.getResources().getStringArray(R.array.categoryList)[type]+title+".pdf");

        // enqueue for download execute in a separate thread
        mDownloadReference=mDownloadManager.enqueue(request);

        // add to globle download list
        MyApplication.getApp().getDownloadList().add(mDownloadReference);

    }

    private void incrementDownloadCounter(int subject,int type)
    {
      // to be implemented..
    }




}
