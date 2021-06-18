package com.boostcourse.iron;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ReviewAdapter reviewAdapter;

    private int clickCount = 0;
    private boolean isLiked = false;
    private boolean isNotLiked = false;

    private ListView lvMovieReview;
    private TextView tvMovieThumbUpCount;
    private TextView tvMovieThumbDownCount;
    private TextView tvSeeReview;
    private TextView tvWriteReview;
    private Button btnMovieThumbUp;
    private Button btnMovieThumbDown;

    //영상에 나오는 방식의 startActivityForResult()메서드가 Deprecated여서 새로운 API를 적용해 보았습니다.
    private final ActivityResultLauncher<Intent> startActivityResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == ReviewWriteActivity.REQUEST_CODE_REVIEW_WRITE) {
                        Intent intent = result.getData();
                        if (intent != null) {
                            Review review = intent.getParcelableExtra("review");
                            reviewAdapter.addReview(review);
                            if (reviewAdapter.getCount() <= 3)
                                setListViewLimitedHeight(); //노출시킬 item의 개수가 3개 이하이면 뷰의 높이를 다시 측정하여 적용합니다.
                        }
                    } else if (result.getResultCode() == ReviewSeeActivity.REQUEST_CODE_REVIEW_SEE) {
                        Intent intent = result.getData();
                        if (intent != null) {
                            ArrayList<Review> items = intent.getParcelableArrayListExtra("items");
                            reviewAdapter.setReviewList(items);
                            if (reviewAdapter.getCount() <= 3)
                                setListViewLimitedHeight(); //노출시킬 item의 개수가 3개 이하이면 뷰의 높이를 다시 측정하여 적용합니다.
                        }
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewInit();
        viewEvent();
    }

    private void viewInit() {
        lvMovieReview = (ListView) findViewById(R.id.lv_movie_review);
        tvMovieThumbUpCount = (TextView) findViewById(R.id.tv_movie_thumbup_count);
        tvMovieThumbDownCount = (TextView) findViewById(R.id.tv_movie_thumbdown_count);
        tvSeeReview = (TextView) findViewById(R.id.tv_review_see);
        tvWriteReview = (TextView) findViewById(R.id.tv_review_write);
        btnMovieThumbUp = (Button) findViewById(R.id.btn_movie_thumb_up);
        btnMovieThumbDown = (Button) findViewById(R.id.btn_movie_thumb_down);

        reviewAdapter = new ReviewAdapter(this);
        reviewAdapter.addReview(new Review("android34", "정말 재미있게 봐서 지인에게 추천하고 싶습니다.", (float) 5.0, R.drawable.user1, 1));
        reviewAdapter.addReview(new Review("github12", "제 기준에는 적당히 재밌습니다.", (float) 2.5, R.drawable.user1, 2));
//        reviewAdapter.addReview(new Review("native3333", "보통입니다. 오랜만에 잠 안오는 영화를 봤습니다.", (float) 3.8, R.drawable.user1, 4));
//        reviewAdapter.addReview(new Review("sleepyZZ", "영화보면서 졸려서 혼났습니다.", (float) 1.2, R.drawable.user1, 10));
//        reviewAdapter.addReview(new Review("machine54", "그저 그런 내용의 영화입니다.", (float) 3.0, R.drawable.user1, 0));
//        reviewAdapter.addReview(new Review("movie12", "영화는 항상 재미있습니다.", (float) 4.0, R.drawable.user1, 1));
        lvMovieReview.setAdapter(reviewAdapter);
        setListViewLimitedHeight();
    }

    private void viewEvent() {
        btnMovieThumbUp.setOnClickListener(view -> {
            like();
        });

        btnMovieThumbDown.setOnClickListener(view -> {
            notLike();
        });

        tvSeeReview.setOnClickListener(view -> { //모두 보기 화면으로 전환합니다.
            Intent intent = new Intent(this, ReviewSeeActivity.class);
            intent.putParcelableArrayListExtra("items", reviewAdapter.getReviewList());
            startActivityResult.launch(intent);
        });

        tvWriteReview.setOnClickListener(view -> { //작성하기 화면으로 전환합니다.
            Intent intent = new Intent(this, ReviewWriteActivity.class);
            startActivityResult.launch(intent);
        });
    }

    public void setListViewLimitedHeight() { //ScrollView 내에서 ListView 사용시 1개의 아이템만 보이는 이슈를 해결하기 위한 로직이며 제한된 갯수의 아이템만 노출시키도록 설정하였습니다.
        BaseAdapter listAdapter = (BaseAdapter) lvMovieReview.getAdapter(); //어댑터에서 item 참조를 위해서는 리스트뷰에 어댑터가 설정이 되어 있어야 합니다.
        if (listAdapter == null) return;

        int totalHeight = 0; //아이템의 총 높이
        int maxItemCount = 3; //노출되는 아이템의 최대 개수
        int itemCount = Math.min(listAdapter.getCount(), maxItemCount); //MainActivity에서는 최대 아이템 3개만 노출시키고, 모두 보기로 전환하여 모든 아이템이 보이도록 하는 목적입니다.
        if (itemCount > 0) { //measure 메서드는 작업은 비용이 큰 작업이고 각각의 item의 높이는 일정하기 때문에 1번(성능 개선 목적)만 작업시켜 줍니다.
            View item = listAdapter.getView(0, null, lvMovieReview);
            item.measure(0, 0); //measure 메서드 내부에서는 onMeasure 메서드를 호출함으로써 뷰의 크기를 알아냅니다.
            totalHeight = item.getMeasuredHeight() * itemCount + (lvMovieReview.getDividerHeight() * (itemCount - 1)); //measure 메서드를 통해 각 뷰들은 자신의 getMeasuredWidth()와 getMeasuredHeight()로 리턴할 값들을 세팅해줍니다.
            ViewGroup.LayoutParams params = lvMovieReview.getLayoutParams();
            params.height = totalHeight;
            lvMovieReview.setLayoutParams(params);
        }
    }

    private void like() { //좋아요 버튼 클릭시 이벤트
        if (isNotLiked) { //싫어요가 눌렸는지 먼저 체크해줍니다.
            btnMovieThumbDown.setBackgroundResource(R.drawable.ic_thumb_down);
            tvMovieThumbDownCount.setText(String.valueOf(clickCount - 1));
            clickCount = 0;
            isNotLiked = false;
        }

        if (isLiked) {
            clickCount--;
            btnMovieThumbUp.setBackgroundResource(R.drawable.ic_thumb_up);
        } else {
            clickCount++;
            btnMovieThumbUp.setBackgroundResource(R.drawable.ic_thumb_up_selected);
        }
        tvMovieThumbUpCount.setText(String.valueOf(clickCount));

        isLiked = !isLiked;
    }

    private void notLike() { //싫어요 버튼 클릭시 이벤트
        if (isLiked) { //좋아요가 눌렸는지 먼저 체크해줍니다.
            btnMovieThumbUp.setBackgroundResource(R.drawable.ic_thumb_up);
            tvMovieThumbUpCount.setText(String.valueOf(clickCount - 1));
            clickCount = 0;
            isLiked = false;
        }

        if (isNotLiked) {
            clickCount--;
            btnMovieThumbDown.setBackgroundResource(R.drawable.ic_thumb_down);
        } else {
            clickCount++;
            btnMovieThumbDown.setBackgroundResource(R.drawable.ic_thumb_down_selected);
        }
        tvMovieThumbDownCount.setText(String.valueOf(clickCount));

        isNotLiked = !isNotLiked;
    }
}