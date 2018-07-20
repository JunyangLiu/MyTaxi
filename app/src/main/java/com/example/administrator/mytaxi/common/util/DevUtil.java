package com.example.administrator.mytaxi.common.util;

import android.app.Activity;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by Administrator on 2018/4/17.
 */

public class DevUtil {
    /**
     * 获取 UID
     * @param context
     * @return
     */
    final public static int REQUEST_CODE_ASK_READ_PHONE_STATE = 123;
    public static String UUID(Context context) {

                TelephonyManager tm = (TelephonyManager)context
                        .getSystemService(Context.TELEPHONY_SERVICE);
                String deviceId = tm.getDeviceId();
                return deviceId + System.currentTimeMillis();


    }
    public static void closeInputMethod(Activity context) {
        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);

    }
}
