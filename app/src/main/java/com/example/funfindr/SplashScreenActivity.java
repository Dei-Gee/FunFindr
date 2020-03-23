package com.example.funfindr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashScreenActivity extends AppCompatActivity {
    // delay time in milliseconds
    private final int SPLASH_DELAY = 2500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // Views
        final ImageView logo = (ImageView) findViewById(R.id.imageViewLogo);

        Handler h = new Handler();

        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                fadeLogoIn(logo);
            }
        }, 500);

        moveToNextActivity(h);
    }

    // This method will animate the logo
    private void fadeLogoIn(ImageView l)
    {
        Animation fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        fadeInAnimation.setDuration(750);

        l.startAnimation(fadeInAnimation);
        l.setVisibility(View.VISIBLE);


    }

    private void moveToNextActivity(Handler handler)
    {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
                finish();
            }
        }, SPLASH_DELAY);
    }
}
