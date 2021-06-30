package com.boostcourse.iron.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import com.boostcourse.iron.R;
import com.boostcourse.iron.data.MovieComment;
import com.boostcourse.iron.data.MovieDetail;
import com.boostcourse.iron.data.MovieResponse;
import com.boostcourse.iron.network.Directory;
import com.boostcourse.iron.network.GsonRequest;
import com.boostcourse.iron.network.VolleyHelper;
import com.boostcourse.iron.util.ToastUtil;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MovieDetailFragment extends Fragment implements CommentAdapter.CommentCallback {

    private CommentAdapter commentAdapter;
    private MovieDetail movieDetail;

    private ListView lvMovieComment;
    private ImageView ivMovieImage;
    private ImageView ivMovieAgeLimit;
    private TextView tvMovieDate;
    private TextView tvMovieTitle;
    private TextView tvMovieGenre;
    private TextView tvMovieDuration;
    private TextView tvMovieRank;
    private TextView tvMovieReservationRate;
    private TextView tvMovieAudience;
    private TextView tvMovieSynopsis;
    private TextView tvMovieLikeCount;
    private TextView tvMovieDislikeCount;
    private TextView tvSeeComment;
    private TextView tvWriteComment;
    private TextView tvMovieAudienceRating;
    private TextView tvMovieDirector;
    private TextView tvMovieActor;
    private RatingBar rbMovieUserRating;
    private Button btnMovieLike;
    private Button btnMovieDislike;

    /**
     * 영상에 나오는 방식의 startActivityForResult()메서드가 Deprecated여서 새로운 API를 적용해 보았습니다.
     */
    private final ActivityResultLauncher<Intent> startActivityResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == CommentWriteActivity.REQUEST_CODE_COMMENT_WRITE) { //from CommentWriteActivity
                        Intent intent = result.getData();
                        if (intent != null) {
                            ArrayList<MovieComment> arrayList = intent.getParcelableArrayListExtra("movieCommentList");
                            int size = commentAdapter.getCount(); //추가되기 전 아이템 개수입니다.
                            commentAdapter.setMovieCommentList(arrayList);
                            if (size < CommentAdapter.PREVIEW_ITEM_MAX_SIZE)
                                setListViewLimitedHeight(); //노출시킬 아이템의 개수가 ITEM_MAX_SIZE개 이하이면 뷰의 높이를 다시 측정하여 적용합니다.
                        }
                    } else if (result.getResultCode() == CommentSeeActivity.REQUEST_CODE_COMMENT_SEE) { //from CommentSeeActivity
                        Intent intent = result.getData();
                        if (intent != null) {
                            ArrayList<MovieComment> items = intent.getParcelableArrayListExtra("movieCommentList");
                            int size = commentAdapter.getCount(); //추가되기 전 아이템 개수입니다.
                            commentAdapter.setMovieCommentList(items);
                            if (size < CommentAdapter.PREVIEW_ITEM_MAX_SIZE)
                                setListViewLimitedHeight(); //노출시킬 아이템의 개수가 ITEM_MAX_SIZE개 이하이면 뷰의 높이를 다시 측정하여 적용합니다.
                        }
                    }
                }
            }
    );

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_movie_detail, container, false);

        viewInit(rootView);
        viewEvent();

        return rootView;
    }

    private void viewInit(ViewGroup rootView) {
        lvMovieComment = (ListView) rootView.findViewById(R.id.lv_movie_comment);
        ivMovieImage = (ImageView) rootView.findViewById(R.id.iv_movie_image);
        ivMovieAgeLimit = (ImageView) rootView.findViewById(R.id.iv_movie_age_limit);
        tvMovieDate = (TextView) rootView.findViewById(R.id.tv_movie_date);
        tvMovieTitle = (TextView) rootView.findViewById(R.id.tv_movie_title);
        tvMovieGenre = (TextView) rootView.findViewById(R.id.tv_movie_genre);
        tvMovieDuration = (TextView) rootView.findViewById(R.id.tv_movie_duration);
        tvMovieRank = (TextView) rootView.findViewById(R.id.tv_movie_rank);
        tvMovieReservationRate = (TextView) rootView.findViewById(R.id.tv_movie_reservation_rate);
        tvMovieAudience = (TextView) rootView.findViewById(R.id.tv_movie_audience);
        tvMovieSynopsis = (TextView) rootView.findViewById(R.id.tv_movie_synopsis);
        tvMovieLikeCount = (TextView) rootView.findViewById(R.id.tv_movie_like_count);
        tvMovieDislikeCount = (TextView) rootView.findViewById(R.id.tv_movie_dislike_count);
        tvSeeComment = (TextView) rootView.findViewById(R.id.tv_comment_see);
        tvWriteComment = (TextView) rootView.findViewById(R.id.tv_comment_write);
        tvMovieAudienceRating = (TextView) rootView.findViewById(R.id.tv_movie_audience_rating);
        tvMovieDirector = (TextView) rootView.findViewById(R.id.tv_movie_director);
        tvMovieActor = (TextView) rootView.findViewById(R.id.tv_movie_actor);
        rbMovieUserRating = (RatingBar) rootView.findViewById(R.id.rb_movie_user_rating);
        btnMovieLike = (Button) rootView.findViewById(R.id.btn_movie_like);
        btnMovieDislike = (Button) rootView.findViewById(R.id.btn_movie_dislike);

        Bundle bundle = getArguments();
        ArrayList<MovieComment> arrayList;
        commentAdapter = new CommentAdapter(requireActivity());
        if (bundle != null) { //MovieScreenFragment 클릭시 MainActivity에서 데이터를 전달합니다. (영화 상세 정보 및 한줄평 리스트)
            movieDetail = (MovieDetail) bundle.getParcelable("movieDetail");
            if (movieDetail != null) {
                tvMovieTitle.setText(movieDetail.getTitle());
                tvMovieDate.setText(movieDetail.getDate());
                tvMovieGenre.setText(movieDetail.getGenre());
                tvMovieDuration.setText(String.valueOf(movieDetail.getDuration()));
                tvMovieLikeCount.setText(String.valueOf(movieDetail.getLike()));
                tvMovieDislikeCount.setText(String.valueOf(movieDetail.getDislike()));
                tvMovieRank.setText(String.valueOf(movieDetail.getId()));
                tvMovieReservationRate.setText(String.valueOf(movieDetail.getReservation_rate()));
                rbMovieUserRating.setRating(movieDetail.getUser_rating());
                tvMovieAudienceRating.setText(String.valueOf(movieDetail.getAudience_rating()));
                tvMovieAudience.setText(movieDetail.getAudience());
                tvMovieSynopsis.setText(movieDetail.getSynopsis());
                tvMovieDirector.setText(movieDetail.getDirector());
                tvMovieActor.setText(movieDetail.getActor());

                int grade = movieDetail.getGrade();
                int gradeId = R.drawable.ic_all;
                if (grade == 12) {
                    gradeId = R.drawable.ic_12;
                } else if (grade == 15) {
                    gradeId = R.drawable.ic_15;
                } else if (grade == 19) {
                    gradeId = R.drawable.ic_19;
                }
                Glide.with(requireActivity()).load(gradeId).into(ivMovieAgeLimit);
                Glide.with(requireActivity()).load(movieDetail.getThumb()).placeholder(R.drawable.image_not_available).into(ivMovieImage);
            }

            arrayList = bundle.getParcelableArrayList("movieCommentList");
            if (arrayList != null) {
                commentAdapter.addAll(arrayList);
                commentAdapter.setRecommendCallbackListener(this);
                lvMovieComment.setAdapter(commentAdapter);
                setListViewLimitedHeight();
            }
        }
    }

    private void viewEvent() {
        btnMovieLike.setOnClickListener(view -> {
            updateLike();
        });

        btnMovieDislike.setOnClickListener(view -> {
            updateDislike();
        });

        tvSeeComment.setOnClickListener(view -> { //모두 보기 화면으로 리뷰 리스트와 함께 전환합니다.
            Intent intent = new Intent(requireActivity(), CommentSeeActivity.class);
            if (commentAdapter.getCount() != 0)
                intent.putParcelableArrayListExtra("movieCommentList", commentAdapter.getMovieCommentList());
            if (movieDetail != null)
                intent.putExtra("movieId", movieDetail.getId()); //한줄평 리스트 업데이트를 위해 영화 ID 전달하기
            startActivityResult.launch(intent);
        });

        tvWriteComment.setOnClickListener(view -> { //작성하기 화면으로 전환합니다.
            Intent intent = new Intent(requireActivity(), CommentWriteActivity.class);
            if (movieDetail != null)
                intent.putExtra("movieId", movieDetail.getId()); //한줄평 작성을 위해 영화 ID 전달하기
            startActivityResult.launch(intent);
        });
    }

    /**
     * ScrollView 내에서 ListView 사용시 1개의 아이템만 보이는 이슈를 해결하기 위한 로직이며 제한된 갯수의 아이템만 노출시키도록 설정하였습니다.
     */
    public void setListViewLimitedHeight() {
        BaseAdapter listAdapter = (BaseAdapter) lvMovieComment.getAdapter(); //어댑터에서 아이템 참조를 위해서는 리스트뷰에 어댑터가 설정이 되어 있어야 합니다.
        if (listAdapter == null) return;

        int totalHeight = 0; //아이템의 높이와 구분선의 총 높이
        int itemCount = Math.min(listAdapter.getCount(), CommentAdapter.PREVIEW_ITEM_MAX_SIZE); //MainActivity에서는 지정된 최대 아이템 개수만 노출시키고, 모두 보기로 전환하여 모든 아이템이 보이도록 하는 목적입니다.
        if (itemCount > 0) { //measure 메서드는 작업은 비용이 큰 작업이고 각각의 아이템의 높이는 일정하기 때문에 1번(성능 개선 목적)만 작업시켜 줍니다.
            View item = listAdapter.getView(0, null, lvMovieComment);
            item.measure(0, 0); //measure 메서드 내부에서는 onMeasure 메서드를 호출함으로써 뷰의 크기를 알아냅니다.
            totalHeight = item.getMeasuredHeight() * itemCount + (lvMovieComment.getDividerHeight() * (itemCount - 1)); //measure 메서드를 통해 각 뷰들은 자신의 getMeasuredWidth()와 getMeasuredHeight()로 리턴할 값들을 세팅해줍니다.
            ViewGroup.LayoutParams params = lvMovieComment.getLayoutParams();
            params.height = totalHeight;
            lvMovieComment.setLayoutParams(params);
        }
    }

    /**
     * 좋아요가 눌렀을 때 UI 및 데이터를 업데이트 한 후 서버에 저장합니다.
     */
    private void updateLike() {
        if (movieDetail.isLiked()) {
            btnMovieLike.setBackgroundResource(R.drawable.ic_thumb_up);
            movieDetail.setLike(movieDetail.getLike() - 1);
        } else {
            btnMovieLike.setBackgroundResource(R.drawable.ic_thumb_up_selected);
            movieDetail.setLike(movieDetail.getLike() + 1);
        }
        tvMovieLikeCount.setText(String.valueOf(movieDetail.getLike()));

        movieDetail.setLiked(!movieDetail.isLiked());

        if (movieDetail.isDisliked()) { //싫어요를 눌렀는지 먼저 체크해줍니다.
            btnMovieDislike.setBackgroundResource(R.drawable.ic_thumb_down);
            movieDetail.setDislike(movieDetail.getDislike() - 1);
            tvMovieDislikeCount.setText(String.valueOf(movieDetail.getDislike()));
            movieDetail.setDisliked(false);
        }

        applyLikeDislikeSync(0);
    }

    /**
     * 싫어요가 눌렀을 때 UI 및 데이터를 업데이트 한 후 서버에 저장합니다.
     */
    private void updateDislike() {
        if (movieDetail.isDisliked()) {
            btnMovieDislike.setBackgroundResource(R.drawable.ic_thumb_down);
            movieDetail.setDislike(movieDetail.getDislike() - 1);
        } else {
            btnMovieDislike.setBackgroundResource(R.drawable.ic_thumb_down_selected);
            movieDetail.setDislike(movieDetail.getDislike() + 1);
        }
        tvMovieDislikeCount.setText(String.valueOf(movieDetail.getDislike()));

        movieDetail.setDisliked(!movieDetail.isDisliked());

        if (movieDetail.isLiked()) { //좋아요를 눌렀는지 먼저 체크해줍니다.
            btnMovieLike.setBackgroundResource(R.drawable.ic_thumb_up);
            movieDetail.setLike(movieDetail.getLike() - 1);
            tvMovieLikeCount.setText(String.valueOf(movieDetail.getLike()));
            movieDetail.setLiked(false);
        }

        applyLikeDislikeSync(0);
    }

    /**
     * DFS 알고리즘을 적용하여 네트워크 통신을 동기적으로 처리하였습니다.
     *
     * DB를 이용한다면 한 번의 업데이트로 좋아요, 싫어요를 업데이트 할 수 있겠지만,
     * 하나의 URL에 likeyn과 dislikeyn을 모두 포함하여 처리할 수 없는 것을 확인하였습니다.
     * 둘 중 마지막 응답에 대한 처리를 고민해보다가 응답 처리가 가능하게 동기적으로 처리할 수 있게 접근해 보았습니다.
     *
     * --> Volley Library에서 비동기로 처리되는 스레드를 제어할 수 있는 방법이 있는지 궁금합니다. <--
     *
     * @param next 0: 좋아요 서버 저장, 1: 싫어요 서버 저장, 2: 종료
     */
    private void applyLikeDislikeSync(int next) {
        if (next == 2) return;

        Map<String, String> params = new HashMap<>();
        params.put("id", String.valueOf(movieDetail.getId()));
        if (next == 0) {
            params.put("likeyn", movieDetail.isLiked() ? "Y" : "N");
        } else if (next == 1) {
            params.put("dislikeyn", movieDetail.isDisliked() ? "Y" : "N");
        }

        VolleyHelper.getInstance(requireActivity()).addRequest(
                new GsonRequest<>(
                        VolleyHelper.getUrl(Directory.LIKE),
                        MovieResponse.class,
                        params,
                        response -> {
                            if (response.getCode() == VolleyHelper.RESPONSE_CODE) {
                                applyLikeDislikeSync(next + 1);
                            } else {
                                ToastUtil.show(requireActivity(), R.string.wrong_data_code);
                            }
                        },
                        error -> {
                            ToastUtil.show(requireActivity(), R.string.error_occurred + error.getMessage());
                        }
                )
        );
    }

    /**
     * 한줄평 추천문구 클릭시 발동하는 콜백 메소드 입니다.
     * 추천수를 서버에 저장한 후에 저장이 완료되면 정보를 다시 불러올 필요없이 UI를 업데이트 합니다.
     *
     * @param position 한줄평 리스트의 인덱스
     */
    @Override
    public void onClickedRecommendItem(int position) {
        Map<String, String> params = new HashMap<>();
        MovieComment movieComment = (MovieComment) commentAdapter.getItem(position);
        params.put("review_id", String.valueOf(movieComment.getId()));
        VolleyHelper.getInstance(requireActivity()).addRequest(
                new GsonRequest<>(
                        VolleyHelper.getUrl(Directory.RECOMMEND),
                        MovieResponse.class,
                        params,
                        response -> {
                            if (response.getCode() == VolleyHelper.RESPONSE_CODE) {
                                int updateRecommend = movieComment.getRecommend() + 1;
                                movieComment.setRecommend(updateRecommend);
                                commentAdapter.setMovieComment(position, movieComment);
                            } else {
                                ToastUtil.show(requireActivity(), R.string.wrong_data_code);
                            }
                        },
                        error -> {
                            ToastUtil.show(requireActivity(), R.string.error_occurred + error.getMessage());
                        }
                )
        );
    }
}
