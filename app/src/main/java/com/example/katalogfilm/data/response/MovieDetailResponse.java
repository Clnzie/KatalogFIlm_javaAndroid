package com.example.katalogfilm.data.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieDetailResponse {

    @SerializedName("id")
    private int id;

    @SerializedName("title")
    private String title;

    @SerializedName("overview")
    private String overview;

    @SerializedName("poster_path")
    private String posterPath;

    @SerializedName("backdrop_path")
    private String backdropPath;

    @SerializedName("release_date")
    private String releaseDate;

    @SerializedName("vote_average")
    private double voteAverage;

    @SerializedName("status")
    private String status;

    @SerializedName("vote_count")
    private int voteCount;

    @SerializedName("original_language")
    private String originalLanguage;

    @SerializedName("popularity")
    private Double popularity;

    @SerializedName("genres")
    private List<Genre> genres;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public String getStatus() {
        return status;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public double getPopularity() {
        return popularity;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public static class Genre {

        @SerializedName("id")
        private int id;

        @SerializedName("name")
        private String name;

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }
}
