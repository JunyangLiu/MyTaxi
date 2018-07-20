package com.example.administrator.mytaxi.account.view;

/**
 * Created by Administrator on 2018/4/20.
 */

public interface ICreatePasswordDialogView extends IView {
    /**
     * 登录
     */
    void login(String phone,String password);

    /**
     * 注册
     */
    void register();
    /**
     * 显示注册成功
     */
    void showRegisterSuc();

    /**
     * 显示登录成功
     */
    void showLoginSuc();

    /**
     * 显示密码为空
     */
    void showPasswordNull();

    /**
     *  显示两次输入密码不一样
     */
    void showPasswordNotEqual();
}
