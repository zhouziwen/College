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
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.zhiyou.colleageapp.R;
import com.zhiyou.colleageapp.appui.BaseFragment;
import com.zhiyou.colleageapp.appui.adapter.ContactSelectAdapter;
import com.zhiyou.colleageapp.appui.listener.IPickContact;
import com.zhiyou.colleageapp.appui.mvpPresenter.PresenterContacts;
import com.zhiyou.colleageapp.appui.mvpView.ViewSuccess;
import com.zhiyou.colleageapp.appui.widget.listview.SideBar;
import com.zhiyou.colleageapp.appui.widget.listview.StickyListHeadersListView;
import com.zhiyou.colleageapp.constants.EaseConstant;
import com.zhiyou.colleageapp.domain.User;
import com.zhiyou.colleageapp.utils.AppLog;
import com.zhiyou.colleageapp.utils.EaseCommonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FriendGroupPickContactsFragment extends BaseFragment implements View.OnClickListener {
    private StickyListHeadersListView listView;
    /**
     * 是否为一个新建的群组
     */
    protected boolean isCreatingNewGroup;
    /**
     * 是否为单选
     */
    private boolean isSignleChecked;
    private ContactSelectAdapter mContactSelectAdapter;
    /**
     * group中一开始就有的成员
     */
    private List<String> exitingMembers;
    private String mFromFragmentTagName;
    private SideBar mSideBar;
    private PresenterContacts mPresenterContacts;
    private TextView mFloatIndex;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_group_pick_contacts, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // String groupName = getIntent().getStringExtra("groupName");
        String groupId = null;
        if (mBundle != null) {
            groupId = mBundle.getString(EaseConstant.GROUP_ID);
            mFromFragmentTagName = mBundle.getString(EaseConstant.FRAGMENT_TAG);
        }
        if (TextUtils.isEmpty(groupId)) {// 创建群组
            isCreatingNewGroup = true;
        } else {
            // 获取此群组的成员列表
            EMGroup group = EMClient.getInstance().groupManager().getGroup(groupId);
            exitingMembers = group.getMembers();
        }
        if (exitingMembers == null) {
            exitingMembers = new ArrayList<>();
        }

    }


    @Override
    protected void initView(View view) {
        super.initView(view);
        listView = (StickyListHeadersListView) view.findViewById(R.id.friend_new_invite_listView);
        mSideBar = (SideBar) view.findViewById(R.id.friend_pick_contact_sidebar);
        mFloatIndex = (TextView) view.findViewById(R.id.floating_header);
        mContactSelectAdapter = new ContactSelectAdapter(getContext(),R.layout.list_item_contact);

        listView.setAdapter(mContactSelectAdapter);
        mPresenterContacts = new PresenterContacts(new MyViewBase());
        mPresenterContacts.loadWhiteContactFromServer(new ViewSuccess<List<User>>() {
            @Override
            public void onSuccess(List<User> users) {
                hiddenLoading();
                String curUserName = EMClient.getInstance().getCurrentUser();
                for (User user : users
                     ) {
                    if (user.getUsername().equals(curUserName)) {
                        users.remove(user);
                        break;
                    }
                }
                mContactSelectAdapter.updateData(users);
            }
        });
    }

    @Override
    protected void setListener() {
        super.setListener();
        mAppTitleBar.getAction().setOnClickListener(this);

        mSideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                char c = s.toLowerCase(Locale.getDefault()).charAt(0);
                int position = mContactSelectAdapter.getPositionForPinyin(c);
                if (position != -1) {
                    listView.setSelection(position);
                }
                mFloatIndex.setText(String.format(Locale.getDefault(),"%s",c));
                mFloatIndex.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTouchUp() {
                mFloatIndex.setVisibility(View.GONE);
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.app_title_action:
                save(mContactSelectAdapter.getSelectUserNameList());
                popSelf();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenterContacts.releaseAll();
    }

    /**
     * @param selectedContactNames :选中的联系人的名字list
     */
    protected void save(List<String> selectedContactNames) {
        if (selectedContactNames.isEmpty()) {
            return;
        }
        Fragment fragment = mBaseActivity.findFragmentByTag(mFromFragmentTagName);
        if (fragment instanceof IPickContact) {
            try {
                String methodName = "selectContact";
                Object[] args = {selectedContactNames, true, new Bundle()};
                EaseCommonUtils.invoke(fragment, methodName, args);
            } catch (Exception e) {
                AppLog.instance().e(e.getMessage());
            }
        }
        popSelf();
    }


}
