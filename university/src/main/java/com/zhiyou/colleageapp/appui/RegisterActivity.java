/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zhiyou.colleageapp.appui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zhiyou.colleageapp.R;
import com.zhiyou.colleageapp.appui.mvpPresenter.PresenterUser;
import com.zhiyou.colleageapp.appui.mvpView.ViewBase;
import com.zhiyou.colleageapp.appui.mvpView.ViewRegister;
import com.zhiyou.colleageapp.appui.mvpView.ViewSuccess;
import com.zhiyou.colleageapp.constants.EaseConstant;
import com.zhiyou.colleageapp.utils.AppToast;

import java.util.Locale;

import cn.smssdk.OnSendMessageHandler;
import cn.smssdk.SMSSDK;

/**
 * 注册页
 */
public class RegisterActivity extends BaseActivity {
    private EditText mEditPhoneNum;
    private EditText mEditPwd;
    private EditText mEditTextCode;
    private TextView mGetCodeTextView;
    private String mPhoneNum;
    private PresenterUser mPresenterRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.em_activity_register);
        Button registerBtn = (Button) findViewById(R.id.registerInBtn);
        mEditPhoneNum = (EditText) findViewById(R.id.phoneNumEdit);
        mEditPwd = (EditText) findViewById(R.id.eidt_password);
        mEditTextCode = (EditText) findViewById(R.id.edit_code);
        mGetCodeTextView = (TextView) findViewById(R.id.get_verify_code);


        mPresenterRegister = new PresenterUser(new MyViewBase());
        SMSSDK.initSDK(getApplicationContext(), "f71152e118aa", "2e7065f5174292e28d4550a97c1b2b5c");
        mGetCodeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //SMSSDK.getSupportedCountries();
                //getSupportedCountries方法，在使用短信验证码功能前请调用此方法，获取当前SDK可以支持的国家列表和号码匹配规则。
                //submitVerificationCode用于向服务器提交接收到的短信验证码，验证成功后会通过EventHandler返回国家代码和电话号码。
                mPhoneNum = mEditPhoneNum.getText().toString().trim();
                if (TextUtils.isEmpty(mPhoneNum)) {
                    AppToast.showCenterText("请输入手机号");
                } else {
                    SMSSDK.getVerificationCode("86", mPhoneNum, new OnSendMessageHandler() {
                        @Override
                        public boolean onSendMessage(String s, String s1) {
                            return false;
                        }
                    });
                    mPresenterRegister.onSetSMS(new ViewSuccess<Integer>() {
                        @Override
                        public void onSuccess(Integer textId) {
                            AppToast.showCenterText(textId);
                            mPresenterRegister.onCountDown(10, mPhoneNum,new MyViewRegister());
                        }
                    });
                }

            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }


    /**
     * 注册
     */
    private void register() {
        mPhoneNum = mEditPhoneNum.getText().toString().trim();
        String pwd = mEditPwd.getText().toString().trim();
        String smsCode = mEditTextCode.getText().toString().trim();

        if (TextUtils.isEmpty(mPhoneNum)) {
            AppToast.showCenterText(getResources().getString(R.string.User_name_cannot_be_empty));
            mEditPhoneNum.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(smsCode)) {
            AppToast.showCenterText(getResources().getString(R.string.Code_cannot_be_empty));
            mEditTextCode.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(pwd)) {
            AppToast.showCenterText(getResources().getString(R.string.Password_cannot_be_empty));
            mEditPwd.requestFocus();
            return;
        }

        hideSoftKeyboard();
        mPresenterRegister.onRegister(mPhoneNum, pwd, smsCode, new ViewSuccess<Integer>() {
            @Override
            public void onSuccess(Integer textId) {
                hiddenLoading();
                AppToast.showBottom(textId);
                Intent data = new Intent();
                data.putExtra(EaseConstant.CHAT_ENTITY_ID, mPhoneNum);
                setResult(RESULT_OK, data);
                finish();
            }
        });
    }


    public void toLogin(View view) {
        finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //registerEventHandler必须和unregisterEventHandler配套使用，否则可能造成内存泄漏
        SMSSDK.unregisterAllEventHandler();
        mPresenterRegister.onCancelRegister();
    }


    private class MyViewRegister implements ViewRegister{
        @Override
        public void onViewDuringCountDown(int time) {
            mGetCodeTextView.setText(String.format(Locale.getDefault(), "%s%d", "重新获取", time));
        }

        @Override
        public void onViewCountDownComplete() {
            mGetCodeTextView.setEnabled(true);
            mGetCodeTextView.setText(R.string.register_verify_code_get);
        }

        @Override
        public void onViewCountDownStart() {
            mGetCodeTextView.setEnabled(false);
        }

        @Override
        public void onViewError(Throwable e) {
            hiddenLoading();
            AppToast.showBottom(e.getMessage());
        }
    }

    private class MyViewBase implements ViewBase{
        @Override
        public void onViewStart(int textId) {
            showLoading(textId);
        }

        @Override
        public void onViewComplete() {
            hiddenLoading();
        }

        @Override
        public void onViewError(int textId, Throwable e) {
            hiddenLoading();
            AppToast.showBottomText(textId,e);
        }

        @Override
        public void onViewFail(int textId, String msg) {
            hiddenLoading();
            AppToast.showBottomText(textId,msg);
        }
    }
}
