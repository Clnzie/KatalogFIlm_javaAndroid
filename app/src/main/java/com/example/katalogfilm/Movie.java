package com.example.katalogfilm;

public class Movie {
    private final int id;
    private final String title;
    private final String posterPath;
    private final double rating;

    public Movie(int id, String title, String posterPath, double rating) {
        this.id = id;
        this.title = title;
        this.posterPath = posterPath;
        this.rating = rating;
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

    public double getRating() {
        return rating;
    }
}
