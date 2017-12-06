/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 * <p>
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
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.zhiyou.colleageapp.R;
import com.zhiyou.colleageapp.appui.adapter.listitem.PopItem;
import com.zhiyou.colleageapp.appui.mvpPresenter.PresenterContacts;
import com.zhiyou.colleageapp.appui.mvpPresenter.PresenterUser;
import com.zhiyou.colleageapp.appui.mvpView.ViewBase;
import com.zhiyou.colleageapp.appui.mvpView.ViewSuccess;
import com.zhiyou.colleageapp.appui.widget.PopWindowView;
import com.zhiyou.colleageapp.constants.EaseConstant;
import com.zhiyou.colleageapp.domain.PersonalProfile;
import com.zhiyou.colleageapp.manager.PreferenceManager;
import com.zhiyou.colleageapp.rxeventbus.RxEventBus;
import com.zhiyou.colleageapp.rxeventbus.event.EventUserLoginSuccess;
import com.zhiyou.colleageapp.utils.AppToast;
import com.zhiyou.colleageapp.utils.EaseCommonUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 登陆页面
 */
public class LoginActivity extends BaseActivity {
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button mLogin;
    private PresenterUser mPresenterLogin;
    private PopWindowView mPopWindowView;
    private ImageView mMoreIv;
    private PresenterContacts mPresenterContacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        mPresenterLogin = new PresenterUser(new LoginViewBase());
        mPresenterContacts = new PresenterContacts();
        setListener();
    }


    private void setListener() {
        // 如果用户名改变，清空密码
        usernameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                passwordEditText.setText(null);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.loginInBtn:
                        login();
                        break;
                    case R.id.login_more:
                        initPop();
                        break;
                }
            }
        };

        mLogin.setOnClickListener(onClickListener);
        mMoreIv.setOnClickListener(onClickListener);

    }

    private void initPop() {
        String currentUser = PreferenceManager.getInstance().getCurrentUsername();
        if (TextUtils.isEmpty(currentUser)) {
            mMoreIv.setVisibility(View.GONE);
            return;
        }
        mMoreIv.setVisibility(View.VISIBLE);
        if (mPopWindowView == null) {
            mPopWindowView = new PopWindowView(getApplicationContext(), usernameEditText.getWidth());
            PopItem item = new PopItem(PreferenceManager.getInstance().getCurrentUsername(), -1);
            List<PopItem> list = new ArrayList<>();
            list.add(item);
            mPopWindowView.setData(list);
            mPopWindowView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    PopItem popItem = (PopItem) parent.getAdapter().getItem(position);
                    if (popItem.getImgResId() == -1) {
                        usernameEditText.setText(popItem.getContent());
                    }
                    mPopWindowView.dismiss();
                }
            });
        }

        mPopWindowView.show(usernameEditText);
    }

    private void initView() {
        usernameEditText = (EditText) findViewById(R.id.phoneNumEdit);
        passwordEditText = (EditText) findViewById(R.id.eidt_password);
        mLogin = (Button) findViewById(R.id.loginInBtn);
        mMoreIv = (ImageView) findViewById(R.id.login_more);

    }

    /**
     * 登录
     */
    private void login() {
        if (!EaseCommonUtils.isNetWorkConnected(this)) {
            AppToast.showCenterText(R.string.network_isnot_available);
            return;
        }
        String currentUsername = usernameEditText.getText().toString().trim();
        String currentPassword = passwordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(currentUsername)) {
            AppToast.showCenterText(R.string.User_name_cannot_be_empty);
            return;
        }
        if (TextUtils.isEmpty(currentPassword)) {
            AppToast.showCenterText(R.string.Password_cannot_be_empty);
            return;
        }

        hideSoftKeyboard();

        mPresenterLogin.loginAppServer(currentUsername, currentPassword, new ViewSuccess<Integer>() {
            @Override
            public void onSuccess(Integer integer) {
                hiddenLoading();
                mPresenterContacts.getContactProfile(null, new ViewSuccess<PersonalProfile>() {
                    @Override
                    public void onSuccess(PersonalProfile personalProfile) {
                        PreferenceManager.getInstance().saveBelong(personalProfile.getBelong());
                        PreferenceManager.getInstance().setCurrentUserAvatar(personalProfile.getAvatar());
                        PreferenceManager.getInstance().saveUserName(personalProfile.getUsername());
                        RxEventBus.getDefault().postEvent(new EventUserLoginSuccess());
                    }
                });
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }

        String userName = data.getStringExtra(EaseConstant.CHAT_ENTITY_ID);
        switch (requestCode) {
            case EaseConstant.REQUEST_CODE_REGISTER:
                usernameEditText.setText(userName);
                break;
            case EaseConstant.REQUEST_CODE_FORGET_PWD:
                usernameEditText.setText(userName);
                break;
        }

    }

    /**
     * 注册
     *
     * @param view
     */
    public void register(View view) {
        hideSoftKeyboard();
        startActivityForResult(new Intent(this, RegisterActivity.class), EaseConstant.REQUEST_CODE_REGISTER);
    }

    /**
     * 忘记密码
     *
     * @param view
     */
    public void forgetPass(View view) {
        hideSoftKeyboard();
        startActivityForResult(new Intent(this, ForgetPassActivity.class), EaseConstant.REQUEST_CODE_FORGET_PWD);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        hiddenLoading();
        mPresenterLogin.cancelLogin();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        hiddenLoading();
    }

    private class LoginViewBase implements ViewBase {
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
            AppToast.showBottomText(textId, e);
        }

        @Override
        public void onViewFail(int textId, String msg) {
            hiddenLoading();
            AppToast.showBottomText(textId, msg);
        }
    }
}
