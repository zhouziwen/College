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
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.zhiyou.colleageapp.Helper;
import com.zhiyou.colleageapp.R;
import com.zhiyou.colleageapp.appui.BaseFragment;
import com.zhiyou.colleageapp.appui.adapter.UniversalClickableAdapter;
import com.zhiyou.colleageapp.appui.adapter.viewholder.UniversalViewHolder;
import com.zhiyou.colleageapp.appui.mvpPresenter.PresenterContacts;
import com.zhiyou.colleageapp.appui.mvpView.ViewBase;
import com.zhiyou.colleageapp.appui.mvpView.ViewSuccess;
import com.zhiyou.colleageapp.domain.InviteMessage;
import com.zhiyou.colleageapp.eenum.InviteMsgStatus;
import com.zhiyou.colleageapp.utils.AppToast;
import com.zhiyou.colleageapp.utils.ResUtils;

import java.util.List;
import java.util.Locale;

/**
 * 申请与通知
 */
public class FriendApplyAndNotifyFragment extends BaseFragment {

    private PresenterContacts mPresenterContacts;
    private UniversalClickableAdapter<InviteMessage> mAdapter;
    /**
     * 被操作的inviteMsg的时间戳
     */

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notify_apply, container, false);
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        ListView mListView = (ListView) view.findViewById(R.id.friend_new_invite_listView);
        List<InviteMessage> messagesList = Helper.getInstance().getInviteMsgList();
        mAdapter = new ApplyAdapter(getContext(), R.layout.em_row_invite_msg);
        mPresenterContacts = new PresenterContacts(new NotifyViewBase());
        mAdapter.updateData(messagesList);
        mListView.setAdapter(mAdapter);
    }


    @Override
    public boolean onBackPressed() {
        hiddenLoading();
        return super.onBackPressed();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenterContacts.releaseAll();
    }


    private void setAgreeStatusBtn(UniversalViewHolder holder) {
        holder.setVisible(R.id.list_item_invite_agree, View.VISIBLE);
        holder.setEnable(R.id.list_item_invite_agree, true);
        holder.setText(R.id.list_item_invite_agree, R.string.agree);

        holder.setVisible(R.id.list_item_invite_refuse, View.VISIBLE);
        holder.setEnable(R.id.list_item_invite_refuse, true);
        holder.setText(R.id.list_item_invite_refuse, R.string.refuse);
    }

    private class NotifyViewBase implements ViewBase{

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


    private class ApplyAdapter extends UniversalClickableAdapter<InviteMessage> {

        public ApplyAdapter(Context context, int layoutId) {
            super(context, layoutId);
        }

        @Override
        public void setViewListener(UniversalViewHolder holder) {
            holder.setClickListener(R.id.list_item_invite_agree,R.id.list_item_invite_refuse);
        }

        @Override
        public void onClickBack(View v, int position, final UniversalViewHolder holder) {
            InviteMessage message = mAdapter.getItem(position);

            switch (v.getId()) {
                case R.id.list_item_invite_agree:
                    mPresenterContacts.agreeInvitation(message, new ViewSuccess<Integer>() {
                        @Override
                        public void onSuccess(Integer textId) {
                            hiddenLoading();
                            AppToast.showCenterText(textId);
                            holder.setText(R.id.list_item_invite_agree,R.string.Has_agreed_to);
                            holder.setEnable(R.id.list_item_invite_agree, false);
                            holder.setVisible(R.id.list_item_invite_refuse,View.INVISIBLE);
                        }
                    });
                    break;
                case R.id.list_item_invite_refuse:
                    mPresenterContacts.refuseInvitation(message, new ViewSuccess<Integer>() {
                        @Override
                        public void onSuccess(Integer textId) {
                            hiddenLoading();
                            AppToast.showCenterText(textId);
                            holder.setText(R.id.list_item_invite_agree,R.string.Has_refused_to);
                            holder.setEnable(R.id.list_item_invite_agree, false);
                            holder.setVisible(R.id.list_item_invite_refuse,View.INVISIBLE);
                        }
                    });
                    break;

            }
        }

        @Override
        public void onLongClickBack(View v, int position,UniversalViewHolder holder) {

        }

        @Override
        public void convert(UniversalViewHolder holder, InviteMessage msg) {

            if (msg == null) {
                return;
            }
            holder.setVisible(R.id.list_item_invite_agree, false);
            if (msg.getGroupId() != null) { // 显示群聊提示
                holder.setVisible(R.id.ll_group, true);
                holder.setText(R.id.tv_groupName, msg.getGroupName());
            } else {
                holder.setVisible(R.id.ll_group, View.GONE);
            }

            String inviteeId = msg.getInviteeId();
            String from = msg.getFrom();
            if (!TextUtils.isEmpty(inviteeId)) {
                holder.setText(R.id.list_item_invite_from_name, inviteeId);
            } else if (!TextUtils.isEmpty(from)){
                holder.setText(R.id.list_item_invite_from_name, from);
            }

            holder.setText(R.id.list_item_invite_reason, msg.getReason());

            InviteMsgStatus msgStatus = msg.getMsgStatus();
            TextView status = holder.getView(R.id.list_item_invite_refuse);
            TextView reason = holder.getView(R.id.list_item_invite_reason);
            switch (msgStatus) {
                case BEAGREED:
                    holder.setVisible(R.id.list_item_invite_refuse, View.INVISIBLE);
                    holder.setText(R.id.list_item_invite_reason, R.string.Has_agreed_to_your_friend_request);
                    break;
                case BEINVITEED:
                    setAgreeStatusBtn(holder);
                    if (msg.getReason() == null) {
                        // 如果没写理由
                        reason.setText(R.string.Request_to_add_you_as_a_friend);
                    }
                    break;
                case BEAPPLYED:
                    setAgreeStatusBtn(holder);
                    if (TextUtils.isEmpty(msg.getReason())) {
                        reason.setText(String.format(Locale.getDefault(), "%s%s", ResUtils.getString(R.string.Apply_to_the_group_of), msg.getGroupName()));
                    }
                    break;
                case GROUPINVITATION:
                    setAgreeStatusBtn(holder);
                    if (TextUtils.isEmpty(msg.getReason())) {
                        reason.setText(String.format(Locale.getDefault(), "%s%s", ResUtils.getString(R.string.invite_join_group), msg.getGroupName()));
                    }
                    break;
                case AGREED:
                    status.setText(R.string.Has_agreed_to);
                    status.setEnabled(false);
                    break;
                case REFUSED:
                    status.setText(R.string.Has_refused_to);
                    status.setEnabled(false);
                    break;
                case GROUPINVITATION_ACCEPTED:
                    String str = String.format(Locale.getDefault(), "%s%s%s", msg.getInviteeId(), ResUtils.getString(R.string.accept_join_group), msg.getGroupName());
                    status.setText(str);
                    status.setEnabled(false);
                    break;
                case GROUPINVITATION_DECLINED:
                    String text = msg.getInviteeId() + ResUtils.getString(R.string.refuse_join_group) + msg.getGroupName();
                    status.setText(text);
                    status.setEnabled(false);
                    break;
            }
        }
    }

}
