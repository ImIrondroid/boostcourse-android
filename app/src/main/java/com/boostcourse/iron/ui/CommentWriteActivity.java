package com.boostcourse.iron.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import com.boostcourse.iron.R;
import com.boostcourse.iron.data.MovieComment;
import com.boostcourse.iron.data.MovieCommentResult;
import com.boostcourse.iron.data.MovieResponse;
import com.boostcourse.iron.network.Directory;
import com.boostcourse.iron.network.GsonRequest;
import com.boostcourse.iron.network.VolleyHelper;
import com.boostcourse.iron.util.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CommentWriteActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_COMMENT_WRITE = 101;

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
        String writer = "tester";
        float rating = rbCommentGrade.getRating();
        String contents = etCommentText.getText().toString().isEmpty() ? //contents를 빈값으로 보내면 400 에러가 발생하여 기본값으로 설정하였습니다.
                getString(R.string.default_comment_message) : etCommentText.getText().toString();

        Map<String, String> params = new HashMap<>();
        params.put("id", String.valueOf(movieId));
        params.put("writer", writer);
        params.put("rating", String.valueOf(rating));
        params.put("contents", contents);
        VolleyHelper.getInstance(this).addRequest(
                new GsonRequest<>(
                        VolleyHelper.getUrl(Directory.CREATE),
                        MovieResponse.class,
                        params,
                        response -> {
                            if (response.getCode() == VolleyHelper.RESPONSE_CODE) {
                                reloadCommentList();
                            } else {
                                ToastUtil.show(this, R.string.wrong_data_code);
                            }
                        },
                        error -> {
                            ToastUtil.show(this, R.string.error_occurred + error.getMessage());
                        }
                )
        );
    }

    /**
     * 선택된 영화에 대한 한줄평 조회합니다.
     */
    private void reloadCommentList() {
        Map<String, String> params = new HashMap<>();
        params.put("id", String.valueOf(movieId));
        params.put("limit", String.valueOf(20));
        VolleyHelper.getInstance(this).addRequest(
                new GsonRequest<>(
                        VolleyHelper.getUrl(Directory.COMMENT),
                        MovieCommentResult.class,
                        params,
                        response -> {
                            if (response.getCode() == VolleyHelper.RESPONSE_CODE) {
                                backToMovieDetailFragmentWithData(response.getResult());
                            } else {
                                ToastUtil.show(this, R.string.wrong_data_code);
                            }
                        },
                        error -> {
                            ToastUtil.show(this, R.string.error_occurred + error.getMessage());
                        }
                )
        );
    }

    /**
     * MovieDetailFragment에서 보여질 한줄평 리스트와의 동기화를 위해 현재 한줄평 리스트를 전달합니다.
     *
     * @param commentList 한줄평 리스트
     */
    private void backToMovieDetailFragmentWithData(ArrayList<MovieComment> commentList) {
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra("movieCommentList", commentList);
        setResult(REQUEST_CODE_COMMENT_WRITE, intent);
        finish();
    }
}