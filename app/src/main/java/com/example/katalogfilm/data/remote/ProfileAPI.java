package com.example.katalogfilm.data.remote;

import com.example.katalogfilm.data.model.Profile;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface ProfileAPI {

    @GET("profiles")
    Call<List<Profile>> getProfile(

            @Query("select") String select,

            @Query("user_id") String userId,

            @Header("apikey") String apiKey,

            @Header("Authorization") String authorization
    );
}