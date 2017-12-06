package com.zhiyou.colleageapp.appui.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.zhiyou.colleageapp.constants.LocalKey;

/**
 * Created by chuyh on 2016/6/1.
 */
public class TaoyuCategory implements Parcelable{
    @SerializedName(LocalKey.TAOYU_CATE_ID)
    private int mId;
    @SerializedName(LocalKey.TAOYU_CATE_NAME)
    private String mName;
    @SerializedName(LocalKey.TAOYU_CATE_PID)
    private int mPid;
    @SerializedName(LocalKey.TAOYU_CATE_ADDTIME)
    private String mAddtime;

    protected TaoyuCategory(Parcel in) {
        mId = in.readInt();
        mName = in.readString();
        mPid = in.readInt();
        mAddtime = in.readString();
    }

    public static final Creator<TaoyuCategory> CREATOR = new Creator<TaoyuCategory>() {
        @Override
        public TaoyuCategory createFromParcel(Parcel in) {
            return new TaoyuCategory(in);
        }

        @Override
        public TaoyuCategory[] newArray(int size) {
            return new TaoyuCategory[size];
        }
    };

    public String getAddtime() {
        return mAddtime;
    }

    public void setAddtime(String addtime) {
        mAddtime = addtime;
    }

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

    public int getPid() {
        return mPid;
    }

    public void setPid(int pid) {
        mPid = pid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mName);
        dest.writeInt(mPid);
        dest.writeString(mAddtime);
    }
}
