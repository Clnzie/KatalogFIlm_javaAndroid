
package com.example.katalogfilm.ui.home;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.katalogfilm.R;
import com.example.katalogfilm.data.model.BannerMovie;
import com.example.katalogfilm.databinding.ItemBannerBinding;

import java.util.ArrayList;
import java.util.List;

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.BannerViewHolder> {

    private final List<BannerMovie> bannerList = new ArrayList<>();

    // Callback untuk button Detail
    public interface OnBannerClickListener {
        void onBannerDetailClick(BannerMovie banner);
    }

    private final OnBannerClickListener listener;

    // Constructor
    public BannerAdapter(OnBannerClickListener listener) {
        this.listener = listener;
    }

    public void setBanners(List<BannerMovie> banners) {
        bannerList.clear();

        if (banners != null) {
            bannerList.addAll(banners);
        }

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BannerViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {

        ItemBannerBinding binding = ItemBannerBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );

        return new BannerViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(
            @NonNull BannerViewHolder holder, int position) {

        BannerMovie banner = bannerList.get(position);

        holder.binding.tvBannerTitle.setText(banner.getTitle());
        holder.binding.tvBannerGenre.setText(banner.getGenre());

        String backdropPath = banner.getBackdropPath();

        if (backdropPath != null && !backdropPath.isEmpty()) {

            String imageUrl =
                    "https://image.tmdb.org/t/p/w1280" + backdropPath;

            Glide.with(holder.itemView.getContext())
                    .load(imageUrl)
                    .centerCrop()
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .into(holder.binding.imgBanner);

        } else {
            Glide.with(holder.itemView.getContext())
                    .clear(holder.binding.imgBanner);

            holder.binding.imgBanner.setImageResource(
                    R.drawable.ic_launcher_background
            );
        }

        // Klik button Detail
        holder.binding.btnDetail.setOnClickListener(v -> {

            int currentPosition = holder.getBindingAdapterPosition();

            if (currentPosition != RecyclerView.NO_POSITION
                    && listener != null) {

                listener.onBannerDetailClick(
                        bannerList.get(currentPosition)
                );
            }
        });
    }

    @Override
    public int getItemCount() {
        return bannerList.size();
    }

    static class BannerViewHolder extends RecyclerView.ViewHolder {

        final ItemBannerBinding binding;

        BannerViewHolder(ItemBannerBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
