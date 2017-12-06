package com.zhiyou.colleageapp.appui.model;

import com.google.gson.annotations.SerializedName;
import com.zhiyou.colleageapp.constants.LocalKey;

/**
 * Created by Administrator on 2016/5/28.
 */
public class SchoolMajor {
    @SerializedName(LocalKey.MAJOR_NAME)
    private String mMajorName;
    @SerializedName(LocalKey.MAJOR_ADD_TIME)
    private String mMajorAddTime;
    @SerializedName(LocalKey.MAJOR_DETAIL)
    private String mMajorDetail;

    public String getMajorName() {
        return mMajorName;
    }

    public void setMajorName(String majorName) {
        mMajorName = majorName;
    }

    public String getMajorAddTime() {
        return mMajorAddTime;
    }

    public void setMajorAddTime(String majorAddTime) {
        mMajorAddTime = majorAddTime;
    }

    public String getMajorDetail() {
        return mMajorDetail;
    }

    public void setMajorDetail(String majorDetail) {
        mMajorDetail = majorDetail;
    }
}
