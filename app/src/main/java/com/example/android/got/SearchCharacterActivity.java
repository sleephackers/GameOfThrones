package com.example.android.got;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.android.got.Character.CharacterResponse;
import com.example.android.got.Character.Data;
import com.example.android.got.RetrofitCalls.ApiClient;
import com.example.android.got.RetrofitCalls.ApiInterface;
import com.example.android.got.data.GotContract;
import com.squareup.picasso.Picasso;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    String imageViewText;

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
                if (response.body() == null)
                    cname.setText("COUDNT FIND THE CHARACTER");
                else
               {
                   Data character=response.body().getData();
                   imageViewText="https://api.got.show"+character.getImageLink();
                   Picasso.get().load("https://api.got.show"+character.getImageLink()).resize(800, 800).into(imageView);
                   cname.setText(character.getName());
                   if(character.getMale())
                       gender.setText("Male");
                   else
                       gender.setText("Female");
                   {
                       if (character.getCulture() == null)
                           culture.setText("No Info Available");
                       else
                           culture.setText(character.getCulture());
                   }
                   if (house == null)
                       house.setText("No Info Available");
                   else
                       house.setText(character.getHouse());
                   for(int i=0;i<character.getBooks().size();i++)
                   {booksText+=character.getBooks().get(i);
                    booksText+="\n";}
                   books.setText(booksText);
                   if(character.getTitles().isEmpty()) {
                       titles.setText("No Title");
                       titlesText = "No Title";
                   }
                   else
                       { for(int j=0;j<character.getTitles().size();j++)
                   {titlesText+=character.getTitles().get(j);titlesText+="\n";}
                   titles.setText(titlesText);}
                   saveCharacter();
               }

            }


            @Override
            public void onFailure(Call<CharacterResponse> call, Throwable t) {
                Log.e(SearchCharacterActivity.class.getSimpleName(), t.toString());
            }
        });
    }

    private void saveCharacter() {

        String images = imageViewText;
        String names = cname.getText().toString();
        String genders = gender.getText().toString();
        String cultures = culture.getText().toString();
        String houses = house.getText().toString();
        String bookss = booksText;
        String titless = titlesText;

        ContentValues values = new ContentValues();
        values.put(GotContract.GotEntry.COLUMN_IMAGE,images );
        values.put(GotContract.GotEntry.COLUMN_NAME,names );
        values.put(GotContract.GotEntry.COLUMN_GENDER,genders );
        values.put(GotContract.GotEntry.COLUMN_CULTURE,cultures );
        values.put(GotContract.GotEntry.COLUMN_HOUSE,houses );
        values.put(GotContract.GotEntry.COLUMN_BOOKS, bookss);
        values.put(GotContract.GotEntry.COLUMN_TITLES,titless );



        Uri newUri = getContentResolver().insert(GotContract.GotEntry.CONTENT_URI, values);
        if (newUri == null) {
            Toast.makeText(this, "SEARCH RESULT WASNT SAVED",
                    Toast.LENGTH_SHORT).show();
        } else {
            {
                Toast.makeText(this, "SEARCH RESULT SAVED",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_location, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.searchLocation:
                Intent intent = new Intent(SearchCharacterActivity.this, LocationActivity.class);
                Bundle extras = new Bundle();
                extras.putString("character",name );
                intent.putExtras(extras);
                startActivity(intent);

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SearchCharacterActivity.this, MainActivity.class);
        startActivity(intent);
    }

}