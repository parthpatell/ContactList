package com.example.contactlist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class ContactDBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "ContactList.db";
    //Changing this entry will allow to set a new database
    public static final int DATABASE_VERSION = 1;

    //All required columns
    private static final String TABLE_NAME = "ContactTable";
    private static final String _ID = "id";
    private static final String COLUMN_NAME= "name";
    private static final String COLUMN_NUMBER = "number";
    private static final String COLUMN_RELATIONSHIPS = "relationships";

    //Using number as string everywhere else but need number as integer for database.

    final String SQL_CREATE_CONTACT_LIST_TABLE = "CREATE TABLE " +
            TABLE_NAME + " (" +
            _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_NAME + " TEXT NOT NULL, " +
            COLUMN_NUMBER + " INTEGER NOT NULL, " +
            COLUMN_RELATIONSHIPS + " TEXT " +
            ");";

    public ContactDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_CONTACT_LIST_TABLE);
    }


    //Change in db happens by this when version is updated.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String name, String phone, String relationships){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME,name);
        cv.put(COLUMN_NUMBER,phone);
        cv.put(COLUMN_RELATIONSHIPS, relationships);


        long result = db.insert(TABLE_NAME, null, cv);
        return (result != -1);

    }

    public boolean deleteTitle(Integer ID) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, _ID + "=" + ID, null) > 0;
    }

    public Cursor viewData(){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select * from " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);

        return cursor;
    }

    public long getID(String name){
        SQLiteDatabase db = this.getReadableDatabase();
        long id;
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_NAME + " MATCH " + name;
        Cursor cursor = db.rawQuery(selectQuery, null);

        return cursor.getLong(0);

    }

    public String getName(String ID){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + TABLE_NAME + " MATCH " + ID;
        Cursor cursor = db.rawQuery(selectQuery, null);

        String output = cursor.getString(1);
        System.out.println("THE NAME IS:" + output);
        return output;

    }
}


