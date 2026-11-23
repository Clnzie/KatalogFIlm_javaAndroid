package com.example.katalogfilm.data.response;

public class AuthSessionResponse {

    private boolean success;
    private String session_id;

    public boolean isSuccess() {
        return success;
    }

    public String getSessionId() {
        return session_id;
    }
}
