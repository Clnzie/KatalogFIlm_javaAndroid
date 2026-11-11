package com.example.katalogfilm.ui.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.katalogfilm.data.model.Movie;
//import com.example.katalogfilm.data.model.remote.TmdbClient;
import com.example.katalogfilm.data.remote.RetrofitClient;
import com.example.katalogfilm.data.response.MovieResponse;
import com.example.katalogfilm.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private MovieAdapter movieAdapter;

    private Call<MovieResponse> movieCall;

    private boolean nowPlayingFinished;

    public HomeFragment() {
        // kosong
        movieAdapter = new MovieAdapter();
    }

    @NonNull
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(
                inflater, container, false
        );
        return binding.getRoot();
    }

    @Override
    public void onViewCreated (
            @NonNull View view,
            @Nullable Bundle savedInstanceState
    ) {
        super.onViewCreated(view, savedInstanceState);

        nowPlayingFinished = false;

        setupRecycleView();
        fetchNowPlayingMovies();
    }

    private void updateLoading() {
        if (binding == null) return;

        boolean loading = !nowPlayingFinished;

        binding.progressBar.setVisibility(
                loading ? View.VISIBLE : View.GONE
        );
    }

    private void setupRecycleView() {
        movieAdapter = new MovieAdapter();

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(
                        requireContext(),
                        LinearLayoutManager.HORIZONTAL,
                        false
                );

        binding.rvNowPlaying.setLayoutManager(layoutManager);
        binding.rvNowPlaying.setAdapter(movieAdapter);

        binding.rvNowPlaying.setNestedScrollingEnabled(false);
    }

    private void fetchNowPlayingMovies() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.txtError.setVisibility(View.GONE);

        movieCall = RetrofitClient.getService().getNowPlayingMovies("id-ID",1);

        movieCall.enqueue(new Callback<MovieResponse>() {

            @Override
            public void onResponse(@NonNull Call<MovieResponse> call, @Nullable Response<MovieResponse> response) {
                if (binding == null) {
                    return;
                }

                nowPlayingFinished = true;

                binding.progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {

                    List<Movie> movies = response.body().getResults();

                    movieAdapter.setMovies(movies);

                    if (movies == null || movies.isEmpty()) {
                        binding.txtError.setText(
                                "Belum ada film yng ditemukan"
                        );
                        binding.txtError.setVisibility(View.VISIBLE);
                    }

                } else {
                    binding.txtError.setText(
                            "Gagal mengabil data. kode: "
                            + response.code()
                    );


                    binding.txtError.setVisibility(View.VISIBLE);

                    updateLoading();
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieResponse> call, @NonNull Throwable t) {
                if (binding == null) {
                    return;
                }

                nowPlayingFinished = true;

                binding.progressBar.setVisibility(View.GONE);
                binding.txtError.setText("Koneksi Gagal: " + t.getLocalizedMessage());

                binding.txtError.setVisibility(View.VISIBLE);

                updateLoading();
            }
        });
    }

    @Override
    public void onDestroyView() {
        if (movieCall != null) {
            movieCall.cancel();
            movieCall = null;
        }

        super.onDestroyView();
        binding = null;
    }
}