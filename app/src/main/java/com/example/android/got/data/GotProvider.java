package com.example.android.got.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class GotProvider extends ContentProvider {

    public static final String LOG_TAG = GotProvider.class.getSimpleName();
    private static final int CHARACTERS = 100;
    private static final int CHARACTERS_ID = 101;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {

        sUriMatcher.addURI("com.example.android.got", "character", CHARACTERS);
        sUriMatcher.addURI("com.example.android.got", "character/#", CHARACTERS_ID);
    }

    private GotDbHelper mDbHelper;


    @Override
    public boolean onCreate() {
        mDbHelper = new GotDbHelper(getContext());

        return true;
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        SQLiteDatabase database = mDbHelper.getReadableDatabase();
        Cursor cursor = null;

        int match = sUriMatcher.match(uri);
        switch (match) {
            case CHARACTERS:

                cursor = database.query(GotContract.GotEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case CHARACTERS_ID:

                selection = GotContract.GotEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                cursor = database.query(GotContract.GotEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    /**
     * Insert new data into the provider with the given ContentValues.
     */

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case CHARACTERS:
                return insertCharacter(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }


    private Uri insertCharacter(Uri uri, ContentValues values) {
        String image = values.getAsString(GotContract.GotEntry.COLUMN_IMAGE);
        if (image == null) {
            throw new IllegalArgumentException("Character requires an image");
        }
        String name = values.getAsString(GotContract.GotEntry.COLUMN_NAME);
        if (name == null) {
            throw new IllegalArgumentException("Character requires a name");
        }
        String gender = values.getAsString(GotContract.GotEntry.COLUMN_GENDER);
        if (gender == null) {
            throw new IllegalArgumentException("Character requires a gender");
        }
        String culture = values.getAsString(GotContract.GotEntry.COLUMN_CULTURE);
        if (culture == null) {
            throw new IllegalArgumentException("Character requires a culture");
        }
        String house = values.getAsString(GotContract.GotEntry.COLUMN_HOUSE);
        if (house == null) {
            throw new IllegalArgumentException("Character requires a house");
        }
        String books = values.getAsString(GotContract.GotEntry.COLUMN_BOOKS);
        if (books == null) {
            throw new IllegalArgumentException("Character requires books");
        }
        String titles = values.getAsString(GotContract.GotEntry.COLUMN_TITLES);
        if (titles == null) {
            throw new IllegalArgumentException("Character requires titles");
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        long id = database.insert(GotContract.GotEntry.TABLE_NAME, null, values);
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case CHARACTERS:
                return updateCharacter(uri, contentValues, selection, selectionArgs);
            case CHARACTERS_ID:

                selection = GotContract.GotEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateCharacter(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);

        }
    }

    private int updateCharacter(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        if (values.containsKey(GotContract.GotEntry.COLUMN_IMAGE)) {
            String image = values.getAsString(GotContract.GotEntry.COLUMN_IMAGE);
            if (image == null) {
                throw new IllegalArgumentException("Character requires an image");
            }

        }
        if (values.containsKey(GotContract.GotEntry.COLUMN_NAME)) {
            String name = values.getAsString(GotContract.GotEntry.COLUMN_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Character requires a name");
            }

        }
        if (values.containsKey(GotContract.GotEntry.COLUMN_GENDER)) {
            String gender = values.getAsString(GotContract.GotEntry.COLUMN_GENDER);
            if (gender == null) {
                throw new IllegalArgumentException("Character requires a gender");
            }
        }


        if (values.containsKey(GotContract.GotEntry.COLUMN_CULTURE)) {
            String culture = values.getAsString(GotContract.GotEntry.COLUMN_CULTURE);
            if (culture == null) {
                throw new IllegalArgumentException("Character requires a culture");
            }
        }
        if (values.containsKey(GotContract.GotEntry.COLUMN_HOUSE)) {
            String house = values.getAsString(GotContract.GotEntry.COLUMN_HOUSE);
            if (house == null) {
                throw new IllegalArgumentException("Character requires a house");
            }
        }
        if (values.containsKey(GotContract.GotEntry.COLUMN_BOOKS)) {
            String books = values.getAsString(GotContract.GotEntry.COLUMN_BOOKS);
            if (books == null) {
                throw new IllegalArgumentException("Character requires books");
            }
        }
        if (values.containsKey(GotContract.GotEntry.COLUMN_TITLES)) {
            String titles = values.getAsString(GotContract.GotEntry.COLUMN_TITLES);
            if (titles == null) {
                throw new IllegalArgumentException("Character requires titles");
            }
        }
        if (values.size() == 0) {
            return 0;
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        int rowsUpdated = database.update(GotContract.GotEntry.TABLE_NAME, values, selection, selectionArgs);


        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;

    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int rowsDeleted;
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case CHARACTERS:
                rowsDeleted = database.delete(GotContract.GotEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case CHARACTERS_ID:
                selection = GotContract.GotEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(GotContract.GotEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);

        }
        return rowsDeleted;

    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case CHARACTERS:
                return GotContract.GotEntry.CONTENT_LIST_TYPE;
            case CHARACTERS_ID:
                return GotContract.GotEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

}
