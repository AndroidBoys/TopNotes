package topnotes.nituk.com.topnotes;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.DialogFragment;

public class DownloadDialogFragment extends Dialog_fragment {

    public static final String notificationChannelId="NOTIFICATION";
    private TextView titleTextView;
    private TextView subjectTextView;
    private TextView sizeTextView;
    private TextView downloadsTextView;
    private TextView authorTextView;
    private TextView dateTextView;
    private TextView creditsTextView;
    private int choosenSubject;
    private int choosenType;
    private Activity activity;
    private Content content;
    private List<File> fileList;
    private ArrayList<String> titleNameofFiles;
//
//    class SizeTask extends AsyncTask{
//
//        //This below code is used to find out the size of file and then set the size of the file in the sizeTextView
//
//        @Override
//        protected Object doInBackground(Object[] objects) {
//
//
//            try {
//                URL url = new URL(content.getDownloadUrl());
//                Log.i("Inside","--------------------------------url"+content.getDownloadUrl());
//                URLConnection urlConnection =url.openConnection();
//                //urlConnection.connect();
//                int file_size=urlConnection.getContentLength();
//                Log.i("FileSize","--------------------------------"+file_size);
//                sizeTextView.setText(file_size/(1024)+"KB");
//                //sizeTextView.setText(String.valueOf(file_size));
//
//            }catch(Exception e){
//                e.printStackTrace();
//            }
//
//
//            return null;
//        }
//    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //it will remove title bar from the dialog
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setWindowAnimations(R.style.dialog_animation_fade);

        activity=getActivity();

        View view = inflater.inflate(R.layout.download_dialog_fragment, container, false);
        Button downloadButton = view.findViewById(R.id.downloadButton);

        titleTextView =view.findViewById(R.id.title);
        subjectTextView = view.findViewById(R.id.subject);
        authorTextView = view.findViewById(R.id.author);
        dateTextView = view.findViewById(R.id.date);
        sizeTextView = view.findViewById(R.id.size);
        creditsTextView = view.findViewById(R.id.credits);
        downloadsTextView = view.findViewById(R.id.downloads);

        //titleNameOfFiles contain the title name present in specific folder.
        titleNameofFiles=new ArrayList<>();

        content = (Content)getArguments().getSerializable("content");
        titleTextView.setText(content.getTitle());

        authorTextView.setText(content.getAuthor());
        sizeTextView.setText(content.getSize());
        creditsTextView.setText("Admins :)");
        dateTextView.setText(content.getDate());
        subjectTextView.setText(content.getSubject());
        downloadsTextView.setText(Integer.toString(content.getDownloads()));

        choosenSubject=getArguments().getInt("subject");
        choosenType=getArguments().getInt("type");

        File directory=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),"TopNotes/"+
                     MyApplication.getApp().subjectNames.get(choosenSubject)+"/"+
                getResources().getStringArray(R.array.categoryList)[choosenType]);


        if(directory.exists()) {
            fileList = Arrays.asList(directory.listFiles());//it will return the list of files present
            //in that folder(specified path)

            //Below code is used to add the title name in titleNameOfFiles arrayList..
            for(int i=0;i<fileList.size();i++){
                titleNameofFiles.add(fileList.get(i).getName());
            }
        }



//        new SizeTask().execute();


        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Here i will check if the file which i am going to download is already present
                //in file system that means users had already download it ..So no need to download it
                //again.
               if(!titleNameofFiles.contains(content.getFileName())) {
                    // Download the content
                    Toast.makeText(getActivity(), "::Download process begins:: with url:" + content.getDownloadUrl(), Toast.LENGTH_SHORT).show();
                    Log.i("url:", content.getDownloadUrl());
//                new ContentDownloader(getActivity()).downloadFile(content.getDownloadUrl(),content.getTitle(),choosenSubject,choosenType);
                    AnotherContentDownloader.getInstance(getActivity()).downloadFile(content.getDownloadUrl(), content.getFileName(), choosenSubject, choosenType);

                }else{
                   Toast.makeText(getActivity()," You already have downloaded this file!",Toast.LENGTH_SHORT).show();
                }
                DialogFragment dialog = (DialogFragment) getFragmentManager().findFragmentByTag("Download");
                dialog.dismiss();
            }
        });



        return view;

    }

    public DownloadDialogFragment() {
    }

    public static DownloadDialogFragment getInstance(Content content,int subject,int type){
        Bundle bundle = new Bundle();
        bundle.putSerializable("content",content);
        bundle.putInt("subject",subject);
        bundle.putInt("type",type);
        DownloadDialogFragment fragment=new DownloadDialogFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

}