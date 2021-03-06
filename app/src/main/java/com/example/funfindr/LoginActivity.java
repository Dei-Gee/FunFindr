package com.example.funfindr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.funfindr.utilites.handlers.CustomToastHandler;
import com.example.funfindr.utilites.handlers.DatabaseHandler;
import com.example.funfindr.utilites.handlers.SharedPreferencesManager;

import java.util.ArrayList;
import java.util.HashMap;
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
        sharedPreferences = SharedPreferencesManager.newPreferences(MyPREFERENCES, this);
        final SQLiteDatabase database = DatabaseHandler.getWritable(this);

        // CHECK IF USER IS LOGGED IN
        if(sharedPreferences.getBoolean("userLoggedIn", false))
        {
            SharedPreferencesManager.checkIfUserLoggedIn(sharedPreferences, LoginActivity.this);
            finish();
        }

        EditText userEmail = (EditText) findViewById(R.id.editTextEmailLogin);
        EditText userPassword = (EditText) findViewById(R.id.editTextPasswordLogin);
        Button buttonLogin = (Button) findViewById(R.id.buttonLogin);
        TextView textViewSignup = (TextView) findViewById(R.id.textViewSignupButton);

        String[] intentLoginDetails = getIntent().getStringArrayExtra("user");
        String[] formLoginDetails = new String[2];

        DatabaseHandler.getWritable(this);

        goToSignupActivity(textViewSignup);

        goToMainUIActivity(database, buttonLogin, intentLoginDetails, userEmail, userPassword);

    }

    @Override
    protected void onResume() {
        sharedPreferences = SharedPreferencesManager.newPreferences(MyPREFERENCES, this);

        // CHECK IF USER IS LOGGED IN
        if(sharedPreferences.getBoolean("userLoggedIn", false))
        {
            SharedPreferencesManager.checkIfUserLoggedIn(sharedPreferences, LoginActivity.this);
            finish();
        }

        super.onResume();
    }

    @Override
    protected void onRestart() {
        sharedPreferences = SharedPreferencesManager.newPreferences(MyPREFERENCES, this);

        // CHECK IF USER IS LOGGED IN
        if(sharedPreferences.getBoolean("userLoggedIn", false))
        {
            SharedPreferencesManager.checkIfUserLoggedIn(sharedPreferences, LoginActivity.this);
            finish();
        }
        super.onRestart();
    }

    private void goToMainUIActivity(final SQLiteDatabase db, Button button, final String[] iLDetails, final EditText useremail, final EditText userpassword)
    {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Map<String,String>> userData;
                String[] formLoginDetails = new String[2];

                // check if the details are coming from the signup activity
                if(iLDetails == null)
                {
                    formLoginDetails[0] = useremail.getText().toString();
                    formLoginDetails[1] = userpassword.getText().toString();
                    userData = DatabaseHandler.LoginUser(db, formLoginDetails);
                }
                else {
                    userData = DatabaseHandler.LoginUser(db, iLDetails);
                }

                // check if user database is null
                if(userData != null)
                {
                    Log.d("USER DATA", userData.toString());
                    new CustomToastHandler(LoginActivity.this,
                            "Login Successful").generateToast(getResources().getColor(R.color.quantum_googgreenA700), getResources().getColor(R.color.colorWhite));


                    /* Passing User Data into the SharedPrefences to manage the session */
                    HashMap<String,String> data = new HashMap<String,String>();


                    // add userData to database array list
                    for(Map.Entry<String,String> entry : userData.get(0).entrySet())
                    {
                        data.put(entry.getKey(),entry.getValue());
                    }


                    SharedPreferencesManager.editPreferences(LoginActivity.this, "String", sharedPreferences, data);
                    SharedPreferencesManager.editPreferencesBoolean(LoginActivity.this, sharedPreferences, "userLoggedIn", true);
                    startActivity(new Intent(LoginActivity.this, MainUIActivity.class));
                    finish();
                }
                else
                {
                    if(!DatabaseHandler.checkIfUserExists(db, formLoginDetails[0], formLoginDetails[1]))
                    {
                        SharedPreferencesManager.editPreferencesBoolean(LoginActivity.this, sharedPreferences, "userLoggedIn", false);
                        new CustomToastHandler(LoginActivity.this,
                                "Sorry! Login Failed!").generateToast(getResources().getColor(R.color.design_default_color_error), getResources().getColor(R.color.colorWhite));

                    }
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
