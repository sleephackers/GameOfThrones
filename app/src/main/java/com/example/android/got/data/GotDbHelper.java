package com.example.android.got.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;

import static com.example.android.got.data.GotContract.GotEntry.COLUMN_NAME;
import static com.example.android.got.data.GotContract.GotEntry.TABLE_NAME;
import static com.example.android.got.data.GotContract.GotEntry._ID;


public class GotDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = GotDbHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "GotCharacters.db";

    private static final int DATABASE_VERSION = 1;

    public GotDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_ARTISTS_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
                + GotContract.GotEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + GotContract.GotEntry.COLUMN_IMAGE+ " TEXT NOT NULL, "
                + COLUMN_NAME + " TEXT NOT NULL, "
                + GotContract.GotEntry.COLUMN_GENDER+ " TEXT NOT NULL, "
                + GotContract.GotEntry.COLUMN_CULTURE + " TEXT NOT NULL, "
                + GotContract.GotEntry.COLUMN_HOUSE + " TEXT NOT NULL, "
                + GotContract.GotEntry.COLUMN_BOOKS + " TEXT NOT NULL, "
                + GotContract.GotEntry.COLUMN_TITLES + " TEXT NOT NULL );";

        db.execSQL(SQL_CREATE_ARTISTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public boolean checkIfRecordExist(String chek) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.query(TABLE_NAME, new String[]{BaseColumns._ID,
                            COLUMN_NAME}, COLUMN_NAME + "=?",
                    new String[]{chek}, null, null, null, null);
            if (cursor != null && cursor.getCount() != 0) {
                cursor.moveToFirst();

            }

            if (cursor.moveToFirst()) {
                db.close();
                return true;//record Exists

            }
            db.close();
        } catch (Exception errorException) {
            Log.d(LOG_TAG, "Exception occured " + errorException);
        }
        return false;
    }

    public Long getCharId(String character) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{BaseColumns._ID,
                        COLUMN_NAME}, COLUMN_NAME + "=?",
                new String[]{character}, null, null, null, null);
        if (cursor != null && cursor.getCount() != 0) {
            cursor.moveToFirst();
            return Long.valueOf(cursor.getString(0));

        } else return null;
    }

    public ArrayList<String> getNames() {
        ArrayList<String> names = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{BaseColumns._ID,
                COLUMN_NAME}, null, null, null, null, null);
        if (cursor != null && cursor.getCount() != 0) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                names.add(cursor.getString(1));
                cursor.moveToNext();
            }
            Collections.reverse(names);
            return names;

        } else return null;
    }
}
