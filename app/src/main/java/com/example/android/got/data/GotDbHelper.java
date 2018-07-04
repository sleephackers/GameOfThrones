package com.example.android.got.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class GotDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = GotDbHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "GotCharacters.db";

    private static final int DATABASE_VERSION = 1;

    public GotDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_ARTISTS_TABLE = "CREATE TABLE " + GotContract.GotEntry.TABLE_NAME + " ("
                + GotContract.GotEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + GotContract.GotEntry.COLUMN_IMAGE+ " TEXT NOT NULL, "
                + GotContract.GotEntry.COLUMN_NAME+ " TEXT NOT NULL, "
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
}
