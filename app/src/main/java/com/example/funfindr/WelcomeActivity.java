package com.example.funfindr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

import com.example.funfindr.utilites.handlers.SharedPreferencesManager;

public class WelcomeActivity extends AppCompatActivity {

    /* GLOBALS */
    public static final String MyPREFERENCES = "MyPrefs" ;

    // SESSION MANAGEMENT
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        sharedPreferences = SharedPreferencesManager.newPreferences(MyPREFERENCES, this);

        // CHECK IF USER IS LOGGED IN
        if(sharedPreferences.getBoolean("userLoggedIn", false))
        {
            SharedPreferencesManager.checkIfUserLoggedIn(sharedPreferences, WelcomeActivity.this);
            finish();
        }

        // VIDEO
        VideoView videoView;
        Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.bg_video);
        videoView = findViewById(R.id.videoViewBackground);
        videoView.setVideoURI(uri);
        videoView.start();

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setLooping(true);
            }
        });

        // BUTTONS
        Button toLogin = findViewById(R.id.buttonToLogin);
        Button toRegister = findViewById(R.id.buttonToRegister);

        toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
            }
        });

        toRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WelcomeActivity.this, SignupActivity.class));
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        // VIDEO
        VideoView videoView;
        Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.bg_video);
        videoView = findViewById(R.id.videoViewBackground);
        videoView.setVideoURI(uri);
        videoView.start();

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setLooping(true);
            }
        });
        sharedPreferences = SharedPreferencesManager.newPreferences(MyPREFERENCES, this);

        // CHECK IF USER IS LOGGED IN
        if(sharedPreferences.getBoolean("userLoggedIn", false))
        {
            SharedPreferencesManager.checkIfUserLoggedIn(sharedPreferences, WelcomeActivity.this);
            finish();
        }
    }

    @Override
    protected void onRestart() {
        sharedPreferences = SharedPreferencesManager.newPreferences(MyPREFERENCES, this);

        // CHECK IF USER IS LOGGED IN
        if(sharedPreferences.getBoolean("userLoggedIn", false))
        {
            SharedPreferencesManager.checkIfUserLoggedIn(sharedPreferences, WelcomeActivity.this);
            finish();
        }
        super.onRestart();
    }
}
