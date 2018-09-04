package com.example.administrator.mytaxi.common.http.api;

import com.example.administrator.mytaxi.account.model.response.BaseResponse;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AccountApi {

    /**
     *  登录
     */
    @FormUrlEncoded
    @POST(API.LOGIN)
    Observable<BaseResponse> login(@Field("phone") String phone, @Field("password") String password);
    /**
     * 下发验证码
     */
    @GET(API.GET_SMS_CODE)
    Observable<BaseResponse>fetchSMSCode(@Query("phone") String phone);

    /**
     * 校验验证码
     * @param phone
     * @param smsCode
     */
    @GET(API.CHECK_SMS_CODE)
    Observable<BaseResponse> checkSmsCode(@Query("phone") String phone, @Query("code") String smsCode);

    /**
     * 查询用户是否注册
     * @param phone
     */
    @GET(API.CHECK_USER_EXIST)
    Observable<BaseResponse> checkUserExist(@Query("phone") String phone);
    /**
     *  注册
     */
    @FormUrlEncoded
    @POST(API.REGISTER)
    Observable<BaseResponse>  register(@Field("phone") String phone, @Field("password") String password, @Field("uid") String uid);



    /**
     * token 登录
     */
    @FormUrlEncoded
    @POST(API.LOGIN_BY_TOKEN)
    Observable<BaseResponse> loginByToken(@Field("token") String token);

}
