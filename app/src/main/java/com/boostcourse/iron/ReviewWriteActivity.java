package com.boostcourse.iron;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

public class ReviewWriteActivity extends AppCompatActivity {

    static final int REQUEST_CODE_REVIEW_WRITE = 101;

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
        rbReviewGrade = (RatingBar) findViewById(R.id.rb_review_grade);
        etReviewText = (EditText) findViewById(R.id.et_review_text);
        btnReviewSave = (Button) findViewById(R.id.btn_review_save);
        btnReviewCancel = (Button) findViewById(R.id.btn_review_cancel);
    }

    private void viewEvent() {
        btnReviewSave.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.putExtra("review", new Review("testID", etReviewText.getText().toString(), rbReviewGrade.getRating(), R.drawable.user1, 2));
            setResult(REQUEST_CODE_REVIEW_WRITE, intent);
            finish();
        });

        btnReviewCancel.setOnClickListener(view -> {
            finish();
        });
    }
}