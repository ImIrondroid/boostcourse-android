package com.boostcourse.iron.callback;

import com.boostcourse.iron.list.model.Movie;

public interface FragmentCallback {

    void onClickedViewsDetailButton(Movie movie); //Fragment에서 상세 보기 버튼 클릭시 발동하는 콜백 메서드
}
