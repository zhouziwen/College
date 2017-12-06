package com.zhiyou.colleageapp.appui.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.zhiyou.colleageapp.constants.LocalKey;

/**
 * Created by chuyh on 2016/6/1.
 */
public class TreeHole implements Parcelable {
    @SerializedName(LocalKey.TREE_HOLE_ID)
    private int mId;
    @SerializedName(LocalKey.TREE_HOLE_TITLE)
    private String mTitle;
    @SerializedName(LocalKey.TREE_HOLE_AUTHOR)
    private String mAuthor;
    @SerializedName(LocalKey.TREE_HOLE_LOVECOUNT)
    private int mLoveCount;
    @SerializedName(LocalKey.TREE_HOLE_COMMENT)
    private int mComment;
    @SerializedName(LocalKey.TREE_HOLE_CID)
    private int mCid;

    protected TreeHole(Parcel in) {
        mId = in.readInt();
        mTitle = in.readString();
        mAuthor = in.readString();
        mLoveCount = in.readInt();
        mComment = in.readInt();
        mCid = in.readInt();
    }

    public static final Creator<TreeHole> CREATOR = new Creator<TreeHole>() {
        @Override
        public TreeHole createFromParcel(Parcel in) {
            return new TreeHole(in);
        }

        @Override
        public TreeHole[] newArray(int size) {
            return new TreeHole[size];
        }
    };

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String author) {
        mAuthor = author;
    }

    public int getLoveCount() {
        return mLoveCount;
    }

    public void setLoveCount(int loveCount) {
        mLoveCount = loveCount;
    }

    public int getComment() {
        return mComment;
    }

    public void setComment(int comment) {
        mComment = comment;
    }

    public int getCid() {
        return mCid;
    }

    public void setCid(int cid) {
        mCid = cid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mTitle);
        dest.writeString(mAuthor);
        dest.writeInt(mLoveCount);
        dest.writeInt(mComment);
        dest.writeInt(mCid);
    }
}
