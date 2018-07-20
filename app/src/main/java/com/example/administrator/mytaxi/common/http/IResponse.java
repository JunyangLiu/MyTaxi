package com.example.administrator.mytaxi.common.http;

/**
 * Created by Administrator on 2018/4/13.
 */

public interface IResponse {
    // 状态码
    int getCode();
    // 数据体
    String getData();
}
