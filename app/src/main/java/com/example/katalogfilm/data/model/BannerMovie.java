package com.example.katalogfilm.data.model;

public class BannerMovie {
    private int id;

    private String title;
    private String genre;
    private String backdropPath;

    public BannerMovie(int id, String title, String genre, String backdropPath) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.backdropPath = backdropPath;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public String getBackdropPath() {
        return backdropPath;
    }
}
