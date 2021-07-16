package com.boostcourse.iron.ui;

import android.os.Bundle;

import com.boostcourse.iron.manage.FinishListener;
import com.boostcourse.iron.network.Directory;

public interface FragmentCallback {

    void onClickedOnFragment(int movieId); //MovieFragment에서 상세 보기 버튼 클릭시 발동하는 콜백 메서드

    void sendRequestOnFragment(Directory type, Bundle bundle, FinishListener listener); //Fragment에서 요청되는 데이터 처리 메서드
}
