package com.example.katalogfilm.ui.home;

import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.katalogfilm.R;
import com.example.katalogfilm.data.model.BannerMovie;

import org.w3c.dom.Text;

import java.util.List;

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.BannerViewHolder>{

    private final List<BannerMovie> bannerList;

    public BannerAdapter(List<BannerMovie> bannerList) {
        this.bannerList = bannerList;
    }

    @NonNull
    @Override
    public BannerViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_banner, parent, false);

        return new BannerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull BannerViewHolder holder, int position) {
        BannerMovie movie = bannerList.get(position);

        holder.imgBanner.setImageResource(movie.getImage());
        holder.tvTitle.setText(movie.getTitle());
        holder.tvGenre.setText(movie.getGenre());
    }

    @Override
    public int getItemCount() {
        return bannerList.size();
    }

    static class BannerViewHolder extends RecyclerView.ViewHolder {
        ImageView imgBanner;
        TextView tvTitle;
        TextView tvGenre;
        Button btnDetail;

        public BannerViewHolder(@NonNull View itemView) {
            super(itemView);

            imgBanner = itemView.findViewById(R.id.imgBanner);
            tvTitle = itemView.findViewById(R.id.tvBannerTitle);
            tvGenre = itemView.findViewById(R.id.tvBannerGenre);
            btnDetail = itemView.findViewById(R.id.btnBannerDetail);
        }
    }
}
