package topnotes.nituk.com.topnotes;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class ContactUsFragment extends Fragment implements View.OnClickListener{

    private AppCompatActivity activity;
    private EditText feedbackEditText;
    private Button showFeedbackButton;
    private Context context;
    public ContactUsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity=(AppCompatActivity)getActivity();
        context=getContext();
        View view=inflater.inflate(R.layout.contact_us,container,false);
        final Button sendFeedBackButton=view.findViewById(R.id.sendFeedbackButton);
        LinearLayout linearLayout=view.findViewById(R.id.superContentLayout);
        feedbackEditText=view.findViewById(R.id.feedbackEditText);
        linearLayout.setOnClickListener(this);
        sendFeedBackButton.setOnClickListener(this);
        showFeedbackButton=view.findViewById(R.id.showFeedbackButton);
        if(CheckAdminMode.isAdminMode()){
            showFeedbackButton.setVisibility(View.VISIBLE);
            showFeedbackButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showFeedback();
                }
            });
        }
        return view;
    }

    public static ContactUsFragment getInstance(){
        return new ContactUsFragment();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.superContentLayout:
                InputMethodManager inputMethodManager=(InputMethodManager)getActivity().getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),0);

                break;
            case R.id.sendFeedbackButton:
                //intent to gmail

                if (feedbackEditText.getText().toString().trim().equals(""))
                    feedbackEditText.setError("Please write feedback first");
                else {
                    Calendar calendar=Calendar.getInstance();
                    SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String feedbackTime = dateFormat.format(calendar.getTime());

                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,MySubjects.getInstance()).commit();
                    FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth
                            .getInstance().getCurrentUser().getUid()).child("Feedback").child(feedbackTime)
                            .setValue(feedbackEditText.getText().toString()+" : "+User.getUser().getName())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    AlertDialog.Builder builder=new AlertDialog.Builder(activity);
                                    builder.setTitle("Feedback Send")
                                            .setMessage("Thank you for your feedback")
                                            .show();
                                    //Toast.makeText(activity,"Feedback Sended!!! Thanks for your feedback",Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            AlertDialog.Builder builder=new AlertDialog.Builder(activity);
                            builder.setTitle("Feedback Not Send")
                                    .setMessage("Error while sending feedback")
                                    .show();

                        }
                    });
                }
                break;
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        ((SubjectListActivity)activity).setActionBarTitle("Contact Us");
    }

    public void showFeedback(){

        final ArrayList<String> feedbacks=new ArrayList<>();

        //extracting feedbacks from firebase database;
        FirebaseDatabase.getInstance().getReference().child("users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                for(final DataSnapshot itemSnapshot: dataSnapshot.getChildren()){
                    if(itemSnapshot.getKey().equals("Feedback")){

                        itemSnapshot.getRef().addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                Log.d("feedbacksuser",dataSnapshot.getValue().toString());
                                feedbacks.add(dataSnapshot.getValue().toString());
                                if(FeedbackList.arrayAdapter!=null)
                                    FeedbackList.arrayAdapter.notifyDataSetChanged();
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


        FeedbackList feedbackList=new FeedbackList(feedbacks);
        feedbackList.show(getFragmentManager(),"feedbacks");
        //
    }

}
