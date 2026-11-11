package com.example.katalogfilm.data.model;

import com.google.gson.annotations.SerializedName;

public class Movie {
    private final int id;

    private final String title;

    @SerializedName("poster_path")
    private final String posterPath;

    @SerializedName("vote_average")
    private final double voteAverage;


    public Movie(int id, String title, String posterPath, double voteAverage) {
        this.id = id;
        this.title = title;
        this.posterPath = posterPath;
        this.voteAverage = voteAverage;
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
}
