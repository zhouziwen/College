package com.zhiyou.colleageapp.appui.friend;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.hyphenate.chat.EMMessage;

/**
 * Author by LongWeiHu on 2016年5月23日
 */
public class SingleChatFragment extends BaseChatFragment {
    public static SingleChatFragment mInstance;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mInstance = this;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        mAppTitleBar.getAction().setVisibility(View.GONE);
    }

    @Override
    public void setListener() {
        super.setListener();

    }

    @Override
    protected void setMsgChatType(EMMessage msg) {
       super.setMsgChatType(msg);
    }
}
