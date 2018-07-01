package topnotes.nituk.com.topnotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mAuth = FirebaseAuth.getInstance();

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

        mProgressDialog = new ProgressDialog(LoginActivity.this);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.show();

        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

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

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                             Toast.makeText(LoginActivity.this,"Google sign in sucessfull! Details:"+acct.getEmail(),Toast.LENGTH_SHORT).show();

                            Log.d("success:", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            saveUserInfo(user);

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this,"Google sign in Failed at server due to:"+task.getException(),Toast.LENGTH_SHORT).show();
                            Log.w("failed:", "signInWithCredential:failure", task.getException());
                        }
                        mProgressDialog.dismiss();
                    }
                });
    }

    private void moveToSubjectListActivity()
    {
        Intent intent = new Intent(LoginActivity.this,SubjectListActivity.class);
        startActivity(intent);
        //
    }

    // Don't go to the splash on pressing the back button
    @Override
    public void onBackPressed() {

        if(mProgressDialog.isShowing())
        mProgressDialog.dismiss();
        // Do nothing as of now
    }
    public void saveUserInfo(FirebaseUser user)
    {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(mAuth.getCurrentUser().getUid());
        Map<String,String> map=new HashMap<>();
        String name = mNameEditText.getText().toString();
        String displayName = user.getDisplayName();
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
                moveToSubjectListActivity();
            }
        });
//        databaseReference.child("Name").setValue(mNameEditText.getText());
//        databaseReference.child("Rn").setValue(mRNEditText.getText());
//        databaseReference.child("Email").setValue(user.getEmail());
//        databaseReference.child("Imageurl").setValue(user.getPhotoUrl());

    }
    public void initUser(String name,String rn,String email,String imageurl)
    {
        User.getUser(name,rn,email,imageurl);
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
}
