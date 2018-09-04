package com.example.administrator.mytaxi.main;

import android.util.Log;

import com.example.administrator.mytaxi.MyApplication;
import com.example.administrator.mytaxi.account.model.AccountManagerImpl;
import com.example.administrator.mytaxi.account.model.IAccountManager;
import com.example.administrator.mytaxi.account.model.response.Account;
import com.example.administrator.mytaxi.account.model.response.BaseResponse;
import com.example.administrator.mytaxi.common.http.Impl.BaResponse;
import com.example.administrator.mytaxi.common.storage.SharedPreferencesDao;

public class MainActivityPresenter implements IMainPresenter{
    private IMainActivity view;
    private IAccountManager accountManager;
    private SharedPreferencesDao dao;
    public MainActivityPresenter(IMainActivity view) {
        this.view = view;
        dao = new SharedPreferencesDao(MyApplication.getInstance(),
                        SharedPreferencesDao.FILE_ACCOUNT);
        accountManager = new AccountManagerImpl();
    }

    @Override
    public void loginByToken() {
        //获取本地登录信息
        final Account account =
                (Account) dao.get(SharedPreferencesDao.KEY_ACCOUNT, Account.class);

        //登录是否过期的flag
        boolean tokenValid = false;

        //检查token是否过期
        if (account != null) {
            if (account.getExpired() > System.currentTimeMillis()) {
                //token有效
                tokenValid = true;
                Log.d("jun", " token有效");
            }
        }
        if (!tokenValid) {
//            token过期跳转输入电话号码界面
            Log.d("jun", "  token过期跳转输入电话号码界面");
            view.onTokenInvalid();
        } else {
            accountManager.loginByToken(new AccountManagerImpl.RequestCallback() {
                @Override
                public void onResponse(Object object) {
                    BaseResponse response = (BaseResponse) object;
                    //请求成功
                    if (response.getCode() == BaResponse.STATE_OK) {
                        Log.d("jun", " 登陆成功");
                        // 保存登录信息
                        if(null != response.getData()){
                            Account account = response.getData();
                            // todo: 加密存储
                            SharedPreferencesDao dao =
                                    new SharedPreferencesDao(MyApplication.getInstance(),
                                            SharedPreferencesDao.FILE_ACCOUNT);
                            dao.save(SharedPreferencesDao.KEY_ACCOUNT, account);
                            view.loginSuccess();
                        }else{
                            Log.d("jun", "  token有效但response.getData() == null");
                            view.onTokenInvalid();
                        }
                    } else {
                        Log.d("jun", "  token有效但登录不成功+response.getCode() "+response.getCode() +" response.getMsg()=  "+response.getMsg()+" response.getData()=  "+response.getData());
                        view.onError();
                    }
                }

                @Override
                public void onError() {
                    Log.d("jun", "  retrofit请求失败");
                    view.onError();
                }
            });
        }
    }
}
