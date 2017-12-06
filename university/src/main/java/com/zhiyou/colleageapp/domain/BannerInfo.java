package com.zhiyou.colleageapp.domain;

/**
 * Created by LongWeiHu on 2016/5/12.
 *
 */
public class BannerInfo extends BaseEntity{
    private int mResId;
    private String mUrl;

    public int getResId() {
        return mResId;
    }

    public void setResId(int resId) {
        mResId = resId;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }
}
