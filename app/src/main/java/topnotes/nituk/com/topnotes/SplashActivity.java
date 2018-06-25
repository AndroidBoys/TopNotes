package topnotes.nituk.com.topnotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;

// Sohan create a splash of 4 sec which leads to the login activity

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new CountDownTimer(4000,1000){
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                Intent intent=new Intent(SplashActivity.this,LoginActivity.class);
                startActivity(intent);

            }
        }.start();



    }
}
