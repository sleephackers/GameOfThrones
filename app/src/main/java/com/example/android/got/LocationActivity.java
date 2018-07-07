
package com.example.android.got;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.got.Character.CharacterResponse;
import com.example.android.got.Character.Data;
import com.example.android.got.Location.DataLocation;
import com.example.android.got.Location.LocationResponse;
import com.example.android.got.RetrofitCalls.ApiClient;
import com.example.android.got.RetrofitCalls.ApiInterface;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationActivity extends AppCompatActivity {
    String name;
    ApiInterface apiInterface;
    String Locations = "Locations the character has been in:\n";
    TextView Location;
    NetworkInfo networkInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) ;
        else {
            Toast.makeText(LocationActivity.this, "CHECK YOUR NETWORK CONNECTIVITY",
                    Toast.LENGTH_SHORT).show();
            finish();
        }
        setContentView(R.layout.activity_location);
        Location = findViewById(R.id.Location);
        Bundle extras = getIntent().getExtras();
        name = extras.getString("character");
        fetchLocation();

    }

    public void fetchLocation() {
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<LocationResponse> call = apiInterface.getLocation(name);
        call.enqueue(new Callback<LocationResponse>() {
            @Override
            public void onResponse(Call<LocationResponse> call, Response<LocationResponse> response) {
                Log.e(SearchCharacterActivity.class.getSimpleName(), "STATUS: " + response.code());
                if (response.body().getMessage().equals("Success")) {
                    DataLocation location = response.body().getData().get(0);
                    if (location.getLocations().isEmpty())
                        Location.setText("No locations available");
                    else {
                        for (int i = 0; i < location.getLocations().size(); i++)
                            Locations += location.getLocations().get(i) + "\n";
                        Location.setText(Locations);
                    }

                } else Location.setText(response.body().getMessage());

            }


            @Override
            public void onFailure(Call<LocationResponse> call, Throwable t) {
                Log.e(LocationActivity.class.getSimpleName(), t.toString());
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(LocationActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
