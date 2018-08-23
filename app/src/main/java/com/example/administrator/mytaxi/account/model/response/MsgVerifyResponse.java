package com.example.administrator.mytaxi.account.model.response;

/**
 * Created by Administrator on 2018/4/18.
 * 短信验证返回的response
 */

public class MsgVerifyResponse {
    public static final int USER_EXIST = 100003;
    public static final int USER_NOT_EXIST = 100002;
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
