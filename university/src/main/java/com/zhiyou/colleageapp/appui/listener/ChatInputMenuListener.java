package com.zhiyou.colleageapp.appui.listener;

import android.view.MotionEvent;
import android.view.View;

import com.zhiyou.colleageapp.domain.EaseEmoIcon;

/**
 * Author ： LongWeiHu on 2016/5/19.
 */
public interface ChatInputMenuListener {
    /**
     * 发送消息按钮点击
     *
     * @param content
     *            文本内容
     */
    void onSendMessage(String content);

    /**
     * 大表情被点击
     * @param emojicon
     */
    void onBigExpressionClicked(EaseEmoIcon emojicon);

    /**
     * 长按说话按钮touch事件
     * @param v
     * @param event
     * @return
     */
    boolean onPressToSpeakBtnTouch(View v, MotionEvent event);
}
