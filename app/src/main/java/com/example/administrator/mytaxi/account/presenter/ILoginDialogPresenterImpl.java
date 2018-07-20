package com.example.administrator.mytaxi.account.presenter;

import android.os.Handler;
import android.os.Message;

import com.example.administrator.mytaxi.MyApplication;
import com.example.administrator.mytaxi.account.model.AccountManagerImpl;
import com.example.administrator.mytaxi.account.model.IAccountManager;
import com.example.administrator.mytaxi.account.view.ILoginView;
import com.example.administrator.mytaxi.common.http.IHttpClient;
import com.example.administrator.mytaxi.common.http.Impl.OkHttpClientImpl;
import com.example.administrator.mytaxi.common.storage.SharedPreferencesDao;

import java.lang.ref.WeakReference;

/**
 * Created by Administrator on 2018/4/20.
 */

public class ILoginDialogPresenterImpl implements ILoginDialogPresenter {
    private ILoginView view;
    private IAccountManager accountManager;

    private static class MyHandler extends Handler{
        WeakReference<ILoginDialogPresenterImpl> reference;

        public MyHandler(ILoginDialogPresenterImpl reference) {
            this.reference = new WeakReference(reference);
        }

        @Override
        public void handleMessage(Message msg) {
            ILoginDialogPresenterImpl loginDialogPresenter = reference.get();
            switch (msg.what){
                case IAccountManager.LOGIN_SUC:
                    loginDialogPresenter.view.showLoginSuc();
                    break;
                case IAccountManager.PW_ERROR:
                    loginDialogPresenter.view.showError(IAccountManager.PW_ERROR,"");
                    break;
                case IAccountManager.SERVER_FAIL:
                    loginDialogPresenter.view.showError(IAccountManager.SERVER_FAIL,"");
                    break;
            }
        }
    }
    public ILoginDialogPresenterImpl(ILoginView view) {
        this.view = view;
        IHttpClient httpClient=new OkHttpClientImpl();
        SharedPreferencesDao dao=new SharedPreferencesDao(MyApplication.getInstance()
                ,SharedPreferencesDao.FILE_ACCOUNT);
        accountManager=new AccountManagerImpl(httpClient,dao);
        accountManager.setHandler(new MyHandler(this));
    }

    @Override
    public void requestLogin(String phone, String password) {
        accountManager.login(phone,password);
    }
}
