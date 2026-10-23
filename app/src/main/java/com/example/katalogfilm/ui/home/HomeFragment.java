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
import com.example.katalogfilm.data.model.remote.TmdbClient;
import com.example.katalogfilm.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.List;



public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private MovieAdapter adapter;

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

        adapter = new MovieAdapter(new ArrayList<>());

        binding.recyclerMovie.setLayoutManager(
                new LinearLayoutManager(requireContext())
        );

        binding.recyclerMovie.setAdapter(adapter);

        loadMovies();
    }

    private void loadMovies() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.txtError.setVisibility(View.GONE);

        TmdbClient.getPopularMovies(
                new TmdbClient.MovieCallback() {
                    @Override
                    public void onSuccess(List<Movie> movies) {
                        if (binding == null) return;

                        binding.progressBar.setVisibility(View.GONE);

                        adapter = new MovieAdapter(movies);
                        binding.recyclerMovie.setAdapter(adapter);
                    }

                    @Override
                    public void onError(String msg) {
                        if (binding == null) return;

                        binding.progressBar.setVisibility(View.GONE);
                        binding.txtError.setVisibility(View.VISIBLE);
                        binding.txtError.setText(
                                "Gagal Memuat Film:" + msg
                        );
                    }
                }
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}