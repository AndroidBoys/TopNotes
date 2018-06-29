package topnotes.nituk.com.topnotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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

public class LoginActivity extends AppCompatActivity {

    private EditText mNameEditText;
    private EditText mRNEditText;
    private SignInButton mSignInButton;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN=1;
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mNameEditText = findViewById(R.id.nameEditText);
        mRNEditText = findViewById(R.id.rnEditText);
        mSignInButton = findViewById(R.id.googlesigninbutton);

         sharedPreferences= getSharedPreferences("topnotes.nituk.com.topnotes",MODE_PRIVATE);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mAuth = FirebaseAuth.getInstance();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        // set listener to the sign in button
        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                // For testing purpose
    
                //Intent intent = new Intent(LoginActivity.this,SubjectListActivity.class);
                //startActivity(intent);

                signIn();
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
//        else
//        {
//            mGoogleSignInClient.signOut().addOnSuccessListener(new OnSuccessListener<Void>() {
//                @Override
//                public void onSuccess(Void aVoid) {
//                    Toast.makeText(LoginActivity.this,"Sign out sucess!",Toast.LENGTH_SHORT).show();
//                }
//            });
//        }
    }
    // Get a google sign in intent
    private void signIn() {
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
                    }
                });
    }

    private void moveToSubjectListActivity()
    {
        Intent intent = new Intent(LoginActivity.this,SubjectListActivity.class);
        startActivity(intent);
    }

    // Don't go to the splash on pressing the back button
    @Override
    public void onBackPressed() {
        // Do nothing as of now
    }
    public void saveUserInfo(FirebaseUser user)
    {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
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
}
