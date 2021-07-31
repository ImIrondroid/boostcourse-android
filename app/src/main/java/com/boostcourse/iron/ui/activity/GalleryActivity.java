package com.boostcourse.iron.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import com.boostcourse.iron.R;
import com.boostcourse.iron.databinding.ActivityGalleryBinding;
import com.boostcourse.iron.ui.adapter.GalleryRecyclerViewAdapter;
import com.boostcourse.iron.ui.base.BaseViewModel;
import com.boostcourse.iron.ui.model.MovieGallery;
import com.boostcourse.iron.ui.base.BaseActivity;
import com.bumptech.glide.Glide;

public class GalleryActivity extends BaseActivity<BaseViewModel, ActivityGalleryBinding> {

    private ScaleGestureDetector mScaleGestureDetector;
    private float mScaleFactor = 1.0f;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_gallery;
    }

    @Override
    protected int getTitleRes() {
        return R.string.app_bar_movie_gallery;
    }

    @Override
    protected boolean isBackButtonVisible() {
        return true;
    }

    @Override
    public void init() {
        super.init();

        Intent intent = getIntent();
        if (intent != null) {
            MovieGallery movieGallery = intent.getParcelableExtra("movieGallery");

            int id = movieGallery.getId();
            if (id == GalleryRecyclerViewAdapter.GALLERY_PHOTO_ID) {
                Glide.with(this).load(movieGallery.getPath()).into(binding.ivMovieGallery);
                mScaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());
            } else if (id == GalleryRecyclerViewAdapter.GALLERY_VIDEO_ID) {
                startVideo(movieGallery.getPath());
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mScaleGestureDetector.onTouchEvent(event);
        return true;
    }

    private void startVideo(String path) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(path));
        startActivity(intent);
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            mScaleFactor *= scaleGestureDetector.getScaleFactor();
            mScaleFactor = Math.max(1.0f, Math.min(mScaleFactor, 5.0f));
            binding.ivMovieGallery.setScaleX(mScaleFactor);
            binding.ivMovieGallery.setScaleY(mScaleFactor);
            return true;
        }
    }
}