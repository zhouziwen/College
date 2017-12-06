package com.zhiyou.colleageapp.appui.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.zhiyou.colleageapp.constants.LocalKey;

/**
 * Created by chuyh on 2016/6/1.
 */
public class SchoolActivity {
    @SerializedName(LocalKey.SCHOOL_ACTIVITY_ID)
    private int mId;
    @SerializedName(LocalKey.SCHOOL_ACTIVITY_APPOINT)
    private int mAppoint;
    @SerializedName(LocalKey.SCHOOL_ACTIVITY_TITLE)
    private String mTitle;
    @SerializedName(LocalKey.SCHOOL_ACTIVITY_DES)
    private String mDes;
    @SerializedName(LocalKey.SCHOOL_ACTIVITY_COUNT)
    private int mCommentCount;
    @SerializedName(LocalKey.SCHOOL_ACTIVITY_ADDTIME)
    private String mAddTime;
    @SerializedName(LocalKey.SCHOOL_ACTIVITY_THUMB)
    private String mThumb;
    @SerializedName(LocalKey.SCHOOL_ACTIVITY_HEAD_URL)
    private String mHeadUrl;
    @SerializedName(LocalKey.SCHOOL_ACTIVITY_NICKNAME)
    private String mNickName;
    @SerializedName(LocalKey.SCHOOL_ACTIVITY_TYPE)
    private String mType;

    @SerializedName(LocalKey.SCHOOL_ACTIVITY_VOTE)
    private int mVote;
    @SerializedName(LocalKey.SCHOOL_ACTIVITY_ISMULT)
    private int mIsMult;
    @SerializedName(LocalKey.SCHOOL_ACTIVITY_IMGURL)
    private String[] mImgUrl;


    protected SchoolActivity(Parcel in) {
        mId = in.readInt();
        mAppoint = in.readInt();
        mTitle = in.readString();
        mDes = in.readString();
        mCommentCount = in.readInt();
        mAddTime = in.readString();
        mThumb = in.readString();
        mHeadUrl = in.readString();
        mNickName = in.readString();
        mType = in.readString();
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public int getAppoint() {
        return mAppoint;
    }

    public void setAppoint(int appoint) {
        mAppoint = appoint;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getDes() {
        return mDes;
    }

    public void setDes(String des) {
        mDes = des;
    }

    public int getCommentCount() {
        return mCommentCount;
    }

    public void setCommentCount(int commentCount) {
        mCommentCount = commentCount;
    }

    public String getAddTime() {
        return mAddTime;
    }

    public void setAddTime(String addTime) {
        mAddTime = addTime;
    }

    public String getThumb() {
        return mThumb;
    }

    public void setThumb(String thumb) {
        mThumb = thumb;
    }

    public String getHeadUrl() {
        return mHeadUrl;
    }

    public void setHeadUrl(String headUrl) {
        mHeadUrl = headUrl;
    }

    public String getNickName() {
        return mNickName;
    }

    public void setNickName(String nickName) {
        mNickName = nickName;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public int getVote() {
        return mVote;
    }

    public void setVote(int vote) {
        mVote = vote;
    }

    public int getIsMult() {
        return mIsMult;
    }

    public void setIsMult(int isMult) {
        mIsMult = isMult;
    }

    public String[] getImgUrl() {
        return mImgUrl;
    }

    public void setImgUrl(String[] imgUrl) {
        mImgUrl = imgUrl;
    }
}
