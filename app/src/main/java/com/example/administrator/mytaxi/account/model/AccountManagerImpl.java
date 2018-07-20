package com.example.administrator.mytaxi.account.model;

import android.os.Handler;
import android.util.Log;

import com.example.administrator.mytaxi.MyApplication;
import com.example.administrator.mytaxi.account.model.response.Account;
import com.example.administrator.mytaxi.account.model.response.LoginResponse;
import com.example.administrator.mytaxi.account.model.response.MsgResponse;
import com.example.administrator.mytaxi.account.model.response.MsgVerifyResponse;
import com.example.administrator.mytaxi.common.http.IHttpClient;
import com.example.administrator.mytaxi.common.http.IRequest;
import com.example.administrator.mytaxi.common.http.IResponse;
import com.example.administrator.mytaxi.common.http.Impl.BaseRequest;
import com.example.administrator.mytaxi.common.http.Impl.BaseResponse;
import com.example.administrator.mytaxi.common.http.api.API;
import com.example.administrator.mytaxi.common.http.biz.BaseBizResponse;
import com.example.administrator.mytaxi.common.storage.SharedPreferencesDao;
import com.example.administrator.mytaxi.common.util.DevUtil;
import com.google.gson.Gson;

/**
 * Created by Administrator on 2018/4/20.
 * model层只依赖数据相关的类
 */

public class AccountManagerImpl implements IAccountManager {
    private static final String TAG = "AccountManagerImpl";
    //网络请求库
    private IHttpClient mHttpClient;
    //数据储存
    private SharedPreferencesDao sharedPreferencesDao;
    //发送消息handler
    private Handler mHandler;

    public AccountManagerImpl(IHttpClient httpClient, SharedPreferencesDao sharedPreferencesDao) {
        this.mHttpClient = httpClient;
        this.sharedPreferencesDao = sharedPreferencesDao;
    }

    @Override
    public void setHandler(Handler handler) {
        this.mHandler=handler;
    }

    /**
     * 获取验证码
     * @param phone
     */
    @Override
    public void fetchSMSCode(final String phone) {
        new Thread(){
            @Override
            public void run() {
                //请求验证码的参数只需要一个phone number
                String url = API.Config.getDomain()+API.GET_SMS_CODE;
                Log.d("jun","请求url："+url);
                IRequest request = new BaseRequest(url);
                request.setBody("phone",phone);
                Log.d("jun","请求phone："+phone);
                Log.d("jun","请求组装url："+request.getUrl());
                BaseResponse data = (BaseResponse) mHttpClient.get(request,false);

                Log.d(TAG,data.getData());
                MsgResponse response=new Gson().fromJson(data.getData(),MsgResponse.class);
//                Log.d("jun","请求response："+response.toString());
                Log.d(TAG,response.getMsg());
                if(response!=null){
                    Log.d("jun","请求response.getMsg()："+response.getMsg());
                }else{
                    Log.d("jun","Response==null");
                }


                //根据response 的state code 判断是否发送成功
                if(response.getCode()== BaseResponse.STATE_OK){
                    Log.d("jun","response.getCode()== BaseResponse.STATE_OK   "+response.getCode());
                    mHandler.sendEmptyMessage(SMS_SEND_SUC);
                }else{

                    mHandler.sendEmptyMessage(SMS_SEND_FAIL);
                }
            }
        }.start();
    }

    /**
     * 校验验证码
     * @param phone
     * @param smsCode
     */
    @Override
    public void checkSmsCode(final String phone, final String smsCode) {
        //网络请求校验验证码
        new Thread(){
            @Override
            public void run() {
                String url = API.Config.getDomain()+API.CHECK_SMS_CODE;
                IRequest request = new BaseRequest(url);
                request.setBody("phone",phone);
                request.setBody("code",smsCode);
                IResponse response = mHttpClient.get(request,false);
                Log.d(TAG,response.getData());
                Log.d("jun","response.getData()"+response.getData());

                /**
                 * response返回示例
                 * {"code":200,"msg":"code has send"}
                 */


                if(response.getCode() == BaseResponse.STATE_OK){

                    BaseBizResponse bizRes =
                            new Gson().fromJson(response.getData(), BaseBizResponse.class);
                    if (bizRes.getCode() == BaseBizResponse.STATE_OK) {
                        mHandler.sendEmptyMessage(SMS_CHECK_SUC);
                    } else  {
                        mHandler.sendEmptyMessage(SMS_CHECK_FAIL);
                    }
                }else{
                    mHandler.sendEmptyMessage(SMS_CHECK_FAIL);
                }

            }
        }.start();
    }

