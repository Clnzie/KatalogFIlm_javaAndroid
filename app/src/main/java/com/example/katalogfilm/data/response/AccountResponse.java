package com.example.katalogfilm.data.response;

import com.google.gson.annotations.SerializedName;

public class AccountResponse {

    private int id;
    private String username;
    private String name;
    private Avatar avatar;

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public Avatar getAvatar() {
        return avatar;
    }

    public static class Avatar {

        private TMDB tmdb;

        public TMDB getTmdb() {
            return tmdb;
        }
    }

    public static class TMDB {

        @SerializedName("avatar_path")
        private String avatarPath;

        public String getAvatarPath() {
            return avatarPath;
        }
    }
}