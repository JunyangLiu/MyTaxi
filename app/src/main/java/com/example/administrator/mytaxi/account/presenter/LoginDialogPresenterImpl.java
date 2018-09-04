package com.example.administrator.mytaxi.account.presenter;

import android.util.Log;

import com.example.administrator.mytaxi.MyApplication;
import com.example.administrator.mytaxi.account.model.AccountManagerImpl;
import com.example.administrator.mytaxi.account.model.IAccountManager;
import com.example.administrator.mytaxi.account.model.response.Account;
import com.example.administrator.mytaxi.account.model.response.BaseResponse;
import com.example.administrator.mytaxi.account.view.ILoginView;
import com.example.administrator.mytaxi.common.http.Impl.BaResponse;
import com.example.administrator.mytaxi.common.storage.SharedPreferencesDao;

/**
 * Created by Administrator on 2018/4/20.
 */

public class LoginDialogPresenterImpl implements ILoginDialogPresenter {
    private ILoginView view;
    private IAccountManager accountManager;


    public LoginDialogPresenterImpl(ILoginView view) {
        this.view = view;
        accountManager=new AccountManagerImpl();

    }

    @Override
    public void requestLogin(String phone, String password) {
        accountManager.login(phone,password,new AccountManagerImpl.RequestCallback(){
            @Override
            public void onResponse(Object object) {
                BaseResponse baseResponse = (BaseResponse) object;
                Log.d("jun","【重要】loginResponse看这里！！！！!!!!!!!"+ baseResponse.getMsg()+" "+ baseResponse.getCode()+" "+ baseResponse.getData().getAccount());
                if (baseResponse.getCode() == BaResponse.STATE_OK) {
                    // 保存登录信息
                    Account account = baseResponse.getData();
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
