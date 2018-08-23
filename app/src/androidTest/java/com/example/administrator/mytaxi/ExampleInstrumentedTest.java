package com.example.administrator.mytaxi;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.example.administrator.mytaxi.account.model.response.LoginResponse;
import com.example.administrator.mytaxi.common.http.Network;

import org.junit.Test;
import org.junit.runner.RunWith;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.example.administrator.mytaxi", appContext.getPackageName());
    }
    @Test
    public void testApi(){
        Network.getLoginApi().login()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<LoginResponse>() {
                    @Override
                    public void accept(LoginResponse loginResponse) throws Exception {
                        System.out.print("【重要】loginResponse看这里！！！！"+loginResponse.getMsg()+" "+loginResponse.getCode()+" "+loginResponse.getData());

                        Log.d("jun","【重要】loginResponse看这里！！！！"+loginResponse.getMsg()+" "+loginResponse.getCode()+" "+loginResponse.getData());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
//                        Log.d("jun","【重要】throwable看这里！！！！"+throwable.getMessage());
                        System.out.print("【重要】throwable看这里！！！！"+throwable.getMessage());
                    }
                });
//        Call<LoginResponse> response = Network.getLoginApi().loginFotData();
//        response.enqueue(new Callback<LoginResponse>() {
//            @Override
//            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
////                LoginResponse loginResponse = response.raw();
////                Log.d("jun","【重要】loginResponse看这里！！！！"+loginResponse.getMsg()+" "+loginResponse.getCode()+" "+loginResponse.getData());
//
//                Log.d("jun","【重要】onResponse！！！！"+response.message()+"  "+response.body()+"  "+response.raw());


//            }
//
//            @Override
//            public void onFailure(Call<LoginResponse> call, Throwable t) {
//                Log.d("jun","【重要】onFailure！！！！"+"  "+t.getMessage());
//            }
//        });
    }
}
