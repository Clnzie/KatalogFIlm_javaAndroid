package com.example.katalogfilm.data.service;

import com.example.katalogfilm.data.model.LoginRequest;
import com.example.katalogfilm.data.model.RegisterRequest;
import com.example.katalogfilm.data.response.AuthResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SupabaseAuthService {

    @POST("signup")
    Call<AuthResponse> register(
            @Body RegisterRequest request
    );

    @POST("token")
    Call<AuthResponse> login(
            @Query("grant_type") String grantType,
            @Body LoginRequest request
    );
}