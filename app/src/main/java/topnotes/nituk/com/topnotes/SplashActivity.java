package topnotes.nituk.com.topnotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

// Sohan create a splash of 4 sec which leads to the login activity

public class SplashActivity extends AppCompatActivity {
    ImageView imageView;
    ProgressBar progressBar;
    TextView quotesTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        // Action bar is depricated from the splash
        getSupportActionBar().hide();

        quotesTextView=findViewById(R.id.quotesTextView);
        imageView=findViewById(R.id.imageViewIcon);
        imageView.setAlpha((float) 0);
        progressBar=findViewById(R.id.progressBar);
        progressBar.setMax(4000);


        new CountDownTimer(4000,1){
            @Override
            public void onTick(long l) {

                progressBar.setProgress((4000-(int)l));
                imageView.setAlpha((float)(4000-l)/4000);
            }

            @Override
            public void onFinish() {
                //when thread is completed this method will be called
                Intent intent=new Intent(SplashActivity.this,LoginActivity.class);
                startActivity(intent);

            }
        }.start();

    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}