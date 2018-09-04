package com.example.administrator.mytaxi.account.model;

import android.util.Log;

import com.example.administrator.mytaxi.MyApplication;
import com.example.administrator.mytaxi.account.model.response.Account;
import com.example.administrator.mytaxi.account.model.response.BaseResponse;
import com.example.administrator.mytaxi.common.http.api.API;
import com.example.administrator.mytaxi.common.http.api.AccountApi;
import com.example.administrator.mytaxi.common.storage.SharedPreferencesDao;
import com.example.administrator.mytaxi.common.util.DevUtil;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2018/4/20.
 * model层只依赖数据相关的类
 */

public class AccountManagerImpl implements IAccountManager {
    private static AccountApi accountApi;
    private static final String TAG = "AccountManagerImpl";
    //数据储存
    private SharedPreferencesDao sharedPreferencesDao;
    private CompositeDisposable compositeDisposable;

    private static OkHttpClient okHttpClient = new OkHttpClient();
    private static Converter.Factory gsonConverterFactory = GsonConverterFactory.create();
    private static CallAdapter.Factory rxJavaCallAdapterFactory = RxJava2CallAdapterFactory.create();
    public interface RequestCallback{
        void onResponse(Object object);
        void onError();
    }
    private RequestCallback mRequestCallback;

    public static AccountApi getAccountApi() {
        if (accountApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(API.Config.getDomain())
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            accountApi = retrofit.create(AccountApi.class);
        }
        return accountApi;
    }
    public AccountManagerImpl() {
        this.sharedPreferencesDao = new SharedPreferencesDao(MyApplication.getInstance(),SharedPreferencesDao.FILE_ACCOUNT);
        compositeDisposable = new CompositeDisposable();
    }



    /**
     * 获取验证码
     * @param phone
     */
    @Override
    public void fetchSMSCode(final String phone,RequestCallback loginCallback) {

        mRequestCallback = loginCallback;
        compositeDisposable.add(

                getAccountApi().fetchSMSCode(phone)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<BaseResponse>() {
                            @Override
                            public void accept(BaseResponse baseResponse) throws Exception {
                                mRequestCallback.onResponse(baseResponse);
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Log.d("jun","fetchSMSCode throwable"+throwable);
                                mRequestCallback.onError();

                            }
                        })

        );
    }

    /**
     * 校验验证码
     * @param phone
     * @param smsCode
     */
    @Override
    public void checkSmsCode(final String phone, final String smsCode,RequestCallback loginCallback) {
//        //网络请求校验验证码
        mRequestCallback = loginCallback;
        compositeDisposable.add(

                getAccountApi().checkSmsCode(phone,smsCode)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<BaseResponse>() {
                            @Override
                            public void accept(BaseResponse baseResponse) throws Exception {
                                mRequestCallback.onResponse(baseResponse);
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Log.d("jun","验证错误 throwable"+throwable);
                                mRequestCallback.onError();

                            }
                        })

        );
    }

    @Override
    public void checkUserExist( String phone,RequestCallback loginCallback) {
        // 检查用户是否存在


        mRequestCallback = loginCallback;
        compositeDisposable.add(

                getAccountApi().checkUserExist(phone)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<BaseResponse>() {
                            @Override
                            public void accept(BaseResponse baseResponse) throws Exception {
                                mRequestCallback.onResponse(baseResponse);
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Log.d("jun","验证错误 throwable"+throwable);
                                mRequestCallback.onError();

                            }
                        })

        );

    }

    @Override
    public void register(final String phone, final String password, final AccountManagerImpl.RequestCallback requestCallback) {


        Log.d("jun","AccountManagerImpl register");
        mRequestCallback = requestCallback;
        String uid= DevUtil.UUID(MyApplication.getInstance());
        compositeDisposable.add(

                getAccountApi().register(phone,password,uid)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<BaseResponse>() {
                            @Override
                            public void accept(BaseResponse response) throws Exception {
                                Log.d("jun","AccountManagerImpl register accept");
                                mRequestCallback.onResponse(response);
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                Log.d("jun","AccountManagerImpl register accept throwable"+throwable.getMessage()+"  "+throwable.getCause()+throwable.getLocalizedMessage());
                                mRequestCallback.onError();

                            }
                        })

        );

    }



    @Override
    public void login(final String phone, final String password, RequestCallback loginCallback) {
        mRequestCallback = loginCallback;
        compositeDisposable.add(

                getAccountApi().login(phone,password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<BaseResponse>() {
                    @Override
                    public void accept(BaseResponse baseResponse) throws Exception {
                        mRequestCallback.onResponse(baseResponse);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        mRequestCallback.onError();

                    }
                })

        );
    }

    @Override
    public void loginByToken(RequestCallback loginCallback) {
        mRequestCallback = loginCallback;
        //用token 登录
        // 获取本地登录信息
        final Account account =
                (Account) sharedPreferencesDao.get(SharedPreferencesDao.KEY_ACCOUNT,
                        Account.class);
        // 登录是否过期
        boolean tokenValid = false;

        // 检查token是否过期

        if (account != null) {
            if (account.getExpired() > System.currentTimeMillis()) {
                // token 有效
                tokenValid = true;
            }
        }
        if (!tokenValid) {

        } else {

            compositeDisposable.add(

                    getAccountApi().loginByToken(account.getToken())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<BaseResponse>() {
                                @Override
                                public void accept(BaseResponse response) throws Exception {
                                    Log.d("jun","AccountManagerImpl register accept success");
                                    mRequestCallback.onResponse(response);
                                }
                            }, new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) throws Exception {
                                    Log.d("jun","AccountManagerImpl register accept throwable"+throwable.getMessage()+"  "+throwable.getCause()+throwable.getLocalizedMessage());
                                    mRequestCallback.onError();

                                }
                            })

            );

        }
    }
    }

