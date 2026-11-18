
package com.example.katalogfilm.ui.seeall;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.katalogfilm.data.model.Movie;
import com.example.katalogfilm.databinding.ItemMovieGridBinding;

import java.util.ArrayList;
import java.util.List;

public class SeeAllAdapter extends RecyclerView.Adapter<SeeAllAdapter.MovieViewHolder> {

    private final List<Movie> movieList = new ArrayList<>();
    private final OnMovieClickListener listener;

    public interface OnMovieClickListener {
        void onMovieClick(Movie movie);
    }

    public SeeAllAdapter(OnMovieClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        ItemMovieGridBinding binding = ItemMovieGridBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );

        return new MovieViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(
            @NonNull MovieViewHolder holder,
            int position) {

        Movie movie = movieList.get(position);

        holder.binding.tvMovieTitle.setText(movie.getTitle());

        String posterUrl = "https://image.tmdb.org/t/p/w500"
                + movie.getPosterPath();

        Glide.with(holder.itemView.getContext())
                .load(movie.getPosterPath() != null
                        ? posterUrl : null)
//                .placeholder(R.drawable.placeholder_movie)
//                .error(R.drawable.placeholder_movie)
                .into(holder.binding.imgMovie);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onMovieClick(movie);
            }
        });
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public void addMovies(List<Movie> movies) {
        int startPosition = movieList.size();

        movieList.addAll(movies);

        notifyItemRangeInserted(startPosition, movies.size());
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {

        ItemMovieGridBinding binding;

        public MovieViewHolder(ItemMovieGridBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
