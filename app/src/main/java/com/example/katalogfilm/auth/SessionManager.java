package com.example.katalogfilm.auth;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.katalogfilm.data.response.AuthResponse;

public class SessionManager {

    private static final String PREF_NAME = "MovixSession";

    private static final String KEY_ACCESS_TOKEN = "access_token";
    private static final String KEY_REFRESH_TOKEN = "refresh_token";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_EMAIL = "email";

    private SharedPreferences preferences;

    public SessionManager(Context context) {

        preferences = context.getSharedPreferences(
                PREF_NAME,
                Context.MODE_PRIVATE
        );
    }

    // SIMPAN SESSION
    public void saveSession(AuthResponse response) {

        if (response == null || response.getUser() == null) {
            return;
        }

        preferences.edit()
                .putString(KEY_ACCESS_TOKEN,
                        response.getAccessToken())
                .putString(KEY_REFRESH_TOKEN,
                        response.getRefreshToken())
                .putString(KEY_USER_ID,
                        response.getUser().getId())
                .putString(KEY_EMAIL,
                        response.getUser().getEmail())
                .apply();
    }

    // CEK LOGIN
    public boolean isLoggedIn() {

        String token = getAccessToken();

        return token != null && !token.isEmpty();
    }

    // AMBIL ACCESS TOKEN
    public String getAccessToken() {

        return preferences.getString(
                KEY_ACCESS_TOKEN,
                null
        );
    }

    // AMBIL REFRESH TOKEN
    public String getRefreshToken() {

        return preferences.getString(
                KEY_REFRESH_TOKEN,
                null
        );
    }

    // AMBIL USER ID
    public String getUserId() {

        return preferences.getString(
                KEY_USER_ID,
                null
        );
    }

    // AMBIL EMAIL
    public String getEmail() {

        return preferences.getString(
                KEY_EMAIL,
                null
        );
    }

    // LOGOUT / HAPUS SESSION
    public void logout() {

        preferences.edit()
                .clear()
                .apply();
    }
}