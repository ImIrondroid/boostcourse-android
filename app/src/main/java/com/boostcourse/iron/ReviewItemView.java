package com.boostcourse.iron;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

public class ReviewItemView extends ConstraintLayout {

    ImageView userImageView;
    TextView userIdTextView;
    TextView contentsTextView;
    TextView recommendCountTextView;
    TextView userDiffTimeTextView;
    RatingBar gradeRatingBar;

    public ReviewItemView(@NonNull Context context) {
        super(context);

        init(context);
    }

    public ReviewItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_review, this, true);

        userImageView = (ImageView) findViewById(R.id.user_image);
        userIdTextView = (TextView) findViewById(R.id.user_id);
        contentsTextView = (TextView) findViewById(R.id.user_contents);
        recommendCountTextView = (TextView) findViewById(R.id.user_recommend_count);
        userDiffTimeTextView = (TextView) findViewById(R.id.user_diff_time);
        gradeRatingBar = (RatingBar) findViewById(R.id.user_grade);
    }

    public void setUserImage(int resId) {
        userImageView.setImageResource(resId);
    }

    public void setUserId(String userId) {
        userIdTextView.setText(userId);
    }

    public void setContents(String contents) {
        contentsTextView.setText(contents);
    }

    public void setRecommendCount(int count) {
        recommendCountTextView.setText(String.valueOf(count));
    }

    public void setGrade(float grade) {
        gradeRatingBar.setRating(grade);
    }

    public void setDiffTime(Long time) { //지금은 0분전으로 고정되어 나오지만 이후에 현재 시간과 저장된 시간과의 차이를 분으로 반환할 예정입니다.
        userDiffTimeTextView.setText(String.valueOf((System.currentTimeMillis() - time) / 60000));
    }
}
