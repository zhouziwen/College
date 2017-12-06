package com.zhiyou.colleageapp.domain;

/**
 * Author ： LongWeiHu on 2016/5/15.
 */
public class UserNearby extends User {

    /**
     * 动态距离
     */
    private double mDistance;
    /**
     * 动态时间戳
     */
    private long mTime;
    /**
     * 属于什么学校，为了兼顾社会人员，所以命名为 mBelong
     */
    private String mBelong;

    public UserNearby(String userName) {
        super(userName);
    }

    public UserNearby() {
    }

    public double getDistance() {
        return mDistance;
    }

    public void setDistance(double distance) {
        mDistance = distance;
    }

    public long getTime() {
        return mTime;
    }

    public void setTime(long time) {
        mTime = time;
    }

    public String getBelong() {
        return mBelong;
    }

    public void setBelong(String belong) {
        mBelong = belong;
    }
}
