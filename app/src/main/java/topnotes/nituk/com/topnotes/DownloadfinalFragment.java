package topnotes.nituk.com.topnotes;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

//  Fragment showing the downloaded files of the user

public class DownloadfinalFragment extends Fragment {


    private static final int PER_REQ_CODE = 1;
    private ListView mDownloadedFilesListView;
    private List<File> fileList;  // field saves the list of all the download files
    private List<String> theNamesOfFiles; // field saves the name of all the download files
    private ArrayList<String>  downloadsAuthorsNameArray;
    private int choosenSubject;
    private int choosenType;
    private List<String> downloadedTitle;
    Activity activity;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity=getActivity();
        View view = inflater.inflate(R.layout.fragment_download, container, false);

        SubjectListActivity.stack.push(R.id.myDownloads);//it will push the id of myDownloads fragment into the
        //SubjectListActivity stack to get proper reflection in navigation menu on pressing back button.

        fileList = new ArrayList<>();
        theNamesOfFiles = new ArrayList<>();
        downloadsAuthorsNameArray=new ArrayList<>();
        downloadedTitle= new ArrayList<>();

        choosenSubject=getArguments().getInt("subject");
        choosenType=getArguments().getInt("type");

        listFiles();
        // get the details of downloaded files
        getContentDetails();

        mDownloadedFilesListView = view.findViewById(R.id.downloadedfilelistview);
        MyDownloadsArrayAdapter myDownloadsArrayAdapter = new MyDownloadsArrayAdapter(getActivity(), theNamesOfFiles, downloadsAuthorsNameArray,choosenSubject,choosenType);
        mDownloadedFilesListView.setAdapter(myDownloadsArrayAdapter);

//        mDownloadedFilesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                openFile(getResources().getStringArray(R.array.subjectList)[choosenSubject]+"/"
//                        +getResources().getStringArray(R.array.categoryList)[choosenType]+"/"
//                        +theNamesOfFiles.get(i)+".pdf");
//                Log.i("clicked:", "" + i);
//                Log.i("file:", theNamesOfFiles.get(i));
//            }
//        });

//         mDownloadedFilesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//             @Override
//             public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                 openFile(getResources().getStringArray(R.array.subjectList)[choosenSubject]+"/"
//                         +getResources().getStringArray(R.array.categoryList)[choosenType]+"/"
//                         +theNamesOfFiles.get(i)+".pdf");
//                 Log.i("clicked:", "" + i);
//                 Log.i("file:", theNamesOfFiles.get(i));
//             }
//         });
//         choosenSubject=getArguments().getInt("subject");
//         choosenType=getArguments().getInt("type");
//         //Toast.makeText(getActivity(),"Subject:"+choosenSubject+"Type:"+choosenType,Toast.LENGTH_SHORT).show();


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((SubjectListActivity)activity).setActionBarTitle(getResources().getStringArray(R.array.categoryList)[choosenType]+" Selected");
    }

    public static DownloadfinalFragment getInstance(int subject,int type)
    {
        Bundle bundle = new Bundle();
        bundle.putInt("subject",subject);
        bundle.putInt("type",type);
        DownloadfinalFragment fragment = new DownloadfinalFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    // list the downloaded files
    private  void listFiles()
    {   // Folder name where all the downloaded notes will be saved
        String dirPath = "TopNotes/"+getResources().getStringArray(R.array.subjectList)[choosenSubject]+"/"+
                getResources().getStringArray(R.array.categoryList)[choosenType];
        // get the directory for the given folder name
        File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),dirPath);
        // if no such folder exists create a new one with such name
        if (!dir.exists()) {
            if(dir.mkdirs())
            Log.i("Folder created:",dir.getAbsolutePath());
            else
            Log.i("folder creation failed",dir.getAbsolutePath());

        }

        // otherwise pull all the files out of the directory, get their names and set on the list view
        else {
            Log.i("Already exists:",dir.getAbsolutePath());
            try {
                //putting all the files present in the dir into the fileList
                fileList = Arrays.asList(dir.listFiles());
            }catch(Exception e)
            {
                e.printStackTrace();
            }
            for (int i = 0; i < fileList.size(); i++) {
                String fileName= fileList.get(i).getName();
                downloadedTitle.add(fileName.substring(0,fileName.length()-4));//to remove .pdf
                Log.i("filename:",fileName.substring(0,fileName.length()-4));
            }


        }
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        if(requestCode==PER_REQ_CODE && grantResults.length>0)
//        {
//            if(grantResults[0]== PackageManager.PERMISSION_GRANTED)
//            {
//                listFiles();
//            }
//            else
//            {
//                Toast.makeText(getActivity(),"Please grant the Read/Write permission first!",Toast.LENGTH_SHORT).show();
//            }
//        }
//
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    }



    //bug here
    public void getContentDetails()
    {
        List<Content> contents = new DbHelper(activity.getApplicationContext())
                .readContentList(choosenSubject,choosenType);
        Log.i("contents:",contents.toString());

        for(int i=0;i<contents.size();i++)
        {   if(downloadedTitle.contains(contents.get(i).getTitle()))
          {
            theNamesOfFiles.add(contents.get(i).getTitle());
            downloadsAuthorsNameArray.add(contents.get(i).getAuthor());

          }

        }
    }

}
