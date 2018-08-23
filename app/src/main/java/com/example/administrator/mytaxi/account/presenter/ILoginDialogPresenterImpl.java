package com.example.administrator.mytaxi.account.presenter;

import android.util.Log;

import com.example.administrator.mytaxi.MyApplication;
import com.example.administrator.mytaxi.account.model.AccountManagerImpl;
import com.example.administrator.mytaxi.account.model.IAccountManager;
import com.example.administrator.mytaxi.account.model.response.Account;
import com.example.administrator.mytaxi.account.model.response.LoginResponse;
import com.example.administrator.mytaxi.account.view.ILoginView;
import com.example.administrator.mytaxi.common.http.IHttpClient;
import com.example.administrator.mytaxi.common.http.Impl.BaseResponse;
import com.example.administrator.mytaxi.common.http.Impl.OkHttpClientImpl;
import com.example.administrator.mytaxi.common.storage.SharedPreferencesDao;

/**
 * Created by Administrator on 2018/4/20.
 */

public class ILoginDialogPresenterImpl implements ILoginDialogPresenter {
    private ILoginView view;
    private IAccountManager accountManager;

//    private static class MyHandler extends Handler{
//        WeakReference<ILoginDialogPresenterImpl> reference;
//
//        public MyHandler(ILoginDialogPresenterImpl reference) {
//            this.reference = new WeakReference(reference);
//        }
//
//        @Override
//        public void handleMessage(Message msg) {
//            ILoginDialogPresenterImpl loginDialogPresenter = reference.get();
//            switch (msg.what){
//                case LOGIN_SUC:
//                    loginDialogPresenter.view.showLoginSuc();
//                    break;
//                case IAccountManager.PW_ERROR:
//                    loginDialogPresenter.view.showError(IAccountManager.PW_ERROR,"");
//                    break;
//                case SERVER_FAIL:
//                    loginDialogPresenter.view.showError(SERVER_FAIL,"");
//                    break;
//            }
//        }
//    }
    public ILoginDialogPresenterImpl(ILoginView view) {
        this.view = view;
        IHttpClient httpClient=new OkHttpClientImpl();
        SharedPreferencesDao dao=new SharedPreferencesDao(MyApplication.getInstance()
                ,SharedPreferencesDao.FILE_ACCOUNT);
        accountManager=new AccountManagerImpl(httpClient,dao);
//        accountManager.setHandler(new MyHandler(this));
    }

    @Override
    public void requestLogin(String phone, String password) {
        accountManager.login(phone,password,new AccountManagerImpl.LoginCallback(){
            @Override
            public void onResponse(LoginResponse loginResponse) {
                       Log.d("jun","【重要】loginResponse看这里！！！！!!!!!!!"+loginResponse.getMsg()+" "+loginResponse.getCode()+" "+loginResponse.getData());
                        if (loginResponse.getCode() == BaseResponse.STATE_OK) {


                            // 保存登录信息
                            Account account = loginResponse.getData();
                            // todo: 加密存储
                            SharedPreferencesDao dao =
                                    new SharedPreferencesDao(MyApplication.getInstance(),
                                            SharedPreferencesDao.FILE_ACCOUNT);
                            dao.save(SharedPreferencesDao.KEY_ACCOUNT, account);

                            // 通知 UI
                            view.showLoginSuc();


                        } else {
                            view.showError(IAccountManager.PW_ERROR,"");
                        }
            }

            @Override
            public void onError() {
                view.showError(IAccountManager.SERVER_FAIL,"");
            }
        });


    }
}
