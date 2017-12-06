package com.zhiyou.colleageapp.appui.model;

import com.google.gson.annotations.SerializedName;
import com.zhiyou.colleageapp.constants.LocalKey;

/**
 * Created by Administrator on 2016/5/28.
 */
public class SocietyNews {
    @SerializedName(LocalKey.SOCIETY_NEWS_ID)
    private int mNewsId;
    @SerializedName(LocalKey.SOCIETY_NEWS_TITLE)
    private String mNewsTitle;
    @SerializedName(LocalKey.SOCIETY_NEWS_ADDTIME)
    private String mNewsAddTime;
    @SerializedName(LocalKey.SOCIETY_NEWS_THUMB)
    private String[] mNewsThumb;
    @SerializedName(LocalKey.SOCIETY_NEWS_TYPE)
    private String mNewsType;
    @SerializedName(LocalKey.SOCIETY_NEWS_DETAIL)
    private String mNewsDetail;
    @SerializedName(LocalKey.SOCIETY_NEWS_THUMB_AD)
    private String mNewsThumbAd;

    public String getNewsThumbAd() {
        return mNewsThumbAd;
    }

    public void setNewsThumbAd(String newsThumbAd) {
        mNewsThumbAd = newsThumbAd;
    }

    public int getNewsId() {
        return mNewsId;
    }

    public void setNewsId(int newsId) {
        mNewsId = newsId;
    }

    public String getNewsTitle() {
        return mNewsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        mNewsTitle = newsTitle;
    }

    public String getNewsAddTime() {
        return mNewsAddTime;
    }

    public void setNewsAddTime(String newsAddTime) {
        mNewsAddTime = newsAddTime;
    }

    public String[] getNewsThumb() {
        return mNewsThumb;
    }

    public void setNewsThumb(String[] newsThumb) {
        mNewsThumb = newsThumb;
    }

    public String getNewsType() {
        return mNewsType;
    }

    public void setNewsType(String newsType) {
        mNewsType = newsType;
    }

    public String getNewsDetail() {
        return mNewsDetail;
    }

    public void setNewsDetail(String newsDetail) {
        mNewsDetail = newsDetail;
    }
}
