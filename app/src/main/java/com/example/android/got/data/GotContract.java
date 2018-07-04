package com.example.android.got.data;

import android.app.PendingIntent;
import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;


public final class GotContract {
    public static final String CONTENT_AUTHORITY = "com.example.android.got";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_CHARACTER = "character";


    private GotContract() {
    }

    public static final class GotEntry implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_CHARACTER);

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CHARACTER;


        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CHARACTER;


        public final static String TABLE_NAME = "characters";

        public final static String _ID = BaseColumns._ID;

        public final static String COLUMN_IMAGE = "image";

        public final static String COLUMN_NAME = "name";

        public final static String COLUMN_GENDER = "gender";

        public final static String COLUMN_CULTURE = "culture";

        public final static String COLUMN_HOUSE = "house";

        public final static String COLUMN_BOOKS = "books";

        public final static String COLUMN_TITLES = "titles";


    }

}
