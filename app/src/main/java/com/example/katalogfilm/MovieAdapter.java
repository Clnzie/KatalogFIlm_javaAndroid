package com.example.katalogfilm;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Locale;

public class MovieAdapter
        extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private final List<Movie> movies;

    public MovieAdapter(List<Movie> movies) {
        this.movies = movies;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.item_movie, parent, false);

        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull MovieViewHolder holder, int position
    ) {
        Movie movie = movies.get(position);

        holder.txtTitle.setText(movie.getTitle());
        holder.txtRating.setText(String.format(Locale.US, "★ %.1f / 10", movie.getRating()));

        String posterPath = movie.getPosterPath();

        if (posterPath != null && !posterPath.isEmpty()) {
            String posterUrl =
                    "https://image.tmdb.org/t/p/w500" + posterPath;

            Glide.with(holder.itemView.getContext())
                    .load(posterUrl)
                    .centerCrop()
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .into(holder.imgPoster);
        } else {
            Glide.with(holder.itemView.getContext())
                    .clear(holder.imgPoster);

            holder.imgPoster.setImageResource(
                    R.drawable.ic_launcher_background
            );
        }
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPoster;
        TextView txtTitle;
        TextView txtRating;

        MovieViewHolder(@NonNull View itemView) {
            super(itemView);

            imgPoster = itemView.findViewById(R.id.imgPoster);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtRating = itemView.findViewById(R.id.txtRating);
        }
    }
}
