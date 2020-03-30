package com.example.funfindr.utilites;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.funfindr.utilites.db_models.FunFindrDatabaseTable;
import com.example.funfindr.utilites.db_models.FunFindrDatabaseTableColumn;
import com.google.android.gms.common.util.NumberUtils;

import java.util.ArrayList;
import java.util.Arrays;
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
     * @param table The table that will be created
     * @param foreignKey The foreign key to be added to the table
     * @param referencedTable The table the foreign key will reference
     * @param referencedColumn The column that the foreign key will reference
     */
    public static void createTable(SQLiteDatabase database, FunFindrDatabaseTable table, String foreignKey,
                                   FunFindrDatabaseTable referencedTable,
                                   FunFindrDatabaseTableColumn referencedColumn)
    {
        database.execSQL(table.generateSQLCreateQuery(foreignKey, referencedTable, referencedColumn));
    }

    /**
     * Creates tables
     * @param database The database that will contain the table
     * @param table The table that will be created
     */
    public static void createTable(SQLiteDatabase database, FunFindrDatabaseTable table)
    {
        database.execSQL(table.generateSQLCreateQuery());
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
     * @param tableName This is the name of the table that the data will be inserted into
     * @param data This is the data that will be inserted
     * @return returns whether the data was successfully inserted or not
     */
    public static boolean insert(SQLiteDatabase db, String tableName, HashMap<String,String> data) {
        ContentValues values = new ContentValues();

        // Iterates through tthe hashmap and adds each entry to the set of values
        for(HashMap.Entry<String,String> entry : data.entrySet())
        {
            if(entry.getValue() != null) {
                if (FunFindrUtils.isInteger(entry.getValue())) {
                    values.put(entry.getKey(), Integer.parseInt(entry.getValue()));
                } else {
                    values.put(entry.getKey(), entry.getValue());
                }
            }
        }

        Long insertion = db.insert(tableName,null, values);

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
     * @param tableName This is the name of the table that will be updated
     * @param column This is the column that will receive the data
     * @param data This is the data that will be inserted
     * @return returns whether the data was successfully updated or not
     */
    public static boolean update(SQLiteDatabase db, String tableName, String column, String data) {
        ContentValues values = new ContentValues();
        values.put(column, data);
        int update = db.update(tableName, values, column+"=?", data.split(""));

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
     * @param tableName This is the name of the table where the record will be deleted
     * @param column This is the column that will be the key for the where clause
     * @param data This is the data that will be determine what row(s) will be deleted
     * @return returns whether the data was successfully deleted or not
     */
    public static boolean delete(SQLiteDatabase db, String tableName, String column, String data) {
        int delete = db.delete(tableName,column+"=?", data.split(""));

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
     * @param tableName This is the name of the table where the record will be deleted
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
    public static ArrayList<Map<String,String>> select(SQLiteDatabase db, boolean unique, String tableName, String[] columns,
                                                             HashMap<String, String> selection, ArrayList<String> optionalParameters)
    {
        Map<String,String> row;
        Set<Map<String, String>> rows = null;
        ArrayList<Map<String,String>> results = new ArrayList<Map<String,String>>();
        Cursor cursor = null;
        ArrayList<String> options = new ArrayList<>();
        options.add("");
        options.add("");
        options.add("");
        options.add("");

        // checking for optional parameters
        if(optionalParameters != null)
        {
            int i = 0;
            for(String optParameter : optionalParameters)
            {
                if(optParameter != null || optParameter != "" || !optParameter.isEmpty())
                {
                   options.set(i, optParameter);
                }
            }
        }

        try{;
            if(selection.size() > 1)
            {
                StringBuilder sb = new StringBuilder();
                String selectionString = "";
                String[] selectionArgs = new String[selection.size()];
                String column = "";
                for(int i = 0; i < selection.size(); i++)
                {
                    column = selection.keySet().toArray()[i].toString();
                    selectionArgs[i] = selection.get(column);
                    if(i != selection.size() - 1)
                    {
                        sb.append(column + " IN (?) AND ");
                    }
                    else
                    {
                        sb.append(column + " IN (?)");
                    }
                }

                selectionString = sb.toString();
                Log.d("selection string => ", selectionString);
                Log.d("selection args => ", Arrays.toString(selectionArgs));
                cursor = db.query(tableName, columns, selectionString, selectionArgs, options.get(0),
                        options.get(1), options.get(2), options.get(3));
            }
            else
            {
                String key = selection.keySet().toArray()[0].toString();
                cursor = db.query(tableName, columns, key+" IN (?)",
                        selection.get(key).toString().split(""), options.get(0),
                        options.get(1), options.get(2), options.get(3));
            }


            // move to first result
            cursor.moveToFirst();

            while(!cursor.isAfterLast()) // as long as we are not at the end of the cursor
            {
                row = new HashMap<String,String>(); // each row is a hash map containing the values of the columns specified
                if(columns.length > 1) // if there is more than one column to return
                {
                    for(String column : columns)
                    {
                        row.put(column, cursor.getString(cursor.getColumnIndex(column)));
                    }
                }
                else {
                    row.put(columns[0], cursor.getString(cursor.getColumnIndex(columns[0])));
                }
                results.add(row); // add the row to the array list of rows
                cursor.moveToNext(); // move to next item in the cursor
            }

        }
        catch (Exception e)
        {
            Log.d("EXCEPTION => ", e.getMessage());
        }
        finally {
            if(cursor != null)
            {
                cursor.close(); // close the cursor
            }
        }

        return results;

    }
}
