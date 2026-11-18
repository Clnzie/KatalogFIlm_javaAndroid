
package com.example.katalogfilm.ui.search;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.katalogfilm.data.model.Movie;
import com.example.katalogfilm.data.response.MovieResponse;
import com.example.katalogfilm.data.remote.RetrofitClient;
import com.example.katalogfilm.databinding.FragmentSearchBinding;
import com.example.katalogfilm.ui.home.MovieDetail.MovieDetail;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;

    private SearchAdapter searchAdapter;
    private GridLayoutManager gridLayoutManager;

    private Handler searchHandler = new Handler(Looper.getMainLooper());
    private Runnable searchRunnable;

    private Call<MovieResponse> searchCall;

    private String currentQuery = "";

    private int currentPage = 1;
    private int totalPages = 1;

    private boolean isLoading = false;
    private boolean isSearching = false;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        binding = FragmentSearchBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        setupRecyclerView();
        setupSearch();

        showEmptyState("No One Movie Found");
    }

    private void setupRecyclerView() {

        gridLayoutManager = new GridLayoutManager(requireContext(), 3);

        searchAdapter = new SearchAdapter(movie -> {

            Intent intent = new Intent(
                    requireContext(),
                    MovieDetail.class
            );

            intent.putExtra("movie_id", movie.getId());

            startActivity(intent);
        });

        binding.rvSearchMovies.setLayoutManager(gridLayoutManager);
        binding.rvSearchMovies.setAdapter(searchAdapter);

        binding.rvSearchMovies.addOnScrollListener(
                new RecyclerView.OnScrollListener() {

                    @Override
                    public void onScrolled(
                            @NonNull RecyclerView recyclerView,
                            int dx,
                            int dy) {

                        super.onScrolled(recyclerView, dx, dy);

                        if (dy <= 0 || isLoading || !isSearching) {
                            return;
                        }

                        int visibleItemCount = gridLayoutManager.getChildCount();
                        int totalItemCount = gridLayoutManager.getItemCount();
                        int firstVisibleItem =
                                gridLayoutManager.findFirstVisibleItemPosition();

                        if (currentPage <= totalPages &&
                                (visibleItemCount + firstVisibleItem)
                                        >= totalItemCount - 5) {

                            fetchSearchMovies(currentQuery, currentPage, false);
                        }
                    }
                });
    }

    private void setupSearch() {

        binding.etSearchMovie.addTextChangedListener(
                new android.text.TextWatcher() {

                    @Override
                    public void beforeTextChanged(
                            CharSequence s,
                            int start,
                            int count,
                            int after) {
                    }

                    @Override
                    public void onTextChanged(
                            CharSequence s,
                            int start,
                            int before,
                            int count) {

                        String query = s.toString().trim();

                        if (searchRunnable != null) {
                            searchHandler.removeCallbacks(searchRunnable);
                        }

                        if (searchCall != null) {
                            searchCall.cancel();
                        }

                        currentQuery = query;
                        currentPage = 1;
                        totalPages = 1;
                        isLoading = false;
                        isSearching = false;

                        searchAdapter.setMovies(new ArrayList<>());

                        if (query.isEmpty()) {
                            showEmptyState("No One Movie Found");
                            return;
                        }

                        if (query.length() < 2) {
                            showEmptyState("Type at least 2 characters");
                            return;
                        }

                        binding.layoutSearchEmpty.setVisibility(View.GONE);
                        binding.rvSearchMovies.setVisibility(View.GONE);
                        binding.progressSearch.setVisibility(View.GONE);

                        searchRunnable = () -> {
                            isSearching = true;
                            fetchSearchMovies(currentQuery, 1, true);
                        };

                        searchHandler.postDelayed(searchRunnable, 500);
                    }

                    @Override
                    public void afterTextChanged(
                            android.text.Editable s) {
                    }
                });

        binding.etSearchMovie.setOnEditorActionListener(
                (v, actionId, event) -> {

                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                        if (searchRunnable != null) {
                            searchHandler.removeCallbacks(searchRunnable);
                        }

                        String query = binding.etSearchMovie
                                .getText().toString().trim();

                        if (query.length() >= 2) {

                            currentQuery = query;
                            currentPage = 1;
                            totalPages = 1;
                            isSearching = true;
                            isLoading = false;

                            searchAdapter.setMovies(new ArrayList<>());

                            fetchSearchMovies(query, 1, true);
                        }

                        return true;
                    }

                    return false;
                });
    }

    private void fetchSearchMovies(
            String query,
            int page,
            boolean isNewSearch) {

        if (isLoading || query.isEmpty()) {
            return;
        }

        isLoading = true;

        binding.progressSearch.setVisibility(View.VISIBLE);

        if (isNewSearch) {
            binding.rvSearchMovies.setVisibility(View.GONE);
            binding.layoutSearchEmpty.setVisibility(View.GONE);
        }

        searchCall = RetrofitClient.getService().searchMovies(
                query,
                "en-US",
                page,
                false
        );

        Call<MovieResponse> request = searchCall;

        request.enqueue(new Callback<MovieResponse>() {

            @Override
            public void onResponse(
                    @NonNull Call<MovieResponse> call,
                    @NonNull Response<MovieResponse> response) {

                if (call.isCanceled() || !query.equals(currentQuery)) {
                    return;
                }

                isLoading = false;
                binding.progressSearch.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {

                    MovieResponse movieResponse = response.body();
                    List<Movie> movies = movieResponse.getResults();

                    totalPages = movieResponse.getTotalPages();

                    if (movies != null && !movies.isEmpty()) {

                        if (page == 1) {
                            searchAdapter.setMovies(movies);
                        } else {
                            searchAdapter.addMovies(movies);
                        }

                        currentPage = page + 1;

                        binding.layoutSearchEmpty.setVisibility(View.GONE);
                        binding.rvSearchMovies.setVisibility(View.VISIBLE);

                    } else if (page == 1) {

                        showEmptyState("No One Movie Found");
                    }

                } else {

                    if (page == 1) {
                        showEmptyState("Failed to search movies");
                    }
                }
            }

            @Override
            public void onFailure(
                    @NonNull Call<MovieResponse> call,
                    @NonNull Throwable t) {

                if (call.isCanceled() || !query.equals(currentQuery)) {
                    return;
                }

                isLoading = false;
                binding.progressSearch.setVisibility(View.GONE);

                if (page == 1) {
                    showEmptyState("Something went wrong");
                }
            }
        });
    }

    private void showEmptyState(String message) {

        binding.rvSearchMovies.setVisibility(View.GONE);
        binding.layoutSearchEmpty.setVisibility(View.VISIBLE);
        binding.progressSearch.setVisibility(View.GONE);

        binding.tvSearchEmpty.setText(message);
    }

    @Override
    public void onDestroyView() {

        if (searchRunnable != null) {
            searchHandler.removeCallbacks(searchRunnable);
        }

        if (searchCall != null) {
            searchCall.cancel();
        }

        binding = null;

        super.onDestroyView();
    }
}
