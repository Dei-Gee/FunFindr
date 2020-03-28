package com.example.funfindr.utilites.db_models;

public class FunFindrDatabaseTableColumn {
    private String TITLE = "";
    private String DATATYPE = "";
    private boolean AUTO_INCREMENT = false;
    private boolean PRIMARY_KEY = false;

    public FunFindrDatabaseTableColumn(String title, String datatype, boolean auto_increment, boolean primary_key)
    {
        this.AUTO_INCREMENT = auto_increment;
        this.DATATYPE = datatype.toUpperCase();
        this.TITLE = title.toLowerCase();
        this.PRIMARY_KEY = primary_key;
    }

    public String generateSQLCreateQuery()
    {
        String autoIncrement = "";
        String primaryKey = "";

        if(this.PRIMARY_KEY == true)
        {
            primaryKey = "PRIMARY KEY";
        }

        if(this.AUTO_INCREMENT ==  true)
        {
            autoIncrement = "AUTO INCREMENT";
        }

        return this.TITLE + " " + this.DATATYPE + " " + primaryKey + " " + autoIncrement;

    }

    /**
     * Gets the title of the column
     * @return returns the title of the column
     */
    public String getTITLE() {
        return TITLE;
    }

    /**
     * Gets the datatype of the column
     * @return returns the datatype of the column
     */
    public String getDATATYPE() {
        return DATATYPE;
    }

    /**
     * Checks if this column will be automatically incremented
     * @return returns a boolean that reveals whether this column is auto-incremented or not
     */
    public boolean isAUTO_INCREMENT() {
        return AUTO_INCREMENT;
    }

    /**
     * Checks if this column is a primary key
     * @return returns a boolean that reveals whether this column is indeed a primary key
     */
    public boolean isPRIMARY_KEY() {
        return PRIMARY_KEY;
    }
}
