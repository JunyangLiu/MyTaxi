package com.example.administrator.mytaxi.account.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.administrator.mytaxi.R;
import com.example.administrator.mytaxi.account.model.IAccountManager;
import com.example.administrator.mytaxi.account.presenter.ILoginDialogPresenter;
import com.example.administrator.mytaxi.account.presenter.LoginDialogPresenterImpl;
import com.example.administrator.mytaxi.common.util.ToastUtil;

/**
 * Created by Administrator on 2018/4/13.
 */

public class LoginDialog extends Dialog implements ILoginView{

    private static final String TAG = "LoginDialog";
    private TextView mPhone;
    private EditText mPw;
    private Button mBtnConfirm;
    private View mLoading;
    private TextView mTips;
    private String mPhoneStr;
    private ILoginDialogPresenter presenter;






    public LoginDialog(Context context, String phone) {
        this(context, R.style.Dialog);
        mPhoneStr = phone;
        presenter=new LoginDialogPresenterImpl(this);
    }

    public LoginDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View root = inflater.inflate(R.layout.dialog_login_input, null);
        setContentView(root);
        initViews();
    }
    private void initViews() {
        mPhone = (TextView) findViewById(R.id.phone);
        mPw = (EditText) findViewById(R.id.password);
        mBtnConfirm = (Button) findViewById(R.id.btn_confirm);
        mLoading = findViewById(R.id.loading);
        mTips = (TextView) findViewById(R.id.tips);
        findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        mPhone.setText(mPhoneStr);

    }


    @Override
    public void login() {
        String password = mPw.getText().toString();
        showLoading(true);
        presenter.requestLogin(mPhoneStr,password);

    }




    /**
     * 处理登录成功 UI
     */
    @Override
    public void showLoginSuc() {
        mLoading.setVisibility(View.GONE);
        mBtnConfirm.setVisibility(View.GONE);
        mTips.setVisibility(View.VISIBLE);
        mTips.setTextColor(getContext().getResources().getColor(R.color.color_text_normal));
        mTips.setText(getContext().getString(R.string.login_suc));
        ToastUtil.show(getContext(), getContext().getString(R.string.login_suc));
        dismiss();

    }
    /**
     * 显示／隐藏 loading
     * @param show
     */
    @Override
    public void showLoading(boolean show) {
        if (show) {
            mLoading.setVisibility(View.VISIBLE);
            mBtnConfirm.setVisibility(View.GONE);
        } else {
            mLoading.setVisibility(View.GONE);
            mBtnConfirm.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showError(int Code, String msg) {
        switch (Code){
            case IAccountManager.PW_ERROR:
                //显示密码错误
                showLoading(false);
                mTips.setVisibility(View.VISIBLE);
                mTips.setTextColor(getContext().getResources().getColor(R.color.error_red));
                mTips.setText(getContext().getString(R.string.password_error));
                break;
            case IAccountManager.SERVER_FAIL:
                // 显示服服务器出错
                showLoading(false);
                mTips.setVisibility(View.VISIBLE);
                mTips.setTextColor(getContext().getResources().getColor(R.color.error_red));
                mTips.setText(getContext().getString(R.string.error_server));
                break;
        }

    }

}
