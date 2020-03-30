package com.example.funfindr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.funfindr.utilites.DatabaseHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;


public class LoginActivity extends AppCompatActivity {

    /* GLOBALS */
    public static final String MyPREFERENCES = "MyPrefs" ;
    // SESSION MANAGEMENT
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText userEmail = (EditText) findViewById(R.id.editTextEmailLogin);
        EditText userPassword = (EditText) findViewById(R.id.editTextPasswordLogin);
        Button buttonLogin = (Button) findViewById(R.id.buttonLogin);
        TextView textViewSignup = (TextView) findViewById(R.id.textViewSignupButton);

        String[] intentLoginDetails = getIntent().getStringArrayExtra("user");
        String[] formLoginDetails = new String[2];

        DatabaseHandler.getWritable(this);
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        goToSignupActivity(textViewSignup);

        goToMainUIActivity(buttonLogin, intentLoginDetails, userEmail, userPassword);

    }

    private void goToMainUIActivity(Button button, final String[] iLDetails, final EditText useremail, final EditText userpassword)
    {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                ArrayList<Map<String,String>> userData;
                String[] formLoginDetails = new String[2];

                // check if the details are coming from the signup activity
                if(iLDetails == null)
                {
                    formLoginDetails[0] = useremail.getText().toString();
                    formLoginDetails[1] = userpassword.getText().toString();
                    userData = DatabaseHandler.LoginUser(formLoginDetails);
                }
                else {
                    userData = DatabaseHandler.LoginUser(iLDetails);
                }

                // check if user data is null
                if(userData != null)
                {
                    Log.d("USER DATA", userData.toString());
//                    editor.putString("firstname", userData)
                    startActivity(new Intent(LoginActivity.this, MainUIActivity.class));
                }
                else
                {

                }

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
