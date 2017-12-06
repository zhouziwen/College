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

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyphenate.EMGroupChangeListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.util.NetUtils;
import com.zhiyou.colleageapp.R;
import com.zhiyou.colleageapp.application.ColleageApplication;
import com.zhiyou.colleageapp.appui.BaseFragment;
import com.zhiyou.colleageapp.appui.adapter.UniversalClickableAdapter;
import com.zhiyou.colleageapp.appui.adapter.viewholder.UniversalViewHolder;
import com.zhiyou.colleageapp.appui.listener.IPickContact;
import com.zhiyou.colleageapp.appui.mvpPresenter.PresenterGroupOperate;
import com.zhiyou.colleageapp.appui.mvpView.ViewSuccess;
import com.zhiyou.colleageapp.appui.widget.CircleImageView;
import com.zhiyou.colleageapp.appui.widget.EaseExpandGridView;
import com.zhiyou.colleageapp.appui.widget.EaseSwitchButton;
import com.zhiyou.colleageapp.constants.EaseConstant;
import com.zhiyou.colleageapp.constants.FragmentTag;
import com.zhiyou.colleageapp.domain.FriendGroup;
import com.zhiyou.colleageapp.domain.Person;
import com.zhiyou.colleageapp.rxeventbus.RxEventBus;
import com.zhiyou.colleageapp.rxeventbus.event.EventChatRecordChanged;
import com.zhiyou.colleageapp.utils.AppToast;
import com.zhiyou.colleageapp.utils.EaseUserUtils;

import java.util.ArrayList;
import java.util.List;

public class FriendGroupDetailFragment extends BaseFragment implements OnClickListener, IPickContact {

    private EaseExpandGridView mMemberGridView;
    private GroupAdapter adapter;
    private RelativeLayout mSwitchLayout;

    // 清空所有聊天记录
    private RelativeLayout clearAllHistory;
    private RelativeLayout blacklistLayout;
    private RelativeLayout changeGroupNameLayout;
    private EaseSwitchButton switchButton;
    private RelativeLayout searchLayout;
    private PresenterGroupOperate mPresenterGroupOperate;
    private FriendGroup mGroup;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 获取传过来的groupid
        mGroup = mBundle.getParcelable(EaseConstant.GROUP);
        if (mGroup == null) {
            popSelf();
            AppToast.showBottom(R.string.group_not_existed);
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.em_activity_group_details, container, false);
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        clearAllHistory = (RelativeLayout) view.findViewById(R.id.clear_all_history);
        mMemberGridView = (EaseExpandGridView) view.findViewById(R.id.group_detail_gridView);
        Button  exitBtn = (Button) view.findViewById(R.id.btn_exit_grp);
        Button deleteBtn = (Button) view.findViewById(R.id.btn_exitdel_grp);
        blacklistLayout = (RelativeLayout) view.findViewById(R.id.rl_blacklist);
        changeGroupNameLayout = (RelativeLayout) view.findViewById(R.id.rl_change_group_name);
        RelativeLayout idLayout = (RelativeLayout) view.findViewById(R.id.rl_group_id);
        idLayout.setVisibility(View.VISIBLE);
        TextView idText = (TextView) view.findViewById(R.id.tv_group_id_value);
        mSwitchLayout = (RelativeLayout) view.findViewById(R.id.rl_switch_block_groupmsg);
        switchButton = (EaseSwitchButton) view.findViewById(R.id.switch_btn);
        searchLayout = (RelativeLayout) view.findViewById(R.id.rl_search);
        TextView groupNameTv = (TextView) view.findViewById(R.id.group_name);

        idText.setText(mGroup.getId());

        groupNameTv.setText(mGroup.getName());

        if (TextUtils.isEmpty(mGroup.getOwner()) || "".equals(mGroup.getOwner()) || !isOwner()) {
            exitBtn.setVisibility(View.GONE);
            deleteBtn.setVisibility(View.GONE);
            blacklistLayout.setVisibility(View.GONE);
            changeGroupNameLayout.setVisibility(View.GONE);
        }
        // 如果自己是群主，显示解散按钮
        if (isOwner()) {
            exitBtn.setVisibility(View.GONE);
            deleteBtn.setVisibility(View.VISIBLE);
        }


