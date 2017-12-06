package com.zhiyou.colleageapp.appui.adapter.listitem;
import com.zhiyou.colleageapp.domain.BaseEntity;
/**
 * Author ： LongWeiHu on 2016/5/13.
 *院校概况，校园风采部分的listItem
 */
public class SchoolInnerListItem extends BaseEntity {
    /**
     * 图片资源的id
     */
    private int mImgResId;
    /**
     * 背景资源的id
     */
    private int mBgResId;

    public int getImgResId() {
        return mImgResId;
    }

    public void setImgResId(int imgResId) {
        mImgResId = imgResId;
    }

    public int getBgResId() {
        return mBgResId;
    }

    public void setBgResId(int bgResId) {
        mBgResId = bgResId;
    }
}
