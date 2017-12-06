package com.zhiyou.colleageapp.appui.model;

import com.google.gson.annotations.SerializedName;
import com.zhiyou.colleageapp.constants.LocalKey;

/**
 * Created by chuyh on 2016/5/20 0020.
 */
public class SchoolSlider {

    @SerializedName(LocalKey.SCHOOL_SLIDER_ID)
    private int mSliderId;
    @SerializedName(LocalKey.SCHOOL_SLIDER_TITLE)
    private String mSliderTitle;
    @SerializedName(LocalKey.SCHOOL_SLIDER_URL)
    private String mSliderUrl;
    @SerializedName(LocalKey.SCHOOL_SLIDER_URLAB)
    private String mSliderUrlAb;

    public int getSliderId() {
        return mSliderId;
    }

    public void setSliderId(int sliderId) {
        mSliderId = sliderId;
    }

    public String getSliderTitle() {
        return mSliderTitle;
    }

    public void setSliderTitle(String sliderTitle) {
        mSliderTitle = sliderTitle;
    }

    public String getSliderUrl() {
        return mSliderUrl;
    }

    public void setSliderUrl(String sliderUrl) {
        mSliderUrl = sliderUrl;
    }

    public String getSliderUrlAb() {
        return mSliderUrlAb;
    }

    public void setSliderUrlAb(String sliderUrlAb) {
        mSliderUrlAb = sliderUrlAb;
    }
}
