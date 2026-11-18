package com.example.katalogfilm.ui.home.MovieDetail;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.katalogfilm.R;
import com.example.katalogfilm.data.response.MovieCreditsResponse;
import com.example.katalogfilm.databinding.ItemCastBinding;

import java.util.ArrayList;
import java.util.List;

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.CastViewHolder> {

    private final List<MovieCreditsResponse.CastMember> castList =
            new ArrayList<>();

    public void setCast(List<MovieCreditsResponse.CastMember> cast) {
        castList.clear();

        if (cast != null) {
            castList.addAll(cast);
        }

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CastViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {

        ItemCastBinding binding = ItemCastBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );

        return new CastViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(
            @NonNull CastViewHolder holder, int position) {

        MovieCreditsResponse.CastMember cast =
                castList.get(position);

        holder.binding.tvCastName.setText(cast.getName());

        holder.binding.tvCastCharacter.setText(
                cast.getCharacter()
        );

        String profilePath = cast.getProfilePath();

        if (profilePath != null && !profilePath.isEmpty()) {

            String imageUrl =
                    "https://image.tmdb.org/t/p/w185"
                            + profilePath;

            Glide.with(holder.itemView.getContext())
                    .load(imageUrl)
                    .centerCrop()
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .into(holder.binding.imgCast);

        } else {

            Glide.with(holder.itemView.getContext())
                    .clear(holder.binding.imgCast);

            holder.binding.imgCast.setImageResource(
                    R.drawable.ic_launcher_background
            );
        }
    }

    @Override
    public int getItemCount() {
        return castList.size();
    }

    static class CastViewHolder extends RecyclerView.ViewHolder {

        final ItemCastBinding binding;

        CastViewHolder(ItemCastBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
