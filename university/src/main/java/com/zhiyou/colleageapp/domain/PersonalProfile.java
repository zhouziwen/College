package com.zhiyou.colleageapp.domain;

import com.google.gson.annotations.SerializedName;
import com.zhiyou.colleageapp.constants.HttpKey;

/**
 * Create by LongWeiHu on 2016/6/17.
 * 个人资料
 */
public class PersonalProfile extends User {
    /**
     * 账号或手机号
     */
    private String mAccount;
    /**
     * 家乡
     */
    @SerializedName(HttpKey.HOME_TOWN)
    private String mHomeTown;
    /**
     * 院系
     */
    @SerializedName(HttpKey.DEPT)
    private String mDept;
    /**
     * 专业
     */
    @SerializedName(HttpKey.MAJOR)
    private String mMajor;
    /**
     * 学号
     */
    @SerializedName(HttpKey.STUDENT_NO)
    private String mStuNum;


    public String getAccount() {
        return mAccount;
    }

    public void setAccount(String account) {
        mAccount = account;
    }

    public String getHomeTown() {
        return mHomeTown;
    }

    public void setHomeTown(String homeTown) {
        mHomeTown = homeTown;
    }

    public String getDept() {
        return mDept;
    }

    public void setDept(String dept) {
        mDept = dept;
    }

    public String getMajor() {
        return mMajor;
    }

    public void setMajor(String major) {
        mMajor = major;
    }

    public String getStuNO() {
        return mStuNum;
    }

    public void setStuNum(String stuNum) {
        mStuNum = stuNum;
    }
}
