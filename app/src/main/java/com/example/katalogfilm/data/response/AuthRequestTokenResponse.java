package com.example.katalogfilm.data.response;

public class AuthRequestTokenResponse {

    private boolean success;
    private String request_token;
    private String expires_at;

    public boolean isSuccess() {
        return success;
    }

    public String getRequestToken() {
        return request_token;
    }

    public String getExpiresAt() {
        return expires_at;
    }
}
