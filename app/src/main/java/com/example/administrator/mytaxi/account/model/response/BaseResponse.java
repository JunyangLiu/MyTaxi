package com.example.administrator.mytaxi.account.model.response;

/**
 * Created by Jun on 2018/4/17
 * 最基本的response
 */

public class BaseResponse {
    int code;
    Account data;
    String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Account getData() {
        return data;
    }

    public void setData(Account data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
