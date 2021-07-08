package com.boostcourse.iron.ui;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.boostcourse.iron.R;
import com.boostcourse.iron.manage.FinishListener;
import com.boostcourse.iron.manage.MovieRepository;
import com.boostcourse.iron.model.MovieComment;
import com.boostcourse.iron.network.Directory;

import java.util.ArrayList;

public class CommentSeeActivity extends AppCompatActivity implements CommentAdapter.CommentCallback {

    public static final int REQUEST_CODE_COMMENT_SEE = 102;
    private int movieId;

    private MovieRepository repository;
    private CommentAdapter commentAdapter;

    private ListView lvMovieReview;
    private TextView tvReviewWrite;

    /**
     * startActivityForResult()의 새로운 API입니다.
     */
    private final ActivityResultLauncher<Intent> startActivityResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == CommentWriteActivity.REQUEST_CODE_COMMENT_WRITE) {
                        Intent intent = result.getData();
                        if (intent != null) {
                            ArrayList<MovieComment> commentList = intent.getParcelableArrayListExtra("commentList");
                            commentAdapter.setMovieCommentList(commentList);
                        }
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_see);

        viewInit();
        viewEvent();
    }

    private void viewInit() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_bar_comment_see_name);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        setSupportActionBar(toolbar);

        lvMovieReview = (ListView) findViewById(R.id.lv_movie_comment);
        tvReviewWrite = (TextView) findViewById(R.id.tv_comment_write);

        repository = new MovieRepository(this);
        commentAdapter = new CommentAdapter(this);
        Intent intent = getIntent();
        if (intent != null) { //MovieDetailFragment에서 전달한 리뷰 리스트를 가져옵니다.
            movieId = intent.getIntExtra("movieId", 0);
            ArrayList<MovieComment> commentList = intent.getParcelableArrayListExtra("commentList");
            commentAdapter.setMovieCommentList(commentList);
            commentAdapter.setRecommendCallbackListener(this);
        }
        lvMovieReview.setAdapter(commentAdapter);
    }

    private void viewEvent() {
        tvReviewWrite.setOnClickListener(view -> {
            Intent intent = new Intent(this, CommentWriteActivity.class);
            intent.putExtra("movieId", movieId);
            startActivityResult.launch(intent);
        });
    }

    /**
     * 액션바의 뒤로가기 버튼 클릭시 한줄평 리스트를 MovieDetailFragment의 한줄평 리스트와 동기화합니다.
     *
     * @param item android.R.id.home = 뒤로가기 버튼 ID
     *             return true
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent();
            intent.putParcelableArrayListExtra("commentList", commentAdapter.getMovieCommentList());
            setResult(REQUEST_CODE_COMMENT_SEE, intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 기기의 뒤로가기 버튼 클릭시 한줄평 리스트를 MovieDetailFragment의 한줄평 리스트와 동기화합니다.
     */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra("commentList", commentAdapter.getMovieCommentList());
        setResult(REQUEST_CODE_COMMENT_SEE, intent);
        super.onBackPressed();
    }

    /**
     * 한줄평 추천 문구 클릭시 발동하는 콜백 메소드 입니다.
     * 추천수를 서버에 저장한 후에 저장이 완료되면 정보를 다시 불러올 필요없이 UI를 업데이트 합니다.
     *
     * @param position 한줄평 리스트의 인덱스
     */
    @Override
    public void onClickedItemRecommend(int position) {
        MovieComment movieComment = (MovieComment) commentAdapter.getItem(position);
        Bundle bundle = new Bundle();
        bundle.putString("review_id", String.valueOf(movieComment.getId()));

        repository.loadMovieList(Directory.RECOMMEND, bundle, new FinishListener() {
            @Override
            public void onFinish() {
                int updateRecommend = movieComment.getRecommend() + 1;
                movieComment.setRecommend(updateRecommend);
                commentAdapter.setMovieComment(position, movieComment);
            }

            @Override
            public void onError(Exception e) {
                Log.e("onClickedItemRecommend", e.getMessage());
            }
        });
    }
}