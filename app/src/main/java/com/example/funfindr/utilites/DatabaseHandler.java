package com.example.funfindr.utilites;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.funfindr.data.User;

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
    public static boolean SignupUser(User user) {
//        if(DatabaseTableHandler.insert(sqldb, "users", ))
//        {
//            return true;
//        }
        return false;
    }

}
