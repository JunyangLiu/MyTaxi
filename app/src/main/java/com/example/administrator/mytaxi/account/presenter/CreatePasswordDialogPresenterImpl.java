package com.example.administrator.mytaxi.account.presenter;

import android.os.Handler;
import android.os.Message;

import com.example.administrator.mytaxi.MyApplication;
import com.example.administrator.mytaxi.account.model.AccountManagerImpl;
import com.example.administrator.mytaxi.account.model.IAccountManager;
import com.example.administrator.mytaxi.account.view.ICreatePasswordDialogView;
import com.example.administrator.mytaxi.common.http.IHttpClient;
import com.example.administrator.mytaxi.common.http.Impl.OkHttpClientImpl;
import com.example.administrator.mytaxi.common.storage.SharedPreferencesDao;

import java.lang.ref.WeakReference;

/**
 * Created by Administrator on 2018/4/20.
 */

public class CreatePasswordDialogPresenterImpl implements ICreatePasswordDialogPresenter {
    private ICreatePasswordDialogView view;
    private IAccountManager accountManager;
    private static class MyHandler extends Handler{
        private WeakReference<CreatePasswordDialogPresenterImpl> reference;
        public MyHandler(CreatePasswordDialogPresenterImpl passwordDialogPresenter) {
            this.reference= new WeakReference(passwordDialogPresenter);
        }

        @Override
        public void handleMessage(Message msg) {
            CreatePasswordDialogPresenterImpl presenter=reference.get();
            switch (msg.what){
                case IAccountManager.REGISTER_SUC:
                    presenter.view.showRegisterSuc();
                    break;
                case IAccountManager.LOGIN_SUC:
                    presenter.view.showLoginSuc();
                    break;
                case IAccountManager.SERVER_FAIL:
                    presenter.view.showError(IAccountManager.SERVER_FAIL,"");
                    break;

            }
        }
    }

    public CreatePasswordDialogPresenterImpl(ICreatePasswordDialogView view) {
        this.view = view;
        IHttpClient client = new OkHttpClientImpl();
        SharedPreferencesDao dao = new SharedPreferencesDao(MyApplication.getInstance(),SharedPreferencesDao.FILE_ACCOUNT);
        accountManager = new AccountManagerImpl(client,dao);
        accountManager.setHandler(new MyHandler(this));

    }

    @Override
    public boolean checkPw(String pw, String pw1) {
        if (pw == null || pw.equals("")) {

            view.showPasswordNull();
            return false;
        }
        if (!pw.equals(pw1)) {

            view.showPasswordNotEqual();
            return false;
        }
        return true;
    }

    @Override
    public void requestRegister(String phone, String pw) {
        accountManager.register(phone,pw);
    }

    @Override
    public void requestLogin(String phone, String pw) {
        accountManager.login(phone,pw,null);
    }
}
