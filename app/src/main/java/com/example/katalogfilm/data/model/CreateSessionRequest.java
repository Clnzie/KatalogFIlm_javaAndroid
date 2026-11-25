package com.example.katalogfilm.data.model;

public class CreateSessionRequest {

    private String request_token;

    public CreateSessionRequest(String requestToken) {
        this.request_token = requestToken;
    }

    public String getRequestToken() {
        return request_token;
    }
}
