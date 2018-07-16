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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.security.spec.ECField;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class UploadFragment extends Fragment {


    private ListView uploadFileListView;
    private static final int DIALOG_REQ_CODE=0;
    private  MyUploadsArrayAdapter myUploadsArrayAdapter;
    private SharedPreferences mSharedPreferences;
    private List<Content> uploadedContent;
    private Activity activity;
    @Override



    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity=getActivity();
        View view = inflater.inflate(R.layout.fragment_upload,container,false);

        uploadedContent=new ArrayList<>();

        fetchUploadList();//it will fetch the uploaded file upload by user from data base and show it in a list

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

        myUploadsArrayAdapter = new MyUploadsArrayAdapter(getActivity(),uploadedContent);
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
        if(requestCode == DIALOG_REQ_CODE) {
            Log.i("In::", "I got the result");
            Content content = (Content) data.getSerializableExtra("content");
            updateUI(content);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    public void updateUI(Content content)
    {
        saveIntoUploadList(content);
    }

    public void saveIntoUploadList(Content content)
    {
        FirebaseDatabase.getInstance().getReference("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("myuploads")
                .child(UUID.randomUUID().toString())
                .setValue(content)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(activity,"Saved to FBDB",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void fetchUploadList()
    {
        FirebaseDatabase.getInstance().getReference("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("myuploads")
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        Content content = dataSnapshot.getValue(Content.class);
                        uploadedContent.add(content);
                        myUploadsArrayAdapter.notifyDataSetChanged();
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
}
