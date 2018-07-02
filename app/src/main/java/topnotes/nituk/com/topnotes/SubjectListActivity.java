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
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;

import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;

import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class SubjectListActivity extends AppCompatActivity {


    //This activity contains navigation drawer and fragments
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private ImageView userImageView;
    private TextView userNameTextView, userEmailTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_list);

        drawerLayout = findViewById(R.id.drawerLayout);


        // fetch and set the current user information

        //ActionBarDrawerToggle is a drawable listener which is used to tie together the drawerLayout
        //with the actionBar.
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        actionBarDrawerToggle.syncState();

        //Below one will set the icon on the action bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        final NavigationView navigationView = findViewById(R.id.navigationView);




        View header = navigationView.getHeaderView(0);
        userNameTextView = header.findViewById(R.id.userNameTextView);
        userImageView = header.findViewById(R.id.userImageView);
        userEmailTextView = header.findViewById(R.id.userEmailTextView);

        //This below method is used for click events of navigaiton

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            MenuItem lastMenuItemSelected = null;

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (lastMenuItemSelected != null) {
                    lastMenuItemSelected.setChecked(false);

                }
                menuItem.setChecked(true);
                lastMenuItemSelected = menuItem;

                switch (menuItem.getItemId()) {

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
                        addDifferentFragments(ContactUsFragment.getInstance());
                        //contactUs fragment will be added
                        break;

                    case R.id.aboutUs:
                        //aboutUs fragment will be added
                        addDifferentFragments(new AboutUsFragment());
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
        if (NetworkCheck.isNetworkAvailable(getApplicationContext())) {
            //if network is connected then user will move into Mysubject fragment
            addDifferentFragments(MySubjects.getInstance());//it will show the list of subjects when this activity will be opened.
            //navigationView.getMenu().getItem(0).setChecked(true);
            navigationView.setCheckedItem(R.id.mySubjects);

        } else {
            //if network is not connected then user will move into DownloadFragment.
            addDifferentFragments(DownloadFirstFragment.getInstance());
//            navigationView.getMenu().getItem(1).setChecked(true);
            navigationView.setCheckedItem(R.id.myDownloads);
        }


        //set current user info to the dashboard
        setCurrentUserInfo();
        // for bug fixing in api > 24 when firing the pdf intent
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        // for api >= 23 asking for all runtime permissions
        if (!hasAllPermissions())
            askForPermissions();
    }



    private void setCurrentUserInfo() {
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
        fragmentTransaction.addToBackStack(null);
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
            new AlertDialog.Builder(this).setTitle("Exit TopNotes?")
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
