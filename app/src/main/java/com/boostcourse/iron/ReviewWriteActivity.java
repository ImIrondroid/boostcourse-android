package com.boostcourse.iron;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class ReviewWriteActivity extends AppCompatActivity {

    static final int REQUEST_CODE_REVIEW_WRITE = 101;

    private RatingBar reviewGradeRatingBar;
    private EditText reviewTextEditText;
    private Button saveButton;
    private Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_write);

        viewInit();
        viewEvent();
    }

    private void viewInit() {
        reviewGradeRatingBar = (RatingBar) findViewById(R.id.review_grade);
        reviewTextEditText = (EditText) findViewById(R.id.review_text);
        saveButton = (Button) findViewById(R.id.review_save);
        cancelButton = (Button) findViewById(R.id.review_cancel);
    }

    private void viewEvent() {
        saveButton.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.putExtra("rating", reviewGradeRatingBar.getRating());
            intent.putExtra("text", reviewTextEditText.getText().toString());
            setResult(REQUEST_CODE_REVIEW_WRITE, intent);
            finish();
        });

        cancelButton.setOnClickListener(view -> {
            finish();
        });
    }
}