package com.example.administrator.mytaxi.account.model;

import android.os.Handler;

/**
 * Created by Jun on 2018/4/20
 * 账号相关业务逻辑抽象
 */

public interface IAccountManager {
    // 服务器错误
    int SERVER_FAIL = -999;
    // 验证码发送成功
    int SMS_SEND_SUC = 1;
    // 验证码发送失败
    int SMS_SEND_FAIL = -1;
    // 验证码校验成功
    int SMS_CHECK_SUC = 2;
    // 验证码错误
    int SMS_CHECK_FAIL = -2;
    // 用户已经存在
    int USER_EXIST = 3;
    // 用户不存在
    int USER_NOT_EXIST = -3;
    // 注册成功
    int REGISTER_SUC = 4;
    // 登录成功
    int LOGIN_SUC = 5;
    // 密码错误
    int PW_ERROR = -5;
    // 登录失效
    int TOKEN_INVALID = -6;

    /**
     * 设置handler
     * @param handler
     */
    void setHandler(Handler handler);

    /**
     * 下发验证码
     */
    void fetchSMSCode(String phone);

    /**
     * 校验验证码
     * @param phone
     * @param smsCode
     */
    void checkSmsCode(String phone,String smsCode);

    /**
     * 查询用户是否注册
     * @param phone
     */
    void checkUserExist(String phone);
    /**
     *  注册
     */
    void register(String phone, String password);

    /**
     *  登录
     */
    void login(String phone, String password, AccountManagerImpl.LoginCallback callback);

    /**
     * token 登录
     */
    void loginByToken();

}
