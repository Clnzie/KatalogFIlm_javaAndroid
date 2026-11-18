package com.example.katalogfilm.data.model;

import com.google.gson.annotations.SerializedName;

public class Movie {
    private final int id;

    private final String title;

    @SerializedName("poster_path")
    private final String posterPath;

    @SerializedName("vote_average")
    private final double voteAverage;

    @SerializedName("backdrop_path")
    private final String backdropPath;


    public Movie(int id, String title, String posterPath, double voteAverage, String backdropPath) {
        this.id = id;
        this.title = title;
        this.posterPath = posterPath;
        this.voteAverage = voteAverage;
        this.backdropPath = backdropPath;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPosterPath () {
        return posterPath;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public String getBackdropPath() {
        return backdropPath;
    }
}
