package com.zhiyou.colleageapp.domain;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.zhiyou.colleageapp.constants.HttpKey;

/**
 * Created by Administrator on 2016/5/25.
 *
 */
public class FriendGroup implements Parcelable{
    @SerializedName(HttpKey.GROUP_ID)
    private String mId;
    @SerializedName(HttpKey.GROUP_NAME)
    private String mName;
    @SerializedName(HttpKey.AVATAR)
    private String mAvatar;
    @SerializedName(HttpKey.GROUP_OWNER)
    private String mOwner;
    @SerializedName(HttpKey.GROUP_IS_NEED_APPROVAL)
    private boolean mIsAllowInvites;

    /**
     * 是否被屏蔽了
     */
    private boolean mIsShield;

    public FriendGroup() {

    }

    protected FriendGroup(Parcel in) {
        mId = in.readString();
        mName = in.readString();
        mAvatar = in.readString();
        mOwner = in.readString();
        mIsAllowInvites = in.readByte() != 0;
        mIsShield = in.readByte() != 0;
    }

    public static final Creator<FriendGroup> CREATOR = new Creator<FriendGroup>() {
        @Override
        public FriendGroup createFromParcel(Parcel in) {
            return new FriendGroup(in);
        }

        @Override
        public FriendGroup[] newArray(int size) {
            return new FriendGroup[size];
        }
    };

    public boolean isAllowInvites() {
        return mIsAllowInvites;
    }

    public void setAllowInvites(boolean allowInvites) {
        mIsAllowInvites = allowInvites;
    }

    public String getAvatar() {
        return mAvatar;
    }

    public void setAvatar(String avatar) {
        mAvatar = avatar;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getOwner() {
        return mOwner;
    }

    public void setOwner(String owner) {
        mOwner = owner;
    }

    public boolean isShield() {
        return mIsShield;
    }

    public void setShield(boolean shield) {
        mIsShield = shield;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mName);
        dest.writeString(mAvatar);
        dest.writeString(mOwner);
        dest.writeByte((byte) (mIsAllowInvites ? 1 : 0));
        dest.writeByte((byte) (mIsShield ? 1 : 0));
    }
}
