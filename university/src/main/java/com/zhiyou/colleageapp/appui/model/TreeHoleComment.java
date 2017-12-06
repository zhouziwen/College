package com.zhiyou.colleageapp.appui.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.zhiyou.colleageapp.constants.LocalKey;

/**
 * Created by chuyh on 2016/6/1.
 */
public class TreeHoleComment {
    @SerializedName(LocalKey.TREE_COMMENT_NAME)
    private String mName;
    @SerializedName(LocalKey.TREE_COMMENT_AVAR)
    private String mAvar;
    @SerializedName(LocalKey.TREE_COMMENT_TRUENAME)
    private String mTrueName;
    @SerializedName(LocalKey.TREE_COMMENT_CONTENT)
    private String mContent;
    @SerializedName(LocalKey.TREE_COMMENT_ADDTIME)
    private String mAddtime;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getAvar() {
        return mAvar;
    }

    public void setAvar(String avar) {
        mAvar = avar;
    }

    public String getTrueName() {
        return mTrueName;
    }

    public void setTrueName(String trueName) {
        mTrueName = trueName;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public String getAddtime() {
        return mAddtime;
    }

    public void setAddtime(String addtime) {
        mAddtime = addtime;
    }
}
