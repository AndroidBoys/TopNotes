package topnotes.nituk.com.topnotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;

import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;
import java.util.Stack;

public class SubjectListActivity extends AppCompatActivity {


    //This activity contains navigation drawer and fragments
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private ImageView userImageView;
    private TextView userNameTextView, userEmailTextView;
    protected static Stack stack;
    private NavigationView navigationView;
    private int flag=0;
    private boolean firstTime=true;
    static int colorFlag=0;
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        registerReceiver(onComplete,new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                    saveQuotesInSharePreferences();
            }
        });

        if(colorFlag==1){
            //As we are giving user to change the theme of an activity so initially we will not set the
            //theme.when user will choose the theme then colorFlag value will be toggle and theme will be
            //set.
            setTheme(getIntent().getIntExtra("theme",0));
        }

        //setTheme(R.style.yellowTheme);
        setContentView(R.layout.activity_subject_list);
        stack=new Stack();
        ////

        drawerLayout = findViewById(R.id.drawerLayout);


        // fetch and set the current user information

        //ActionBarDrawerToggle is a drawable listener which is used to tie together the drawerLayout
        //with the actionBar.
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        //it is working fine but don't know how??
        actionBarDrawerToggle.syncState();

        //Below one will set the icon on the action bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        navigationView = findViewById(R.id.navigationView);


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

                stack.push(new Integer(menuItem.getItemId()));
                Log.i("Inside menuitem",String.valueOf(menuItem.getItemId()));
                switch (menuItem.getItemId()) {


                    case R.id.mySubjects:

                        MySubjects mySubjects = MySubjects.getInstance();
                        addDifferentFragments(mySubjects,"subjects");//it will set the subject list fragment in frameLayout.
                        break;

                    case R.id.myDownloads:
                        addDifferentFragments(DownloadFirstFragment.getInstance(),"downloads");
                        //MyDownloads fragment will be added
                        break;

                    case R.id.myuploads:
                        //MyUploads fragment will be added
                        UploadFragment uploadFragment = UploadFragment.getInstance();
                        addDifferentFragments(uploadFragment,"uploads");
                        break;

                    case R.id.announcement:
                        //Toast.makeText(SubjectListActivity.this,"announcement selected",Toast.LENGTH_SHORT).show();
                        //LeaderFragment will be added
                        addDifferentFragments(new AnnouncementFragment(),"announcement");
                        break;

                    case R.id.contactUs:
                        addDifferentFragments(ContactUsFragment.getInstance(),"contactUs");
                        //contactUs fragment will be added
                        break;

                    case R.id.aboutUs:
                        //aboutUs fragment will be added
                        addDifferentFragments(new AboutUsFragment(),"aboutUs");
                        break;

                    case R.id.logOut:
                        //logOut fragment will be added
                        new AlertDialog.Builder(SubjectListActivity.this)
                                .setTitle("LogOut")
                                .setMessage("All your data will be lost  :(-\nDo you really want to Logout ? ")
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        signOut();
                                    }
                                })
                                .setNegativeButton("No",null).show();
                        break;
                }

                drawerLayout.closeDrawer(Gravity.START);
                return true;
            }
        });
        if (NetworkCheck.isNetworkAvailable(getApplicationContext())) {
            //if network is connected then user will move into Mysubject fragment
            addDifferentFragments(MySubjects.getInstance(),"downloads");//it will show the list of subjects when this activity will be opened.
            //navigationView.getMenu().getItem(0).setChecked(true);
            stack.push(R.id.mySubjects);//Pusing the id in the stack when app opened first
            navigationView.setCheckedItem(R.id.mySubjects);

        } else {
            //if network is not connected then user will move into DownloadFragment.
            addDifferentFragments(DownloadFirstFragment.getInstance(),"subjects");
            stack.push(R.id.myDownloads);//Pusing the id in the stack when app opened first
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


        // testing purpose
//        if(getIntent()!=null)
//        {
//            Intent i = getIntent();
//            String extra = i.getStringExtra("fromSplashActivity");
//            if(extra!=null)
//            {
//                Toast.makeText(this, "fromSplashActivity:"+extra, Toast.LENGTH_SHORT).show();
//            }
//            else
//            {
//                Toast.makeText(this, "well that's sad :(", Toast.LENGTH_SHORT).show();
//            }
//        }
    }



    private void setCurrentUserInfo() {
        // if the user object is not initialised in LoginActivity than it might throw null pointer exception be careful
        userNameTextView.setText(User.getUser().getName());
        userEmailTextView.setText(User.getUser().getEmail());
        Picasso.get().load(User.getUser().getImageUrl()).into(userImageView);

    }

    void addDifferentFragments(Fragment replacableFragment,String tag){
        //
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        // to set a custom animation in fragment
//        fragmentTransaction.setCustomAnimations(R.anim.fragment_open_enter,
//                R.anim.fragment_open_exit, R.anim.fragment_close_enter,
//                R.anim.fragment_close_exit);
//        fragmentTransaction.setCustomAnimations(R.anim.fade_in_dialog,R.anim.fade_out_dialog);
//        Log.d("above replace","this is me");
        fragmentTransaction.replace(R.id.frameLayout,replacableFragment,tag);
//        fragmentTransaction.commitNow();
//        Log.d("below replace","this is me");
        //
        fragmentTransaction.addToBackStack(null );//it will push the fragment in the stack
        fragmentTransaction.commit();
        Log.d("after commit","this is me");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.setting_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    //This below function is used the selection of item in action bar.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        item.setChecked(true);

        Intent intent=new Intent(this,SubjectListActivity.class);
//        Intent contentIntent=new Intent(this,ContentsActivity.class);
        switch(item.getItemId()){

            case R.id.redTheme:
                intent.putExtra("theme",R.style.redTheme);
                startActivity(intent);

                break;

            case R.id.purpleTheme:
                intent.putExtra("theme",R.style.purpleTheme);
                startActivity(intent);
                break;

            case R.id.greenTheme:
                intent.putExtra("theme",R.style.greenTheme);
                startActivity(intent);
                break;

            case R.id.defaultTheme:
                intent.putExtra("theme",R.style.AppTheme);
                startActivity(intent);
                break;


        }

        colorFlag=1;
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {


        DrawerLayout drawer = findViewById(R.id.drawerLayout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else if(getSupportFragmentManager().getBackStackEntryCount()!=1) {
//                    Fragment fragment=getSupportFragmentManager().findFragmentByTag("subjects");
//                    if(==fragment && !firstTime){
//                        //This if condition is to check if user reach in MySubject fragment or not.
//                        //Because we are pushing fragment into the stack. if we reach in MySubject fragment and
//                        //there are fragments present in stack, As we don't to go from MySubject fragment to another
//                        //fragment that's why i put this condition here.
//                        flag=1;
//                    }else {
                        stack.pop();
                        getSupportFragmentManager().popBackStack(); //it will pop the fragment from the stack
                        Integer menuId = (Integer) stack.peek();
//                      Log.i("menuId", String.valueOf(menuId));
                        navigationView.setCheckedItem(menuId);
                        //firstTime=false;
        } else {
            alertDialog();
        }
    }

    void alertDialog(){
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

    public void setActionBarTitle(String title){
        getSupportActionBar().setTitle(title);
    }

    public void saveQuotesInSharePreferences(){
        FirebaseDatabase.getInstance().getReference().child("quotes")
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sharedPreferences=getSharedPreferences("topnotes.nituk.com.topnotes.quotes",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                try {
                    editor.putString("quotes", dataSnapshot.getValue().toString())
                            .apply();
                }catch(Exception e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // Reciever to recieve the download complete event
    BroadcastReceiver onComplete = new BroadcastReceiver() {

        public void onReceive(Context ctxt, Intent intent) {

            // get the refid from the download manager
            long referenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

           // remove it from our list
           List<Long> list=MyApplication.getApp().getDownloadList();
           list.remove(referenceId);

// if list is empty means all downloads completed
            if (list.isEmpty())
            {
              // increment the downloads counter;

                AnotherContentDownloader.getInstance(ctxt).incrementDownloadCounter();




// show a notification
                Log.e("INSIDE", "" + referenceId);
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(SubjectListActivity.this)
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentTitle("TopNotes")
                                .setContentText("Download Completed");


                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(455, mBuilder.build());

                Toast.makeText(MyApplication.getApp(), "Download completed!", Toast.LENGTH_SHORT).show();
            }

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(onComplete);
    }


}

