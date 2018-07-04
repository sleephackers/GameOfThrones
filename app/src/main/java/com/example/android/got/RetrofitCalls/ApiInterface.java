package com.example.android.got.RetrofitCalls;


import com.example.android.got.Character.CharacterResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("characters/{name}")
    Call<CharacterResponse> getCharacter(@Path("name") String name);




}
