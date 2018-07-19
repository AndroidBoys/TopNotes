package topnotes.nituk.com.topnotes;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;

public class ContactUsFragment extends Fragment implements View.OnClickListener{

    private Activity activity;
    private EditText feedbackEditText;
    public ContactUsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity=getActivity();
        View view=inflater.inflate(R.layout.contact_us,container,false);
        final Button sendFeedBackButton=view.findViewById(R.id.sendFeedbackButton);
        LinearLayout linearLayout=view.findViewById(R.id.superContentLayout);
        feedbackEditText=view.findViewById(R.id.feedbackEditText);
        linearLayout.setOnClickListener(this);
        sendFeedBackButton.setOnClickListener(this);

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

                    FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth
                            .getInstance().getCurrentUser().getUid()).child("Feedback").child(feedbackTime)
                            .setValue(feedbackEditText.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                                    builder.setTitle("Feedback Send")
                                            .setMessage("Thank you for your feedback")
                                            .show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                            builder.setTitle("Feedback Send")
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
}
