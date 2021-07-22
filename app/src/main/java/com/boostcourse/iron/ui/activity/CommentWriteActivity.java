package com.boostcourse.iron.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import com.boostcourse.iron.R;
import com.boostcourse.iron.data.FinishListener;
import com.boostcourse.iron.data.Directory;
import com.boostcourse.iron.ui.MovieViewModel;
import com.boostcourse.iron.ui.base.BaseActivity;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CommentWriteActivity extends BaseActivity<MovieViewModel> {

    private int movieId;

    private RatingBar rbCommentGrade;
    private EditText etCommentText;
    private Button btnCommentSave;
    private Button btnCommentCancel;

    @Override
    protected boolean isBackButtonVisible() {
        return true;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_comment_write;
    }

    @Override
    protected int getTitleRes() {
        return R.string.app_bar_comment_write_name;
    }

    @Override
    protected Class<MovieViewModel> getViewModelClazz() {
        return MovieViewModel.class;
    }

    @Override
    public void init() {
        super.init();

        rbCommentGrade = (RatingBar) findViewById(R.id.rb_review_grade);
        etCommentText = (EditText) findViewById(R.id.et_review_text);
        btnCommentSave = (Button) findViewById(R.id.btn_review_save);
        btnCommentCancel = (Button) findViewById(R.id.btn_review_cancel);

        Intent intent = getIntent();
        if (intent != null) {
            movieId = intent.getIntExtra("movieId", 0);
        }

        btnCommentSave.setOnClickListener(view -> {
            createComment();
        });

        btnCommentCancel.setOnClickListener(view -> {
            finish();
        });
    }

    /**
     * 임의의 ID로 설정하여 선택된 영화에 한줄평을 작성합니다.
     */
    private void createComment() {
        Bundle bundle = new Bundle();
        bundle.putString("movieId", String.valueOf(movieId));
        bundle.putString("writer", getString(R.string.app_nickname));
        bundle.putString("rating", String.valueOf(rbCommentGrade.getRating()));
        bundle.putString("contents", etCommentText.getText().toString().isEmpty() ?
                getString(R.string.default_comment_message) : etCommentText.getText().toString()); //contents를 빈값으로 보내면 400 에러가 발생하여 기본값으로 설정하였습니다.

        viewModel.sendRequest(Directory.CREATE, bundle, new FinishListener() {
            @Override
            public void onFinish() {
                reloadCommentList();
            }

            @Override
            public void onError(Exception e) {
                Log.e("saveComment()", e.getMessage());
            }

            @Override
            public void onError() {
                Log.e("saveComment()", getString(R.string.please_connect_internet));
            }
        });
    }

    /**
     * 선택된 영화의 한줄평을 조회합니다.
     */
    private void reloadCommentList() {
        Bundle bundle = new Bundle();
        bundle.putString("movieId", String.valueOf(movieId));

        viewModel.sendRequest(Directory.COMMENTCREATE, bundle, new FinishListener() {
            @Override
            public void onFinish() {
                finish();
            }

            @Override
            public void onError(Exception e) {
                Log.e("reloadCommentList()", e.getMessage());
            }

            @Override
            public void onError() {
                Log.e("reloadCommentList()", getString(R.string.please_connect_internet));
            }
        });
    }
}