package com.zhiyou.colleageapp.rxeventbus.event;

import com.hyphenate.chat.EMMessage;

/**
 * Create by LongWeiHu on 2016/6/20.
 * 聊天记录改变
 */
public class EventChatRecordChanged extends EventBase{
    private EMMessage.ChatType mChatType;

    public EventChatRecordChanged(EMMessage.ChatType chatType) {
        mChatType = chatType;
    }

    public EMMessage.ChatType getChatType() {
        return mChatType;
    }

    public void setChatType(EMMessage.ChatType chatType) {
        mChatType = chatType;
    }
}
