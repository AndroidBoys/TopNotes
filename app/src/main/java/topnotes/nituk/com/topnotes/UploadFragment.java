package topnotes.nituk.com.topnotes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.security.spec.ECField;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class UploadFragment extends Fragment {


    private ListView uploadFileListView;
    private static final int DIALOG_REQ_CODE=0;
    private List<String> notesNameList;
    private List<String> subjectNameList;
    private List<String> dateNameList;
    private  MyUploadsArrayAdapter myUploadsArrayAdapter;
    private SharedPreferences mSharedPreferences;
    Activity activity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity=getActivity();
        View view = inflater.inflate(R.layout.fragment_upload,container,false);
        try {
            mSharedPreferences = this.getActivity().getSharedPreferences("topnotes.nituk.com.topnotes", Context.MODE_PRIVATE);
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        notesNameList=new ArrayList<>();
        subjectNameList=new ArrayList<>();
        dateNameList=new ArrayList<>();

        retrieveLocally();

        FloatingActionButton floatingActionButton = view.findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
                UploadDialogFragment uploadDialogFragment = UploadDialogFragment.getInstance();
                uploadDialogFragment.setTargetFragment(UploadFragment.this,DIALOG_REQ_CODE);
                uploadDialogFragment.show(fragmentManager,"upload");//Here upload is a tag name.
            }
        });

        uploadFileListView=view.findViewById(R.id.uploadFileListView);

//        ArrayAdapter<String> uploadFileListAdapter=new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.subjectList));
//        uploadFileListView.setAdapter(uploadFileListAdapter);

        myUploadsArrayAdapter = new MyUploadsArrayAdapter(getActivity(),(ArrayList) notesNameList,(ArrayList)subjectNameList,(ArrayList)dateNameList);
        uploadFileListView.setAdapter(myUploadsArrayAdapter);
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        ((SubjectListActivity)activity).setActionBarTitle("My Uploads");
    }

    public static UploadFragment getInstance()
    {
        return new UploadFragment();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("onActivityResult","yup lets see");
        if(requestCode == DIALOG_REQ_CODE)
        {
            Log.i("In::","I got the result");
            Content content = (Content) data.getSerializableExtra("content");
            updateUI(content);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void updateUI(Content content)
    {
       subjectNameList.add("Subject");
       notesNameList.add(content.getTitle());
       dateNameList.add(content.getDate());



       //save the upload list locally
        saveLocally();

        myUploadsArrayAdapter.notifyDataSetChanged();

    }
    public void saveLocally()
    {
        mSharedPreferences.edit().putStringSet("titles",new HashSet<String>(subjectNameList))
                .putStringSet("subjects",new HashSet<String>(subjectNameList))
                .putStringSet("dates",new HashSet<String>(dateNameList)).apply();
    }

    public void retrieveLocally()
    {
        try {
            notesNameList = new ArrayList<>(mSharedPreferences.getStringSet("titles", new HashSet<String>(notesNameList)));
            subjectNameList = new ArrayList<>(mSharedPreferences.getStringSet("subjects", new HashSet<String>(subjectNameList)));
            dateNameList = new ArrayList<>(mSharedPreferences.getStringSet("dates", new HashSet<String>(dateNameList)));
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
