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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;

import com.zhiyou.colleageapp.R;
import com.zhiyou.colleageapp.appui.BaseFragment;
import com.zhiyou.colleageapp.appui.listener.IPickContact;
import com.zhiyou.colleageapp.appui.mvpPresenter.PresenterGroupOperate;
import com.zhiyou.colleageapp.appui.mvpView.ViewBase;
import com.zhiyou.colleageapp.appui.mvpView.ViewSuccess;
import com.zhiyou.colleageapp.constants.EaseConstant;
import com.zhiyou.colleageapp.constants.FragmentTag;
import com.zhiyou.colleageapp.domain.FriendGroup;
import com.zhiyou.colleageapp.rxeventbus.RxEventBus;
import com.zhiyou.colleageapp.rxeventbus.event.EventCreateNewGroup;
import com.zhiyou.colleageapp.utils.AppToast;

import java.util.ArrayList;

/**
 * create a group
 */
public class CreateGroupFragment extends BaseFragment implements IPickContact {
    private EditText mEditGroupName;
    private EditText mEditDesc;
    private CheckBox mCheckBoxPublic;
    private CheckBox mCheckBoxAddNeedApproval;
    private TextView secondTextView;
    private PresenterGroupOperate mPresenterCreateFriendGroup;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_group, container, false);
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        mEditGroupName = (EditText) view.findViewById(R.id.edit_group_name);
        mEditDesc = (EditText) view.findViewById(R.id.edit_group_introduction);
        mCheckBoxPublic = (CheckBox) view.findViewById(R.id.cb_public);
        mCheckBoxAddNeedApproval = (CheckBox) view.findViewById(R.id.cb_member_inviter);
        secondTextView = (TextView) view.findViewById(R.id.second_desc);
        mPresenterCreateFriendGroup = new PresenterGroupOperate(new MyViewGroupOperate());
    }

    @Override
    protected void setListener() {
        super.setListener();
        mCheckBoxPublic.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    secondTextView.setText(R.string.join_need_owner_approval);
                } else {
                    secondTextView.setText(R.string.Open_group_members_invited);
                }
            }
        });

        mAppTitleBar.getAction().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mEditGroupName.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    AppToast.showCenterText(R.string.Group_name_cannot_be_empty);
                } else {
                    // 进通讯录选人
                    Bundle bundle = new Bundle();
                    bundle.putString(EaseConstant.FRAGMENT_TAG, getTag());
                    mBaseActivity.showFragment(FriendGroupPickContactsFragment.class, FragmentTag.FRAGMENT_CREATE_GROUP_2_PICK_CONTACT, bundle, true);
                }
            }
        });
    }


    @Override
    public void selectContact(ArrayList<String> selectedUserNames, Boolean isChanged, Bundle extraData) {
        if (selectedUserNames == null || selectedUserNames.isEmpty()) {
            return;
        }
        mPresenterCreateFriendGroup.createFriendGroup(getText(mEditGroupName),getText(mEditDesc),mCheckBoxPublic.isChecked(),
                mCheckBoxAddNeedApproval.isChecked(),
                selectedUserNames, new ViewSuccess<FriendGroup>() {
                    @Override
                    public void onSuccess(FriendGroup newGroup) {
                        AppToast.showCenterText(R.string.create_group_success);
                        EventCreateNewGroup event = new EventCreateNewGroup();
                        event.setGroup(newGroup);
                        RxEventBus.getDefault().postEvent(event);
                    }
                });
    }

    private class MyViewGroupOperate implements ViewBase {

        @Override
        public void onViewFail(int textId, String msg) {
            hiddenLoading();
            AppToast.showBottomText(textId,msg);
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

    private String getText(EditText editText) {
        return editText.getText().toString();
    }
}
