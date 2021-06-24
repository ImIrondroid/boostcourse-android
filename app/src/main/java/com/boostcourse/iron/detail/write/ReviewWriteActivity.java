package com.boostcourse.iron.detail.write;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import com.boostcourse.iron.R;
import com.boostcourse.iron.detail.model.Review;

public class ReviewWriteActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_REVIEW_WRITE = 101;

    private RatingBar rbReviewGrade;
    private EditText etReviewText;
    private Button btnReviewSave;
    private Button btnReviewCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_write);

        viewInit();
        viewEvent();
    }

    private void viewInit() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_bar_review_write_name);
        setSupportActionBar(toolbar);

        rbReviewGrade = (RatingBar) findViewById(R.id.rb_review_grade);
        etReviewText = (EditText) findViewById(R.id.et_review_text);
        btnReviewSave = (Button) findViewById(R.id.btn_review_save);
        btnReviewCancel = (Button) findViewById(R.id.btn_review_cancel);
    }

    private void viewEvent() {
        btnReviewSave.setOnClickListener(view -> { //한줄평 작성 후 저장하기 입니다.
            Intent intent = new Intent();
            intent.putExtra("review", new Review("testID", etReviewText.getText().toString(), rbReviewGrade.getRating(), R.drawable.user1, 2));
            setResult(REQUEST_CODE_REVIEW_WRITE, intent);
            finish();
        });

        btnReviewCancel.setOnClickListener(view -> { //한줄평 작성 중 취소하기 입니다.
            finish();
        });
    }
}