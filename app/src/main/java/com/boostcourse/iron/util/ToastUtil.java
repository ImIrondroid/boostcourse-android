package com.boostcourse.iron.util;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {

    public static void show(Context context, int id) {
        Toast.makeText(context, context.getString(id), Toast.LENGTH_SHORT).show();
    }

    public static void show(Context context, String str) {
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }
}
