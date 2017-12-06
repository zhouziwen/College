package com.zhiyou.colleageapp.appui.listener;

import com.zhiyou.colleageapp.domain.EaseEmoIcon;

/**
 * Author ： LongWeiHu on 2016/5/19.
 */
public interface EaseEmojiconPagerViewListener {
    /**
     * pagerview初始化完毕
     * @param groupMaxPageSize 最大表情组的page大小
     * @param firstGroupPageSize 第一组的page大小
     */
    void onPagerViewInited(int groupMaxPageSize, int firstGroupPageSize);

    /**
     * 表情组位置变动(从一组表情组移动另一组)
     * @param groupPosition 表情组位置
     * @param pagerSizeOfGroup 表情组里的pager的size
     */
    void onGroupPositionChanged(int groupPosition, int pagerSizeOfGroup);
    /**
     * 表情组内的page位置变动
     * @param oldPosition
     * @param newPosition
     */
    void onGroupInnerPagePostionChanged(int oldPosition, int newPosition);

    /**
     * 从别的表情组切过来的page位置变动
     * @param position
     */
    void onGroupPagePostionChangedTo(int position);

    /**
     * 表情组最大pager数变化
     * @param maxCount
     */
    void onGroupMaxPageSizeChanged(int maxCount);

    void onDeleteImageClicked();
    void onExpressionClicked(EaseEmoIcon emojicon);

}
