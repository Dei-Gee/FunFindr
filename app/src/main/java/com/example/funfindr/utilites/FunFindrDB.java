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

    // Compuslory Constructor
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
        TABLE1.addColumn("_id", "INTEGER", true, true);
        TABLE1.addColumn("firstname", "VARCHAR", false, false);
        TABLE1.addColumn("lastname", "VARCHAR", false, false);
        TABLE1.addColumn("email", "VARCHAR", false, false);
        TABLE1.addColumn("profile_pic", "BLOB", false, false);

        /* Favorites Table */
        TABLE2.addColumn("_id", "INTEGER", true, true);
        TABLE2.addColumn("user_id", "INTEGER", false, false);
        TABLE2.addColumn("name", "VARCHAR", false, false);
        TABLE2.addColumn("logo", "BLOB", false, false);
        TABLE2.addColumn("type", "VARCHAR", false, false);

        /* Events Table */
        TABLE3.addColumn("_id", "INTEGER", true, true);
        TABLE3.addColumn("user_id", "INTEGER", false, false);
        TABLE3.addColumn("location", "VARCHAR", false, false);
        TABLE3.addColumn("address", "VARCHAR", false, false);
        TABLE3.addColumn("type", "VARCHAR", false, false);
        TABLE3.addColumn("notes", "TEXT", false, false);
        TABLE3.addColumn("date", "DATETIME", false, false);

        // Create Tables
        DatabaseTableHandler.createTable(db, TABLE1);
        DatabaseTableHandler.createTable(db, TABLE2);
        DatabaseTableHandler.createTable(db, TABLE3);

        // Add Foreign Key Constraints
        DatabaseTableHandler.addForeignKeyConstraints(db, TABLE2, "user_id", TABLE1, TABLE1.getCOLUMN("_id"));
        DatabaseTableHandler.addForeignKeyConstraints(db, TABLE3, "user_id", TABLE1, TABLE1.getCOLUMN("_id"));

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
