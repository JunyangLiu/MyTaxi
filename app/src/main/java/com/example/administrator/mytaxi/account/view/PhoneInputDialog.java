package com.example.administrator.mytaxi.account.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.administrator.mytaxi.R;
import com.example.administrator.mytaxi.common.util.FormatUtil;

/**
 * Created by jun
 * 用户注册对话框
 */

public class PhoneInputDialog extends Dialog{
    private View mRoot;
    private EditText mPhone;
    private Button mButton;
    public PhoneInputDialog(@NonNull Context context) {
        this(context, R.style.Dialog);
    }

    public PhoneInputDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater= (LayoutInflater) getContext().
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mRoot=inflater.inflate(R.layout.dialog_phone_input,null);
        setContentView(mRoot);
        initListener();
    }
    private void initListener() {
        mButton = (Button) findViewById(R.id.btn_next);
        mButton.setEnabled(false);
        mPhone = (EditText) findViewById(R.id.phone);
        mPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                check();
            }
        });
        // 按钮注册监听
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                String phone =  mPhone.getText().toString();
                SmsCodeDialog dialog = new SmsCodeDialog(getContext(), phone);
                dialog.show();
                Log.d("jun","点击下一步按钮");

            }
        });
    }
    private void check(){
        String phone =  mPhone.getText().toString();
        boolean legal = FormatUtil.checkMobile(phone);
        mButton.setEnabled(legal);

    }

}
