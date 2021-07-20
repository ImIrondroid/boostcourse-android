package com.boostcourse.iron.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;

public class NetworkUtil {

    /**
     * 버전별로 나누어 인터넷 연결 여부를 체크합니다.
     *
     * @param context SystemService에 접근하기 위한 파라미터
     * @return 인터넷 연결 여부
     */
    public static boolean isInternetConnected(Context context) {
        boolean isConnected = false;
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager != null) {
            NetworkCapabilities capabilities = manager.getNetworkCapabilities(manager.getActiveNetwork());
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    isConnected = true;
                }
            }
        }

        return isConnected;
    }
}
