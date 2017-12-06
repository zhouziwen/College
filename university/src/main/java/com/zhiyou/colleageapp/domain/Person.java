package com.zhiyou.colleageapp.domain;

import com.google.gson.annotations.SerializedName;
import com.zhiyou.colleageapp.constants.HttpKey;

/**
 * Create by LongWeiHu on 2016/6/7.
 * the base class of  human
 */
public class Person {
    @SerializedName(HttpKey.USER_NAME)
    protected String mUserName;
    @SerializedName(HttpKey.AVATAR)
    protected String mAvatar;

    public String getUsername() {
        return mUserName;
    }

    public void setUserName(String name) {
        mUserName = name;
    }

    public String getAvatar() {
        return mAvatar;
    }

    public void setAvatar(String Avatar) {
        this.mAvatar = Avatar;
    }
}
