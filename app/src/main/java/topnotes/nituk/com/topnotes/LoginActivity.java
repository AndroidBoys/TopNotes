package topnotes.nituk.com.topnotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //This below codde is just for testing purpose.
        //Don't need to add this one into your project
        Intent intent=new Intent(LoginActivity.this,SubjectListActivity.class);
        startActivity(intent);

    }
}