        adapter = new GroupAdapter(getContext(), R.layout.em_grid);
        mMemberGridView.setAdapter(adapter);
        mPresenterGroupOperate = new PresenterGroupOperate(new MyViewBase());
        loadGroupMembers();
    }

    private boolean isOwner() {
        return EMClient.getInstance().getCurrentUser().equals(mGroup.getOwner());
    }

    private void loadGroupMembers() {
        mPresenterGroupOperate.loadGroupMembers(mGroup.getId(), new ViewSuccess<List<Person>>() {
            @Override
            public void onSuccess(List<Person> persons) {
                hiddenLoading();
                persons.add(new Person());
                persons.add(new Person());
                adapter.updateData(persons);
            }
        });
    }

    @Override
    protected void setListener() {
        super.setListener();
        EMClient.getInstance().groupManager().addGroupChangeListener(new GroupChangeListener());
        // 设置OnTouchListener
        mMemberGridView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (adapter.isInDeleteMode) {
                            adapter.isInDeleteMode = false;
                            adapter.notifyDataSetChanged();
                            return true;
                        }
                        break;
                    default:

                        break;
                }
                return false;
            }
        });

        clearAllHistory.setOnClickListener(this);
        blacklistLayout.setOnClickListener(this);
        changeGroupNameLayout.setOnClickListener(this);
        mSwitchLayout.setOnClickListener(this);
        searchLayout.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (TextUtils.isEmpty(mGroup.getId())) {
            return;
        }
        switch (v.getId()) {
            case R.id.rl_switch_block_groupmsg: // 屏蔽或取消屏蔽群组

                if (switchButton.isSwitchOpen()) {
                    mPresenterGroupOperate.actUnBlockGroup(mGroup.getId(), new MyViewSuccess());
                } else {
                    mPresenterGroupOperate.actBlockGroup(mGroup.getId(), new MyViewSuccess());
                }
                break;

            case R.id.clear_all_history: // 清空聊天记录
                mPresenterGroupOperate.actClearGroupHistoryRecord(mGroup.getId(), new ViewSuccess<Integer>() {
                    @Override
                    public void onSuccess(Integer integer) {
                        hiddenLoading();
                        RxEventBus.getDefault().postEvent(new EventChatRecordChanged(EMMessage.ChatType.GroupChat));
                    }
                });
                break;

            case R.id.rl_blacklist: // 黑名单列表
                Bundle bundle = new Bundle();
                bundle.putString(EaseConstant.GROUP_ID, mGroup.getId());
                mBaseActivity.showFragment(FriendGroupBlackMemberListFragment.class, FragmentTag.GROUP_DETAIL_2_GROUP_BLACK_LIST, null, true);
                break;

            case R.id.rl_change_group_name:
                Bundle bundleChangeName = new Bundle();
                bundleChangeName.putString(EaseConstant.DATA, mGroup.getName());
                mBaseActivity.showFragment(EditFragment.class, FragmentTag.GROUP_DETAIL_2_EDIT_GROUP_NAME, null, true);
                break;
            case R.id.rl_search:
                // TODO: 2016/5/27
//                startActivity(new Intent(this, GroupSearchMessageActivity.class).putExtra("groupId", groupId));

                break;
            default:
                break;
        }

    }


    @Override
    public void selectContact(ArrayList<String> selectedUserNames, Boolean isChanged, Bundle extraData) {
        // TODO: 2016/5/27
        mPresenterGroupOperate.actAddMembers(mGroup.getOwner(), EMClient.getInstance().getCurrentUser(), mGroup.getId(), selectedUserNames, new ViewSuccess<Integer>() {
            @Override
            public void onSuccess(Integer textId) {
                hiddenLoading();
                AppToast.showCenterText(textId);

            }
        });
        loadGroupMembers();
    }

    private class MyViewSuccess implements ViewSuccess<Integer> {
        @Override
        public void onSuccess(Integer integer) {
            hiddenLoading();
            AppToast.showCenterText(integer);
        }
    }


    private class GroupAdapter extends UniversalClickableAdapter<Person> {
        public boolean isInDeleteMode;

        public GroupAdapter(Context context, int layoutId) {
            super(context, layoutId);
        }


        @Override
        public void setViewListener(UniversalViewHolder holder) {
            holder.setClickListener(R.id.group_member_iv_avatar, R.id.group_member_delete_img);
        }

        @Override
        public void onClickBack(View v, int position, UniversalViewHolder holder) {
            Bundle bundle;
            switch (v.getId()) {
                case R.id.group_member_delete_img:
                    String clickUser = getItem(position).getUsername();
                    if (EMClient.getInstance().getCurrentUser().equals(clickUser)) {
                        AppToast.showBottom("不能删除自己");
                        return;
                    }
                    if (!NetUtils.hasNetwork(ColleageApplication.getInstance().getApplicationContext())) {
                        AppToast.showCenterText(R.string.network_unavailable);
                        return;
                    }
                    mPresenterGroupOperate.actQuitGroup(mGroup.getId(), clickUser, new ViewSuccess<Integer>() {
                        @Override
                        public void onSuccess(Integer textId) {
                            hiddenLoading();
                            AppToast.showCenterText(textId);
                        }
                    });

                    break;
                case R.id.group_member_iv_avatar:
                    if (isDeleteBtn(position)) { //delete friend
                        isInDeleteMode = true;
                        notifyDataSetChanged();
                    } else if (isAddBtn(position)) { //add friend
                        bundle = new Bundle();
                        bundle.putString(EaseConstant.FRAGMENT_TAG, getTag());
                        mBaseActivity.showFragment(FriendGroupPickContactsFragment.class, FragmentTag.FRAGMENT_GROUP_DETAIL_2_GROUP_PICK_CONTACTS, bundle, true);
                    } else { // common
                        bundle = new Bundle();
                        bundle.putString(EaseConstant.CHAT_ENTITY_ID, getItem(position).getUsername());
                        mBaseActivity.showFragment(FragmentFriendInfo.class, FragmentTag.FRIEND_INFO, bundle, true);
                    }
                    break;
            }


        }

        private boolean isDeleteBtn(int position) {
            return position == getCount() - 1;
        }

        private boolean isAddBtn(int position) {
            return position == getCount() - 2;
        }

        @Override
        public void onLongClickBack(View v, int position, UniversalViewHolder holder) {

        }

        @Override
        public void convert(UniversalViewHolder holder, Person person) {
            TextView textView = holder.getView(R.id.group_member_tv_name);
            CircleImageView imageView = holder.getView(R.id.group_member_iv_avatar);
            ImageView badgeDeleteView = holder.getView(R.id.group_member_delete_img);
            // 最后一个item，减人按钮
            if (isDeleteBtn(holder.getPosition())) {
                textView.setText("");
                // 设置成删除按钮
                imageView.setImageResource(R.drawable.em_smiley_minus_btn);
                badgeDeleteView.setVisibility(View.GONE);
                if (isOwner()) {
                    holder.getConvertView().setVisibility(View.VISIBLE);
                } else {
                    holder.getConvertView().setVisibility(View.GONE);
                }
            } else if (isAddBtn(holder.getPosition())) { // 添加群组成员按钮
                textView.setText("");
                badgeDeleteView.setVisibility(View.GONE);
                imageView.setImageResource(R.drawable.em_smiley_add_btn);
                if (mGroup.isAllowInvites()) {
                    holder.getConvertView().setVisibility(View.VISIBLE);
                } else {
                    holder.getConvertView().setVisibility(View.INVISIBLE);
                }
            } else { // 普通item，显示群组成员

                holder.getConvertView().setVisibility(View.VISIBLE);
                final String username = getItem(holder.getPosition()).getUsername();

                EaseUserUtils.setUserNick(username, textView);
                EaseUserUtils.setUserAvatar(getContext(), username, imageView);

                if (isInDeleteMode) {
                    if (username.equals(EMClient.getInstance().getCurrentUser())) {
                        badgeDeleteView.setVisibility(View.GONE);
                    } else {
                        badgeDeleteView.setVisibility(View.VISIBLE);
                    }
                } else {
                    badgeDeleteView.setVisibility(View.INVISIBLE);
                }

            }
        }
    }


    private class GroupChangeListener implements EMGroupChangeListener {

        @Override
        public void onInvitationReceived(String groupId, String groupName,
                                         String inviter, String reason) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onApplicationReceived(String groupId, String groupName,
                                          String applyer, String reason) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onApplicationAccept(String groupId, String groupName,
                                        String accepter) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onApplicationDeclined(String groupId, String groupName,
                                          String decliner, String reason) {

        }

        @Override
        public void onInvitationAccpted(String groupId, String inviter, String reason) {
            mBaseActivity.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    loadGroupMembers();
                }

            });

        }

        @Override
        public void onInvitationDeclined(String groupId, String invitee,
                                         String reason) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onUserRemoved(String groupId, String groupName) {
            popSelf();

        }

        @Override
        public void onGroupDestroy(String groupId, String groupName) {
            popSelf();

        }

        @Override
        public void onAutoAcceptInvitationFromGroup(String groupId, String inviter, String inviteMessage) {
            // TODO Auto-generated method stub

        }

    }

}
