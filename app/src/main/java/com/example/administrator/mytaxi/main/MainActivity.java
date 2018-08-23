package com.example.administrator.mytaxi.main;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.administrator.mytaxi.MyApplication;
import com.example.administrator.mytaxi.R;
import com.example.administrator.mytaxi.account.model.response.Account;
import com.example.administrator.mytaxi.account.view.PhoneInputDialog;
import com.example.administrator.mytaxi.common.http.IHttpClient;
import com.example.administrator.mytaxi.common.http.Impl.OkHttpClientImpl;
import com.example.administrator.mytaxi.common.storage.SharedPreferencesDao;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = "MainActivity";
    private IHttpClient mHttpClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mHttpClient =  new OkHttpClientImpl();
        requestPermission();
        checkLoginState();

    }
    /**
     *  检查用户是否登录
     */
    private void checkLoginState() {
        //获取本地登录信息
        SharedPreferencesDao dao=
                new SharedPreferencesDao(MyApplication.getInstance(),
                        SharedPreferencesDao.FILE_ACCOUNT);
        final Account account =
                (Account) dao.get(SharedPreferencesDao.KEY_ACCOUNT, Account.class);

        //登录是否过期的flag
        boolean tokenValid=false;

        //检查token是否过期

        if(account!=null){
            if(account.getExpired()>System.currentTimeMillis()){
                Log.d("jun", TAG+" account.getExpired():"+account.getExpired());
                Log.d("jun", TAG+" System.currentTimeMillis():"+System.currentTimeMillis());
                //token有效
                tokenValid=true;
                Log.d("jun", TAG+" token有效");
            }
        }

//        if(!tokenValid){
            //token过期跳转输入电话号码界面
            showPhoneInputDialog();
//        }else{
//            new Thread(){
//                @Override
//                public void run() {
//                    //用token 登录
//                    String url= API.Config.getDomain()+API.LOGIN_BY_TOKEN;
//                    IRequest request=new BaseRequest(url);
//                    request.setBody("token",account.getToken());
//                    IResponse response=mHttpClient.get(request,false);
//                    Log.d(TAG, response.getData());
//                    Log.d("jun", TAG+" code:"+response.getCode()+" data:"+response.getData());
//
//                    //请求成功
//                    if (response.getCode() == BaseResponse.STATE_OK) {
//                        Log.d("jun", "登录请求成功");
//                        LoginResponse bizRes =
//                                new Gson().fromJson(response.getData(), LoginResponse.class);
//                        Log.d("jun", TAG+" code:"+bizRes.getCode()+" data:"+bizRes.getData()+" msg:"+bizRes.getMsg());
//                        //登录成功
//                        if (bizRes.getCode() == BaseBizResponse.STATE_OK) {
//                            Log.d("jun", TAG+" 登陆成功");
//                            // 保存登录信息
//                            Account account =  bizRes.getData();
//                            // todo: 加密存储
//                            SharedPreferencesDao dao =
//                                    new SharedPreferencesDao(MyApplication.getInstance(),
//                                            SharedPreferencesDao.FILE_ACCOUNT);
//                            dao.save(SharedPreferencesDao.KEY_ACCOUNT, account);
//
//                            // 通知 UI
//                            MainActivity.this.runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    ToastUtil.show(MainActivity.this,
//                                            getString(R.string.login_suc));
//                                }
//                            });
//                        }
//                        if(bizRes.getCode() == BaseBizResponse.STATE_TOKEN_INVALID) {
//                            MainActivity.this.runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    showPhoneInputDialog();
//                                }
//                            });
//                        }
//                    }else{
//                        //请求失败
//                        MainActivity.this.runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                ToastUtil.show(MainActivity.this,
//                                        getString(R.string.error_server));
//                            }
//                        });
//                    }
//
//                }
//            }.start();
//        }

    }
    /**
     * 显示手机输入框
     */
    private void showPhoneInputDialog() {
        PhoneInputDialog dialog = new PhoneInputDialog(this);
        dialog.show();
    }
    /**
     * 请求授权
     */
    private void requestPermission(){

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){ //表示未授权时
            //进行授权
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_PHONE_STATE},1);
        }else{
            Toast.makeText(this,"请授权后再操作",Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){ //同意权限申请
                    Toast.makeText(this,"已授权",Toast.LENGTH_LONG).show();
                }else { //拒绝权限申请
                    Toast.makeText(this,"权限被拒绝了",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }


    }
}
