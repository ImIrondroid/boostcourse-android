package com.boostcourse.iron.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.boostcourse.iron.R;
import com.boostcourse.iron.ui.model.MovieGallery;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.MediaViewHolder> {

    private final LayoutInflater inflater;
    private final Context context;
    private GalleryCallback callback;

    private final ArrayList<MovieGallery> galleryList = new ArrayList<>();

    public interface GalleryCallback {

        void onClickedGalleyItem(int position);
    }

    public void setGalleryCallback(GalleryCallback callback) {
        this.callback = callback;
    }

    public GalleryAdapter(Context context) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
    }

    public void addPhoto(String photos) {
        if(photos == null) return;

        String[] photoUrl = photos.split(",");
        for (String url : photoUrl) {
            this.galleryList.add(new MovieGallery(1, url));
        }
        notifyDataSetChanged();
    }

    public void addVideo(String videos) {
        if(videos == null) return;

        String[] videoUrl = videos.split(",");
        for(String url : videoUrl) {
            this.galleryList.add(new MovieGallery(2, url));
        }
        notifyDataSetChanged();
    }

    public MovieGallery getItem(int position) {
        return galleryList.get(position);
    }

    @NonNull
    @Override
    public MediaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MediaViewHolder(inflater.inflate(R.layout.item_gallery, parent, false));
    }

    /**
     * @param holder   MediaViewHolder
     * @param position 아이템 인덱스
     */
    @Override
    public void onBindViewHolder(@NonNull MediaViewHolder holder, int position) {
        MovieGallery gallery = galleryList.get(position);
        Glide.with(context).load(gallery.getImagePath()).centerCrop().into(holder.ivGalleryImage);
        if(gallery.getId() == 1) {
            holder.ivGalleryPlayImage.setVisibility(View.INVISIBLE);
        } else {
            holder.ivGalleryPlayImage.setVisibility(View.VISIBLE);
        }

        holder.setGalleryItemClickListener(callback);
    }

    @Override
    public int getItemCount() {
        return galleryList.size();
    }

    static class MediaViewHolder extends RecyclerView.ViewHolder {

        private GalleryCallback callback;
        public ImageView ivGalleryImage;
        public ImageView ivGalleryPlayImage;

        public MediaViewHolder(@NonNull View itemView) {
            super(itemView);

            onBind(itemView);
        }

        private void onBind(View itemView) {
            ivGalleryImage = itemView.findViewById(R.id.iv_gallery_image);
            ivGalleryPlayImage = itemView.findViewById(R.id.iv_gallery_play_image);

            itemView.setOnClickListener(view -> {
                int position = getAbsoluteAdapterPosition();
                if (callback != null) {
                    callback.onClickedGalleyItem(position);
                }
            });
        }

        public void setGalleryItemClickListener(GalleryCallback callback) {
            this.callback = callback;
        }
    }
}
