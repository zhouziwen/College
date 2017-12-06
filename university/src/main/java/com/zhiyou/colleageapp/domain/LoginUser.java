package com.zhiyou.colleageapp.domain;

import com.google.gson.annotations.SerializedName;
import com.zhiyou.colleageapp.constants.HttpKey;

/**
 * Create by LongWeiHu on 2016/6/7.
 */
public class LoginUser {
    @SerializedName(HttpKey.USER_NAME)
    private String mUserName;
    @SerializedName(HttpKey.TOKEN)
    private String mToken;

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    public String getToken() {
        return mToken;
    }

    public void setToken(String token) {
        mToken = token;
    }
}
