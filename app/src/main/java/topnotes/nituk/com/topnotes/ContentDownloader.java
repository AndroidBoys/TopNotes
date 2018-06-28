package topnotes.nituk.com.topnotes;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.PRDownloaderConfig;
import com.downloader.Progress;
import com.downloader.Status;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import androidx.annotation.NonNull;

// This class object encapsulates the task of downloading a single item from firebase with firebase and external download library

public class ContentDownloader {
    private int downloadId;
    private Context mContext;
    //private Uri mDownloadUri;

    public ContentDownloader(Context context) {
        mContext = context;
        PRDownloaderConfig config = PRDownloaderConfig.newBuilder()
                .setReadTimeout(30_000)
                .setConnectTimeout(30_000)
                .setDatabaseEnabled(true)
                .build();
        PRDownloader.initialize(mContext, config);
        getDownloadUrl();
    }
    public void downloadFile(Uri uri)
    {
        downloadId = PRDownloader.download(uri.toString(),Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getPath()+"/TopNotes","NotesSample.jpg")
                .build()
                .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                    @Override
                    public void onStartOrResume() {
                        Toast.makeText(mContext,"Download started..",Toast.LENGTH_SHORT).show();
                    }
                })
                .setOnPauseListener(new OnPauseListener() {
                    @Override
                    public void onPause() {

                    }
                })
                .setOnCancelListener(new OnCancelListener() {
                    @Override
                    public void onCancel() {
                        Toast.makeText(mContext,"Download Canceled",Toast.LENGTH_SHORT).show();

                    }
                })
                .setOnProgressListener(new OnProgressListener() {
                    @Override
                    public void onProgress(Progress progress) {
                        Toast.makeText(mContext,"Downloading progress:"+progress,Toast.LENGTH_SHORT).show();

                    }
                })
                .start(new OnDownloadListener() {
                    @Override
                    public void onDownloadComplete() {
                        Toast.makeText(mContext,"Download Completed sucessfully!",Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onError(Error error) {
                        Toast.makeText(mContext,"Download failed:"+error.toString(),Toast.LENGTH_SHORT).show();

                    }


                });

    }
    public void pauseDownload()
    {
        PRDownloader.pause(downloadId);
    }
    public void resumeDownload()
    {
        PRDownloader.resume(downloadId);
    }
    public void cancelDownload()
    {
        PRDownloader.cancel(downloadId);

    }
    // suspicious
    /*public Status getStatus()
    {
        Status status = PRDownloader.getStatus(downloadId);
        return status;
    }*/
    public void cleanOldDownlods(int days)
    {
        PRDownloader.cleanUp(days);
    }
    public void getDownloadUrl()
    {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        storageReference.child("Notes/test1").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.i("file url:",uri.toString());
                downloadFile(uri);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("Url fetch failed:",e.getMessage());
            }
        });
    }
}
