package com.example.funfindr.utilites;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.example.funfindr.LoginActivity;
import com.example.funfindr.MainUIActivity;

import java.util.ArrayList;

public class SharedPreferencesManager {
    private static final String DEFAULT_MESSAGE = "Value not found";

    /**
     * Creates and new SharedPreferences instance or calls an existing one
     * @param name
     * @return returns SharedPreferencs object
     */
    public static SharedPreferences newPreferences(String name, Context context)
    {
        SharedPreferences newPrefs = context.getSharedPreferences(name, context.MODE_PRIVATE);
        return newPrefs;
    }

    /**
     * Edits the values in the shared preferences
     * @param context The context of the Activity or Fragment calling this method
     * @param type The type of data to be passed
     * @param prefs The SharedPreferences Object
     * @param data The data to be passed
     */
    public static void editPreferences(Context context, String type, SharedPreferences prefs, ArrayList<String> data)
    {
        SharedPreferences.Editor editor = prefs.edit();

        switch(type.toLowerCase())
        {
            case "string":
                for(String s : data)
                {
                    editor.putString(s.toLowerCase(), s);
                }
                editor.commit();
            break;
            case "integer":
                for(String s : data)
                {
                    editor.putInt(s.toLowerCase(), Integer.parseInt(s));
                }
                editor.commit();
            break;
            default:
                Log.d("ERROR! => ", "SOMETHING WENT WRONG");
        }

    }

    /**
     * Edits the values in the shared preferences
     * @param context The context of the Activity or Fragment calling this method
     * @param prefs The SharedPreferences Object
     * @param key The key of the new entry
     * @param value The value of the new entry
     */
    public static void editPreferencesBoolean(Context context, SharedPreferences prefs, String key, boolean value)
    {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(key,value);
        editor.commit();

    }

    /**
     * Checks if the SharedPreferences Object contains the value based on the key passed
     * @param prefs The SharedPreferences Object
     * @param key The key used to search
     * @return returns a boolean value representing whether the SharedPreferences Object contains
     *          any such value
     */
    public static boolean checkIfSharedPreferencesContain(SharedPreferences prefs, String key)
    {
        return prefs.contains(key);
    }

    /**
     * Returns a string value of the SharedPreferences data based on a its key
     * @param prefs The SharedPreferences Object
     * @param key The key used to find the data
     * @return returns the value of the key passed
     */
    public static String getString (SharedPreferences prefs, String key)
    {
        return prefs.getString(key, DEFAULT_MESSAGE);
    }

    /**
     * Checks if the user is still logged in
     * @param prefs The SharedPreferences Objec
     * @param context The context of the Activity or Fragment calling this method
     */
    public static void checkIfUserLoggedIn(SharedPreferences prefs, Context context)
    {
        if(prefs != null && prefs.contains("userLoggedIn"))
        {
            if(prefs.getBoolean("userLoggedIn", false))
            {
                Toast.makeText(context, "Logging in...", Toast.LENGTH_SHORT).show();
                Toast.makeText(context, "Login Successful!", Toast.LENGTH_SHORT).show();
                context.startActivity(new Intent(context, MainUIActivity.class));
            }
        }
    }
}