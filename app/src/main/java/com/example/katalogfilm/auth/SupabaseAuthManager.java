package com.example.katalogfilm.auth;

import com.example.katalogfilm.data.model.LoginRequest;
import com.example.katalogfilm.data.model.RegisterRequest;
import com.example.katalogfilm.data.response.AuthResponse;
import com.example.katalogfilm.data.service.SupabaseClient;

import retrofit2.Call;
import retrofit2.Callback;

public class SupabaseAuthManager {

    public void register(
            String email,
            String password,
            Callback<AuthResponse> callback
    ) {

        RegisterRequest request =
                new RegisterRequest(email, password);

        Call<AuthResponse> call =
                SupabaseClient.getAuthService().register(request);

        call.enqueue(callback);
    }

    // LOGIN
    public void login(
            String email,
            String password,
            Callback<AuthResponse> callback
    ) {

        LoginRequest request =
                new LoginRequest(email, password);

        Call<AuthResponse> call =
                SupabaseClient.getAuthService().login(
                        "password",
                        request
                );

        call.enqueue(callback);
    }
}