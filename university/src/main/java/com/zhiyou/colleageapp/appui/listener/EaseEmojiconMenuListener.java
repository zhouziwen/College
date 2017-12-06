package com.zhiyou.colleageapp.appui.listener;

import com.zhiyou.colleageapp.domain.EaseEmoIcon;

/**
 * Author ： LongWeiHu on 2016/5/19.
 */
public interface EaseEmojiconMenuListener {
    /**
     * 表情被点击
     * @param emojicon
     */
    void onExpressionClicked(EaseEmoIcon emojicon);
    /**
     * 删除按钮被点击
     */
    void onDeleteImageClicked();
}
