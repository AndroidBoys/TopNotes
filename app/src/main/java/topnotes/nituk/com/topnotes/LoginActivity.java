package topnotes.nituk.com.topnotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText mNameEditText;
    private EditText mRNEditText;
    private Button mSignInButton;
    static TextView mConnectingTextView;
    private FirebaseAuth mAuth;
    private ProgressDialog mProgressDialog;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN=1;
    private SharedPreferences sharedPreferences;
    private LinearLayout superLoginLayout;
//    private List<String> subjectNames;
//    private List<String> subjectNamesToken;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        mProgressDialog = new ProgressDialog(LoginActivity.this);

//        // copy the globle list into local variables
//        subjectNames = MyApplication.getApp().subjectNames;
//        subjectNamesToken=MyApplication.getApp().subjectNamesToken;

        mNameEditText = findViewById(R.id.nameEditText);
        mRNEditText = findViewById(R.id.rnEditText);
        mSignInButton = findViewById(R.id.googlesigninbutton);
        mConnectingTextView = findViewById(R.id.connectiong);
        superLoginLayout=findViewById(R.id.superLoginLayout);
        sharedPreferences = getSharedPreferences("topnotes.nituk.com.topnotes", MODE_PRIVATE);

        // set listener to the sign in button and superloginlayout
        mSignInButton.setOnClickListener(this);
        superLoginLayout.setOnClickListener(this);


        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        //to open a google activity
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mAuth = FirebaseAuth.getInstance();//firebase authentication object

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

 
       //setting keyListener on the mRNEditText so when user press the enter key it will call directly sign in
        mRNEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(i==KeyEvent.KEYCODE_ENTER&& keyEvent.getAction()==KeyEvent.ACTION_DOWN)
                    onClick(mSignInButton);//passing the view inside onClick whose action you want to perform

                return false;
            }
        });




    }

    @Override
    protected void onStart() {
        super.onStart();
        // if the user is already signed in
        if(mAuth.getCurrentUser()!=null)
        {
           // initialise the user object & Move to the SubjectListActivity
            initUser(sharedPreferences.getString("Name","username"),
                    sharedPreferences.getString("Rn","BT16XXX"),
                    sharedPreferences.getString("Email","example@gmail.com"),
                    sharedPreferences.getString("Imageurl",null));
            moveToSubjectListActivity();
        }


    }
    // Get a google sign in intent
    private void signIn() {


        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);//RC_SIGN_IN is request code

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.

            mProgressDialog.show();


            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {

            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            //it have the data, from this data we will get a configured google account corresponding to that data

            // Signed in successfully, show authenticated UI
            firebaseAuthWithGoogle(account);

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Failed:", e.toString());
            //Toast.makeText(LoginActivity.this,"Google sign in Failed!",Toast.LENGTH_SHORT).show();

        }
    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        Log.d("Authenticating:", "firebaseAuthWithGoogle:" + acct.getId());

        //extracting the information from the account,to sign in in firebase
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                             //Toast.makeText(LoginActivity.this,"Google sign in sucessfull! Details:"+acct.getEmail(),Toast.LENGTH_SHORT).show();

                            //Log.d("success:", "signInWithCredential:success");
                            final FirebaseUser  user = mAuth.getCurrentUser();
                            moveToSubjectListActivity();

                            new Handler().post(

                                    new Runnable() {
                                @Override
                                public void run() {
                                    saveUserInfo(user);
                                }
                            });


                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this,"Google sign in Failed at server due to:"+task.getException(),Toast.LENGTH_SHORT).show();
                            Log.w("failed:", "signInWithCredential:failure", task.getException());
                        }

                    }
                });
    }

    private void moveToSubjectListActivity()
    {
        List<String> subjects = getSubjects();// try to get subjects from shared pref

        if(subjects.size()==0) // no subjects are saved locally
        {
            fetchSubjects();
        }
        else   // subjects are saved locally
        {
            extractSubjectInfo(subjects);
        }
        // moving to the subject list activity will happen soon

       // fetch new subjects in background if any and save to pref for next time
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                checkForChangeInSubjectList();
            }
        });
    }

    // Don't go to the splash on pressing the back button
    @Override
    public void onBackPressed() {

        if(mProgressDialog.isShowing()) {
            mConnectingTextView.setVisibility(View.INVISIBLE);
            mProgressDialog.dismiss();

        }
        else{
            new AlertDialog.Builder(this).setTitle("Exit TopNotes ?")
                    .setMessage("Do you really want to exit ?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finishAffinity();//it will pop up all the activity from the stack
                            finish();
                        }
                    })
                    .setNegativeButton("Cancel",null).show();

        }

    }
    public void saveUserInfo(FirebaseUser user)
    {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(mAuth.getCurrentUser().getUid());
        Map<String,String> map=new HashMap<>();
        String name = mNameEditText.getText().toString();

        String displayName = user.getDisplayName();//it will return the username associated with google account
        String rn =mRNEditText.getText().toString();
        String email = user.getEmail();
        String imageurl = user.getPhotoUrl().toString();

        map.put("Name",displayName);
        map.put("Rn",rn);
        map.put("Email",email);
        map.put("Imageurl",imageurl);

        // initialise the current user object
        initUser(displayName,rn,email,imageurl);
        // save the user info in sharedPreferences to remember the user
        sharedPreferences.edit()
                .putString("Name",displayName)
                .putString("Rn",rn)
                .putString("Email",email)
                .putString("Imageurl",imageurl).apply();

         databaseReference.setValue(map).addOnSuccessListener(this, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // move with all proper information

            }
        });

    }
    public void initUser(String name,String rn,String email,String imageurl)
    {

        User.setUser(name,rn,email,imageurl);
    }

   public boolean validatingDetails(){
            String rollNumber=mRNEditText.getText().toString();

            if(mNameEditText.getText().toString().trim().length()==0){
                mNameEditText.setError("Empty field");
            }
            if(mRNEditText.getText().toString().trim().length()==0){
                mRNEditText.setError("Empty field");
            }

            else if(rollNumber.length()<10||rollNumber.length()>10){
                Toast.makeText(getApplicationContext(),"Please Enter Valid Roll Number ! ",Toast.LENGTH_SHORT).show();
            }
            else if(validatingRollnumber(rollNumber)){
                if(NetworkCheck.isNetworkAvailable(getApplicationContext())) {
                    return  true;
                }else{
                    InternetAlertDialogfragment internetAlertDialogfragment = new InternetAlertDialogfragment();
                    internetAlertDialogfragment.show(getSupportFragmentManager().beginTransaction(), "net_dialog");

                }
            }
            else{
                Toast.makeText(getApplicationContext(),"Please Enter Valid Roll Number ! ",Toast.LENGTH_SHORT).show();

            }
            return false;

        }

    boolean validatingRollnumber(String rollNumber){
        String upper=rollNumber.toUpperCase();
        Log.i("Roll",upper.substring(0,8));

        if(upper.substring(0,8).equals("BT16CSE0")&&(upper.charAt(8)<=54&&upper.charAt(8)>=48)) {
            if(upper.charAt(8)==54 &&(upper.charAt(9)>=48&&upper.charAt(9)<=50)||(upper.charAt(8)<=53&&upper.charAt(8)>=48)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.googlesigninbutton:
                if (validatingDetails()) {
                    // making sure that before login user isn't sign in with mGoogleSiginInClient in firebase
                    //there is difference b/w login in firebase and login with Google sign api
                    //doubt-> why we are using this here, can we use this when user press logout button?
                    mGoogleSignInClient.signOut().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Toast.makeText(LoginActivity.this,"Sign out sucess!",Toast.LENGTH_SHORT).show();
                             mConnectingTextView.setVisibility(View.VISIBLE);
                             signIn();
                        }
                    });
                }
                break;

            case R.id.superLoginLayout:
                //To hide the keyboard from the screen if user press anywhere in outside the editTexts
                InputMethodManager inputMethodManager=(InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
                break;

        }

    }

    // fetching the subjectlist kind of stuff

    public List<String> getSubjects()
    {
        Set<String> subjectsSet = new LinkedHashSet<>();
        subjectsSet=sharedPreferences.getStringSet("subjects",subjectsSet);
        List<String> savedSubjects = new ArrayList<>(subjectsSet);
        Collections.sort(savedSubjects);
        return savedSubjects;

    }

    public void fetchSubjects()
    {

        FirebaseDatabase.getInstance().getReference("subjectNames").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String,String> subjectMap;
                subjectMap = (Map<String,String>)dataSnapshot.getValue();
                List<String> subjects = new ArrayList<>();
                for(Map.Entry<String,String> entry:subjectMap.entrySet())
                {
                    subjects.add(entry.getKey()+":"+entry.getValue());
                }
                Collections.sort(subjects);
                Log.i("subjects",subjects.toString());
                extractSubjectInfo(subjects);
                saveSubjects(subjects);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void saveSubjects(List<String> subjects)
    {
        sharedPreferences.edit().putStringSet("subjects",new LinkedHashSet<>(subjects)).apply();
        Log.i("savedtopref","saved");

    }

    public void extractSubjectInfo(List<String> subjects)
    {
        MyApplication.getApp().subjectNames=new ArrayList<>();
        MyApplication.getApp().subjectNamesToken = new ArrayList<>();
        MyApplication.getApp().spinnerSubjectList = new ArrayList<>();
        MyApplication.getApp().spinnerSubjectList.add("Select");
        for(int i=0;i<subjects.size();i++)
        {
            String[] parts = subjects.get(i).split(":");

            MyApplication.getApp().subjectNamesToken.add(parts[0]);
            MyApplication.getApp().subjectNames.add(parts[1]);
            MyApplication.getApp().spinnerSubjectList.add(parts[1]);

        }
        Log.i("extraction subnames",MyApplication.getApp().subjectNames.toString());
        Log.i("extraction subtokens",MyApplication.getApp().subjectNamesToken.toString());

        // moving to the subject list activity after being sure that we get some list
        mProgressDialog.dismiss();
        Intent intent = new Intent(LoginActivity.this,SubjectListActivity.class);
        startActivity(intent);
    }

    public void checkForChangeInSubjectList()
    {
        FirebaseDatabase.getInstance().getReference("subjectNames").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String,String> subjectMap;
                subjectMap = (Map<String,String>)dataSnapshot.getValue();
                List<String> subjects = new ArrayList<>();
                for(Map.Entry<String,String> entry:subjectMap.entrySet())
                {
                    subjects.add(entry.getKey()+":"+entry.getValue());
                }
                Collections.sort(subjects);
                if(!getSubjects().equals(subjects)) // if there is a new subject list available at server update the preferences.
                {
                    Log.i("update:","subjects are update at server Please restart the app");
                    saveSubjects(subjects);
                }
                else
                {
                    Log.i("update:","No new subjects at the server");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
