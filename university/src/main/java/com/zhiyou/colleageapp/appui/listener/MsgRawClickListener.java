package com.zhiyou.colleageapp.appui.listener;

import com.hyphenate.chat.EMMessage;

/**
 * Author ： LongWeiHu on 2016/5/19.
 */
public interface MsgRawClickListener {
    void onResendClick(EMMessage message);
    /**
     * 控件有对气泡做点击事件默认实现，如果需要自己实现，return true。
     * 当然也可以在相应的chatrow的onBubbleClick()方法里实现点击事件
     * @param message :
     * @return :
     */
    boolean onBubbleClick(EMMessage message);
    void onBubbleLongClick(EMMessage message);
    void onUserAvatarClick(String username);
}
