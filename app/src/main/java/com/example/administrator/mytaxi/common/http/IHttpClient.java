package com.example.administrator.mytaxi.common.http;

/**
 * Created by Administrator on 2018/4/13.
 */

public interface IHttpClient {
    IResponse get(IRequest request, boolean forceCache);
    IResponse post(IRequest request, boolean forceCache);
}
