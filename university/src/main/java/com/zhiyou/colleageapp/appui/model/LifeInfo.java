package com.zhiyou.colleageapp.appui.model;

import com.google.gson.annotations.SerializedName;
import com.zhiyou.colleageapp.constants.LocalKey;

import java.util.List;

/**
 * Created by chuyh on 2016/5/20 0020.
 */
public class LifeInfo {

    @SerializedName(LocalKey.LIFE_BANNERS)
    private List<LifeBanner> mBanners;
    @SerializedName(LocalKey.LIFE_ACTIVITYS)
    private List<SchoolActivity> mActivities;

    public List<LifeBanner> getBanners() {
        return mBanners;
    }

    public void setBanners(List<LifeBanner> banners) {
        mBanners = banners;
    }

    public List<SchoolActivity> getActivities() {
        return mActivities;
    }

    public void setActivities(List<SchoolActivity> activities) {
        mActivities = activities;
    }
}
