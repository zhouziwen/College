package com.zhiyou.colleageapp.appui.listener;

import com.hyphenate.chat.EMMessage;

/**
 * Author ： LongWeiHu on 2016/5/19.
 *  /**
 * 新消息提示设置的提供者
 *
 */

public interface EaseSettingsProvider {
    boolean isMsgNotifyAllowed(EMMessage message);
    boolean isMsgSoundAllowed(EMMessage message);
    boolean isMsgVibrateAllowed(EMMessage message);
    boolean isSpeakerOpened();
}
