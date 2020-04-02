package com.example.funfindr.utilites;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.funfindr.utilites.db_models.FunFindrDatabaseTable;

public class FunFindrDB extends SQLiteOpenHelper {
    // FINAL GLOBAL VARIABLES
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "funfindr_database";
    private static final FunFindrDatabaseTable TABLE1 = new FunFindrDatabaseTable("users");
    private static final FunFindrDatabaseTable TABLE2 = new FunFindrDatabaseTable("favorites");
    private static final FunFindrDatabaseTable TABLE3 = new FunFindrDatabaseTable("events");

    // Compulsory Constructor
    public FunFindrDB(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        // if the database is not read only
        if(!db.isReadOnly()) {
            // turn on foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Adding Columns to the tables

        /* Users Table */
        TABLE1.addColumn("_id", "INTEGER", true, true, true);
        TABLE1.addColumn("firstname", "VARCHAR", false, false, true);
        TABLE1.addColumn("lastname", "VARCHAR", false, false, true);
        TABLE1.addColumn("email", "VARCHAR", false, false, true);
        TABLE1.addColumn("password", "VARCHAR", false, false, true);
        TABLE1.addColumn("profile_pic", "BLOB", false, false, false);

        /* Favorites Table */
        TABLE2.addColumn("_id", "INTEGER", true, true, true);
        TABLE2.addColumn("user_id", "INTEGER", false, false, true);
        TABLE2.addColumn("name", "VARCHAR", false, false, true);
        TABLE2.addColumn("logo", "BLOB", false, false, true);
        TABLE2.addColumn("type", "VARCHAR", false, false, false);

        /* Events Table */
        TABLE3.addColumn("_id", "INTEGER", true, true, true);
        TABLE3.addColumn("user_id", "INTEGER", false, false, true);
        TABLE3.addColumn("title", "VARCHAR", false, false, true);
        TABLE3.addColumn("location", "VARCHAR", false, false, true);
        TABLE3.addColumn("address", "VARCHAR", false, false, true);
        TABLE3.addColumn("type", "VARCHAR", false, false, true);
        TABLE3.addColumn("notes", "TEXT", false, false, false);
        TABLE3.addColumn("date", "TEXT", false, false, true);

        // Create Tables
        DatabaseTableHandler.createTable(db, TABLE1);
        DatabaseTableHandler.createTable(db, TABLE2,"user_id", TABLE1, TABLE1.getCOLUMN("_id"));
        DatabaseTableHandler.createTable(db, TABLE3,"user_id", TABLE1, TABLE1.getCOLUMN("_id"));

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // drop tables if they exist
        DatabaseTableHandler.dropTableIfExists(db, TABLE1.getNAME());
        DatabaseTableHandler.dropTableIfExists(db, TABLE2.getNAME());
        DatabaseTableHandler.dropTableIfExists(db, TABLE3.getNAME());
        onCreate(db); // recreate database
    }

}
