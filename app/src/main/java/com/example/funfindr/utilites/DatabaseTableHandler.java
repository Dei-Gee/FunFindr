package com.example.funfindr.utilites;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Handles table queries for a database
 */
public class DatabaseTableHandler {
    /**
     * Creates tables
     * @param database The database that will contain the table
     * @param tableName The name of the table to be created
     * @param tableColumns The columns that the table will have
     */
    public static void createTable(SQLiteDatabase database, String tableName, String tableColumns)
    {
        String primaryKey = "id INTEGER PRIMARY KEY AUTOINCREMENT";
        String query = "";

        query = "CREATE TABLE "+ tableName + " (" + primaryKey + tableColumns + ")";
        database.execSQL(query);
    }


    /**
     * Creates tables
     * @param database The database that contains the table
     * @param tableName The name of the table to be altered
     * @param foreignKey The foreign key to be added to the table. This is the column that will
     *                   become the foreign key and reference another column in another table
     * @param referencedTable The table that contains the column that the foreign key references
     * @param referencedColumn The column that is referenced by the foreign key
     */
    public static void addForeignKeyConstraints(SQLiteDatabase database, String tableName,
                                                String foreignKey, String referencedTable,
                                                String referencedColumn)
    {
        String query = "";

        query = "ALTER TABLE "+ tableName + " ADD FOREIGN KEY(" + foreignKey + ") REFERENCES " +
                referencedTable + "(" + referencedColumn + ")";
        database.execSQL(query);
    }

    /**
     * Drops tables if they exist in the database
     * @param database The database that this method will work on
     * @param tableName The name of the table to be dropeed if it exists
     */
    public static void dropTableIfExists(SQLiteDatabase database, String tableName) {
        database.execSQL("DROP TABLE IF EXISTS " + tableName);
    }

    /**
     * Inserts data into the table
     * @param db This is the writable database that contains the table that the data will be
     *           inserted into
     * @param table This is the table that the data will be inserted into
     * @param column This is the column that will receive the data
     * @param data This is the data that will be inserted
     * @return returns whether the data was successfully inserted or not
     */
    public static boolean insert(SQLiteDatabase db, String table, String column, String data) {
        ContentValues values = new ContentValues();
        values.put(column, data);
        Long insertion = db.insert(table,null, values);

        if(insertion == null) {
            return false;
        }
        else {
            return true;
        }
    }

    /**
     * Updates data in the table
     * @param db This is the writable database that contains the table that the data will be
     *           inserted into
     * @param table This is the table that will be updated
     * @param column This is the column that will receive the data
     * @param data This is the data that will be inserted
     * @return returns whether the data was successfully updated or not
     */
    public static boolean update(SQLiteDatabase db, String table, String column, String data) {
        ContentValues values = new ContentValues();
        values.put(column, data);
        int update = db.update(table, values, column+"=?", data.split(""));

        if(update == 0) {
            return false;
        }
        else {
            return true;
        }
    }

    /**
     * Updates data in the table
     * @param db This is the writable database that contains the table that the data will be
     *           inserted into
     * @param table This is the table where the record will be deleted
     * @param column This is the column that will be the key for the where clause
     * @param data This is the data that will be determine what row(s) will be deleted
     * @return returns whether the data was successfully deleted or not
     */
    public static boolean delete(SQLiteDatabase db, String table, String column, String data) {
        int delete = db.delete(table,column+"=?", data.split(""));

        if(delete == 0) {
            return false;
        }
        else {
            return true;
        }
    }

    /**
     * Updates data in the table
     * @param db This is the writable database that contains the table that the data will be
     *           inserted into
     * @param table This is the table where the record will be deleted
     * @param columns These are the columns that will be returned
     * @param selection This arraylist contains the column and corresponding data that will be the
     *                  filter to determine which rows will be returned. It is the equivalent of an
     *                  SQL WHERE clause
     * @param optionalParameters An arraylist containing the remaining parameters of the
     *                           SQLiteDatabase query() method The index order is
     *                           [
     *                           "groupBy", (SQL GROUP BY clause)
     *                           "having", (SQL HAVING clause)
     *                           "orderBy", (SQL ORDER BY clause)
     *                           "limit" (SQL LIMIT clause)
     *                           ]
     *                           Note: All these clauses are formatted without their keywords so
     *                           exclude them.
     * @param unique Boolean parameter that determines whether or not to return unique rows
     * @return returns whether the data was successfully deleted or not
     */
    public static ArrayList<Map<String,String>> select(SQLiteDatabase db, boolean unique, String table, String[] columns,
                                                             HashMap<String, String> selection, ArrayList<String> optionalParameters)
    {
        Map<String,String> row = null;
        Set<Map<String, String>> rows = null;
        ArrayList<Map<String,String>> results = null;

        Cursor cursor = db.query(table, columns, selection.keySet().toArray()[0].toString(),
                selection.get("s").toString().split(""), optionalParameters.get(0),
                optionalParameters.get(1), optionalParameters.get(2), optionalParameters.get(3));

        // move to first result
        cursor.moveToFirst();

        while(!cursor.isAfterLast()) // as long as we are not at the end of the cursor
        {
            row = new HashMap<String,String>(); // each row is a hash map containing the values of the columns specified
            if(columns.length == 1) // if there is more than one column to return
            {
                for(String column : columns)
                {
                    row.put(column, cursor.getString(cursor.getColumnIndex(column)));
                }
            }
            else {
                row.put(columns[0], cursor.getString(cursor.getColumnIndex(columns[0])));
            }
            rows.add(row); // add the row to the set of rows
            cursor.moveToNext(); // move to next item in the cursor
        }

        cursor.close(); // close the cursor

        // create a new array list from the set of rows
        results = new ArrayList<Map<String,String>>(rows);

        return results;

    }
}
