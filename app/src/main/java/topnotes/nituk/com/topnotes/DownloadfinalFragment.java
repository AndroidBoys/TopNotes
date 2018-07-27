package topnotes.nituk.com.topnotes;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

//  Fragment showing the downloaded files of the user

public class DownloadfinalFragment extends Fragment {


    private static final int PER_REQ_CODE = 1;
    private ListView mDownloadedFilesListView;
    private List<String> theNamesOfFiles; //contains file names from file system
    private List<String>  fileTitleList; // contains the titles associated with the file names
    private List<Content> contentList; // contains the corresponding content objects present in the file system
    private int choosenSubject;
    private int choosenType;
    Activity activity;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity=getActivity();
        View view = inflater.inflate(R.layout.fragment_download, container, false);

        SubjectListActivity.stack.push(R.id.myDownloads);//it will push the id of myDownloads fragment into the
        //SubjectListActivity stack to get proper reflection in navigation menu on pressing back button.

        theNamesOfFiles = new ArrayList<>();
        fileTitleList= new ArrayList<>();
        contentList = new ArrayList<>();

        choosenSubject=getArguments().getInt("subject");
        choosenType=getArguments().getInt("type");

        // get the file names from the file system
        listFiles();
        // get the contents corresponding to the files present in the file system
        getContentDetails();

        mDownloadedFilesListView = view.findViewById(R.id.downloadedfilelistview);
        MyDownloadsArrayAdapter myDownloadsArrayAdapter = new MyDownloadsArrayAdapter(getActivity(), contentList, fileTitleList,choosenSubject,choosenType);
        mDownloadedFilesListView.setAdapter(myDownloadsArrayAdapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((SubjectListActivity)activity).setActionBarTitle(getResources().getStringArray(R.array.categoryList)[choosenType]);
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
    {
        List<File> fileList= new ArrayList<>();
        // Folder name where all the downloaded notes will be saved
        String dirPath = "TopNotes/"+MyApplication.getApp().subjectNames.get(choosenSubject)+"/"+
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
            // Exract the filenames of files present in the file system
            for (int i = 0; i < fileList.size(); i++) {
                String fileName= fileList.get(i).getName();
                theNamesOfFiles.add(fileName);
                Log.i("filename:",fileName);
            }
        }
    }

    public void getContentDetails()
    {
        // get all the content objects present in the db
        List<Content> contents = new DbHelper(activity.getApplicationContext())
                .readContentList(choosenSubject,choosenType);
        Log.i("contents:",contents.toString());

        // extract the content objects corresponding to the filenames present in the file system only
        for(int i=0;i<contents.size();i++)
        {
            //Here we are comparing the file present in file system and the file present in sqlite database
            //if it matches it means users have downloaded it(because only after downloading file enter into file system)
            if(theNamesOfFiles.contains(contents.get(i).getFileName()))
          {
            fileTitleList.add(contents.get(i).getTitle());
            contentList.add(contents.get(i));
          }

        }
    }

}
