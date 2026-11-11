package com.example.katalogfilm.data.remote;

import com.example.katalogfilm.data.service.TMDBService;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL = "https://api.themoviedb.org/3/";

    private static final String TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJiMGIxYjc1NDI5NjNiZWZjMmY4NDhjZTM2M2U1YzRhYiIsIm5iZiI6MTcwNTk3NDE0MC4wMzgsInN1YiI6IjY1YWYxOTdjYmQ1ODhiMDEwYjVkYzQ4NSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.U-e93328v0DHgDO4dWJyCha_THXFdANzWr1uy37SF-A";

    private static Retrofit retrofit;

    public static TMDBService getService() {

        if (retrofit == null) {

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(chain -> {

                        Request request = chain.request()
                                .newBuilder()
                                .addHeader(
                                        "Authorization",
                                        "Bearer " + TOKEN
                                )
                                .addHeader(
                                        "accept",
                                        "application/json"
                                )
                                .build();

                        return chain.proceed(request);
                    }).build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(
                            GsonConverterFactory.create()
                    ).build();
        }

        return retrofit.create(TMDBService.class);
    }
}
