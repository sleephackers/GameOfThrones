package com.example.android.got;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.got.data.GotContract;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class SearchHistoryCharacterInfo extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EXISTING_CHARACTER_LOADER = 0;
    private Uri mCurrentUri;
    ImageView imageView;
    TextView cname;
    TextView gender;
    TextView culture;
    TextView house;
    TextView books;
    TextView titles;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_character);
        Intent intent = getIntent();
        mCurrentUri = intent.getData();
        imageView=findViewById(R.id.image);
        cname=findViewById(R.id.name);
        gender=findViewById(R.id.gender);
        culture=findViewById(R.id.culture);
        house=findViewById(R.id.house);
        books=findViewById(R.id.books);
        titles=findViewById(R.id.titles);
        getLoaderManager().initLoader(EXISTING_CHARACTER_LOADER, null, this);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_history, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                showDeleteConfirmationDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                GotContract.GotEntry._ID,
                GotContract.GotEntry.COLUMN_IMAGE,
                GotContract.GotEntry.COLUMN_NAME,
                GotContract.GotEntry.COLUMN_GENDER,
                GotContract.GotEntry.COLUMN_CULTURE,
                GotContract.GotEntry.COLUMN_HOUSE,
                GotContract.GotEntry.COLUMN_BOOKS,
                GotContract.GotEntry.COLUMN_TITLES,
        };
        return new CursorLoader(this,   // Parent activity context
                mCurrentUri,         // Query the content URI for the current character
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }


        if (cursor.moveToFirst()) {
            int imageci = cursor.getColumnIndex(GotContract.GotEntry.COLUMN_IMAGE);
            int nameci = cursor.getColumnIndex(GotContract.GotEntry.COLUMN_NAME);
            int genderci = cursor.getColumnIndex(GotContract.GotEntry.COLUMN_GENDER);
            int cultureci = cursor.getColumnIndex(GotContract.GotEntry.COLUMN_CULTURE);
            int houseci = cursor.getColumnIndex(GotContract.GotEntry.COLUMN_HOUSE);
            int booksci = cursor.getColumnIndex(GotContract.GotEntry.COLUMN_BOOKS);
            int titlesci = cursor.getColumnIndex(GotContract.GotEntry.COLUMN_TITLES);

            // Extract out the value from the Cursor for the given column index
            String images = cursor.getString(imageci);
            String names = cursor.getString(nameci);
            String genders = cursor.getString(genderci);
            String cultures = cursor.getString(cultureci);
            String houses = cursor.getString(houseci);
            String bookss = cursor.getString(booksci);
            String titless = cursor.getString(titlesci);
            ConnectivityManager connMgr = (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo;

            networkInfo = connMgr.getActiveNetworkInfo();
            // Update the views on the screen with the values from the database
            if (networkInfo != null && networkInfo.isConnected())
                Picasso.get().load(images).resize(800, 800).into(imageView);
            else
                imageView.setImageResource(R.drawable.imagedb);

            cname.setText(names);
            gender.setText(genders);
            culture.setText(cultures);
            house.setText(houses);
            books.setText(bookss);
            titles.setText(titless);


        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cname.setText("");
        gender.setText("");
        culture.setText("");
        house.setText("");
        books.setText("");
        titles.setText("");
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        return true;
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Delete this Search Info");
        builder.setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteTrack();
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private void deleteTrack() {
        if (mCurrentUri != null) {
            int rowsDeleted = getContentResolver().delete(mCurrentUri, null, null);
            if (rowsDeleted == 0) {
                Toast.makeText(this, "Error with deleting history",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Search history deleted",
                        Toast.LENGTH_SHORT).show();

            }
            Intent intent = new Intent(SearchHistoryCharacterInfo.this, MainActivity.class);
            startActivity(intent);
            finish();

        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SearchHistoryCharacterInfo.this, MainActivity.class);
        startActivity(intent);
    }


}
