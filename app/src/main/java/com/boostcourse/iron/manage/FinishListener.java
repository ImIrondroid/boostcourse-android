package com.boostcourse.iron.manage;

import com.boostcourse.iron.model.MovieResponse;

import java.util.List;

/**
 * Activity, Fragment와 같은 View들과 데이터를 처리하는 Manager 클래스의 기능을 나누기 위해 사용하였습니다.
 * onFinish() : 처리가 완료될 때, onNext() : 이후의 처리가 남아있을 때, onError() : 에러가 발생했을 때
 */
public interface FinishListener {

    default void onFinish(List<? extends MovieResponse> result) {}
    default void onFinish() {}
    default void onNext() {}
    default void onError(Exception e) {}
    default void onError() {}
}
