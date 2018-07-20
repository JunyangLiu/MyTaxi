package com.example.administrator.mytaxi.common.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Administrator on 2018/4/13.
 */

public class ToastUtil {
    public static void show(Context context, String string) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
    }
}
