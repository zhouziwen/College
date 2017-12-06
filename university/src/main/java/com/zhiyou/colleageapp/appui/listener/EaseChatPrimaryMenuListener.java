package com.zhiyou.colleageapp.appui.listener;

import android.view.MotionEvent;
import android.view.View;

/**
 * Author ： LongWeiHu on 2016/5/19.
 */
public interface EaseChatPrimaryMenuListener {

    /**
     * 发送按钮点击事件
     * @param content 发送内容
     */
    void onSendBtnClicked(String content);

    /**
     * 长按说话按钮ontouch事件
     * @return
     */
    boolean onPressToSpeakBtnTouch(View v, MotionEvent event);

    /**
     * 长按说话按钮隐藏或显示事件
     */
    void onToggleVoiceBtnClicked();

    /**
     * 隐藏或显示扩展menu按钮点击点击事件
     */
    void onToggleExtendClicked();

    /**
     * 隐藏或显示表情栏按钮点击事件
     */
    void onToggleEmojiconClicked();

    /**
     * 文字输入框点击事件
     */
    void onEditTextClicked();
}
