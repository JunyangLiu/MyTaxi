package com.example.administrator.mytaxi.account.model;

/**
 * Created by Jun on 2018/4/20
 * 账号相关业务逻辑抽象
 */

public interface IAccountManager {
    // 服务器错误
    int SERVER_FAIL = -999;

    // 验证码发送失败
    int SMS_SEND_FAIL = -1;

    // 验证码错误
    int SMS_CHECK_FAIL = -2;

    // 密码错误
    int PW_ERROR = -5;


    /**
     * 下发验证码
     */
    void fetchSMSCode(String phone,AccountManagerImpl.RequestCallback callback);

    /**
     * 校验验证码
     * @param phone
     * @param smsCode
     */
    void checkSmsCode(String phone,String smsCode,AccountManagerImpl.RequestCallback callback);

    /**
     * 查询用户是否注册
     * @param phone
     */
    void checkUserExist(String phone,AccountManagerImpl.RequestCallback callback);
    /**
     *  注册
     */
    void register(String phone, String password, AccountManagerImpl.RequestCallback callback);

    /**
     *  登录
     */
    void login(String phone, String password, AccountManagerImpl.RequestCallback callback);

    /**
     * token 登录
     */
    void loginByToken(AccountManagerImpl.RequestCallback callback);

}
