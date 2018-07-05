package com.example.android.got.RetrofitCalls;


import com.example.android.got.Character.CharacterResponse;
import com.example.android.got.Location.LocationResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("characters/{name}")
    Call<CharacterResponse> getCharacter(@Path("name") String name);

    @GET("characters/locations/{name}")
    Call<LocationResponse> getLocation(@Path("name") String name);



}
