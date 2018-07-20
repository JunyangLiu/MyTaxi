package com.example.administrator.mytaxi;

import android.app.Application;

/**
 * Created by Administrator on 2018/4/13.
 */

public class MyApplication extends Application {
    private static MyApplication instance;
    @Override
    public void onCreate() {
        super.onCreate();
        instance  = this;
    }
    public static MyApplication getInstance() {
        return  instance;
    }
}
