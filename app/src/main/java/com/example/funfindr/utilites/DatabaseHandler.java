package com.example.funfindr.utilites;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

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
        HashMap<String,String> map = new HashMap<String,String>();

        map.put("firstname", user.getFirstname());
        map.put("lastname", user.getLastname());
        map.put("email", user.getEmail());
        map.put("password", user.getPassword());
        map.put("profile_pic", null);

        if(DatabaseTableHandler.insert(sqldb, "users", map))
        {
            return true;
        }
        return false;
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

        ArrayList<Map<String,String>> userData = DatabaseTableHandler.select(sqldb, true, "users", returnColumns, map, null);

        return userData;
    }


}
