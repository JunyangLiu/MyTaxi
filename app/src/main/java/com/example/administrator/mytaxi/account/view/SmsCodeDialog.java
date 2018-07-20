package com.example.administrator.mytaxi.account.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dalimao.corelibrary.VerificationCodeInput;
import com.example.administrator.mytaxi.R;
import com.example.administrator.mytaxi.account.model.IAccountManager;
import com.example.administrator.mytaxi.account.presenter.ISmsCodeDialogPresenter;
import com.example.administrator.mytaxi.account.presenter.SmsCodeDialogPresenterImpl;
import com.example.administrator.mytaxi.common.util.ToastUtil;

/**
 * 获取验证码的dialog
 * 用handler 统一处理各种请求结果
 * Created by jun on 2018/4/13.
 */

public class SmsCodeDialog  extends Dialog implements ISmsCodeDialogView{
    private static final String TAG = "SmsCodeDialog";
    //验证码接收状态码
    private static final int SMS_SEND_SUC = 1;
    private static final int SMS_SEND_FAIL = -1 ;
    private static final int SMS_CHECK_SUC = 2;
    private static final int SMS_CHECK_FAIL = -2;
    private static final int USER_EXIST = 3;
    private static final int USER_NOT_EXIST = -3;
    private static final int SMS_SERVER_FAIL = 100;

    //手机号码
    private String mPhone;
    //重新发送按钮
    private Button mResentBtn;
    //验证码输入框
    private VerificationCodeInput mVerificationInput;
    //正在加载的progressbar
    private View mLoading;
    //提示验证码错误的textview
    private View mErrorView;
    //显示短信已发送到号码XXXXXXXXX
    private TextView mPhoneTv;

    private ISmsCodeDialogPresenter mPresenter;
    /**
     * 验证码倒计时
     *
     */
    private CountDownTimer mCountDownTimer = new CountDownTimer(100000,1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            //在倒计时阶段，重新发送按钮无法点击
            //显示倒数多少秒后重新发送
            mResentBtn.setEnabled(false);
            mResentBtn.setText(String.format(
                    getContext().getString(R.string.after_time_resend),
                    millisUntilFinished/1000));
            Log.d("jun","倒计时onTick"+millisUntilFinished/1000);
        }

        @Override
        public void onFinish() {
            //倒数完成，重新发送按钮可以点击并取消对话框
            mResentBtn.setEnabled(true);
            mResentBtn.setText(getContext().getString(R.string.resend));
            Log.d("jun","倒计时onFinish");
            cancel();

        }
    };




    public SmsCodeDialog(@NonNull Context context,String phone) {
        this(context,R.style.Dialog);
        //上一个界面传来的手机号
        this.mPhone=phone;
        mPresenter = new SmsCodeDialogPresenterImpl(this);


    }

    public SmsCodeDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化各种View
        LayoutInflater inflate= (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View root=inflate.inflate(R.layout.dialog_smscode_input,null);
        setContentView(root);
        mPhoneTv= (TextView) findViewById(R.id.phone);
        String template=getContext().getString(R.string.sending);
        mPhoneTv.setText(String.format(template,mPhone));
        mResentBtn= (Button) findViewById(R.id.btn_resend);
        mVerificationInput= (VerificationCodeInput) findViewById(R.id.verificationCodeInput);
        mLoading = findViewById(R.id.loading);
        mErrorView = findViewById(R.id.error);
        mErrorView.setVisibility(View.GONE);
        //初始化按钮的点击事件
        initListeners();
        requestSendmsCode();
    }

    /**
     * 请求下发验证码
     */
    private void requestSendmsCode(){
        mPresenter.requestSendSmsCode(mPhone);
    }

    /**
     * 初始化点击事件监听器
     */
    private void initListeners() {

        //  关闭按钮组册监听器
        findViewById(R.id.close).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Log.d("jun","点击了关闭按钮");
                dismiss();
            }
        });

        // 重发验证码按钮注册监听器
        mResentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("jun","点击了重发验证码按钮");
                resend();
            }
        });

        // 验证码输入完成监听器
        mVerificationInput.setOnCompleteListener(new VerificationCodeInput.Listener() {
            @Override
            public void onComplete(String code) {
                Log.d("jun","验证码输入完成");
                commit(code);
            }
        });
    }
    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mCountDownTimer.cancel();

    }
    @Override
    public void dismiss() {
        super.dismiss();
    }
    /**
     * 提交验证码
     * @param code
     */
    private void commit(final String code) {
        mPresenter.requestCheckSmsCode(mPhone, code);
    }
    /**
     * 重新发送验证码
     */
    private void resend() {

        String template = getContext().getString(R.string.sending);
        mPhoneTv.setText(String.format(template, mPhone));

    }




    @Override
    public void showLoading(boolean show) {
        if(show){
            mLoading.setVisibility(View.VISIBLE);
        }else{
            mLoading.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public void showError(int Code, String msg) {
        mLoading.setVisibility(View.GONE);
        switch (Code) {
            case IAccountManager.SMS_SEND_FAIL:
                ToastUtil.show(getContext(),
                        getContext().getString(R.string.sms_send_fail));
                break;
            case IAccountManager.SMS_CHECK_FAIL:
                // 提示验证码错误
                mErrorView.setVisibility(View.VISIBLE);
                mVerificationInput.setEnabled(true);

                break;
            case IAccountManager.SERVER_FAIL:
                ToastUtil.show(getContext(),
                        getContext().getString(R.string.error_server));
                break;
        }
    }
    @Override
    public void showCountDownTimer() {
        mPhoneTv.setText(String.format(getContext()
                .getString(R.string.sms_code_send_phone), mPhone));
        mCountDownTimer.start();
        mResentBtn.setEnabled(false);
    }

    @Override
    public void showSmsCodeCheckState(boolean b) {
        if (!b) {
            //提示验证码错误
            mErrorView.setVisibility(View.VISIBLE);
            mVerificationInput.setEnabled(true);
            mLoading.setVisibility(View.GONE);
        } else {

            mErrorView.setVisibility(View.GONE);
            mLoading.setVisibility(View.VISIBLE);
            mPresenter.requestCheckUserExist(mPhone);
        }
    }

    /**
     * 验证用户后的逻辑处理
     * @param exist
     */
    @Override
    public void showUserExist(boolean exist) {
        mLoading.setVisibility(View.GONE);
        mErrorView.setVisibility(View.GONE);
        dismiss();
        if (!exist) {
            // 用户不存在,进入注册
            CreatePasswordDialog dialog =
                    new CreatePasswordDialog(getContext(), mPhone);
            dialog.show();

        } else {
            // 用户存在 ，进入登录
            LoginDialog dialog = new LoginDialog(getContext(), mPhone);
            dialog.show();

        }
    }
}
