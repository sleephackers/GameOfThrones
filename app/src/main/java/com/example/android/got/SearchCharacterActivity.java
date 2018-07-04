package com.example.android.got;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.got.Character.CharacterResponse;
import com.example.android.got.Character.Data;
import com.example.android.got.RetrofitCalls.ApiClient;
import com.example.android.got.RetrofitCalls.ApiInterface;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;

public class SearchCharacterActivity extends AppCompatActivity {
    String name;
    ApiInterface apiInterface;
    ImageView imageView;
    TextView cname;
    TextView gender;
    TextView culture;
    TextView house;
    TextView books;
    TextView titles;
    String booksText="";
    String titlesText="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_character);
        imageView=findViewById(R.id.image);
        cname=findViewById(R.id.name);
        gender=findViewById(R.id.gender);
        culture=findViewById(R.id.culture);
        house=findViewById(R.id.house);
        books=findViewById(R.id.books);
        titles=findViewById(R.id.titles);
        Bundle extras = getIntent().getExtras();
        name = extras.getString("character");
        fetchCharacter();

    }
    public void fetchCharacter() {
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<CharacterResponse> call = apiInterface.getCharacter(name);
        call.enqueue(new Callback<CharacterResponse>() {
            @Override
            public void onResponse(Call<CharacterResponse> call, Response<CharacterResponse> response) {
               Log.e(SearchCharacterActivity.class.getSimpleName(),"STATUS: "+response.code());
                if(response.body().getMessage().equals("Success"))
               {
                   Data character=response.body().getData();
                   Picasso.get().load("https://api.got.show"+character.getImageLink()).resize(800, 800).into(imageView);
                   cname.setText(character.getName());
                   if(character.getMale())
                       gender.setText("Male");
                   else
                       gender.setText("Female");
                   culture.setText(character.getCulture());
                   house.setText(character.getHouse());
                   for(int i=0;i<character.getBooks().size();i++)
                   {booksText+=character.getBooks().get(i);
                    booksText+="\n";}
                   books.setText(booksText);
                   if(character.getTitles().isEmpty())
                       titles.setText("");
                   else
                       { for(int j=0;j<character.getTitles().size();j++)
                   {titlesText+=character.getTitles().get(j);titlesText+="\n";}
                   titles.setText(titlesText);}
                  // saveCharacter();
               }
               else cname.setText(response.body().getMessage());

            }


            @Override
            public void onFailure(Call<CharacterResponse> call, Throwable t) {
                Log.e(SearchCharacterActivity.class.getSimpleName(), t.toString());
            }
        });
    }

    /*private void saveCharacter() {

        String images = title.getText().toString();
        String names = artist.getText().toString();
        String genders = album.getText().toString();
        String cultures = genres.getText().toString();
        String houses = yearofrelease.getText().toString();
        String  = lyricsText;

        ContentValues values = new ContentValues();
        values.put(TrackContract.TrackEntry.COLUMN_TITLE, titles);
        values.put(TrackContract.TrackEntry.COLUMN_ARTIST, artists);
        values.put(TrackContract.TrackEntry.COLUMN_ALBUM, albums);
        values.put(TrackContract.TrackEntry.COLUMN_GENRE, genress);
        values.put(TrackContract.TrackEntry.COLUMN_YEAROFRELEASE, yearofreleases);
        values.put(TrackContract.TrackEntry.COLUMN_LYRICS, lyricss);


        Uri newUri = getContentResolver().insert(TrackContract.TrackEntry.CONTENT_URI, values);

        if (newUri == null) {
            Toast.makeText(this, "INSERT FAILED",
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "INSERT SUCCESSFUL",
                    Toast.LENGTH_SHORT).show();
        }
    }*/
}