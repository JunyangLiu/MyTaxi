package com.example.administrator.mytaxi.common.http.api;

import com.example.administrator.mytaxi.account.model.response.LoginResponse;
import com.example.administrator.mytaxi.account.model.response.User;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AccountApi {

    /**
     *  登录
     */
    @POST(API.LOGIN)
    Observable<LoginResponse> login(@Body User user);
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

    void login(String phone, String password);

    /**
     * token 登录
     */
    void loginByToken();

}
