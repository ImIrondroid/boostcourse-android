package com.boostcourse.iron.ui.adapter;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.util.LruCache;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.boostcourse.iron.R;
import com.boostcourse.iron.data.network.VolleyHelper;
import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BindingAdapters {

    /**
     * 서버에 'YYYY-MM-DD' 형식으로 날짜가 저장되어있어 시간 차이가 정확하지 않습니다.
     *
     * @param time 날짜 형식의 String
     * @return H일 or H시간 M분 or M분
     */
    @BindingAdapter("textTime")
    public static void setConvertTimeText(TextView view, String time) {

        @SuppressLint("SimpleDateFormat") final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String convertString = "";
        long dayDivider = 24 * 60 * 60 * 1000L;
        long hourDivider = 60 * 60 * 1000L;
        long minDivider = 60 * 1000L;

        try {
            Date d = format.parse(time);
            long milliseconds = d != null ? System.currentTimeMillis() - d.getTime() : 0;
            if (milliseconds > 0L) {
                long rest = milliseconds / dayDivider;
                if (rest > 365) convertString = "오래";
                else if (rest > 0) convertString = rest + "일";
                else {
                    milliseconds %= dayDivider;
                    rest = milliseconds / hourDivider;
                    if (rest > 0) convertString = rest + "시간 ";

                    milliseconds %= hourDivider;
                    rest = milliseconds / minDivider;
                    convertString += rest + "분";
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        view.setText(convertString.isEmpty() ? "오래" : convertString);
    }

    /**
     * 오늘 날짜와 영화 개봉 예정일의 차이를 이용하여 화면에 표시된 날짜를 구합니다.
     *
     * @param date 영화 개봉 예정일
     * @return 화면에 표시될 잔여 날짜 예) D-10 or 오늘 개봉 or D+20
     */

    @BindingAdapter("textDate")
    public static void setConvertDateText(TextView view, String date) {

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dDay = "";
        Date todayDate = new Date();
        long todayTime = todayDate.getTime();
        try {
            Date releaseDate = format.parse(date);
            long releaseTime = releaseDate.getTime();
            long diffTime = todayTime - releaseTime;
            long divider = 24 * 60 * 60 * 1000;
            long realTime = diffTime / divider;
            if (realTime > 0) { //오늘 이전에 개봉한 영화인 경우
                dDay = "D-" + (diffTime / divider);
            } else if (realTime < 0) { //오늘 이후로 개봉할 영화인 경우
                dDay = "D+" + Math.abs(diffTime / divider);
            } else { //오늘 개봉한 영화인 경우
                dDay = "오늘 개봉";
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        view.setText(dDay);
    }

    @BindingAdapter("imageUrl")
    public static void setMovieImage(ImageView view, String imageUrl) {
        Glide.with(view.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.image_not_available)
                .centerCrop()
                .into(view);
    }

    @BindingAdapter("imageUrl")
    public static void setMovieImage(NetworkImageView view, String imageUrl) {
        view.setImageUrl(imageUrl, new ImageLoader(VolleyHelper.getInstance(view.getContext().getApplicationContext()).getRequestQueue(), new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap>
                    cache = new LruCache<>(20);

            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url, bitmap);
            }
        }));
    }

    @BindingAdapter("imageId")
    public static void setGradeImage(ImageView view, int grade) {
        int gradeResId = -1;

        if (grade == 12) {
            gradeResId = R.drawable.ic_12;
        } else if (grade == 15) {
            gradeResId = R.drawable.ic_15;
        } else if (grade == 19) {
            gradeResId = R.drawable.ic_19;
        }

        if (gradeResId != -1) {
            Glide.with(view.getContext()).
                    load(gradeResId)
                    .into(view);
        }
    }

    @BindingAdapter("galleryId")
    public static void setBadgeImage(ImageView view, int galleryId) {
        if (galleryId == 1) {
            view.setVisibility(View.INVISIBLE);
        } else if (galleryId == 2) {
            view.setVisibility(View.VISIBLE);
        }
    }

    @BindingAdapter("itemDecoration")
    public static void binding(RecyclerView view, int fullDp) {
        if (view.getItemDecorationCount() == 0) {
            view.addItemDecoration(new DefaultItemDecoration(view.getContext(), fullDp));
        }
    }
}
