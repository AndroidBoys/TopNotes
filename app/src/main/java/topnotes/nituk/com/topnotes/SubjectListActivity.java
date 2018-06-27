package topnotes.nituk.com.topnotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class SubjectListActivity extends AppCompatActivity {


    //This activity contains navigation drawer and fragments
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_list);

        drawerLayout=findViewById(R.id.drawerLayout);

        //ActionBarDrawerToggle is a drawable listener which is used to tie together the drawerLayout
        //with the actionBar.
        actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        actionBarDrawerToggle.syncState();

        //Below one will set the icon on the action bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        addDifferentFragments(MySubjects.getInstance());//it will show the list of subjects when this activity will be opened.

        NavigationView navigationView=findViewById(R.id.navigationView);

        //This below method is used for click events of navigaiton menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId())
                {

                    case R.id.mySubjects:
                        Toast.makeText(SubjectListActivity.this,"mySubject selected",Toast.LENGTH_SHORT).show();
                         MySubjects mySubjects = MySubjects.getInstance();
                        addDifferentFragments(mySubjects);//it will set the subject list fragment in frameLayout.
                        break;

                    case R.id.myDownloads:
                        addDifferentFragments(DownloadFragment.getInstance());
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
                        //logOut fragment will be adde
                        signOut();
                        break;

                }
                drawerLayout.closeDrawer(Gravity.START);
                return true;
            }
        });


    }

    void addDifferentFragments(Fragment replacableFragment){
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        // to set a custom animation in fragment
        fragmentTransaction.setCustomAnimations(R.anim.fragment_open_enter,
                R.anim.fragment_open_exit, R.anim.fragment_close_enter,
                R.anim.fragment_close_exit);
        fragmentTransaction.replace(R.id.frameLayout,replacableFragment);
        fragmentTransaction.commit();
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
            //exit from the app//
        }
    }

    // sign out
    private void signOut()
    {
        FirebaseAuth.getInstance().signOut();
        finish();

    }
}
