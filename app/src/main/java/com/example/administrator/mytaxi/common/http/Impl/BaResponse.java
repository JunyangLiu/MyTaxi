package com.example.administrator.mytaxi.common.http.Impl;

import com.example.administrator.mytaxi.common.http.IResponse;

/**
 * Created by Administrator on 2018/4/13.
 */

public class BaResponse implements IResponse {
    public static final int STATE_UNKNOWN_ERROR = 100001;
    public static final int STATE_OK = 200;
    // 状态码
    private int code;
    // 响应数据
    private String data;

    @Override
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String getData() {

        return data;
    }
}
