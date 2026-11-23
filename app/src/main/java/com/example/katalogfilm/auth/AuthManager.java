package com.example.katalogfilm.auth;

import com.example.katalogfilm.data.model.CreateSessionRequest;
import com.example.katalogfilm.data.response.AuthRequestTokenResponse;
import com.example.katalogfilm.data.response.AuthSessionResponse;
import com.example.katalogfilm.data.remote.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;

public class AuthManager {

    // Request Token
    public void createRequestToken(
            Callback<AuthRequestTokenResponse> callback
    ) {
        Call<AuthRequestTokenResponse> call =
                RetrofitClient.getService().createRequestToken();

        call.enqueue(callback);
    }

    // Create Session
    public void createSession(
            String requestToken,
            Callback<AuthSessionResponse> callback
    ) {
        CreateSessionRequest request =
                new CreateSessionRequest(requestToken);

        Call<AuthSessionResponse> call =
                RetrofitClient.getService().createSession(request);

        call.enqueue(callback);
    }
}