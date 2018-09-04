package com.example.administrator.mytaxi.account.presenter;

import android.util.Log;

import com.example.administrator.mytaxi.account.model.AccountManagerImpl;
import com.example.administrator.mytaxi.account.model.IAccountManager;
import com.example.administrator.mytaxi.account.model.response.BaseResponse;
import com.example.administrator.mytaxi.account.model.response.MsgVerifyResponse;
import com.example.administrator.mytaxi.account.view.ISmsCodeDialogView;
import com.example.administrator.mytaxi.common.http.Impl.BaResponse;

/**
 * Created by Administrator on 2018/4/20.
 */

public class SmsCodeDialogPresenterImpl implements ISmsCodeDialogPresenter {
    private ISmsCodeDialogView view;
    private IAccountManager accountManager;


    public SmsCodeDialogPresenterImpl(ISmsCodeDialogView view) {
        this.view = view;
        accountManager= new AccountManagerImpl();


    }

    /**
     * 请求验证码
     * @param phone
     */
    @Override
    public void requestSendSmsCode(String phone) {
        accountManager.fetchSMSCode(phone, new AccountManagerImpl.RequestCallback() {
            @Override
            public void onResponse(Object object) {
                //根据response 的state code 判断是否发送成功
                BaseResponse response = (BaseResponse) object;
                if(response.getCode()== BaResponse.STATE_OK){
                    Log.d("jun","requestSendSmsCode onResponse code =  "+response.getCode());
                    view.showCountDownTimer();
                }else{

                    view.showError(IAccountManager.SMS_SEND_FAIL, "");
                }
            }

            @Override
            public void onError() {
                view.showError(IAccountManager.SMS_SEND_FAIL, "");
            }
        });
    }

    /**
     * 校验验证码
     * @param phone
     * @param smsCode
     */
    @Override
    public void requestCheckSmsCode(String phone, String smsCode) {
        accountManager.checkSmsCode(phone, smsCode, new AccountManagerImpl.RequestCallback() {
            @Override
            public void onResponse(Object object) {
                BaseResponse response = (BaseResponse) object;
                if(response.getCode()== BaResponse.STATE_OK){
                    Log.d("jun","校验验证码 response.getCode()== BaResponse.STATE_OK code="+response.getCode()+response.getMsg());
                    view.showSmsCodeCheckState(true);
                }else{
                    view.showError(IAccountManager.SMS_CHECK_FAIL, "");
                    Log.d("jun","校验验证码 response.getCode()!= BaResponse.STATE_OK code="+response.getCode()+response.getMsg());

                }
            }

            @Override
            public void onError() {
                view.showError(IAccountManager.SERVER_FAIL, "");
            }
        });
    }

    /**
     * 验证用户是否存在
     * @param phone
     */
    @Override
    public void requestCheckUserExist(String phone) {
        accountManager.checkUserExist(phone, new AccountManagerImpl.RequestCallback() {
            @Override
            public void onResponse(Object object) {
                BaseResponse data = (BaseResponse) object;
//                Log.d("jun", data.getData());
                Log.d("jun", "验证用户data.getData()" + data.getData());

                if (data.getCode() == MsgVerifyResponse.USER_EXIST) {
                    Log.d("jun","用户存在");
                    // 用户存在，跳转登录dialog
                    view.showUserExist(true);
                } else if (data.getCode() == MsgVerifyResponse.USER_NOT_EXIST) {
                    Log.d("jun","用户不存在");
                    // 用户不存在，跳转注册dialog
                    view.showUserExist(false);
                } else {
                    Log.d("jun","服务器繁忙");
                    // 服务器错误，toast提示
                    view.showError(IAccountManager.SERVER_FAIL, "");
                }
            }

            @Override
            public void onError() {
                // 服务器错误，toast提示
                view.showError(IAccountManager.SERVER_FAIL, "");
            }

        });
    }
}
