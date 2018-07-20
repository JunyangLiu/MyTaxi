package com.example.administrator.mytaxi.common.http.Impl;

import com.example.administrator.mytaxi.common.http.IHttpClient;
import com.example.administrator.mytaxi.common.http.IRequest;
import com.example.administrator.mytaxi.common.http.IResponse;
import com.example.administrator.mytaxi.common.http.api.API;

import org.junit.Before;
import org.junit.Test;

/**
 * Created by Administrator on 2018/4/13.
 */
public class OkHttpClientImplTest {
    IHttpClient httpClient;
    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void get() throws Exception {
        httpClient=new OkHttpClientImpl();

        // 设置 request 参数
        String url = API.Config.getDomain() + API.TEST_GET;
        IRequest request = new BaseRequest(url);
        request.setMethod(IRequest.GET);
        request.setHeader("testHeader", "test header");
        request.setBody("uid", "123456");
        if(httpClient==null){
            System.out.println("response==null " );
        }else{
            System.out.println("response!=null " );
            System.out.println(request.getUrl());
            System.out.println(request.getBody());
        }
        if(request==null){
            System.out.println("request==null");
        }else{
            System.out.println("request!=null");
        }
        IResponse response=  httpClient.get(request, false);
        if(response==null){
            System.out.println("response==null " );
        }else{
            System.out.println("stateCode = " + response.getCode());
            System.out.println("body =" + response.getData());
        }

    }

    @Test
    public void post() throws Exception {

    }

}