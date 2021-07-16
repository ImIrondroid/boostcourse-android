package com.boostcourse.iron.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;

import com.boostcourse.iron.R;
import com.boostcourse.iron.model.MovieGallery;
import com.bumptech.glide.Glide;

public class GalleryActivity extends AppCompatActivity {

    private ScaleGestureDetector mScaleGestureDetector;
    private float mScaleFactor = 1.0f;

    private ImageView ivMovieGallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        viewInit();
    }

    private void viewInit() {
        ivMovieGallery = (ImageView) findViewById(R.id.iv_movie_gallery);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_bar_movie_gallery);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        setSupportActionBar(toolbar);

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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
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