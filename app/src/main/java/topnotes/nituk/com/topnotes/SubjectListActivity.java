package topnotes.nituk.com.topnotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class SubjectListActivity extends AppCompatActivity {


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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView=findViewById(R.id.navigationView);

        //This below method is used for click events of navigaiton menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId())
                {

                    case R.id.mySubjects:
                        Toast.makeText(SubjectListActivity.this,"mySubject selected",Toast.LENGTH_SHORT).show();
                        return true;

                    case R.id.myDownloads:
                        //MyDownloads fragment will be added
                        return true;

                    case R.id.myuploads:
                        //MyUploads fragment will be added
                        return true;

                    case R.id.leaderboard:
                        //Toast.makeText(SubjectListActivity.this,"leaderboard selected",Toast.LENGTH_SHORT).show();
                        //LeaderFragment will be added
                        return true;

                    case R.id.contactUs:
                        //contactUs fragment will be added
                        return true;

                    case R.id.aboutUs:
                        //aboutUs fragment will be added
                        return true;

                    case R.id.logOut:
                        //logOut fragment will be added
                        return true;
                }
                return false;
            }
        });


    }

    //This below function is for the selection of item in action bar.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
