package com.example.katalogfilm.data.repository;

import com.example.katalogfilm.data.model.Profile;
import com.example.katalogfilm.data.remote.ProfileAPI;
import com.example.katalogfilm.data.remote.SupabaseClient;
import com.example.katalogfilm.data.supabase.SupabaseConfig;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileRepository {

    private final ProfileAPI api;

    public ProfileRepository() {
        api = SupabaseClient.getClient()
                .create(ProfileAPI.class);
    }

    public void getProfile(
            String userId,
            String accessToken,
            ProfileCallback callback
    ) {

        Call<List<Profile>> call = api.getProfile(
                "*",
                "eq." + userId,
                SupabaseConfig.API_KEY,
                "Bearer " + accessToken
        );

        call.enqueue(new Callback<List<Profile>>() {

            @Override
            public void onResponse(
                    Call<List<Profile>> call,
                    Response<List<Profile>> response
            ) {

                if (response.isSuccessful()
                        && response.body() != null
                        && !response.body().isEmpty()) {

                    callback.onSuccess(
                            response.body().get(0)
                    );

                } else {

                    callback.onError(
                            "Gagal mengambil Profile. HTTP "
                                    + response.code()
                    );
                }
            }

            @Override
            public void onFailure(
                    Call<List<Profile>> call,
                    Throwable t
            ) {

                callback.onError(t.getMessage());
            }
        });
    }

    public interface ProfileCallback {

        void onSuccess(Profile profile);

        void onError(String message);
    }
}