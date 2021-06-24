package com.boostcourse.iron.detail.see;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.boostcourse.iron.R;
import com.boostcourse.iron.detail.model.Review;
import com.boostcourse.iron.detail.ReviewAdapter;
import com.boostcourse.iron.detail.write.ReviewWriteActivity;

import java.util.ArrayList;

public class ReviewSeeActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_REVIEW_SEE = 102;

    private ReviewAdapter reviewAdapter;

    private ListView lvMovieReview;
    private TextView tvReviewWrite;

    //영상에 나오는 방식의 startActivityForResult()메서드가 Deprecated여서 새로운 API를 적용해 보았습니다.
    private final ActivityResultLauncher<Intent> startActivityResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == ReviewWriteActivity.REQUEST_CODE_REVIEW_WRITE) {
                        Intent intent = result.getData();
                        if(intent != null) {
                            Review review = intent.getParcelableExtra("review");
                            reviewAdapter.addReview(review);
                        }
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_see);

        viewInit();
        viewEvent();
    }

    private void viewInit() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_bar_review_see_name);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        setSupportActionBar(toolbar);

        lvMovieReview = (ListView) findViewById(R.id.lv_movie_review);
        tvReviewWrite = (TextView) findViewById(R.id.tv_review_write);

        reviewAdapter = new ReviewAdapter(this);
        Intent intent = getIntent();
        if(intent != null) { //MovieDetailFragment에서 전달한 리뷰 리스트를 가져옵니다.
            ArrayList<Review> items = intent.getParcelableArrayListExtra("reviewList");
            reviewAdapter.setReviewList(items);
        }
        lvMovieReview.setAdapter(reviewAdapter);
    }

    private void viewEvent() {
        tvReviewWrite.setOnClickListener(view -> {
            Intent intent = new Intent(this, ReviewWriteActivity.class);
            startActivityResult.launch(intent);
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home) { //액션바의 뒤로가기 버튼 클릭시 리뷰 리스트를 MovieDetailFragment의 리뷰 리스트와 동기화합니다.
            Intent intent = new Intent();
            intent.putParcelableArrayListExtra("reviewList", reviewAdapter.getReviewList());
            setResult(REQUEST_CODE_REVIEW_SEE, intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() { //기기의 뒤로가기 버튼 클릭시 리뷰 리스트를 MovieDetailFragment의 리뷰 리스트와 동기화합니다.
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra("reviewList", reviewAdapter.getReviewList());
        setResult(REQUEST_CODE_REVIEW_SEE, intent);
        super.onBackPressed();
    }
}