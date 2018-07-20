package com.example.administrator.mytaxi.account.presenter;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.administrator.mytaxi.MyApplication;
import com.example.administrator.mytaxi.account.model.AccountManagerImpl;
import com.example.administrator.mytaxi.account.model.IAccountManager;
import com.example.administrator.mytaxi.account.view.ISmsCodeDialogView;
import com.example.administrator.mytaxi.common.http.IHttpClient;
import com.example.administrator.mytaxi.common.http.Impl.OkHttpClientImpl;
import com.example.administrator.mytaxi.common.storage.SharedPreferencesDao;

import java.lang.ref.WeakReference;

/**
 * Created by Administrator on 2018/4/20.
 */

public class SmsCodeDialogPresenterImpl implements ISmsCodeDialogPresenter {
    private ISmsCodeDialogView view;
    private IAccountManager accountManager;

    /**
     * 接受消息并处理
     */
    private static class MyHandler extends Handler{
        WeakReference<SmsCodeDialogPresenterImpl> refContext;

        public MyHandler(SmsCodeDialogPresenterImpl context) {
            refContext = new WeakReference(context);
        }

        @Override
        public void handleMessage(Message msg) {
            SmsCodeDialogPresenterImpl presenter = refContext.get();
            switch (msg.what){
                case IAccountManager.SMS_SEND_SUC:
                    //显示发送成功,计时器倒计时.
                    Log.d("jun","发送成功");
                    presenter.view.showCountDownTimer();
                    break;
                case IAccountManager.SMS_SEND_FAIL:
                    //发送失败
                    Log.d("jun","发送失败");
                    presenter.view.showError(IAccountManager.SMS_SEND_FAIL, "");
                    break;
                case IAccountManager.SMS_CHECK_SUC:
                    //验证码校验成功,进一步验证用户是否存在
                    Log.d("jun","验证成功");
                    presenter.view.showSmsCodeCheckState(true);

                    break;
                case IAccountManager.SMS_CHECK_FAIL:
                    //验证码校验失败
                    Log.d("jun","验证失败");
                    presenter.view.showError(IAccountManager.SMS_CHECK_FAIL, "");
                    break;
                case IAccountManager.USER_EXIST:
                     Log.d("jun","用户存在");
                    // 用户存在，跳转登录dialog
                    presenter.view.showUserExist(true);
                    break;
                case IAccountManager.USER_NOT_EXIST:
                     Log.d("jun","用户不存在");
                    // 用户不存在，跳转注册dialog
                    presenter.view.showUserExist(false);
                    break;
                case IAccountManager.SERVER_FAIL:
                    Log.d("jun","服务器繁忙");
                    // 服务器错误，toast提示
                    presenter.view.showError(IAccountManager.SERVER_FAIL, "");
                    break;
            }
        }
    }

    public SmsCodeDialogPresenterImpl(ISmsCodeDialogView view) {
        this.view = view;
        IHttpClient httpClient = new OkHttpClientImpl();
        SharedPreferencesDao dao =
                new SharedPreferencesDao(MyApplication.getInstance(),
                        SharedPreferencesDao.FILE_ACCOUNT);
        accountManager= new AccountManagerImpl(httpClient, dao);
        accountManager.setHandler(new MyHandler(this));

    }

    /**
     * 请求验证码
     * @param phone
     */
    @Override
    public void requestSendSmsCode(String phone) {
        accountManager.fetchSMSCode(phone);
    }

    /**
     * 校验验证码
     * @param phone
     * @param smsCode
     */
    @Override
    public void requestCheckSmsCode(String phone, String smsCode) {
        accountManager.checkSmsCode(phone,smsCode);
    }

    /**
     * 验证用户是否存在
     * @param phone
     */
    @Override
    public void requestCheckUserExist(String phone) {
        accountManager.checkUserExist(phone);
    }
}
