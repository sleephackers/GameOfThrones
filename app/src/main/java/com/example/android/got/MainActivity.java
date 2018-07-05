package com.example.android.got;

import android.app.Activity;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.got.data.GotContract;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity/* implements android.app.LoaderManager.LoaderCallbacks<Cursor>*/ {
    private static final int CHARACTER_LOADER = 0;
    GotCursorAdapter mCursorAdapter;
    ListView characterList;
    int textlength;
    ArrayList<String> characters,array_sort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        array_sort=new ArrayList<>();
        characters=new ArrayList<>();
        loadData();
        characterList = (ListView) findViewById(R.id.SearchHistory);
        baseAdapter adapter = new baseAdapter(this, characters);

        characterList.setAdapter(adapter);

        characterList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                final Uri currentUri = ContentUris.withAppendedId(GotContract.GotEntry.CONTENT_URI, id);
                Intent intent = new Intent(MainActivity.this, SearchHistoryCharacterInfo.class);
                intent.setData(currentUri);
                startActivity(intent);

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
                Intent intent = new Intent(MainActivity.this, SearchCharacterActivity.class);
                Bundle extras = new Bundle();
                extras.putString("character", query);
                intent.putExtras(extras);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                textlength = newText.length();
                if(!array_sort.isEmpty())
                     array_sort.clear();
                for (int i = 0; i < characters.size(); i++) {
                    if (textlength <= characters.get(i).length()) {
                        if (characters.get(i).toLowerCase().contains(
                               newText.toLowerCase().trim()))
                        {
                            array_sort.add(characters.get(i));
                        }
                    }
                }
                AppendList(array_sort);

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
    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("Name list", null);
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        characters = gson.fromJson(json, type);
        if (characters == null) {
            characters = new ArrayList<>();
        }

    }
    public void AppendList(ArrayList<String> str)
    {
        baseAdapter adapter = new baseAdapter(this, str);

        characterList.setAdapter(adapter);
    }


}
