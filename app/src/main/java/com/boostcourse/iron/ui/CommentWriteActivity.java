package com.boostcourse.iron.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import com.boostcourse.iron.R;
import com.boostcourse.iron.manage.FinishListener;
import com.boostcourse.iron.manage.MovieRepository;
import com.boostcourse.iron.model.MovieComment;
import com.boostcourse.iron.model.MovieResponse;
import com.boostcourse.iron.network.Directory;

import java.util.ArrayList;
import java.util.List;

public class CommentWriteActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_COMMENT_WRITE = 101;
    private MovieRepository repository;

    private int movieId;

    private RatingBar rbCommentGrade;
    private EditText etCommentText;
    private Button btnCommentSave;
    private Button btnCommentCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_write);

        viewInit();
        viewEvent();
    }

    private void viewInit() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_bar_comment_write_name);
        setSupportActionBar(toolbar);

        rbCommentGrade = (RatingBar) findViewById(R.id.rb_review_grade);
        etCommentText = (EditText) findViewById(R.id.et_review_text);
        btnCommentSave = (Button) findViewById(R.id.btn_review_save);
        btnCommentCancel = (Button) findViewById(R.id.btn_review_cancel);

        Intent intent = getIntent();
        if (intent != null) {
            movieId = intent.getIntExtra("movieId", 0);
        }

        repository = new MovieRepository(this);
    }

    private void viewEvent() {
        btnCommentSave.setOnClickListener(view -> { //한줄평 작성 후 저장하기 입니다.
            saveComment();
        });

        btnCommentCancel.setOnClickListener(view -> { //한줄평 작성 중 취소하기 입니다.
            finish();
        });
    }

    /**
     * 임의의 ID로 설정하여 선택된 영화에 한줄평을 작성합니다.
     */
    private void saveComment() {
        Bundle bundle = new Bundle();
        bundle.putString("movieId", String.valueOf(movieId));
        bundle.putString("writer", getString(R.string.app_nickname));
        bundle.putString("rating", String.valueOf(rbCommentGrade.getRating()));
        bundle.putString("contents", etCommentText.getText().toString().isEmpty() ?
                getString(R.string.default_comment_message) : etCommentText.getText().toString()); //contents를 빈값으로 보내면 400 에러가 발생하여 기본값으로 설정하였습니다.

        repository.loadMovieList(Directory.CREATE, bundle, new FinishListener() {
            @Override
            public void onFinish() {
                reloadCommentList();
            }

            @Override
            public void onError(Exception e) {
                Log.e("saveComment()", e.getMessage());
            }
        });
    }

    /**
     * 선택된 영화의 한줄평을 조회합니다.
     */
    private void reloadCommentList() {
        Bundle bundle = new Bundle();
        bundle.putString("movieId", String.valueOf(movieId));
        bundle.putString("limit", String.valueOf(20));

        repository.loadMovieList(Directory.COMMENTSEND, bundle, new FinishListener() {
            @Override
            public void onFinish(List<? extends MovieResponse> result) {
                onBackPressedWithData((List<MovieComment>) result);
            }

            @Override
            public void onError(Exception e) {
                Log.e("reloadCommentList()", e.getMessage());
            }
        });
    }

    /**
     * MovieDetailFragment에서 보여질 한줄평 리스트와의 동기화를 위해 현재 한줄평 리스트를 전달합니다.
     *
     * @param commentList 한줄평 리스트
     */
    private void onBackPressedWithData(List<MovieComment> commentList) {
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra("commentList", new ArrayList<>(commentList));
        setResult(REQUEST_CODE_COMMENT_WRITE, intent);
        finish();
    }
}