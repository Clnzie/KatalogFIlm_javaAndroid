package com.example.katalogfilm.auth;

import com.example.katalogfilm.data.response.AccountResponse;
import com.example.katalogfilm.data.remote.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;

public class AccountManager {

    public void getAccount(String sessionId,
                           Callback<AccountResponse> callback) {

        Call<AccountResponse> call =
                RetrofitClient.getService().getAccount(sessionId);

        call.enqueue(callback);
    }
}