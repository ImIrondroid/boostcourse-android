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

    ListView reviewListView;
    TextView thumbUpCountTextView;
    TextView thumbDownCountTextView;
    TextView seeReviewTextView;
    TextView writeReviewTextView;
    Button thumbUpButton;
    Button thumbDownButton;

    boolean isLiked = false;
    boolean isNotLiked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewInit();
        viewEvent();
    }

    private void viewInit() {
        reviewListView = (ListView) findViewById(R.id.list_review);
        thumbUpCountTextView = (TextView) findViewById(R.id.movie_thumb_up_count);
        thumbDownCountTextView = (TextView) findViewById(R.id.movie_thumb_down_count);
        seeReviewTextView = (TextView) findViewById(R.id.see_review);
        writeReviewTextView = (TextView) findViewById(R.id.write_review);
        thumbUpButton = (Button) findViewById(R.id.movie_thumb_up);
        thumbDownButton = (Button) findViewById(R.id.movie_thumb_down);

        ReviewAdapter reviewAdapter = new ReviewAdapter(this);
        reviewAdapter.addReview(new Review("android34", "정말 재미있게 봐서 지인에게 추천하고 싶습니다.", (float) 5.0, R.drawable.ic_launcher_foreground, 1));
        reviewAdapter.addReview(new Review("github12", "제 기준에는 적당히 재밌습니다.", (float) 2.5, R.drawable.ic_launcher_foreground, 2));
        reviewAdapter.addReview(new Review("native3333", "보통입니다. 오랜만에 잠 안오는 영화를 봤습니다.", (float) 3.8, R.drawable.ic_launcher_foreground, 3));
        reviewListView.setAdapter(reviewAdapter);
        setListViewHeightBasedOnChildren();
    }

    private void viewEvent() {
        thumbUpButton.setOnClickListener(view -> {
            like();
        });
        thumbDownButton.setOnClickListener(view -> {
            notLike();
        });
        seeReviewTextView.setOnClickListener(view -> {
            Toast.makeText(this, R.string.movie_see_review, Toast.LENGTH_LONG).show();
        });
        writeReviewTextView.setOnClickListener(view -> {
            Toast.makeText(this, R.string.movie_write_review, Toast.LENGTH_LONG).show();
        });
    }

    //ScrollView 내에서 ListView 사용시 1개의 아이템만 보이는 이슈를 해결하기 위한 로직입니다.
    //이 방법 말고도 다른방법으로도 해결할 수 있는지 궁금합니다!
    public void setListViewHeightBasedOnChildren() {
        //어댑터에서 item 참조를 위해서는 리스트뷰에 어댑터가 설정되어있어야 합니다.
        BaseAdapter listAdapter = (BaseAdapter) reviewListView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0; //아이템의 총 높이
        int count = listAdapter.getCount();
        for (int i = 0; i < count; i++) {
            View item = listAdapter.getView(i, null, reviewListView);
            item.measure(0, 0); //measure 메서드 내부에서는 onMeasure 메서를 호출함으로써 뷰의 크기를 알아냅니다.
            totalHeight += item.getMeasuredHeight(); //measure 동안에 각 뷰들은 자신의 getMeasuredWidth()와 getMeasuredHeight()로 리턴할 값들을 세팅해줍니다.
        }

        //총 높이는 모든 아이템들의 높이에다가 (총 아이템의 갯수 - 1)개의 Divider 높이의 합으로 지정합니다.
        ViewGroup.LayoutParams params = reviewListView.getLayoutParams();
        params.height = totalHeight + (reviewListView.getDividerHeight() * (listAdapter.getCount() - 1));
        reviewListView.setLayoutParams(params);
    }

    private void like() { //좋아요 버튼 클릭시 이벤트
        int likeCount = Integer.parseInt(thumbUpCountTextView.getText().toString());
        int notLikeCount = Integer.parseInt(thumbDownCountTextView.getText().toString());

        if (isNotLiked) { //싫어요가 눌렸는지 먼저 체크해줍니다.
            thumbDownButton.setBackgroundResource(R.drawable.ic_thumb_down);
            thumbDownCountTextView.setText(String.valueOf(notLikeCount - 1));
            isNotLiked = false;
        }

        if (isLiked) {
            thumbUpButton.setBackgroundResource(R.drawable.ic_thumb_up);
            thumbUpCountTextView.setText(String.valueOf(likeCount - 1));
        } else {
            thumbUpButton.setBackgroundResource(R.drawable.ic_thumb_up_selected);
            thumbUpCountTextView.setText(String.valueOf(likeCount + 1));
        }

        isLiked = !isLiked;
    }

    private void notLike() { //싫어요 버튼 클릭시 이벤트
        int likeCount = Integer.parseInt(thumbUpCountTextView.getText().toString());
        int notLikeCount = Integer.parseInt(thumbDownCountTextView.getText().toString());

        if (isLiked) { //좋아요가 눌렸는지 먼저 체크해줍니다.
            thumbUpButton.setBackgroundResource(R.drawable.ic_thumb_up);
            thumbUpCountTextView.setText(String.valueOf(likeCount - 1));
            isLiked = false;
        }

        if (isNotLiked) {
            thumbDownButton.setBackgroundResource(R.drawable.ic_thumb_down);
            thumbDownCountTextView.setText(String.valueOf(notLikeCount - 1));
        } else {
            thumbDownButton.setBackgroundResource(R.drawable.ic_thumb_down_selected);
            thumbDownCountTextView.setText(String.valueOf(notLikeCount + 1));
        }

        isNotLiked = !isNotLiked;
    }
}