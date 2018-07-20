package com.example.administrator.mytaxi.account.view;

/**
 * Created by Administrator on 2018/4/20.
 */

public interface IView {
    /**
     * 显示loading
     */
    void showLoading(boolean show);
    /**
     *  显示错误
     */
    void showError(int code, String msg);
}
