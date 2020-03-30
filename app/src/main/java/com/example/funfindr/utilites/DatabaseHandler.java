package com.example.funfindr.utilites;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.funfindr.LoginActivity;
import com.example.funfindr.data.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class DatabaseHandler {
    private static SQLiteDatabase sqldb = null;
    private static FunFindrDB ffdb = null;

    public static SQLiteDatabase getWritable(Context context)
    {
        if(ffdb == null)
        {
            ffdb = new FunFindrDB(context);
        }

        if(sqldb == null)
        {
            sqldb = ffdb.getWritableDatabase();
        }

        return sqldb;
    }

    /**
     * This attempts to add the user to the database and creat a new account
     * @return returns a boolean value that indicates whether the signup method was successful
     */
    public static boolean SignupUser(User user)
    {
        boolean success = false;
        HashMap<String,String> map = new HashMap<String,String>();

        map.put("firstname", user.getFirstname());
        map.put("lastname", user.getLastname());
        map.put("email", user.getEmail());
        map.put("password", user.getPassword());
        map.put("profile_pic", null);

        String[] returnColumns =  new String[]{"firstname", "lastname", "email", "password"};
        HashMap<String,String> selectMap = new HashMap<String,String>();
        selectMap.put("email", user.getEmail());
        selectMap.put("password", user.getPassword());


        if(!checkIfUserExists(selectMap.get("email"), selectMap.get("password")))
        {
            if(DatabaseTableHandler.insert(sqldb, "users", map))
            {
                success = true;
            }
        }

        return success;
    }

    /**
     * This attempts to add the user to the database and creat a new account
     * @return returns the userData if the login method was successful or null if it was unsuccessful
     */
    public static ArrayList<Map<String,String>> LoginUser(String[] loginDetails)
    {
        String[] returnColumns =  new String[]{"firstname", "lastname", "email", "password"};
        HashMap<String,String> map = new HashMap<String,String>();
        map.put("email", loginDetails[0]);
        map.put("password", loginDetails[1]);

        ArrayList<Map<String,String>> userData = null;

        // if user exists get the user data
        if(checkIfUserExists(map.get("email"), map.get("password")))
        {
            userData = DatabaseTableHandler.select(sqldb, true, "users", returnColumns, map, null);
        }

        return userData;
    }

    /**
     * Logs out the user by destroying the shared preferences
     * @param prefs The SharedPreferences Object
     */
    public static void LogoutUser(SharedPreferences prefs, Context context)
    {
        prefs.edit().clear();
        SharedPreferencesManager.editPreferencesBoolean(context, prefs, "userLoggedIn", false);


        Log.d("BOOLEAN => ", String.valueOf(prefs.getBoolean("blah", false)));

        if(!prefs.getBoolean("userLoggedIn", false))
        {
            context.startActivity(new Intent(context, LoginActivity.class));
        }
    }

    /**
     * Checks if any user in the database has the smae email and password
     * @param email
     * @param password
     * @return returns whether a user exists with the passed in credentials or not
     */
    public static boolean checkIfUserExists(String email, String password)
    {
        String[] returnColumns =  new String[]{"firstname", "lastname", "email", "password"};
        HashMap<String,String> selectMap = new HashMap<String,String>();
        selectMap.put("email", email);
        selectMap.put("password", password);

        ArrayList<Map<String,String>> userData = DatabaseTableHandler.select(sqldb, false, "users", returnColumns, selectMap, null);

        if(userData == null || userData.size() < 1)
        {
            return false;
        }

        Log.d("EXISTING USER => ", Arrays.toString(userData.toArray()));
        return true;
    }


}
