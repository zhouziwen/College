package com.zhiyou.colleageapp.appui.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.zhiyou.colleageapp.constants.LocalKey;

/**
 * Created by chuyh on 2016/6/1.
 */
public class PartTime {
    @SerializedName(LocalKey.OFFER_TYPE)
    private String mType;
    @SerializedName(LocalKey.OFFER_ID)
    private String mID;
    @SerializedName(LocalKey.OFFER_TITLE)
    private String mTitle;
    @SerializedName(LocalKey.OFFER_DATE)
    private String mDate;
    @SerializedName(LocalKey.OFFER_SALARY)
    private String mSalary;
    @SerializedName(LocalKey.OFFER_MAP)
    private String mMap;
    @SerializedName(LocalKey.OFFER_DAY)
    private String mDay;

    @SerializedName(LocalKey.OFFER_TIME)
    private String mTime;
    @SerializedName(LocalKey.OFFER_PEOPLE)
    private int mPeople;
    @SerializedName(LocalKey.OFFER_ENDTIME)
    private String mEndTime;
    @SerializedName(LocalKey.OFFER_CONTENT)
    private String mContent;
    @SerializedName(LocalKey.OFFER_ADDTIME)
    private String mAddtime;
    @SerializedName(LocalKey.OFFER_NAME)
    private String mName;
    @SerializedName(LocalKey.OFFER_TEL)
    private String mTel;

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public String getID() {
        return mID;
    }

    public void setID(String ID) {
        mID = ID;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public String getSalary() {
        return mSalary;
    }

    public void setSalary(String salary) {
        mSalary = salary;
    }

    public String getMap() {
        return mMap;
    }

    public void setMap(String map) {
        mMap = map;
    }

    public String getDay() {
        return mDay;
    }

    public void setDay(String day) {
        mDay = day;
    }

    public String getTime() {
        return mTime;
    }

    public void setTime(String time) {
        mTime = time;
    }

    public int getPeople() {
        return mPeople;
    }

    public void setPeople(int people) {
        mPeople = people;
    }

    public String getEndTime() {
        return mEndTime;
    }

    public void setEndTime(String endTime) {
        mEndTime = endTime;
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

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getTel() {
        return mTel;
    }

    public void setTel(String tel) {
        mTel = tel;
    }
}
