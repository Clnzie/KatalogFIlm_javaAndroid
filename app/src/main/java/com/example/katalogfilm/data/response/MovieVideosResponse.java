
package com.example.katalogfilm.data.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieVideosResponse {

    @SerializedName("id")
    private int id;

    @SerializedName("results")
    private List<Video> results;

    public int getId() {
        return id;
    }

    public List<Video> getResults() {
        return results;
    }

    public static class Video {

        @SerializedName("key")
        private String key;

        @SerializedName("name")
        private String name;

        @SerializedName("site")
        private String site;

        @SerializedName("type")
        private String type;

        @SerializedName("official")
        private boolean official;

        public String getKey() {
            return key;
        }

        public String getName() {
            return name;
        }

        public String getSite() {
            return site;
        }

        public String getType() {
            return type;
        }

        public boolean isOfficial() {
            return official;
        }
    }
}
