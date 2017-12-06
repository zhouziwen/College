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
package com.zhiyou.colleageapp.appui.friend;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhiyou.colleageapp.R;
import com.zhiyou.colleageapp.appui.BaseFragment;
import com.zhiyou.colleageapp.appui.mvpPresenter.PresenterContacts;
import com.zhiyou.colleageapp.appui.mvpView.ViewBase;
import com.zhiyou.colleageapp.appui.mvpView.ViewSuccess;
import com.zhiyou.colleageapp.appui.widget.CircleImageView;
import com.zhiyou.colleageapp.appui.widget.dialog.BaseDialog;
import com.zhiyou.colleageapp.appui.widget.dialog.TextDialog;
import com.zhiyou.colleageapp.domain.User;
import com.zhiyou.colleageapp.manager.PreferenceManager;
import com.zhiyou.colleageapp.utils.AppToast;
import com.zhiyou.colleageapp.utils.EaseUserUtils;

/**
 * 添加联系人界面
 */
public class AddContactFragment extends BaseFragment implements View.OnClickListener {
    private EditText editText;
    private RelativeLayout searchedUserLayout;
    private TextView nameText;
    private Button mAddBtn;
    private CircleImageView mAvatar;
    private String toAddUsername;
    private PresenterContacts mPresenterAddContact;
    private TextDialog mDialogEdit;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_contact, container, false);
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        editText = (EditText) view.findViewById(R.id.edit_note);
        editText.setHint(R.string.user_name);
        searchedUserLayout = (RelativeLayout) view.findViewById(R.id.ll_user);
        nameText = (TextView) view.findViewById(R.id.add_contact_name);
        mAddBtn = (Button) view.findViewById(R.id.add_contact);
        mAvatar = (CircleImageView) view.findViewById(R.id.add_contact_avatar);
        searchedUserLayout.setVisibility(View.GONE);
        mPresenterAddContact = new PresenterContacts(mViewBase);
    }

    @Override
    protected void setListener() {
        super.setListener();
        mAddBtn.setOnClickListener(this);
        mAppTitleBar.getAction().setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.app_title_action:
                searchContact();
                break;
            case R.id.add_contact:
                sendInvite();
                break;
        }
    }



    private void showEditDialog() {
        if (mDialogEdit == null) {
            mDialogEdit = new TextDialog(getContext(), R.string.add_friend, BaseDialog.EDialogButtonType.BUTTON_TWO);

            mDialogEdit.setRightButtonListener(new BaseDialog.RightListener() {

                @Override
                public void onRightListener() {
                    EditText editText = (EditText) mDialogEdit.getLayout().getChildAt(0);
                    String msg = "";
                    if (editText != null) {
                        msg = editText.getText().toString();
                    }
                    mPresenterAddContact.sendInvite(toAddUsername, msg, new ViewSuccess<Integer>() {
                        @Override
                        public void onSuccess(Integer integer) {
                            hiddenLoading();
                            AppToast.showCenterText(integer);
                        }
                    });
                    mDialogEdit.dismiss();
                }
            });

            mDialogEdit.setLeftButtonListener(new BaseDialog.LeftListener() {
                @Override
                public void onLeftListener() {
                    mDialogEdit.dismiss();
                }
            });
            mDialogEdit.setEditTextView("");
            mDialogEdit.setRight(R.string.global_send);
            mDialogEdit.setLeft(R.string.cancel);
            mDialogEdit.setCancelable(true);
        }
        mDialogEdit.show();
    }

    private void sendInvite() {
        hideSoftKeyboard();
        showEditDialog();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenterAddContact.releaseAll();
    }

    /**
     *
     */
    private void searchContact() {
        if (isAddRight()) {
            mPresenterAddContact.isContactExists(toAddUsername, new ViewSuccess<User>() {
                @Override
                public void onSuccess(User user) {
                    hiddenLoading();
                    if (user == null ) {
                        AppToast.showCenterText(R.string.user_not_exists);
                        searchedUserLayout.setVisibility(View.GONE);
                    } else {
                        toAddUsername = user.getUsername();
                        nameText.setText(user.getUsername());
                        EaseUserUtils.setUserAvatar(getContext(), user.getAvatar(), mAvatar);
                        searchedUserLayout.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
        hideSoftKeyboard();

    }

    private ViewBase mViewBase = new ViewBase() {

        @Override
        public void onViewFail(int textId, String msg) {
            hiddenLoading();
            AppToast.showBottomText(textId, msg);
        }

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
            AppToast.showBottom(textId);
        }
    };


    private String getText(TextView view) {
        return view.getText().toString();
    }

    private boolean isAddRight() {
        toAddUsername = getText(editText).trim();
        if (TextUtils.isEmpty(toAddUsername)) {
            AppToast.showCenterText(R.string.Please_enter_a_username);
            return false;
        }


        String currentUser = PreferenceManager.getInstance().getCurrentUsername();
        if (currentUser.equals(toAddUsername)) {
            AppToast.showCenterText(R.string.group_add_self_illegal);
            return false;
        }

        return true;
    }
}
