package topnotes.nituk.com.topnotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.os.PersistableBundle;

import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInApi;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

public class SubjectListActivity extends AppCompatActivity {


    //This activity contains navigation drawer and fragments
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private ImageView userImageView;
    private TextView userNameTextView,userEmailTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_list);

        drawerLayout=findViewById(R.id.drawerLayout);


        // fetch and set the current user information

        //ActionBarDrawerToggle is a drawable listener which is used to tie together the drawerLayout
        //with the actionBar.
        actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        actionBarDrawerToggle.syncState();

        //Below one will set the icon on the action bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        final NavigationView navigationView=findViewById(R.id.navigationView);

        if(NetworkCheck.isNetworkAvailable(getApplicationContext())) {
            //if network is connected then user will move into Mysubject fragment
            addDifferentFragments(MySubjects.getInstance());//it will show the list of subjects when this activity will be opened.

        }else{
            //if network is not connected then user will move into DownloadFragment.
            addDifferentFragments(DownloadFirstFragment.getInstance());
        }




        View header = navigationView.getHeaderView(0);
        userNameTextView=header.findViewById(R.id.userNameTextView);
        userImageView=header.findViewById(R.id.userImageView);
        userEmailTextView=header.findViewById(R.id.userEmailTextView);

        //This below method is used for click events of navigaiton

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            MenuItem lastMenuItemSelected=null;
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if(lastMenuItemSelected!=null){
                    lastMenuItemSelected.setChecked(false);

                }
                menuItem.setChecked(true);
                lastMenuItemSelected=menuItem;

                switch (menuItem.getItemId())
                {

                    case R.id.mySubjects:

                         MySubjects mySubjects = MySubjects.getInstance();
                        addDifferentFragments(mySubjects);//it will set the subject list fragment in frameLayout.
                        break;

                    case R.id.myDownloads:
                        addDifferentFragments(DownloadFirstFragment.getInstance());
                        //MyDownloads fragment will be added
                        break;

                    case R.id.myuploads:
                        //MyUploads fragment will be added
                        UploadFragment uploadFragment = UploadFragment.getInstance();
                        addDifferentFragments(uploadFragment);
                        break;

                    case R.id.leaderboard:
                        //Toast.makeText(SubjectListActivity.this,"leaderboard selected",Toast.LENGTH_SHORT).show();
                        //LeaderFragment will be added
                        break;

                    case R.id.contactUs:
                        //contactUs fragment will be added
                        break;

                    case R.id.aboutUs:
                        //aboutUs fragment will be added
                        break;

                    case R.id.logOut:
                        //logOut fragment will be added
                        signOut();
                        break;

                }

                drawerLayout.closeDrawer(Gravity.START);
                return true;
            }
        });
        //set current user info to the dashboard
        setCurrentUserInfo();
        // for bug fixing in api > 24 when firing the pdf intent
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder(); StrictMode.setVmPolicy(builder.build());
        // for api >= 23 asking for all runtime permissions
         if(!hasAllPermissions())
         askForPermissions();
    }

    /*public void onSaveInstance(Bundle outState) {
        ArrayList positions = findSelectedPosition();
        if(positions.size()>0) {
            outState.putIntegerArrayList(STATE_SELECTED_POSITION, positions);
        }
    }

    private ArrayList findSelectedPosition() {
        Menu menu = navDrawerFirstPart.getMenu();
        int count = menu.size();
        ArrayList result = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            if(menu.getItem(i).isChecked()){
                result.add(i);
            }
        }
        return result;
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        if(savedInstanceState.containsKey(STATE_SELECTED_POSITION)){
            restoreSelectedPosition(savedInstanceState.getIntegerArrayList(STATE_SELECTED_POSITION));
        }
    }

    private void restoreSelectedPosition(ArrayList<Integer> positions) {
        Menu menu = navDrawerFirstPart.getMenu();
        for(int i=0; i<positions.size(); i++){
            menu.getItem(positions.get(i)).setChecked(true);
        }
    }*/

    private void setCurrentUserInfo() {
//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
//        databaseReference.child("Name").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                userNameTextView.setText(dataSnapshot.getValue().toString());
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//        Log.i("retrieved name:",databaseReference.child("Name").getKey());
//        databaseReference.child("Email").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//            userEmailTextView.setText(dataSnapshot.getValue().toString());
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//        databaseReference.child("Imageurl").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
        // if the user object is not initialised in LoginActivity than it might throw null pointer exception be careful
        userNameTextView.setText(User.getUser().getName());
        userEmailTextView.setText(User.getUser().getEmail());
        Picasso.get().load(User.getUser().getImageUrl()).into(userImageView);

    }

    void addDifferentFragments(Fragment replacableFragment){
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        // to set a custom animation in fragment
//        fragmentTransaction.setCustomAnimations(R.anim.fragment_open_enter,
//                R.anim.fragment_open_exit, R.anim.fragment_close_enter,
//                R.anim.fragment_close_exit);
//        fragmentTransaction.setCustomAnimations(R.anim.fade_in_dialog,R.anim.fade_out_dialog);
//        Log.d("above replace","this is me");
        fragmentTransaction.replace(R.id.frameLayout,replacableFragment);
//        fragmentTransaction.commitNow();
//        Log.d("below replce","this is me");
        //
//        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        Log.d("after commit","this is me");

    }

    //This below function is used the selection of item in action bar.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawerLayout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //exit from the app
            finish();
        }
    }

    // sign out

    private void signOut()
    {
        FirebaseAuth.getInstance().signOut();
        finish();

        LoginActivity.mConnectingTextView.setVisibility(View.INVISIBLE);
    }

    private void askForPermissions()
    {
        String permissions[]={Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.ACCESS_NETWORK_STATE};
        if(Build.VERSION.SDK_INT>=23)
        requestPermissions(permissions,1);
    }


    private boolean hasAllPermissions()
    {
        if(Build.VERSION.SDK_INT>=23)
        return ActivityCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED
                &&ActivityCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED
                &&ActivityCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.ACCESS_NETWORK_STATE)==PackageManager.PERMISSION_GRANTED;

        return true;
    }
}
