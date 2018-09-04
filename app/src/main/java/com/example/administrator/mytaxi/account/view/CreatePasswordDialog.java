package com.example.administrator.mytaxi.account.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.administrator.mytaxi.R;
import com.example.administrator.mytaxi.account.model.IAccountManager;
import com.example.administrator.mytaxi.account.presenter.CreatePasswordDialogPresenterImpl;
import com.example.administrator.mytaxi.account.presenter.ICreatePasswordDialogPresenter;
import com.example.administrator.mytaxi.common.util.ToastUtil;

/**
 * Created by Administrator on 2018/4/16.
 * 自定义创建账号密码Dialog
 */

public class CreatePasswordDialog extends Dialog implements ICreatePasswordDialogView{
    final public static int REQUEST_CODE_READ_PHONE_STATE = 123;
    private  static final String TAG = "CreatePasswordDialog";
    private static final int REGISTER_SUC = 1;
    private static final int SERVER_FAIL = 100;
    private static final int LOGIN_SUC = 2;
    //标题
    private TextView mTitle;
    //电话号码
    private TextView mPhone;
    //
    private EditText mPw;
    private EditText mPw1;
    private Button mBtnConfirm;
    private View mLoading;
    private TextView mTips;
    private String mPhoneStr;
    private ICreatePasswordDialogPresenter presenter;




    public CreatePasswordDialog(@NonNull Context context, String phone) {
        this(context, R.style.Dialog);
        // 上一个页面传来的手机号
        mPhoneStr = phone;
        presenter = new CreatePasswordDialogPresenterImpl(this);

    }

    public CreatePasswordDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater= (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View root=inflater.inflate(R.layout.dialog_create_pw,null);
        setContentView(root);
        initViews();
    }
    private void initViews() {


        mPhone = (TextView) findViewById(R.id.phone);
        mPw = (EditText) findViewById(R.id.pw);
        mPw1 = (EditText) findViewById(R.id.pw1);
        mBtnConfirm = (Button) findViewById(R.id.btn_confirm);
        mLoading = findViewById(R.id.loading);
        mTips = (TextView) findViewById(R.id.tips);
        mTitle = (TextView) findViewById(R.id.dialog_title);
        findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mBtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("jun","CreatePasswordDialog onClick");
                register();
            }
        });
        mPhone.setText(mPhoneStr);

    }






    /**
     *  处理注册成功
     */
    @Override
    public void showRegisterSuc() {
        mLoading.setVisibility(View.VISIBLE);
        mBtnConfirm.setVisibility(View.GONE);
        mTips.setVisibility(View.VISIBLE);
        mTips.setTextColor(getContext()
                .getResources()
                .getColor(R.color.color_text_normal));
        mTips.setText(getContext()
                .getString(R.string.register_suc_and_loging));
        // 请求网络，完成自动登录
        login(mPhoneStr, mPw.getText().toString());


    }


    /**
     * 登录
     * @param phone
     * @param password
     */
    @Override
    public void login(String phone,String password) {
        presenter.requestLogin(phone,password);

    }

    /**
     * 注册
     */
    @Override
    public void register() {
        String password = mPw.getText().toString();
        String password1 = mPw1.getText().toString();
        boolean check = presenter.checkPw(password, password1);
        if (check) {
            Log.d("jun","CreatePasswordDialog register");
            presenter.requestRegister(mPhoneStr, password);
        }
    }

    /**
     *  显示登录成功
     */

    @Override
    public void showLoginSuc() {
        dismiss();
        ToastUtil.show(getContext(), getContext().getString(R.string.login_suc));
    }
    //
    @Override
    public void showPasswordNull() {
        mTips.setVisibility(View.VISIBLE);
        mTips.setText(getContext().getString(R.string.password_is_null));
        mTips.setTextColor(getContext()
                .getResources().getColor(R.color.error_red));
    }

    @Override
    public void showPasswordNotEqual() {
        mTips.setVisibility(View.VISIBLE);
        mTips.setText(getContext()
                .getString(R.string.password_is_not_equal));
        mTips.setTextColor(getContext()
                .getResources().getColor(R.color.error_red));
    }



    /**
     * 显示或者隐藏加载进度条
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
    public void showError(int code, String msg) {
        switch (code){
            case IAccountManager.SERVER_FAIL:
                mTips.setTextColor(getContext()
                        .getResources().getColor(R.color.error_red));
                mTips.setText(getContext().getString(R.string.error_server));
                break;
            default:
                break;

        }
    }


}
