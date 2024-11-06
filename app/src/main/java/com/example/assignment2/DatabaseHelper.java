package com.example.assignment2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "LocationFinder.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "locations";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_ADDRESS = "address";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ADDRESS + " TEXT, " +
                COLUMN_LATITUDE + " REAL, " +
                COLUMN_LONGITUDE + " REAL" + ")";
        db.execSQL(createTable);
        Log.d("DatabaseHelper", "Database table created.");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Method to add a location entry
    public boolean addLocation(String address, double latitude, double longitude) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ADDRESS, address);
        values.put(COLUMN_LATITUDE, latitude);
        values.put(COLUMN_LONGITUDE, longitude);

        long result = db.insert(TABLE_NAME, null, values);
        if (result == -1) {
            Log.e("DatabaseHelper", "Failed to insert location: " + address);
            return false; // Return false if insertion failed
        } else {
            Log.d("DatabaseHelper", "Inserted location: " + address + " with coordinates (" + latitude + ", " + longitude + ")");
            return true; // Return true if insertion succeeded
        }
    }

    // Method to query a location by address
    public Cursor queryLocation(String address) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT " + COLUMN_LATITUDE + ", " + COLUMN_LONGITUDE +
                " FROM " + TABLE_NAME + " WHERE " + COLUMN_ADDRESS + " = ?", new String[]{address});
    }

    // Method to update an existing location
    public boolean updateLocation(String address, double latitude, double longitude) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_LATITUDE, latitude);
        values.put(COLUMN_LONGITUDE, longitude);

        int result = db.update(TABLE_NAME, values, COLUMN_ADDRESS + " = ?", new String[]{address});
        if (result > 0) {
            Log.d("DatabaseHelper", "Updated location: " + address);
            return true; // Return true if update succeeded
        } else {
            Log.e("DatabaseHelper", "Failed to update location: " + address);
            return false; // Return false if update failed
        }
    }

    // Method to delete a location
    public boolean deleteLocation(String address) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_NAME, COLUMN_ADDRESS + " = ?", new String[]{address});
        if (result > 0) {
            Log.d("DatabaseHelper", "Deleted location: " + address);
            return true; // Return true if deletion succeeded
        } else {
            Log.e("DatabaseHelper", "Failed to delete location: " + address);
            return false; // Return false if deletion failed
        }
    }
}
