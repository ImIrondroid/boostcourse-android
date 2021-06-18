package com.boostcourse.iron;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private ListView lvMovieReview;
    private TextView tvMovieThumupCount;
    private TextView tvMovieThumbdownCount;
    private TextView tvSeeReview;
    private TextView tvWriteReview;
    private Button btnMovieThumbup;
    private Button btnMovieThumbdown;

    private int likeCount = 0;
    private int notLikeCount = 0;
    private boolean isLiked = false;
    private boolean isNotLiked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewInit();
        viewEvent();
    }

    private void viewInit() {
        lvMovieReview = (ListView) findViewById(R.id.lv_movie_review);
        tvMovieThumupCount = (TextView) findViewById(R.id.tv_movie_thumbup_count);
        tvMovieThumbdownCount = (TextView) findViewById(R.id.tv_movie_thumbdown_count);
        tvSeeReview = (TextView) findViewById(R.id.tv_see_review);
        tvWriteReview = (TextView) findViewById(R.id.tv_write_review);
        btnMovieThumbup = (Button) findViewById(R.id.btn_movie_thumb_up);
        btnMovieThumbdown = (Button) findViewById(R.id.btn_movie_thumb_down);

        ReviewAdapter reviewAdapter = new ReviewAdapter(this);
        reviewAdapter.addReview(new Review("android34", "정말 재미있게 봐서 지인에게 추천하고 싶습니다.", (float) 5.0, R.drawable.user1, 1));
        reviewAdapter.addReview(new Review("github12", "제 기준에는 적당히 재밌습니다.", (float) 2.5, R.drawable.user1, 2));
        reviewAdapter.addReview(new Review("native3333", "보통입니다. 오랜만에 잠 안오는 영화를 봤습니다.", (float) 3.8, R.drawable.user1, 3));
        lvMovieReview.setAdapter(reviewAdapter);
        setListViewHeightBasedOnChildren();
    }

    private void viewEvent() {
        btnMovieThumbup.setOnClickListener(view -> {
            like();
        });

        btnMovieThumbdown.setOnClickListener(view -> {
            notLike();
        });

        tvSeeReview.setOnClickListener(view -> {
            Toast.makeText(this, R.string.movie_see_review, Toast.LENGTH_LONG).show();
        });

        tvWriteReview.setOnClickListener(view -> {
            Toast.makeText(this, R.string.movie_write_review, Toast.LENGTH_LONG).show();
        });
    }

    //ScrollView 내에서 ListView 사용시 1개의 아이템만 보이는 이슈를 해결하기 위한 로직입니다.
    public void setListViewHeightBasedOnChildren() {
        //어댑터에서 item 참조를 위해서는 리스트뷰에 어댑터가 설정되어있어야 합니다.
        BaseAdapter listAdapter = (BaseAdapter) lvMovieReview.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0; //아이템의 총 높이
        int count = listAdapter.getCount();
        for (int i = 0; i < count; i++) {
            View item = listAdapter.getView(i, null, lvMovieReview);
            item.measure(0, 0); //measure 메서드 내부에서는 onMeasure 메서를 호출함으로써 뷰의 크기를 알아냅니다.
            totalHeight += item.getMeasuredHeight(); //measure 동안에 각 뷰들은 자신의 getMeasuredWidth()와 getMeasuredHeight()로 리턴할 값들을 세팅해줍니다.
        }

        //총 높이는 모든 아이템들의 높이에다가 (총 아이템의 갯수 - 1)개의 Divider 높이의 합으로 지정합니다.
        ViewGroup.LayoutParams params = lvMovieReview.getLayoutParams();
        params.height = totalHeight + (lvMovieReview.getDividerHeight() * (listAdapter.getCount() - 1));
        lvMovieReview.setLayoutParams(params);
    }

    private void like() { //좋아요 버튼 클릭시 이벤트
        if (isNotLiked) { //싫어요가 눌렸는지 먼저 체크해줍니다.
            notLikeCount--;
            btnMovieThumbdown.setBackgroundResource(R.drawable.ic_thumb_down);
            tvMovieThumbdownCount.setText(String.valueOf(notLikeCount));
            isNotLiked = false;
        }

        if (isLiked) {
            likeCount--;
            btnMovieThumbup.setBackgroundResource(R.drawable.ic_thumb_up);
            tvMovieThumupCount.setText(String.valueOf(likeCount));
        } else {
            likeCount++;
            btnMovieThumbup.setBackgroundResource(R.drawable.ic_thumb_up_selected);
            tvMovieThumupCount.setText(String.valueOf(likeCount));
        }

        isLiked = !isLiked;
    }

    private void notLike() { //싫어요 버튼 클릭시 이벤트
        if (isLiked) { //좋아요가 눌렸는지 먼저 체크해줍니다.
            likeCount--;
            btnMovieThumbup.setBackgroundResource(R.drawable.ic_thumb_up);
            tvMovieThumupCount.setText(String.valueOf(likeCount));
            isLiked = false;
        }

        if (isNotLiked) {
            notLikeCount--;
            btnMovieThumbdown.setBackgroundResource(R.drawable.ic_thumb_down);
            tvMovieThumbdownCount.setText(String.valueOf(notLikeCount));
        } else {
            notLikeCount++;
            btnMovieThumbdown.setBackgroundResource(R.drawable.ic_thumb_down_selected);
            tvMovieThumbdownCount.setText(String.valueOf(notLikeCount));
        }

        isNotLiked = !isNotLiked;
    }
}