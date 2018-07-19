package topnotes.nituk.com.topnotes;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AnnouncementFragment extends Fragment {
    private Activity activity;
    private TextView announcementTextView;
    private EditText announcementEditText;
    private EditText quotesEditText;
    private Button quotesSendButton;
    private Button sendAnnouncementButton;
    private final String sohanEmail="sohan.cse16@nituk.ac.in",
                            amitEmail="amitkishorraturi.cse16@nituk.ac.in",
                            arvindEmail="arvind7799.cse16@nituk.ac.in";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = getActivity();
        View view = inflater.inflate(R.layout.announcement_fragment, container, false);
        announcementTextView = view.findViewById(R.id.announcementTextView);
        announcementEditText=view.findViewById(R.id.announcementEditText);
        sendAnnouncementButton=view.findViewById(R.id.announcementSendButton);
        quotesEditText=view.findViewById(R.id.quotesEditText);
        quotesSendButton=view.findViewById(R.id.quotesSendButton);
        setAnnouncement();
        checkAdminMode();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((SubjectListActivity) activity).setActionBarTitle("LeaderBoard");
    }

    public void setAnnouncement() {

        //since their is single value in announcement child we use addListenerForSingleValueEvent

        FirebaseDatabase.getInstance().getReference()
                .child("announcement").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                announcementTextView.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });
    }
    public void checkAdminMode(){

        String userEmail=FirebaseAuth.getInstance().getCurrentUser().getEmail();
        if(userEmail.equals(sohanEmail)||userEmail.equals(amitEmail)
                ||userEmail.equals(arvindEmail)){
            announcementEditText.setVisibility(View.VISIBLE);
            sendAnnouncementButton.setVisibility(View.VISIBLE);
            quotesSendButton.setVisibility(View.VISIBLE);
            quotesEditText.setVisibility(View.VISIBLE);
            sendAnnouncementButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(announcementEditText.getText().toString().trim().equals(""))
                        announcementEditText.setError("enter announcement");
                    else{
                        FirebaseDatabase.getInstance().getReference().child("announcement").setValue(announcementEditText.getText().toString())
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(activity, "failed", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(activity, "Success", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }
            });
            quotesSendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(quotesEditText.getText().toString().trim().equals(""))
                        quotesEditText.setError("enter quotes ");
                    else{
                        FirebaseDatabase.getInstance().getReference().child("quotes")
                                .setValue(quotesEditText.getText().toString()).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(activity, "failed", Toast.LENGTH_SHORT).show();
                            }
                        })
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(activity, "Success", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }
            });
        }
    }
}
