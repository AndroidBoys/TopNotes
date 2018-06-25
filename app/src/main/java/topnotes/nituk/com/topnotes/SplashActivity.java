package topnotes.nituk.com.topnotes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

// Sohan create a splash of 4 sec which leads to the login activity

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }
}
