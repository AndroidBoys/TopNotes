package topnotes.nituk.com.topnotes;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AnotherContentDownloader {
   private Context mContext;
   private DownloadManager mDownloadManager;
   private long mDownloadReference;
   private static AnotherContentDownloader sDownloader;
   private String url;
   private int subject;
   private int type;

    private AnotherContentDownloader(Context context)
    {
        mContext = context;
        mDownloadManager=(DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);


    }
    public static AnotherContentDownloader getInstance(Context context)
    {
        if(sDownloader==null)
        {
           sDownloader = new AnotherContentDownloader(context);
        }
        return sDownloader;
    }



    public void downloadFile(String url,String title,int subject,int type)
    {
        this.url = url;
        this.subject = subject;
        this.type = type;

        //since download manager takes a uri not a url
        Uri uri = Uri.parse(url);

        //converting url into uri
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

    public void incrementDownloadCounter()
    {
      // to be implemented..
        // get reference to content object section
      final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("courses").child(mContext.getResources().getStringArray(R.array.subjectToken)[subject])
      .child(mContext.getResources().getStringArray(R.array.typeToken)[type]);

       reference.addChildEventListener(new ChildEventListener() {
           @Override
           public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

               Content content=dataSnapshot.getValue(Content.class);

               if(content.getDownloadUrl().equals(url))
               {
                  DatabaseReference downloadReference=dataSnapshot.getRef().child("downloads");
                  runIncreamentTransaction(downloadReference);

               }


           }

           @Override
           public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

           }

           @Override
           public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

           }

           @Override
           public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });



    }

    private void runIncreamentTransaction(DatabaseReference reference)
    {
        reference.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                if (mutableData.getValue() == null) {
                    mutableData.setValue(1);
                } else {

                    int count = mutableData.getValue(Integer.class);
                    mutableData.setValue(count + 1);
                }
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean success, DataSnapshot dataSnapshot) {
                // Analyse databaseError for any error during increment
            }
        });
    }





}
