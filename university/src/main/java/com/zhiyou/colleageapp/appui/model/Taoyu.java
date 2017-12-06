package com.zhiyou.colleageapp.appui.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.zhiyou.colleageapp.constants.LocalKey;

import java.util.Arrays;

/**
 * Created by chuyh on 2016/6/1.
 */
public class Taoyu implements Parcelable {
    @SerializedName(LocalKey.TAOYU_LIST_ID)
    private int mId;
    @SerializedName(LocalKey.TAOYU_LIST_NAME)
    private String mName;
    @SerializedName(LocalKey.TAOYU_LIST_PRICE)
    private String mPrice;
    @SerializedName(LocalKey.TAOYU_LIST_THUMB)
    private String[] mThumb;
    @SerializedName(LocalKey.TAOYU_LIST_SHOW)
    private String mShow;
    @SerializedName(LocalKey.TAOYU_LIST_APPOINT)
    private int mAppoint;
    @SerializedName(LocalKey.TAOYU_LIST_COMMENT)
    private int mComment;
    @SerializedName(LocalKey.TAOYU_LIST_STATUS)
    private String mStatus;
    @SerializedName(LocalKey.TAOYU_LIST_DES)
    private String mDes;
    @SerializedName(LocalKey.TAOYU_LIST_TIME)
    private String mTime;
    @SerializedName(LocalKey.TAOYU_LIST_USERNAME)
    private String mUserName;
    @SerializedName(LocalKey.TAOYU_LIST_USERID)
    private int mUserid;
    @SerializedName(LocalKey.TAOYU_LIST_SCHOOL)
    private String mSchool;

    protected Taoyu(Parcel in) {
        mId = in.readInt();
        mName = in.readString();
        mPrice = in.readString();
        mThumb = in.createStringArray();
        mShow = in.readString();
        mAppoint = in.readInt();
        mComment = in.readInt();
        mStatus = in.readString();
        mDes = in.readString();
        mTime = in.readString();
        mUserName = in.readString();
        mUserid = in.readInt();
        mSchool = in.readString();
    }

    public static final Creator<Taoyu> CREATOR = new Creator<Taoyu>() {
        @Override
        public Taoyu createFromParcel(Parcel in) {
            return new Taoyu(in);
        }

        @Override
        public Taoyu[] newArray(int size) {
            return new Taoyu[size];
        }
    };

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getPrice() {
        return mPrice;
    }

    public void setPrice(String price) {
        mPrice = price;
    }

    public String[] getThumb() {
        return mThumb;
    }

    public void setThumb(String[] thumb) {
        mThumb = thumb;
    }

    public String getShow() {
        return mShow;
    }

    public void setShow(String show) {
        mShow = show;
    }

    public int getAppoint() {
        return mAppoint;
    }

    public void setAppoint(int appoint) {
        mAppoint = appoint;
    }

    public int getComment() {
        return mComment;
    }

    public void setComment(int comment) {
        mComment = comment;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

    public String getDes() {
        return mDes;
    }

    public void setDes(String des) {
        mDes = des;
    }

    public String getTime() {
        return mTime;
    }

    public void setTime(String time) {
        mTime = time;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    public int getUserid() {
        return mUserid;
    }

    public void setUserid(int userid) {
        mUserid = userid;
    }

    public String getSchool() {
        return mSchool;
    }

    public void setSchool(String school) {
        mSchool = school;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mName);
        dest.writeString(mPrice);
        dest.writeStringArray(mThumb);
        dest.writeString(mShow);
        dest.writeInt(mAppoint);
        dest.writeInt(mComment);
        dest.writeString(mStatus);
        dest.writeString(mDes);
        dest.writeString(mTime);
        dest.writeString(mUserName);
        dest.writeInt(mUserid);
        dest.writeString(mSchool);
    }

    @Override
    public String toString() {
        return "Taoyu{" +
                "mId=" + mId +
                ", mName='" + mName + '\'' +
                ", mPrice='" + mPrice + '\'' +
                ", mThumb=" + Arrays.toString(mThumb) +
                ", mShow='" + mShow + '\'' +
                ", mAppoint=" + mAppoint +
                ", mComment=" + mComment +
                ", mStatus='" + mStatus + '\'' +
                ", mDes='" + mDes + '\'' +
                ", mTime='" + mTime + '\'' +
                ", mUserName='" + mUserName + '\'' +
                ", mUserid=" + mUserid +
                ", mSchool='" + mSchool + '\'' +
                '}';
    }
}
