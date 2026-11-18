
package com.example.katalogfilm.ui.home;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.katalogfilm.R;
import com.example.katalogfilm.data.model.Movie;
import com.example.katalogfilm.databinding.ItemMovieBinding;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter
        extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private final List<Movie> movieList = new ArrayList<>();

    // Interface untuk menangani klik card
    public interface OnMovieClickListener {
        void onMovieClick(Movie movie);
    }

    private final OnMovieClickListener listener;

    // Constructor
    public MovieAdapter(OnMovieClickListener listener) {
        this.listener = listener;
    }

    public void setMovies(List<Movie> movies) {
        movieList.clear();

        if (movies != null) {
            movieList.addAll(movies);
        }

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {

        ItemMovieBinding binding = ItemMovieBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );

        return new MovieViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(
            @NonNull MovieViewHolder holder, int position) {

        Movie movie = movieList.get(position);

        holder.binding.tvMovieTitle.setText(movie.getTitle());

        String posterPath = movie.getPosterPath();

        if (posterPath != null && !posterPath.isEmpty()) {

            String posterUrl =
                    "https://image.tmdb.org/t/p/w500" + posterPath;

            Glide.with(holder.itemView.getContext())
                    .load(posterUrl)
                    .centerCrop()
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .into(holder.binding.imgMovie);

        } else {

            Glide.with(holder.itemView.getContext())
                    .clear(holder.binding.imgMovie);

            holder.binding.imgMovie.setImageResource(
                    R.drawable.ic_launcher_background
            );
        }

        // Klik seluruh area card
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                int currentPosition = holder.getBindingAdapterPosition();

                if (currentPosition != RecyclerView.NO_POSITION) {
                    listener.onMovieClick(
                            movieList.get(currentPosition)
                    );
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {

        final ItemMovieBinding binding;

        MovieViewHolder(ItemMovieBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
