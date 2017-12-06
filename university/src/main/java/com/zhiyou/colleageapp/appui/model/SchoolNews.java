package com.zhiyou.colleageapp.appui.model;

import com.google.gson.annotations.SerializedName;
import com.zhiyou.colleageapp.constants.LocalKey;

/**
 * Created by Administrator on 2016/5/28.
 */
public class SchoolNews {
    @SerializedName(LocalKey.SCHOOL_NEWS_TITLE)
    private int mNewsTitle;
    @SerializedName(LocalKey.SCHOOL_NEWS_ADD_TIME)
    private String mNewsAddTime;
    @SerializedName(LocalKey.SCHOOL_NEWS_THUMB)
    private String mNewsThumb;
    @SerializedName(LocalKey.SCHOOL_NEWS_THUMB_AD)
    private String mNewsThumbAd;
    @SerializedName(LocalKey.SCHOOL_NEWS_DETAIL)
    private String mNewsDetail;

}
