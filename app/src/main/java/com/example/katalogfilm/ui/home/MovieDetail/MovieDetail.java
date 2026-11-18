package com.example.katalogfilm.ui.home.MovieDetail;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.katalogfilm.R;
import com.example.katalogfilm.data.response.MovieDetailResponse;
import com.example.katalogfilm.data.remote.RetrofitClient;
import com.example.katalogfilm.databinding.ActivityDetailMovieBinding;
import com.example.katalogfilm.data.response.MovieCreditsResponse;
import com.example.katalogfilm.data.response.MovieVideosResponse;

import java.util.Locale;
import java.util.List;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetail extends AppCompatActivity {

    private ActivityDetailMovieBinding binding;

    private int movieId;

    private Call<MovieDetailResponse> detailCall;

    private CastAdapter castAdapter;

    private Call<MovieCreditsResponse> creditsCall;

    private Call<MovieVideosResponse> videosCall;

    private String trailerKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDetailMovieBinding.inflate(
                getLayoutInflater()
        );

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

        // Setup buat recycle cast movie
        setupCastRecyclerView();

        // Menerima ID film dari Intent
        movieId = getIntent().getIntExtra("movie_id", -1);

        // Tombol kembali
        binding.btnBack.setOnClickListener(v -> finish());

        if (movieId == -1) {
            finish();
            return;
        }

        // Ambil detail film dari TMDB
        fetchMovieDetail();

        // Ambil cast movie
        fetchMovieCredits();

        // ambil trailer movie
        fetchMovieTrailer();
    }

    private void fetchMovieDetail() {

        detailCall = RetrofitClient.getService()
                .getMovieDetail(movieId, "en-US");

        detailCall.enqueue(new Callback<MovieDetailResponse>() {

            @Override
            public void onResponse(
                    Call<MovieDetailResponse> call,
                    Response<MovieDetailResponse> response) {

                if (response.isSuccessful()
                        && response.body() != null) {

                    MovieDetailResponse movie = response.body();

                    displayMovieDetail(movie);

                } else {
                    Log.e("DETAIL_MOVIE",
                            "Response error: " + response.code());
                }
            }

            @Override
            public void onFailure(
                    Call<MovieDetailResponse> call,
                    Throwable t) {

                if (!call.isCanceled()) {
                    Log.e("DETAIL_MOVIE",
                            "Request failed: " + t.getMessage());
                }
            }
        });
    }

    private void displayMovieDetail(MovieDetailResponse movie) {

        // Judul
        binding.tvDetailTitle.setText(movie.getTitle());

        // Rating dan jumlah vote
        String rating = String.format(
                Locale.getDefault(),
                "★ %.1f  (%,d votes)",
                movie.getVoteAverage(),
                movie.getVoteCount()
        );

        binding.tvPopularity.setText(rating);

        // Overview
        String overview = movie.getOverview();

        if (overview != null && !overview.isEmpty()) {
            binding.tvOverview.setText(overview);
        } else {
            binding.tvOverview.setText("Sinopsis belum tersedia.");
        }

        // Status
        binding.tvStatus.setText(
                movie.getStatus() != null
                        ? movie.getStatus()
                        : "-"
        );

        // Release date
        binding.tvReleaseDate.setText(
                movie.getReleaseDate() != null
                        && !movie.getReleaseDate().isEmpty()
                        ? movie.getReleaseDate()
                        : "-"
        );

        // Original language
        binding.tvLanguage.setText(
                movie.getOriginalLanguage() != null
                        ? movie.getOriginalLanguage().toUpperCase(
                        Locale.ROOT
                )
                        : "-"
        );

        // Backdrop
        String backdropPath = movie.getBackdropPath();

        if (backdropPath != null && !backdropPath.isEmpty()) {

            String backdropUrl =
                    "https://image.tmdb.org/t/p/w1280"
                            + backdropPath;

            Glide.with(this)
                    .load(backdropUrl)
                    .centerCrop()
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .into(binding.imgDetailBackdrop);
        }

        // Genre
        displayGenres(movie.getGenres());
    }

    private void displayGenres(
            List<MovieDetailResponse.Genre> genres) {

        binding.layoutGenres.removeAllViews();

        if (genres == null || genres.isEmpty()) {
            return;
        }

        for (MovieDetailResponse.Genre genre : genres) {

            TextView genreText = new TextView(this);

            genreText.setText(genre.getName());
            genreText.setTextColor(Color.WHITE);
            genreText.setTextSize(14);
            genreText.setPadding(30, 12, 30, 12);

            GradientDrawable background =
                    new GradientDrawable();

            background.setColor(Color.rgb(57, 57, 57));
            background.setCornerRadius(100);

            genreText.setBackground(background);

            android.widget.LinearLayout.LayoutParams params =
                    new android.widget.LinearLayout.LayoutParams(
                            android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
                            android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
                    );

            params.setMargins(0, 0, 10, 0);

            genreText.setLayoutParams(params);

            binding.layoutGenres.addView(genreText);
        }
    }

    private void setupCastRecyclerView() {

        castAdapter = new CastAdapter();

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(
                        this,
                        LinearLayoutManager.HORIZONTAL,
                        false
                );

        binding.rvCast.setLayoutManager(layoutManager);
        binding.rvCast.setAdapter(castAdapter);
    }


    private void fetchMovieCredits() {

        creditsCall = RetrofitClient.getService()
                .getMovieCredits(movieId);

        creditsCall.enqueue(new Callback<MovieCreditsResponse>() {

            @Override
            public void onResponse(
                    Call<MovieCreditsResponse> call,
                    Response<MovieCreditsResponse> response) {

                if (response.isSuccessful()
                        && response.body() != null) {

                    MovieCreditsResponse credits = response.body();

                    if (credits.getCast() != null) {

                        castAdapter.setCast(credits.getCast());

                    }

                } else {

                    Log.e("MOVIE_CAST",
                            "Response error: " + response.code());

                }
            }

            @Override
            public void onFailure(
                    Call<MovieCreditsResponse> call,
                    Throwable t) {

                if (!call.isCanceled()) {
                    Log.e("MOVIE_CAST",
                            "Request failed: " + t.getMessage());
                }
            }
        });
    }


    private void fetchMovieTrailer() {

        videosCall = RetrofitClient.getService()
                .getMovieVideos(movieId, "en-US");

        videosCall.enqueue(new Callback<MovieVideosResponse>() {

            @Override
            public void onResponse(
                    Call<MovieVideosResponse> call,
                    Response<MovieVideosResponse> response) {

                if (response.isSuccessful()
                        && response.body() != null) {

                    List<MovieVideosResponse.Video> videos =
                            response.body().getResults();

                    MovieVideosResponse.Video selectedTrailer =
                            findTrailer(videos);

                    if (selectedTrailer != null) {

                        trailerKey = selectedTrailer.getKey();

                        displayTrailer(trailerKey);

                    } else {

                        showTrailerUnavailable();

                        Log.d("MOVIE_TRAILER",
                                "Trailer tidak tersedia");
                    }

                } else {

                    showTrailerUnavailable();

                    Log.e("MOVIE_TRAILER",
                            "Response error: " + response.code());
                }
            }

            @Override
            public void onFailure(
                    Call<MovieVideosResponse> call,
                    Throwable t) {

                if (!call.isCanceled()) {

                    showTrailerUnavailable();

                    Log.e("MOVIE_TRAILER",
                            "Request failed: " + t.getMessage());
                }
            }
        });
    }

    private MovieVideosResponse.Video findTrailer(
            List<MovieVideosResponse.Video> videos) {

        if (videos == null || videos.isEmpty()) {
            return null;
        }

        MovieVideosResponse.Video selected = null;

        // Prioritaskan trailer resmi dari YouTube
        for (MovieVideosResponse.Video video : videos) {

            if ("YouTube".equalsIgnoreCase(video.getSite())
                    && "Trailer".equalsIgnoreCase(video.getType())
                    && video.isOfficial()
                    && video.getKey() != null
                    && !video.getKey().isEmpty()) {

                return video;
            }
        }

        // Jika tidak ada trailer resmi, ambil trailer YouTube lain
        for (MovieVideosResponse.Video video : videos) {

            if ("YouTube".equalsIgnoreCase(video.getSite())
                    && "Trailer".equalsIgnoreCase(video.getType())
                    && video.getKey() != null
                    && !video.getKey().isEmpty()) {

                selected = video;
                break;
            }
        }

        return selected;
    }

    private void displayTrailer(String key) {

        String thumbnailUrl =
                "https://img.youtube.com/vi/"
                        + key
                        + "/hqdefault.jpg";

        Glide.with(this)
                .load(thumbnailUrl)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(binding.imgTrailer);

        binding.btnPlayTrailer.setEnabled(true);

        binding.btnPlayTrailer.setText("▶  Play Trailer");

        binding.btnPlayTrailer.setOnClickListener(v -> {
            openYoutubeTrailer(key);
        });
    }

    private void openYoutubeTrailer(String key) {

        String youtubeUrl =
                "https://www.youtube.com/watch?v=" + key;

        Intent intent = new Intent(
                Intent.ACTION_VIEW,
                Uri.parse(youtubeUrl)
        );

        startActivity(intent);
    }

    private void showTrailerUnavailable() {

        trailerKey = null;

        binding.imgTrailer.setImageResource(
                R.drawable.ic_launcher_background
        );

        binding.btnPlayTrailer.setText("Trailer Unavailable");

        binding.btnPlayTrailer.setEnabled(false);
    }



    @Override
    protected void onDestroy() {

        if (detailCall != null) {
            detailCall.cancel();
        }

        if (creditsCall != null) {
            creditsCall.cancel();
        }

        if (videosCall != null) {
            videosCall.cancel();
        }

        binding = null;

        super.onDestroy();
    }
}