    @Override
    public void checkUserExist(final String phone) {
        // 检查用户是否存在
        new Thread() {
            @Override
            public void run() {
                String url = API.Config.getDomain() + API.CHECK_USER_EXIST;
                IRequest request = new BaseRequest(url);
                request.setBody("phone", phone);
                IResponse data = mHttpClient.get(request, false);
                Log.d(TAG, data.getData());
                Log.d("jun", "验证用户data.getData()" + data.getData());
                MsgVerifyResponse response = new Gson().fromJson(data.getData(), MsgVerifyResponse.class);
                if (response.getCode() == MsgVerifyResponse.USER_EXIST) {
                    mHandler.sendEmptyMessage(USER_EXIST);
                } else if (response.getCode() == MsgVerifyResponse.USER_NOT_EXIST) {
                    mHandler.sendEmptyMessage(USER_NOT_EXIST);
                } else {
                    mHandler.sendEmptyMessage(SERVER_FAIL);
                }
            }

        }.start();
    }

    @Override
    public void register(final String phone,final String password) {

        //开启子线程，请求网络 提交注册
        new Thread(){
            @Override
            public void run() {
                String url= API.Config.getDomain()+API.REGISTER;
                IRequest request=new BaseRequest(url);
                request.setBody("phone",phone);
                request.setBody("password",password);

                String uid=DevUtil.UUID(MyApplication.getInstance());
                request.setBody("uid", uid);

                IResponse response = mHttpClient.post(request, false);
                Log.d(TAG, response.getData());
                if (response.getCode() == BaseResponse.STATE_OK) {
                    BaseBizResponse bizRes =
                            new Gson().fromJson(response.getData(), BaseBizResponse.class);
                    //注册成功
                    if (bizRes.getCode() == BaseBizResponse.STATE_OK) {
                        mHandler.sendEmptyMessage(REGISTER_SUC);
                    } else {
                        mHandler.sendEmptyMessage(SERVER_FAIL);
                    }
                } else {
                    mHandler.sendEmptyMessage(SERVER_FAIL);
                }
            }
        }.start();

    }

    @Override
    public void login(final String phone,final String password) {
        //  网络请求登录
        new Thread() {
            @Override
            public void run() {
                String url = API.Config.getDomain() + API.LOGIN;
                IRequest request = new BaseRequest(url);
                request.setBody("phone", phone);
                request.setBody("password", password);


                IResponse data = mHttpClient.post(request, false);
                Log.d("jun","登录 response.getCode()"+data.getCode());
                Log.d("jun","登录 response.getData()"+data.getData());
                LoginResponse response=new Gson().fromJson(data.getData(),LoginResponse.class);

                if (response.getCode() == BaseResponse.STATE_OK) {


                    // 保存登录信息
                    Account account = response.getData();
                    // todo: 加密存储
                    SharedPreferencesDao dao =
                            new SharedPreferencesDao(MyApplication.getInstance(),
                                    SharedPreferencesDao.FILE_ACCOUNT);
                    dao.save(SharedPreferencesDao.KEY_ACCOUNT, account);

                    // 通知 UI
                    mHandler.sendEmptyMessage(LOGIN_SUC);


                } else {
                    mHandler.sendEmptyMessage(SERVER_FAIL);
                }

            }
        }.start();
    }

    @Override
    public void loginByToken() {
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
            mHandler.sendEmptyMessage(TOKEN_INVALID);
        } else {
            new Thread() {
                @Override
                public void run() {

                    String url = API.Config.getDomain() + API.LOGIN_BY_TOKEN;
                    IRequest request = new BaseRequest(url);
                    request.setBody("token", account.getToken());
                    IResponse response = mHttpClient.get(request, false);
                    Log.d(TAG, response.getData());
                    Log.d("jun", TAG + " code:" + response.getCode() + " data:" + response.getData());

                    //请求成功
                    if (response.getCode() == BaseResponse.STATE_OK) {
                        Log.d("jun", "登录请求成功");
                        LoginResponse bizRes =
                                new Gson().fromJson(response.getData(), LoginResponse.class);
                        Log.d("jun", TAG + " code:" + bizRes.getCode() + " data:" + bizRes.getData() + " msg:" + bizRes.getMsg());
                        //登录成功
                        if (bizRes.getCode() == BaseBizResponse.STATE_OK) {
                            Log.d("jun", TAG + " 登陆成功");
                            // 保存登录信息
                            Account account = bizRes.getData();
                            // todo: 加密存储
                            SharedPreferencesDao dao =
                                    new SharedPreferencesDao(MyApplication.getInstance(),
                                            SharedPreferencesDao.FILE_ACCOUNT);
                            dao.save(SharedPreferencesDao.KEY_ACCOUNT, account);

                            // 通知 UI
                            mHandler.sendEmptyMessage(LOGIN_SUC);
                        } else if (bizRes.getCode() == BaseBizResponse.STATE_TOKEN_INVALID) {
                            mHandler.sendEmptyMessage(PW_ERROR);

                        } else {
                            mHandler.sendEmptyMessage(SERVER_FAIL);
                        }
                    } else {
                        mHandler.sendEmptyMessage(SERVER_FAIL);
                    }
                }

        }.start();
        }
    }
    }

