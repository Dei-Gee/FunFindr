package com.example.funfindr.utilites;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {
    // VARIABLES
    private static final String DATABASE_NAME = "funfindr_database";
    private static final String TABLE1 = "users";
    private static final String TABLE2 = "favorites";
    private static final String TABLE3 = "events";

    // Compuslory Constructor
    public DatabaseHandler(Context context){
        super(context, DATABASE_NAME, null, 1);
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
        // SQL Strings
        String table1Columns = "firstname VARCHAR\n" +
                "  lastname VARCHAR \n" +
                "  email VARCHAR\n" +
                "  password VARCHAR\n" +
                "  profile_pic VARBINARY(MAX)";

        String table2Columns = "name VARCHAR\n" +
                "  user_id INTEGER\n" +
                "  address VARCHAR\n" +
                "  logo VARBINARY(MAX)\n" +
                "  type VARCHAR";

        String table3Columns = "name VARCHAR\n" +
                "  user_id INTEGER\n" +
                "  location VARCHAR\n" +
                "  address VARCHAR\n" +
                "  type VARCHAR\n" +
                "  notes TEXT\n" +
                "  date DATETIME";

        // Create Tables
        DatabaseTableHandler.createTable(db, TABLE1, table1Columns);
        DatabaseTableHandler.createTable(db, TABLE2, table2Columns);
        DatabaseTableHandler.createTable(db, TABLE3, table3Columns);

        // Add Foreign Key Constraints
        DatabaseTableHandler.addForeignKeyConstraints(db, TABLE2, "user_id", "users", "id");
        DatabaseTableHandler.addForeignKeyConstraints(db, TABLE3, "user_id", "users", "id");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // drop tables if they exist
        DatabaseTableHandler.dropTableIfExists(db, TABLE1);
        DatabaseTableHandler.dropTableIfExists(db, TABLE2);
        DatabaseTableHandler.dropTableIfExists(db, TABLE3);
        onCreate(db); // recreate database
    }





}
