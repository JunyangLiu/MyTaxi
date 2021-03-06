package com.example.administrator.mytaxi.account.presenter;

/**
 * Created by Administrator on 2018/4/20.
 */

public interface ISmsCodeDialogPresenter {
    /**
     *  请求下发验证码
     */
    void requestSendSmsCode(String phone);
    /**
     * 请求校验验证码
     */
    void requestCheckSmsCode(String phone, String smsCode);
    /**
     * 用户是否存在
     */
    void requestCheckUserExist(String phone);
}
