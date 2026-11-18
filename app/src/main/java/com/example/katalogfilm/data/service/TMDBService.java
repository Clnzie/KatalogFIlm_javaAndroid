package com.example.katalogfilm.data.service;

import com.example.katalogfilm.data.model.Movie;
import com.example.katalogfilm.data.response.MovieCreditsResponse;
import com.example.katalogfilm.data.response.MovieDetailResponse;
import com.example.katalogfilm.data.response.MovieResponse;
import com.example.katalogfilm.data.response.MovieVideosResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TMDBService {

    @GET("movie/now_playing")
    Call<MovieResponse> getNowPlayingMovies(
            @Query("language") String language,
            @Query("page") int page
    );

    @GET("movie/popular")
    Call<MovieResponse> getPopularMovies(
            @Query("language") String language,
            @Query("page") int page
    );

    @GET("movie/top_rated")
    Call<MovieResponse> getTopRatedMovies(
            @Query("language") String language,
            @Query("page") int page
    );

    @GET("movie/{movie_id}")
    Call<MovieDetailResponse> getMovieDetail(
            @Path("movie_id") int movieId,
            @Query("language") String language
    );

    @GET("movie/{movie_id}/credits")
    Call<MovieCreditsResponse> getMovieCredits(
            @Path("movie_id") int movieId
    );

    @GET("movie/{movie_id}/videos")
    Call<MovieVideosResponse> getMovieVideos(
            @Path("movie_id") int movieId,
            @Query("language") String language
    );

    @GET("search/movie")
    Call<MovieResponse> searchMovies(
            @Query("query") String query,
            @Query("language") String language,
            @Query("page") int page,
            @Query("include_adult") boolean includeAdult
    );
}
