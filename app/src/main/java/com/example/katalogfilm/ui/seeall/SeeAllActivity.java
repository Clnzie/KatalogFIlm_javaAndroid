
package com.example.katalogfilm.ui.seeall;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.katalogfilm.data.model.Movie;
import com.example.katalogfilm.data.response.MovieResponse;
import com.example.katalogfilm.data.remote.RetrofitClient;
import com.example.katalogfilm.databinding.ActivitySeeAllBinding;
import com.example.katalogfilm.ui.home.MovieDetail.MovieDetail;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SeeAllActivity extends AppCompatActivity {

    private ActivitySeeAllBinding binding;

    private SeeAllAdapter adapter;

    private String category;

    private int currentPage = 1;
    private int totalPages = 1;

    private boolean isLoading = false;

    private Call<MovieResponse> movieCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySeeAllBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);

        ViewCompat.setOnApplyWindowInsetsListener(
                binding.getRoot(),
                (view, windowInsets) -> {

                    Insets systemBars = windowInsets.getInsets(
                            WindowInsetsCompat.Type.systemBars()
                    );

                    view.setPadding(
                            0,
                            systemBars.top,
                            0,
                            systemBars.bottom
                    );

                    return windowInsets;
                }
        );

        category = getIntent().getStringExtra("category");

        setupToolbar();
        setupRecyclerView();

        fetchMovies();

    }

    private void setupToolbar() {

        if (category == null) {
            finish();
            return;
        }

        if (category.equals("now_playing")) {
            binding.tvPageTitle.setText("Now Playing");

        } else if (category.equals("popular")) {
            binding.tvPageTitle.setText("Popular Movies");

        } else if (category.equals("top_rated")) {
            binding.tvPageTitle.setText("Top Rated Movies");

        } else {
            finish();
            return;
        }

        binding.btnBack.setOnClickListener(v -> finish());
    }

    private void setupRecyclerView() {

        GridLayoutManager layoutManager =
                new GridLayoutManager(this, 3);

        adapter = new SeeAllAdapter(movie -> {

            Intent intent = new Intent(
                    SeeAllActivity.this,
                    MovieDetail.class
            );

            intent.putExtra("movie_id", movie.getId());

            startActivity(intent);

        });

        binding.rvSeeAll.setLayoutManager(layoutManager);
        binding.rvSeeAll.setAdapter(adapter);

        binding.rvSeeAll.addOnScrollListener(
                new RecyclerView.OnScrollListener() {

                    @Override
                    public void onScrolled(
                            RecyclerView recyclerView,
                            int dx,
                            int dy) {

                        super.onScrolled(recyclerView, dx, dy);

                        if (dy <= 0) return;

                        int visibleItemCount =
                                layoutManager.getChildCount();

                        int totalItemCount =
                                layoutManager.getItemCount();

                        int firstVisibleItem =
                                layoutManager.findFirstVisibleItemPosition();

                        if (!isLoading &&
                                currentPage < totalPages &&
                                (visibleItemCount + firstVisibleItem)
                                        >= totalItemCount - 5) {

                            fetchMovies();
                        }
                    }
                });
    }

    private void fetchMovies() {

        if (isLoading || currentPage > totalPages) {
            return;
        }

        isLoading = true;
        binding.progressBar.setVisibility(android.view.View.VISIBLE);

        if (category.equals("now_playing")) {

            movieCall = RetrofitClient.getService()
                    .getNowPlayingMovies("en-US", currentPage);

        } else if (category.equals("popular")) {

            movieCall = RetrofitClient.getService()
                    .getPopularMovies("en-US", currentPage);

        } else if (category.equals("top_rated")) {

            movieCall = RetrofitClient.getService()
                    .getTopRatedMovies("en-US", currentPage);
        }

        if (movieCall == null) {
            isLoading = false;
            binding.progressBar.setVisibility(android.view.View.GONE);
            return;
        }

        movieCall.enqueue(new Callback<MovieResponse>() {

            @Override
            public void onResponse(
                    Call<MovieResponse> call,
                    Response<MovieResponse> response) {

                isLoading = false;
                binding.progressBar.setVisibility(
                        android.view.View.GONE
                );

                if (response.isSuccessful() && response.body() != null) {

                    MovieResponse movieResponse = response.body();

                    List<Movie> movies = movieResponse.getResults();

                    if (movies != null && !movies.isEmpty()) {
                        adapter.addMovies(movies);
                    }

                    totalPages = movieResponse.getTotalPages();

                    currentPage++;

                } else {

                    Toast.makeText(
                            SeeAllActivity.this,
                            "Gagal mengambil data film",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }

            @Override
            public void onFailure(
                    Call<MovieResponse> call,
                    Throwable t) {

                isLoading = false;
                binding.progressBar.setVisibility(
                        android.view.View.GONE
                );

                if (!call.isCanceled()) {
                    Toast.makeText(
                            SeeAllActivity.this,
                            "Koneksi gagal: " + t.getMessage(),
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {

        if (movieCall != null) {
            movieCall.cancel();
        }

        super.onDestroy();
    }
}
