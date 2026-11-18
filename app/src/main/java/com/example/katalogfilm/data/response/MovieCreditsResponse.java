
package com.example.katalogfilm.data.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieCreditsResponse {

    @SerializedName("id")
    private int id;

    @SerializedName("cast")
    private List<CastMember> cast;

    public int getId() {
        return id;
    }

    public List<CastMember> getCast() {
        return cast;
    }

    public static class CastMember {

        @SerializedName("id")
        private int id;

        @SerializedName("name")
        private String name;

        @SerializedName("character")
        private String character;

        @SerializedName("profile_path")
        private String profilePath;

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getCharacter() {
            return character;
        }

        public String getProfilePath() {
            return profilePath;
        }
    }
}
