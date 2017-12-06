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
package com.zhiyou.colleageapp.appui.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;

import com.hyphenate.chat.EMMessage;
import com.zhiyou.colleageapp.appui.adapter.viewholder.UniversalViewHolder;
import com.zhiyou.colleageapp.appui.listener.EaseCustomChatRowProvider;
import com.zhiyou.colleageapp.appui.listener.MsgRawClickListener;
import com.zhiyou.colleageapp.appui.widget.chatrow.EaseChatRow;
import com.zhiyou.colleageapp.appui.widget.chatrow.EaseChatRowBigExpression;
import com.zhiyou.colleageapp.appui.widget.chatrow.EaseChatRowFile;
import com.zhiyou.colleageapp.appui.widget.chatrow.EaseChatRowImage;
import com.zhiyou.colleageapp.appui.widget.chatrow.EaseChatRowLocation;
import com.zhiyou.colleageapp.appui.widget.chatrow.EaseChatRowText;
import com.zhiyou.colleageapp.appui.widget.chatrow.EaseChatRowVideo;
import com.zhiyou.colleageapp.appui.widget.photoview.EaseChatRowVoice;
import com.zhiyou.colleageapp.constants.EaseConstant;
import com.zhiyou.colleageapp.utils.AppLog;

public class MessageAdapter extends UniversalBaseAdapter<EMMessage> {
    private Context context;
    private interface MsgOrientation {
        int RECEIVE_TXT = 0;
        int SENT_TXT = 1;
        int SENT_IMAGE = 2;
        int SENT_LOCATION = 3;
        int RECEIVE_LOCATION = 4;
        int RECEIVE_IMAGE = 5;
        int SENT_VOICE = 6;
        int RECEIVE_VOICE = 7;
        int SENT_VIDEO = 8;
        int RECEIVE_VIDEO = 9;
        int SENT_FILE = 10;
        int RECEIVE_FILE = 11;
        int SENT_EXPRESSION = 12;
        int RECEIVE_EXPRESSION = 13;
    }

    private EaseCustomChatRowProvider mCustomRowProvider;
    private MsgRawClickListener mChatRawClickListener;
    private boolean mShowUserNick,mShowAvatar;
    private Drawable mBubble,mOtherBubble;


    public MessageAdapter(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public int getViewTypeCount() {
        if (mCustomRowProvider != null && mCustomRowProvider.getCustomChatRowTypeCount() > 0) {
            return mCustomRowProvider.getCustomChatRowTypeCount() + 14;
        }
        return 14;
    }


    /**
     * 获取item类型
     */
    @Override
    public int getItemViewType(int position) {
        EMMessage message = getItem(position);
        if (message == null) {
            return -1;
        }

        if (mCustomRowProvider != null && mCustomRowProvider.getCustomChatRowType(message) > 0) {
            return mCustomRowProvider.getCustomChatRowType(message) + 13;
        }

        EMMessage.Type type = message.getType();
        EMMessage.Direct direct = message.direct();
        int value = -1;

        switch (type) {
            case TXT:
                if (message.getBooleanAttribute(EaseConstant.MESSAGE_ATTR_IS_BIG_EXPRESSION, false)) {
                    return direct == EMMessage.Direct.RECEIVE ? MsgOrientation.RECEIVE_EXPRESSION : MsgOrientation.SENT_EXPRESSION;
                }
                return direct == EMMessage.Direct.RECEIVE ? MsgOrientation.RECEIVE_TXT : MsgOrientation.SENT_TXT;
            case IMAGE:
                value = (direct == EMMessage.Direct.RECEIVE ? MsgOrientation.RECEIVE_IMAGE : MsgOrientation.SENT_IMAGE);
                break;
            case LOCATION:
                value = (direct == EMMessage.Direct.RECEIVE ? MsgOrientation.RECEIVE_LOCATION : MsgOrientation.SENT_LOCATION);
                break;
            case VOICE:
                value = (direct == EMMessage.Direct.RECEIVE ? MsgOrientation.RECEIVE_VOICE : MsgOrientation.SENT_VOICE);
                break;
            case VIDEO:
                value = (direct == EMMessage.Direct.RECEIVE ? MsgOrientation.RECEIVE_VIDEO : MsgOrientation.SENT_VIDEO);
                break;
            case FILE:
                value = (direct == EMMessage.Direct.RECEIVE ? MsgOrientation.RECEIVE_FILE : MsgOrientation.SENT_FILE);
                break;
        }


        return value;// invalid
    }


    protected EaseChatRow createChatRow(Context context, EMMessage message, int position) {
        EaseChatRow easeChatRow = null;
        switch (message.getType()) {
            case TXT:
                if (message.getBooleanAttribute(EaseConstant.MESSAGE_ATTR_IS_BIG_EXPRESSION, false)) {
                    easeChatRow = new EaseChatRowBigExpression(context, message, position, this);
                } else {
                    easeChatRow = new EaseChatRowText(context, message, position, this);
                }
                break;
            case LOCATION:
                easeChatRow = new EaseChatRowLocation(context, message, position, this);
                break;
            case FILE:
                easeChatRow = new EaseChatRowFile(context, message, position, this);
                break;
            case IMAGE:
                easeChatRow = new EaseChatRowImage(context, message, position, this);
                break;
            case VOICE:
                easeChatRow = new EaseChatRowVoice(context, message, position, this);
                break;
            case VIDEO:
                easeChatRow = new EaseChatRowVideo(context, message, position, this);
                break;
            default:
                break;
        }

        return easeChatRow;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        EMMessage message = getItem(position);
        if (convertView == null) {
            convertView = createChatRow(context, message, position);
            AppLog.instance().i("MsgAdapter===");
        }
        //缓存的view的message很可能不是当前item的，传入当前message和position更新ui
        ((EaseChatRow) convertView).setUpView(message, position, mChatRawClickListener);

        return convertView;
    }

    @Override
    public void convert(UniversalViewHolder holder, EMMessage emMessage) {

    }


    public void setShowUserNick(boolean showUserNick) {
        this.mShowUserNick = showUserNick;
    }


    public void setShowAvatar(boolean showAvatar) {
        this.mShowAvatar = showAvatar;
    }


    public void setBubble(Drawable bubble) {
        this.mBubble = bubble;
    }


    public void setOtherBubble(Drawable otherBubble) {
        this.mOtherBubble = otherBubble;
    }


    public void setChatRawClickListener(MsgRawClickListener listener) {
        mChatRawClickListener = listener;
    }

    public void setCustomChatRowProvider(EaseCustomChatRowProvider rowProvider) {
        mCustomRowProvider = rowProvider;
    }


    public boolean isShowUserNick() {
        return mShowUserNick;
    }


    public boolean isShowAvatar() {
        return mShowAvatar;
    }


    public Drawable getBubble() {
        return mBubble;
    }


    public Drawable getOtherBubble() {
        return mOtherBubble;
    }


}
