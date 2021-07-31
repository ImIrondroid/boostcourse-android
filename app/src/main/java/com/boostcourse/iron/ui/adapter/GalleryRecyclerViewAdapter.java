package com.boostcourse.iron.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.boostcourse.iron.R;
import com.boostcourse.iron.databinding.ItemGalleryBinding;
import com.boostcourse.iron.ui.base.BaseViewHolder;
import com.boostcourse.iron.ui.model.MovieGallery;

import java.util.ArrayList;

public class GalleryRecyclerViewAdapter extends RecyclerView.Adapter<GalleryRecyclerViewAdapter.MediaViewHolder> {

    private final LayoutInflater inflater;
    private GalleryCallback callback;

    public static int GALLERY_PHOTO_ID = 1;
    public static int GALLERY_VIDEO_ID = 2;

    private final ArrayList<MovieGallery> galleryList = new ArrayList<>();

    public interface GalleryCallback {

        void onClickedGalleyItem(int position);
    }

    public void setGalleryCallback(GalleryCallback callback) {
        this.callback = callback;
    }

    public GalleryRecyclerViewAdapter(Context context) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addPhoto(String photos) {
        if (photos == null) return;

        String[] photoUrl = photos.split(",");
        for (String url : photoUrl) {
            this.galleryList.add(new MovieGallery(GALLERY_PHOTO_ID, url));
        }

        notifyDataSetChanged();
    }

    public void addVideo(String videos) {
        if (videos == null) return;

        String[] videoUrl = videos.split(",");
        for (String url : videoUrl) {
            this.galleryList.add(new MovieGallery(GALLERY_VIDEO_ID, url));
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

    @Override
    public void onBindViewHolder(@NonNull MediaViewHolder holder, int position) {
        MovieGallery gallery = galleryList.get(position);
        holder.binding.setItem(gallery);

        holder.setGalleryItemClickListener(callback);
    }

    @Override
    public int getItemCount() {
        return galleryList.size();
    }

    static class MediaViewHolder extends BaseViewHolder<ItemGalleryBinding> {

        private GalleryCallback callback;

        public MediaViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        protected void init() {
            binding.getRoot().setOnClickListener(view -> {
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
