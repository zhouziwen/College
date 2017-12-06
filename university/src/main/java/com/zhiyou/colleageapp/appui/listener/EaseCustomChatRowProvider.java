package com.zhiyou.colleageapp.appui.listener;

import android.widget.BaseAdapter;

import com.hyphenate.chat.EMMessage;
import com.zhiyou.colleageapp.appui.widget.chatrow.EaseChatRow;

/**
 * Author ： LongWeiHu on 2016/5/19.
 */
public interface EaseCustomChatRowProvider {
    /**
     * 获取多少种类型的自定义chatrow<br/>
     * 注意，每一种chatrow至少有两种type：发送type和接收type
     * @return
     */
    int getCustomChatRowTypeCount();

    /**
     * 获取chatrow type，必须大于0, 从1开始有序排列
     * @return
     */
    int getCustomChatRowType(EMMessage message);

    /**
     * 根据给定message返回chat row
     * @return
     */
    EaseChatRow getCustomChatRow(EMMessage message, int position, BaseAdapter adapter);
}
