package com.example.katalogfilm.data.model;

public class BannerMovie {

    private String title;
    private String genre;
    private int image;

    public BannerMovie(String title, String genre, int image) {
        this.title = title;
        this.genre = genre;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public int getImage() {
        return image;
    }
}
