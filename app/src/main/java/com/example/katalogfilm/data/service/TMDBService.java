package com.example.katalogfilm.data.service;

import com.example.katalogfilm.data.response.MovieResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TMDBService {

    @GET("movie/now_playing")
    Call<MovieResponse> getNowPlayingMovies(
            @Query("language") String language,
            @Query("page") int page
    );
}
