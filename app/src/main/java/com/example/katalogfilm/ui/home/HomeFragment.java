package com.example.katalogfilm.ui.home;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.katalogfilm.R;
import com.example.katalogfilm.data.model.BannerMovie;
import com.example.katalogfilm.data.model.Movie;
//import com.example.katalogfilm.data.model.remote.TmdbClient;
import com.example.katalogfilm.data.remote.RetrofitClient;
import com.example.katalogfilm.data.response.MovieResponse;
import com.example.katalogfilm.databinding.FragmentHomeBinding;
import com.example.katalogfilm.ui.home.MovieDetail.MovieDetail;
import com.example.katalogfilm.ui.seeall.SeeAllActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private MovieAdapter nowPlayingAdapter;

    private MovieAdapter popularAdapter;

    private MovieAdapter topRatedAdapter;
    private BannerAdapter bannerAdapter;

    private Call<MovieResponse> nowPlayingCall;
    private Call<MovieResponse> popularCall;

    private Call<MovieResponse> topRatedCall;




    public HomeFragment() {
        // kosong
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

        binding.tvSeeAllNowPlaying.setOnClickListener(v -> {
            openSeeAll("now_playing");
        });

        binding.tvSeeAllPopular.setOnClickListener(v -> {
            openSeeAll("popular");
        });

        binding.tvSeeAllTopRated.setOnClickListener(v -> {
            openSeeAll("top_rated");
        });

        setupBannerPopular();
        setupRecycleViewNowPlaying();
        setupRecycleViewPopular();
        setupRecycleViewTopRated();

        fetchPopularMoviesBanner();
        fetchNowPlayingMovies();
        fetchPopularMovies();
        fetchTopRatedMovies();
    }

    // Fucntion untuk membuka halaman see all
    private void openSeeAll(String category) {
        Intent intent = new Intent(requireContext(), SeeAllActivity.class);
        intent.putExtra("category", category);
        startActivity(intent);
    }

    // Setup buat banner (API Popular)
    private void setupBannerPopular() {

        // Siapkan adapter MovieAdapter milikmu.
        bannerAdapter = new BannerAdapter(banner -> {

            Intent intent = new Intent(
                    requireContext(),
                    MovieDetail.class
            );

            intent.putExtra("movie_id", banner.getId());

            startActivity(intent);

        });

        binding.viewPagerBanner.setAdapter(bannerAdapter);
    }

    // Fetch api baner (API Popular)
    private void fetchPopularMoviesBanner() {

        popularCall = RetrofitClient.getService()
                .getPopularMovies("id-ID", 1);

        popularCall.enqueue(new Callback<MovieResponse>() {

            @Override
            public void onResponse(
                    @NonNull Call<MovieResponse> call,
                    @NonNull Response<MovieResponse> response) {

                if (binding == null) return;

                if (response.isSuccessful()
                        && response.body() != null) {

                    List<Movie> movies =
                            response.body().getResults();

                    List<BannerMovie> banners =
                            new ArrayList<>();

                    if (movies != null && !movies.isEmpty()) {

                        for (Movie movie : movies) {

                            String backdropPath =
                                    movie.getBackdropPath();

                            if (backdropPath != null
                                    && !backdropPath.isEmpty()) {

                                banners.add(
                                        new BannerMovie(
                                                movie.getId(),
                                                movie.getTitle(),
                                                "Popular Movies",
                                                backdropPath
                                        )
                                );
                            }
                        }
                    }


                    // Kirim data ke BannerAdapter
                    bannerAdapter.setBanners(banners);

                    Log.d(
                            "TMDB_POPULAR",
                            "Jumlah banner: " + banners.size()
                    );

                } else {

                    Log.e(
                            "TMDB_POPULAR",
                            "Gagal mengambil data. Kode: "
                                    + response.code()
                    );
                }
            }

            @Override
            public void onFailure(
                    @NonNull Call<MovieResponse> call,
                    @NonNull Throwable t) {

                if (binding == null) return;

                if (!call.isCanceled()) {
                    Log.e(
                            "TMDB_POPULAR",
                            "Koneksi gagal: " + t.getMessage()
                    );
                }
            }
        });
    }


    // Setup buat card now playing (API Now Playing)
    private void setupRecycleViewNowPlaying() {
        nowPlayingAdapter = new MovieAdapter(movie -> {

            Intent intent = new Intent(
                    requireContext(),
                    MovieDetail.class
            );

            intent.putExtra("movie_id", movie.getId());

            startActivity(intent);

        });

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(
                        requireContext(),
                        LinearLayoutManager.HORIZONTAL,
                        false
                );

        binding.rvNowPlaying.setLayoutManager(layoutManager);

        binding.rvNowPlaying.setAdapter(nowPlayingAdapter);

    }

    // Fetch api card Now Playing (API Now Playing)
    private void fetchNowPlayingMovies() {

        nowPlayingCall = RetrofitClient.getService()
                .getNowPlayingMovies("id-ID", 1);

        nowPlayingCall.enqueue(new Callback<MovieResponse>() {

            @Override
            public void onResponse(
                    @NonNull Call<MovieResponse> call,
                    @NonNull Response<MovieResponse> response) {

                if (binding == null) return;

                if (response.isSuccessful()
                        && response.body() != null) {

                    List<Movie> movies =
                            response.body().getResults();

                    if (movies != null && !movies.isEmpty()) {

                        // Kirim data ke MovieAdapter
                        nowPlayingAdapter.setMovies(movies);

                        Log.d(
                                "TMDB_NOW_PLAYING",
                                "Jumlah film: " + movies.size()
                        );
                    }

                } else {

                    Log.e(
                            "TMDB_NOW_PLAYING",
                            "Gagal mengambil data. Kode: "
                                    + response.code()
                    );
                }
            }

            @Override
            public void onFailure(
                    @NonNull Call<MovieResponse> call,
                    @NonNull Throwable t) {

                if (binding == null) return;

                if (!call.isCanceled()) {
                    Log.e(
                            "TMDB_NOW_PLAYING",
                            "Koneksi gagal: " + t.getMessage()
                    );
                }
            }
        });
    }

    // Setup buat card Popular (API Popular)
    private void setupRecycleViewPopular() {
        popularAdapter = new MovieAdapter(movie -> {

            Intent intent = new Intent(
                    requireContext(),
                    MovieDetail.class
            );

            intent.putExtra("movie_id", movie.getId());

            startActivity(intent);

        });

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(
                        requireContext(),
                        LinearLayoutManager.HORIZONTAL,
                        false
                );

        binding.rvPopular.setLayoutManager(layoutManager);

        binding.rvPopular.setAdapter(popularAdapter);

    }

    // // Fetch api card Popular (API Popular)
    private void fetchPopularMovies() {

        popularCall = RetrofitClient.getService()
                .getPopularMovies("id-ID", 1);

        popularCall.enqueue(new Callback<MovieResponse>() {

            @Override
            public void onResponse(
                    @NonNull Call<MovieResponse> call,
                    @NonNull Response<MovieResponse> response) {

                if (binding == null) return;

                if (response.isSuccessful()
                        && response.body() != null) {

                    List<Movie> movies =
                            response.body().getResults();

                    if (movies != null && !movies.isEmpty()) {

                        // Kirim data ke MovieAdapter
                        popularAdapter.setMovies(movies);

                        Log.d(
                                "TMDB_POPULAR",
                                "Jumlah film: " + movies.size()
                        );
                    }

                } else {

                    Log.e(
                            "TMDB_POPULAR",
                            "Gagal mengambil data. Kode: "
                                    + response.code()
                    );
                }
            }

            @Override
            public void onFailure(
                    @NonNull Call<MovieResponse> call,
                    @NonNull Throwable t) {

                if (binding == null) return;

                if (!call.isCanceled()) {
                    Log.e(
                            "TMDB_POPULAR",
                            "Koneksi gagal: " + t.getMessage()
                    );
                }
            }
        });
    }

    // Setup buat card Top Rated (API Top Rated)
    private void setupRecycleViewTopRated() {
        topRatedAdapter = new MovieAdapter(movie -> {

            Intent intent = new Intent(
                    requireContext(),
                    MovieDetail.class
            );

            intent.putExtra("movie_id", movie.getId());

            startActivity(intent);

        });

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(
                        requireContext(),
                        LinearLayoutManager.HORIZONTAL,
                        false
                );

        binding.rvTopRated.setLayoutManager(layoutManager);

        binding.rvTopRated.setAdapter(topRatedAdapter);

    }

    // // Fetch api card Top Rated (API Top Rated)
    private void fetchTopRatedMovies() {

        topRatedCall = RetrofitClient.getService()
                .getTopRatedMovies("id-ID", 1);

        topRatedCall.enqueue(new Callback<MovieResponse>() {

            @Override
            public void onResponse(
                    @NonNull Call<MovieResponse> call,
                    @NonNull Response<MovieResponse> response) {

                if (binding == null) return;

                if (response.isSuccessful()
                        && response.body() != null) {

                    List<Movie> movies =
                            response.body().getResults();

                    if (movies != null && !movies.isEmpty()) {

                        // Kirim data ke MovieAdapter
                        topRatedAdapter.setMovies(movies);

                        Log.d(
                                "TMDB_TOP_RATED",
                                "Jumlah film: " + movies.size()
                        );
                    }

                } else {

                    Log.e(
                            "TMDB_TOP_RATED",
                            "Gagal mengambil data. Kode: "
                                    + response.code()
                    );
                }
            }

            @Override
            public void onFailure(
                    @NonNull Call<MovieResponse> call,
                    @NonNull Throwable t) {

                if (binding == null) return;

                if (!call.isCanceled()) {
                    Log.e(
                            "TMDB_POPULAR",
                            "Koneksi gagal: " + t.getMessage()
                    );
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        if (nowPlayingCall != null) {
            nowPlayingCall.cancel();
            nowPlayingCall = null;
        }

        if (popularCall != null) {
            popularCall.cancel();
            popularCall = null;
        }

        if (topRatedCall != null) {
            topRatedCall.cancel();
            topRatedCall = null;
        }

        super.onDestroyView();
        binding = null;
    }
}