package com.example.funfindr.utilites.db_models;

import android.util.Log;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FunFindrDatabaseTable {
    private String NAME = "";
    private Map<String,FunFindrDatabaseTableColumn> COLUMNS = new HashMap<String,FunFindrDatabaseTableColumn>();

    /**
     * The public constructor for the Database Table
     * @param name The name of the table
     */
    public FunFindrDatabaseTable(String name)
    {
        this.NAME = name;
    }


    /**
     * Adds a new column to the array list of columns for this table
     * @param title The title of the column
     * @param datatype The datatype of the column
     * @param autoIncrement Is the column automatically incrementable?
     * @param isPrimaryKey Is the column a primary key?
     */
    public void addColumn(String title, String datatype, boolean autoIncrement, boolean isPrimaryKey)
    {
        FunFindrDatabaseTableColumn newColumn = new FunFindrDatabaseTableColumn(title, datatype, autoIncrement, isPrimaryKey);
        COLUMNS.put(newColumn.getTITLE(), newColumn);
    }

    /**
     * Generates a raw SQL CREATE TABLE query
     * @return returns a raw sql query for creating a table
     */
    public String generateSQLCreateQuery()
    {
        StringBuilder sb = new StringBuilder();
        String columns = "";

        for(String key : this.COLUMNS.keySet())
        {
            sb.append(this.COLUMNS.get(key).generateSQLCreateQuery()).append(",");
        }
        columns = sb.substring(0, sb.length()-1);

        return "CREATE TABLE " + this.NAME + " (" + columns + ")";
    }

    /**
     * Generates a raw SQL ADD FOREIGN KEY query
     * @return returns a raw sql query for adding a foreign key constraint to a table
     */
    public String generateSQLAddForeignKeyQuery(String foreignKey, FunFindrDatabaseTable referencedTable,
                                                FunFindrDatabaseTableColumn referencedColumn)
    {
        String query = "";
        if(this.COLUMNS.containsKey(foreignKey) && referencedTable.COLUMNS.containsKey(referencedColumn))
        {
            query = "ALTER TABLE " + this.NAME + " ADD FOREIGN KEY(" + foreignKey + ") REFERENCES " +
                    referencedTable.NAME + "(" + referencedColumn.getTITLE() + ")";
        }

        return query;
    }

    /**
     * Gets the name of the table
     * @return returns the name of the table
     */
    public String getNAME() {
        return NAME;
    }

    /**
     * Gets the columns of the table
     * @return returns an array list of the tables columns
     */
    public ArrayList<FunFindrDatabaseTableColumn> getCOLUMNS() {
        ArrayList<FunFindrDatabaseTableColumn> columns = new ArrayList<FunFindrDatabaseTableColumn>();
        for(HashMap.Entry<String,FunFindrDatabaseTableColumn> entry : this.COLUMNS.entrySet())
        {
            columns.add(entry.getValue());
        }
        return columns;
    }

    /**
     * Gets the columns of the table
     * @return returns an array list of the tables columns
     */
    public FunFindrDatabaseTableColumn getCOLUMN(String columnName) {
        FunFindrDatabaseTableColumn selectedColumn = null;

        if(this.COLUMNS.containsKey(columnName))
        {
            selectedColumn =  this.COLUMNS.get(columnName);
        }

        return selectedColumn;
    }
}
