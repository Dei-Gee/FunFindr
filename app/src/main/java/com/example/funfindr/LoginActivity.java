package com.example.funfindr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_small);
        // get size of the screen in pixels
//        DisplayMetrics displayMetrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//        int height = displayMetrics.heightPixels;
//        int width = displayMetrics.widthPixels;
//
//        if(width > 480)
//        {
//            setContentView(R.layout.activity_main);
//        }
//        else
//        {
//            setContentView(R.layout.activity_main_small);
//        }

        Button buttonLogin = (Button) findViewById(R.id.buttonLogin);
        TextView textViewSignup = (TextView) findViewById(R.id.textViewSignupButton);

        goToSignupActivity(textViewSignup);
        goToMainUIActivity(buttonLogin);

    }

    private void goToMainUIActivity(Button button)
    {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, MainUIActivity.class));
            }
        });
    }

    private void goToSignupActivity(TextView textView)
    {
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });
    }
}
