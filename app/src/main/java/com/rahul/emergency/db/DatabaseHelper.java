package com.rahul.emergency.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Table Name
    public static final String TABLE_NAME = "DOCTORS";

    // Table columns
    public static final String _ID = "_id";
    public static final String NAME = "name";
    public static final String MOBILE = "mobile";
    public static final String SPECIALIST = "specialist";
    public static final String LATITUDE = "latitude";
    public static final String LANGITUDE = "langitude";
    public static final String ADDRESS = "address";

    // Database Information
    static final String DB_NAME = "JOURNALDEV_COUNTRIES.DB";

    // database version
    static final int DB_VERSION = 1;

    // Creating table query
    private static final String CREATE_TABLE = "create table " + TABLE_NAME + "(" + _ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + NAME + " TEXT NOT NULL, "+ LATITUDE +" TEXT,"  +
            SPECIALIST+" TEXT NOT NULL,"+ADDRESS+" TEXT NOT NULL,"+LANGITUDE +" TEXT," + MOBILE + " TEXT);";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}