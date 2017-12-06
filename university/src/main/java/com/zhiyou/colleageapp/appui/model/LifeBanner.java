package com.zhiyou.colleageapp.appui.model;

import com.google.gson.annotations.SerializedName;
import com.zhiyou.colleageapp.constants.LocalKey;

/**
 * Created by chuyh on 2016/5/20 0020.
 */
public class LifeBanner {

    @SerializedName(LocalKey.LIFE_BANNER_IMGURL)
    private String mImageUrl;

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }
}
