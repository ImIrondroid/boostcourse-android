package com.boostcourse.iron;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

public class ReviewSeeActivity extends AppCompatActivity {

    private ListView reviewListView;
    private TextView reviewWriteTextView;

    //영상에 나오는 방식의 startActivityForResult가 deprecated라 나와서 새로운 API를 사용하였습니다.
    private final ActivityResultLauncher<Intent> startActivityResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == ReviewWriteActivity.REQUEST_CODE_REVIEW_WRITE) {
                        Intent intent = result.getData();
                        if(intent != null) {
                            float grade = intent.getFloatExtra("rating", 0.0f);
                            String text = intent.getStringExtra("text");
                            Log.e("grade : " + grade, ", text : " + text);
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
        reviewListView = (ListView) findViewById(R.id.review_list);
        reviewWriteTextView = (TextView) findViewById(R.id.review_write);

        ReviewAdapter reviewAdapter = new ReviewAdapter(this);
        reviewAdapter.addReview(new Review("android34", "정말 재미있게 봐서 지인에게 추천하고 싶습니다.", (float) 5.0, R.drawable.user1, 1));
        reviewAdapter.addReview(new Review("github12", "제 기준에는 적당히 재밌습니다.", (float) 2.5, R.drawable.user1, 2));
        reviewAdapter.addReview(new Review("native3333", "보통입니다. 오랜만에 잠 안오는 영화를 봤습니다.", (float) 3.8, R.drawable.user1, 4));
        reviewAdapter.addReview(new Review("sleepyZZ", "영화보면서 졸려서 혼났습니다.", (float) 1.2, R.drawable.user1, 10));
        reviewAdapter.addReview(new Review("machine54", "그저 그런 내용의 영화입니다.", (float) 3.0, R.drawable.user1, 0));
        reviewAdapter.addReview(new Review("movie12", "영화는 항상 재미있습니다.", (float) 4.0, R.drawable.user1, 1));
        reviewListView.setAdapter(reviewAdapter);
    }

    private void viewEvent() {
        reviewWriteTextView.setOnClickListener(view -> {
            Intent intent = new Intent(this, ReviewWriteActivity.class);
            startActivityResult.launch(intent);
        });
    }
}