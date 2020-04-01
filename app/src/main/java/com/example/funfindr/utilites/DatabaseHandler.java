package com.example.funfindr.utilites;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.funfindr.LoginActivity;
import com.example.funfindr.data.Event;
import com.example.funfindr.data.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class DatabaseHandler {
    private static SQLiteDatabase sqldb = null;
    private static FunFindrDB ffdb = null;

    /**
     * Gets a new instance of a writable database
     * @param context The context of the activity calling this method
     * @return returns an SQLite writable database
     */
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

    /* USER METHODS --start-- */

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

    /**
     * Gets the id of the user using their email
     * @param email The email of the user
     * @return returns the user's id
     */
    public static String getUserId(String email)
    {
        String[] returnColumns = new String[1];
        returnColumns[0] = "_id";
        HashMap<String,String> map = new HashMap<String,String>();
        map.put("email", email);
        return DatabaseTableHandler.
                select(sqldb, true, "users", returnColumns, map,
                        null).get(0).get("_id");
    }

    /* USER METHODS --end-- */

    /* EVENT METHODS --start-- */

    /**
     * Creates a new event
     * @param event Event object passed in
     * @param email Email of the user creating this event
     * @return returns a boolean value that indicates if the event was successfully created
     */
    public static boolean createEvent(Event event, String email)
    {
        boolean success = false;
        String[] returnColumns = new String[1];
        returnColumns[0] = "_id";

        HashMap<String,String> selectionMap = new HashMap<>(); // map that will be used to find the user id
        selectionMap.put("email", email);

        Map<String, String> userData = DatabaseTableHandler.select(sqldb, true, "users", returnColumns, selectionMap, null).get(0);
        String userId = "";

        HashMap<String,String> insertionMap = new HashMap<>(); // map that will be used to insert a new event into the database

        if(userData != null)
        {
            userId = userData.get("_id");

            insertionMap.put("user_id", userId);
            insertionMap.put("address", event.getAddress());
            insertionMap.put("title", event.getTitle());
            insertionMap.put("date", event.getDate());
            insertionMap.put("location", event.getLocation());
            insertionMap.put("notes", event.getNotes());
            insertionMap.put("type", event.getType());

            if(DatabaseTableHandler.insert(sqldb, "events", insertionMap))
            {
                return true;
            }
        }

        return success;
    }

    /**
     * Selects all the events associated with this user
     * @param userId The user's id
     * @return returns all of this user's events
     */
    public static ArrayList<Map<String, String>> selectAllEvents(String userId)
    {
        String[] returnColumns = new String[]{"_id", "title","address", "date", "location", "notes", "type"};

        HashMap<String,String> selectionMap = new HashMap<>(); // map that will be used to find the user id
        selectionMap.put("user_id", userId);

        return DatabaseTableHandler.select(sqldb, true, "events", returnColumns, selectionMap, null);
    }

    /**
     * Deletes an event
     * @param id The id of the event
     * @return returns a boolean that states whether the deletion was successful
     */
    public static boolean deleteEvent(String id)
    {
        if(DatabaseTableHandler.delete(sqldb, "events", "_id", id))
        {
            return true;
        }
        return false;
    }

    /**
     * Updates the given event using its id
     * @param id The id of the event
     * @param data The data that will be used to update the event
     * @return returns a boolean value that stsates whether this operation was successful or not
     */
    public static boolean updateEvent(String id, HashMap<String,String> data)
    {
        if(DatabaseTableHandler.update(sqldb, "events", "_id", id, data))
        {
            return true;
        }
        return false;
    }



    /* EVENT METHODS --end-- */


    /* FAVORITE METHODS --start-- */
    /* FAVORITE METHODS --end-- */

}
