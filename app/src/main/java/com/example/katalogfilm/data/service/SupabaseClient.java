package com.example.katalogfilm.data.service;

import com.example.katalogfilm.BuildConfig;

import okhttp3.OkHttpClient;
import okhttp3.Request;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SupabaseClient {

    private static Retrofit retrofit;

    public static Retrofit getClient() {

        if (retrofit == null) {

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(chain -> {

                        Request request = chain.request()
                                .newBuilder()
                                .addHeader("apikey", BuildConfig.SUPABASE_KEY)
                                .addHeader("Content-Type", "application/json")
                                .build();

                        return chain.proceed(request);
                    })
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BuildConfig.SUPABASE_URL + "/auth/v1/")
                    .client(client)
                    .addConverterFactory(
                            GsonConverterFactory.create()
                    )
                    .build();
        }

        return retrofit;
    }

    public static SupabaseAuthService getAuthService() {
        return getClient().create(SupabaseAuthService.class);
    }
}