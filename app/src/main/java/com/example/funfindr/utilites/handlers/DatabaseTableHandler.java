package com.example.funfindr.utilites.handlers;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.funfindr.utilites.FunFindrUtils;
import com.example.funfindr.database.db_structure_models.FunFindrDatabaseTable;
import com.example.funfindr.database.db_structure_models.FunFindrDatabaseTableColumn;

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
     * Inserts database into the table
     * @param db This is the writable database that contains the table that the database will be
     *           inserted into
     * @param tableName This is the name of the table that the database will be inserted into
     * @param data This is the database that will be inserted
     * @return returns whether the database was successfully inserted or not
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
     * Updates database in the table
     * @param db This is the writable database that contains the table that the database will be
     *           inserted into
     * @param tableName This is the name of the table that will be updated
     * @param column This is the column that will be the filter
     * @param data This is the database that will be matched against the filter
     * @param entries The values to be put in the table
     * @return returns whether the database was successfully updated or not
     */
    public static boolean update(SQLiteDatabase db, String tableName, String column, String data, HashMap<String,String> entries) {
        ContentValues values = new ContentValues();

        for(Map.Entry<String,String> entry : entries.entrySet())
        {
            values.put(entry.getKey(), entry.getValue());
        }

        String[] whereArgs = new String[1];
        whereArgs[0] = data;
        int update = db.update(tableName, values, column+"=?", whereArgs);

        if(update == 0) {
            return false;
        }
        else {
            return true;
        }
    }

    /**
     * Updates database in the table
     * @param db This is the writable database that contains the table that the database will be
     *           inserted into
     * @param tableName This is the name of the table where the record will be deleted
     * @param column This is the column that will be the key for the where clause
     * @param data This is the database that will be determine what row(s) will be deleted
     * @return returns whether the database was successfully deleted or not
     */
    public static boolean delete(SQLiteDatabase db, String tableName, String column, String data) {
        int delete = 0;
        String[] whereArgs = new String[1];
        whereArgs[0] = data;

        delete = db.delete(tableName,column+"=?", whereArgs);

        if(delete == 0) {
            return false;
        }
        else {
            return true;
        }
    }

    /**
     * Updates database in the table
     * @param db This is the writable database that contains the table that the database will be
     *           inserted into
     * @param tableName This is the name of the table where the record will be deleted
     * @param columns These are the columns that will be returned
     * @param selection This arraylist contains the column and corresponding database that will be the
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
     * @return returns whether the database was successfully deleted or not
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

        String filterString;

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
                i++;
            }
        }

        try{
            Set<String> columnKeys;
            String[] columnsArray;
            columnKeys = selection.keySet();
            columnsArray = columnKeys.toArray(new String[columnKeys.size()]);

            if(columnsArray.length > 1)
            {
                StringBuilder sb = new StringBuilder();
                String selectionString = "";
                String[] selectionArgs = new String[columnsArray.length];
                columnsArray = columnKeys.toArray(new String[columnKeys.size()]);

                for(int i = 0; i < columnsArray.length; i++)
                {
                    selectionArgs[i] = selection.get(columnsArray[i]);
                    if(i == (columnsArray.length - 1))
                    {
                        sb.append(columnsArray[i]).append(" IN (?)");
                    }
                    else
                    {
                        sb.append(columnsArray[i]).append(" IN (?)").append(" AND ");
                    }
                }

                selectionString = sb.toString();

                cursor = db.query(tableName, columns, selectionString, selectionArgs, options.get(0),
                        options.get(1), options.get(2), options.get(3));
            }else
            {
                String key = String.valueOf(columnsArray[0]);
                Log.d(key.toUpperCase()+" => ", key + " " + selection.get(key));
                cursor = db.query(tableName, columns, key.toLowerCase()+" IN (?)",
                        new String[]{selection.get(key)}, options.get(0),
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
            Log.d("HERE => ", String.valueOf(e.getStackTrace()[0].getLineNumber()));
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
