package com.zhiyou.colleageapp.appui.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.zhiyou.colleageapp.constants.LocalKey;

/**
 * Created by chuyh on 2016/5/20 0020.
 */
public class LifeActivity implements Parcelable {

    @SerializedName(LocalKey.LIFE_ACTIVITY_ID)
    private int mId;
    @SerializedName(LocalKey.LIFE_ACTIVITY_NAME)
    private String mName;
    @SerializedName(LocalKey.LIFE_ACTIVITY_ADDTIME)
    private String mAddTime;
    @SerializedName(LocalKey.LIFE_ACTIVITY_DES)
    private String mDes;
    @SerializedName(LocalKey.LIFE_ACTIVITY_PUBLISHER)
    private int mPublisher;
    @SerializedName(LocalKey.LIFE_ACTIVITY_APPOINT)
    private int mAppoint;
    @SerializedName(LocalKey.LIFE_ACTIVITY_COMMENT)
    private int mComment;
    @SerializedName(LocalKey.LIFE_ACTIVITY_IMGURL)
    private String mImgurl;
    @SerializedName(LocalKey.LIFE_ACTIVITY_MANAGER_ID)
    private int mManagerId;
    @SerializedName(LocalKey.LIFE_ACTIVITY_MANAGER_NAME)
    private String mManagerName;
    @SerializedName(LocalKey.LIFE_ACTIVITY_MANAGER_PASSWORD)
    private String mManagerPassword;
    @SerializedName(LocalKey.LIFE_ACTIVITY_MANAGER_GROUPID)
    private int mManagerGroupid;
    @SerializedName(LocalKey.LIFE_ACTIVITY_MANAGER_SALT)
    private String mManagerSalt;
    @SerializedName(LocalKey.LIFE_ACTIVITY_MANAGER_HEADEURL)
    private String mManagerHeadeUrl;
    @SerializedName(LocalKey.LIFE_ACTIVITY_MANAGER_NICKNAME)
    private String mManagerNickname;

    protected LifeActivity(Parcel in) {
        mId = in.readInt();
        mName = in.readString();
        mAddTime = in.readString();
        mDes = in.readString();
        mPublisher = in.readInt();
        mAppoint = in.readInt();
        mComment = in.readInt();
        mImgurl = in.readString();
        mManagerId = in.readInt();
        mManagerName = in.readString();
        mManagerPassword = in.readString();
        mManagerGroupid = in.readInt();
        mManagerSalt = in.readString();
        mManagerHeadeUrl = in.readString();
        mManagerNickname = in.readString();
    }

    public LifeActivity() {

    }

    public static final Creator<LifeActivity> CREATOR = new Creator<LifeActivity>() {
        @Override
        public LifeActivity createFromParcel(Parcel in) {
            return new LifeActivity(in);
        }

        @Override
        public LifeActivity[] newArray(int size) {
            return new LifeActivity[size];
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

    public String getAddTime() {
        return mAddTime;
    }

    public void setAddTime(String addTime) {
        mAddTime = addTime;
    }

    public String getDes() {
        return mDes;
    }

    public void setDes(String des) {
        mDes = des;
    }

    public int getPublisher() {
        return mPublisher;
    }

    public void setPublisher(int publisher) {
        mPublisher = publisher;
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

    public String getImgurl() {
        return mImgurl;
    }

    public void setImgurl(String imgurl) {
        mImgurl = imgurl;
    }

    public int getManagerId() {
        return mManagerId;
    }

    public void setManagerId(int managerId) {
        mManagerId = managerId;
    }

    public String getManagerName() {
        return mManagerName;
    }

    public void setManagerName(String managerName) {
        mManagerName = managerName;
    }

    public String getManagerPassword() {
        return mManagerPassword;
    }

    public void setManagerPassword(String managerPassword) {
        mManagerPassword = managerPassword;
    }

    public int getManagerGroupid() {
        return mManagerGroupid;
    }

    public void setManagerGroupid(int managerGroupid) {
        mManagerGroupid = managerGroupid;
    }

    public String getManagerSalt() {
        return mManagerSalt;
    }

    public void setManagerSalt(String managerSalt) {
        mManagerSalt = managerSalt;
    }

    public String getManagerHeadeUrl() {
        return mManagerHeadeUrl;
    }

    public void setManagerHeadeUrl(String managerHeadeUrl) {
        mManagerHeadeUrl = managerHeadeUrl;
    }

    public String getManagerNickname() {
        return mManagerNickname;
    }

    public void setManagerNickname(String managerNickname) {
        mManagerNickname = managerNickname;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mName);
        dest.writeString(mAddTime);
        dest.writeString(mDes);
        dest.writeInt(mPublisher);
        dest.writeInt(mAppoint);
        dest.writeInt(mComment);
        dest.writeString(mImgurl);
        dest.writeInt(mManagerId);
        dest.writeString(mManagerName);
        dest.writeString(mManagerPassword);
        dest.writeInt(mManagerGroupid);
        dest.writeString(mManagerSalt);
        dest.writeString(mManagerHeadeUrl);
        dest.writeString(mManagerNickname);
    }
}
