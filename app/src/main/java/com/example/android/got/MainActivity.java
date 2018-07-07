package com.example.android.got;

import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;
import com.example.android.got.data.GotContract;
import com.example.android.got.data.GotDbHelper;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    baseAdapter mAdapter;
    ListView characterList;
    GotDbHelper dbHelper;
    int textlength;
    NetworkInfo networkInfo;
    ArrayList<String> characters,array_sort;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        characters=new ArrayList<>();
        array_sort = new ArrayList<>();
        dbHelper = new GotDbHelper(this);
        characterList = (ListView) findViewById(R.id.SearchHistory);
        View emptyView = findViewById(R.id.empty_view);
        characterList.setEmptyView(emptyView);
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connMgr.getActiveNetworkInfo();
        refreshSearch();
        characterList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                final Uri currentUri = ContentUris.withAppendedId(GotContract.GotEntry.CONTENT_URI, dbHelper.getCharId(characters.get(position)));
                Intent intent = new Intent(MainActivity.this, SearchHistoryCharacterInfo.class);
                intent.setData(currentUri);
                startActivity(intent);
                refreshSearch();

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem menuItem=menu.findItem(R.id.menuSearch);
        SearchView searchView=(SearchView)menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query)
            {
                boolean recordExists = dbHelper.checkIfRecordExist(query);
                if (recordExists) {
                    final Uri currentUri = ContentUris.withAppendedId(GotContract.GotEntry.CONTENT_URI, dbHelper.getCharId(query));
                    Intent intent = new Intent(MainActivity.this, SearchHistoryCharacterInfo.class);
                    intent.setData(currentUri);
                    startActivity(intent);
                } else {
                        Intent intent = new Intent(MainActivity.this, SearchCharacterActivity.class);
                        Bundle extras = new Bundle();
                        extras.putString("character", query);
                        intent.putExtras(extras);

                    startActivity(intent);

                }
                refreshSearch();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                if (characters == null) ;
                else {
                    textlength = newText.length();
                if(!array_sort.isEmpty())
                    array_sort.clear();
                for (int i = 0; i < characters.size(); i++) {
                    if (textlength <= characters.get(i).length()) {
                        if (characters.get(i).toLowerCase().contains(
                                newText.toLowerCase().trim())) {
                            array_sort.add(characters.get(i));
                        }
                    }
                }
                    AppendList(array_sort);
                }

                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuSearch:

                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        return true;
    }

    public void AppendList(ArrayList<String> str)
    {
        baseAdapter adapter = new baseAdapter(this, str);

        characterList.setAdapter(adapter);
    }

    public void refreshSearch() {
        characters = dbHelper.getNames();
        if (characters == null)
            mAdapter = new baseAdapter(this);
        else
            mAdapter = new baseAdapter(this, characters);
        characterList.setAdapter(mAdapter);

    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Closing Application")
                .setMessage("Are you sure you want to close application?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }



}
