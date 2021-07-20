package com.boostcourse.iron.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;

import com.boostcourse.iron.R;
import com.boostcourse.iron.ui.model.MovieGallery;
import com.boostcourse.iron.ui.base.BaseActivity;
import com.bumptech.glide.Glide;

public class GalleryActivity extends BaseActivity {

    private ScaleGestureDetector mScaleGestureDetector;
    private float mScaleFactor = 1.0f;

    private ImageView ivMovieGallery;

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

        ivMovieGallery = (ImageView) findViewById(R.id.iv_movie_gallery);

        Intent intent = getIntent();
        if (intent != null) {
            MovieGallery movieGallery = intent.getParcelableExtra("movieGallery");

            int id = movieGallery.getId();
            int photoId = 1;
            int videoId = 2;
            if (id == photoId) {
                Glide.with(this).load(movieGallery.getPath()).into(ivMovieGallery);
                mScaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());
            } else if (id == videoId) {
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
            ivMovieGallery.setScaleX(mScaleFactor);
            ivMovieGallery.setScaleY(mScaleFactor);
            return true;
        }
    }
}