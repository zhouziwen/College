package com.zhiyou.colleageapp.domain;

import android.location.Location;

import com.zhiyou.colleageapp.eenum.ResourceType;

import java.util.List;

/**
 * Author ： LongWeiHu on 2016/5/16.
 * 校园tab下的组织，是一个 Organization
 */
public class SchoolGroup extends Organization {

    /**
     * 位置
     */
    private Location mLocation;
    private List<String> mGroupState;
    private List<User> mMembers;
    private long mFemaleCount;
    private long mTotalCount;
    private ResourceType mResourceType;
    /**
     * 测试字段，群组图片资源id，以后换成资源的url
     */
    private List<Integer> mPicResList;

    /**
     * 测试字段，组织的头像资源id，以后换成资源的url
     */
    private int mIconResId;





    public Location getLocation() {
        return mLocation;
    }

    public void setLocation(Location location) {
        mLocation = location;
    }

    public List<String> getGroupState() {
        return mGroupState;
    }

    public void setGroupState(List<String> groupState) {
        mGroupState = groupState;
    }

    public List<User> getMembers() {
        return mMembers;
    }

    public void setMembers(List<User> members) {
        mMembers = members;
    }

    public List<Integer> getPicResList() {
        return mPicResList;
    }

    public void setPicResList(List<Integer> picResList) {
        mPicResList = picResList;
    }

    public int getIconResId() {
        return mIconResId;
    }

    public void setIconResId(int iconResId) {
        mIconResId = iconResId;
    }

    public ResourceType getResourceType() {
        return mResourceType;
    }

    public void setResourceType(ResourceType resourceType) {
        mResourceType = resourceType;
    }

    public long getFemaleCount() {
        return mFemaleCount;
    }

    public void setFemaleCount(long femaleCount) {
        mFemaleCount = femaleCount;
    }

    public long getTotalCount() {
        return mTotalCount;
    }

    public void setTotalCount(long totalCount) {
        mTotalCount = totalCount;
    }

}
