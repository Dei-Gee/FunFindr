package com.example.funfindr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.funfindr.database.models.User;
import com.example.funfindr.utilites.handlers.CustomToastHandler;
import com.example.funfindr.utilites.handlers.DatabaseHandler;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class SignupActivity extends AppCompatActivity {


    private static final int SELECT_PHOTO = 1;
    private static final int CAPTURE_PHOTO = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Writeable Database
        final SQLiteDatabase database = DatabaseHandler.getWritable(this);

        // VIEWS
        final EditText firstnameInput = (EditText) findViewById(R.id.editTextFirstName);
        final EditText lastnameInput = (EditText) findViewById(R.id.editTextLastName);
        final EditText emailInput = (EditText) findViewById(R.id.editTextEmailSignup);
        final EditText passwordInput = (EditText) findViewById(R.id.editTextPasswordSignup);
        Button submitButton = (Button) findViewById(R.id.buttonSignup);

        if(ContextCompat.checkSelfPermission(SignupActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(SignupActivity.this, new String[] {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }
        else
        {

        }

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // instantiate new User object
                User newUser = new User();
                newUser.setFirstname(firstnameInput.getText().toString());
                newUser.setLastname(lastnameInput.getText().toString());
                newUser.setEmail(emailInput.getText().toString());
                newUser.setPassword(passwordInput.getText().toString());
//                newUser.setProfileImage();
                String[] userLoginDetails;
                Intent newIntent;

                // Login Details
                userLoginDetails = new String[2];
                userLoginDetails[0] = newUser.getEmail();
                userLoginDetails[1] = newUser.getPassword();

                if(newUser.getFirstname() == "" || newUser.getLastname() == "" ||
                        newUser.getEmail() == "" || newUser.getPassword() == "")
                {
                    new CustomToastHandler(SignupActivity.this,
                            "Please complete the form!").
                            generateToast(R.color.design_default_color_error, R.color.colorWhite);
                }
                else if(newUser.getFirstname() == null || newUser.getLastname() == null ||
                        newUser.getEmail() == null || newUser.getPassword() == null)
                {
                    new CustomToastHandler(SignupActivity.this,
                            "Please complete the form!").
                            generateToast(R.color.design_default_color_error, R.color.colorWhite);
                }
                else
                {
                    if(DatabaseHandler.SignupUser(database, newUser))
                    {
                        Toast.makeText(SignupActivity.this, "You have been signed up", Toast.LENGTH_SHORT).show();
                        newIntent = new Intent(SignupActivity.this, LoginActivity.class).putExtra("user", userLoginDetails);
                        startActivity(newIntent);
                        finish();
                    }
                    else
                    {
                        if(DatabaseHandler.checkIfUserExists(database, userLoginDetails[0], userLoginDetails[1]) == true)
                        {
                            Toast.makeText(SignupActivity.this, "Email/Password already taken", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(SignupActivity.this, "Error! Signup failed!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }



            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SELECT_PHOTO){
            if(resultCode == RESULT_OK) {
                try {
                    final Uri imageUri = data.getData();
                    final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }

        }else if(requestCode == CAPTURE_PHOTO){
            if(resultCode == RESULT_OK) {
                // Creat success toast
            }
        }
    }


}
