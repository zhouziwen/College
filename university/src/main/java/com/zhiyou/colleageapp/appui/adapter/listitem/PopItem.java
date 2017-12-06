package com.zhiyou.colleageapp.appui.adapter.listitem;
import android.text.TextUtils;
import com.zhiyou.colleageapp.utils.ResUtils;
/**
 * Author by LongWei Hu on 2016/5/24.
 */
public class PopItem {
    private String mContent;
    private int mImgResId;
    private int mContentTextId;

    public PopItem() {
    }

    public PopItem(String content, int imgResId) {
        mContent = content;
        mImgResId = imgResId;
    }

    public PopItem(int contentTextId) {
        mContentTextId = contentTextId;
    }

    public String getContent() {
        if (TextUtils.isEmpty(mContent)) {
            if (mContentTextId > 0) {
                mContent  = ResUtils.getString(mContentTextId);
            }
        }
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public int getImgResId() {
        return mImgResId;
    }

    public void setImgResId(int imgResId) {
        mImgResId = imgResId;
    }

    public int getContentTextId() {
        return mContentTextId;
    }

    public void setContentTextId(int contentTextId) {
        mContentTextId = contentTextId;
    }
}
