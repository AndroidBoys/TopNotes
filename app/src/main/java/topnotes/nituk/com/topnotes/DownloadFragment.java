package topnotes.nituk.com.topnotes;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

//  Fragment showing the downloaded files of the user

public class DownloadFragment extends Fragment {


    private static final int PER_REQ_CODE = 1;
    private ListView mDownloadedFilesListView;
    //private ArrayAdapter mArrayAdapter;
    private List<File> fileList;  // field saves the list of all the download files
    private List<String> theNamesOfFiles; // field saves the name of all the download files
    private ArrayList<String>  downloadsAuthorsNameArray;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_download,container,false);

        fileList = new ArrayList<>();
        theNamesOfFiles = new ArrayList<>();
        downloadsAuthorsNameArray=new ArrayList<>();
        downloadsAuthorsNameArray.add("Arvind Negi");
        downloadsAuthorsNameArray.add("amit kishor");
        downloadsAuthorsNameArray.add("sohan kathait");
        downloadsAuthorsNameArray.add("Ayush Bisht");
        downloadsAuthorsNameArray.add("Arvind Negi");
        downloadsAuthorsNameArray.add("amit kishor");
        downloadsAuthorsNameArray.add("sohan kathait");
        downloadsAuthorsNameArray.add("Ayush Bisht");
        downloadsAuthorsNameArray.add("Arvind Negi");
        downloadsAuthorsNameArray.add("amit kishor");

        if(checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE)&&checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE))
        {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},PER_REQ_CODE);
        }
        mDownloadedFilesListView = view.findViewById(R.id.downloadedfilelistview);
        /*mArrayAdapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,theNamesOfFiles);
        mDownloadedFilesListView.setAdapter(mArrayAdapter);*/

        MyDownloadsArrayAdapter myDownloadsArrayAdapter=new MyDownloadsArrayAdapter(getActivity(),theNamesOfFiles,downloadsAuthorsNameArray);
        mDownloadedFilesListView.setAdapter(myDownloadsArrayAdapter);
        listFiles();
        // Reference to the firebase storage
        return view;
    }
    public static DownloadFragment getInstance()
    {
        return new DownloadFragment();
    }

    // list the downloaded files
    private  void listFiles()
    {   // Folder name where all the downloaded notes will be saved
        String dirPath = "TopNotes";
        // get the directory for the given folder name
        File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),dirPath);
        // if no such folder exists create a new one with such name
        if (!dir.exists()) {
            if(dir.mkdirs())
            Log.i("Folder created:",dir.getAbsolutePath());
            else
            Log.i("foleder creation failed",dir.getAbsolutePath());

        }
        // otherwise pull all the files out of the directory, get their names and set on the list view
        else {
            Log.i("Already exists:",dir.getAbsolutePath());
            fileList = Arrays.asList(dir.listFiles());
            for (int i = 0; i < fileList.size(); i++) {
                theNamesOfFiles.add(fileList.get(i).getName());
            }

            //mArrayAdapter.notifyDataSetChanged();

        }
    }
    // check for runtime permission
    private boolean checkPermission(String permission)
    {
        return ActivityCompat.checkSelfPermission(getActivity(),permission)!=PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==PER_REQ_CODE && grantResults.length>0)
        {
            if(grantResults[0]== PackageManager.PERMISSION_GRANTED)
            {
                listFiles();
            }
            else
            {
                Toast.makeText(getActivity(),"Please grant the Read/Write permission first!",Toast.LENGTH_SHORT).show();
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
