package com.vahagn.barber_line.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class BarberLineDatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "BarberShops.db";
    private static final int DATABASE_VERSION = 2; // Updated version to reflect the schema change

    public static final String TABLE_NAME = "Barber_Shops";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_NAME = "Name";
    public static final String COLUMN_ADDRESS = "Address";
    public static final String COLUMN_RATING = "Rating";
    public static final String COLUMN_SPECIALISTS = "Specialists";
    public static final String COLUMN_SERVICES = "Services";
    public static final String COLUMN_REVIEWS = "Reviews";
    public static final String COLUMN_IMAGE = "Image";
    public static final String COLUMN_LOGO = "Logo"; // New column for logo

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT NOT NULL, " +
                    COLUMN_ADDRESS + " TEXT NOT NULL, " +
                    COLUMN_RATING + " REAL NOT NULL, " +
                    COLUMN_SPECIALISTS + " TEXT NOT NULL, " +
                    COLUMN_SERVICES + " TEXT NOT NULL, " +
                    COLUMN_REVIEWS + " TEXT, " +
                    COLUMN_IMAGE + " TEXT, " +
                    COLUMN_LOGO + " TEXT);"; // Added the Logo column

    public BarberLineDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            String addLogoColumn = "ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + COLUMN_LOGO + " TEXT;";
            db.execSQL(addLogoColumn);
        }
    }

    public void addBarberShop(String name, String address, float rating, String specialists, String services, String reviews, String imagePath, String logoPath) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_NAME, name);
        cv.put(COLUMN_ADDRESS, address);
        cv.put(COLUMN_RATING, rating);
        cv.put(COLUMN_SPECIALISTS, specialists);
        cv.put(COLUMN_SERVICES, services);
        cv.put(COLUMN_REVIEWS, reviews);
        cv.put(COLUMN_IMAGE, imagePath);
        cv.put(COLUMN_LOGO, logoPath); // Add logo to content values

        long result = db.insert(TABLE_NAME, null, cv);

        if (result == -1) {
            Toast.makeText(context, "Failed to add Barber Shop", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Barber Shop added successfully!", Toast.LENGTH_SHORT).show();
        }

        db.close();
    }

    public Cursor readAllData() {
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }
}
