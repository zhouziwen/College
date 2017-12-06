package com.zhiyou.colleageapp.domain;

import com.zhiyou.colleageapp.eenum.ResourceType;

import java.util.List;

/**
 * Author ： LongWeiHu on 2016/5/16.
 */
public class Dynamic  {
    private UserNearby mSender;
    private String mContent;
    private String mSrcUrl;
    private long mPriseCount;
    private long mCommentCount;
    private ResourceType mResourceType;
    /**
     * 测试数据字段，用户头像resId
     */
    private int mUserIcon;
    /**
     * 测试字段，里面是用户发表的动态中的图片resId
     */
    private List<Integer> mPicResIdList;

    public UserNearby getSender() {
        return mSender;
    }

    public void setSender(UserNearby sender) {
        mSender = sender;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public String getSrcUrl() {
        return mSrcUrl;
    }

    public void setSrcUrl(String srcUrl) {
        mSrcUrl = srcUrl;
    }


    public long getPriseCount() {
        return mPriseCount;
    }

    public void setPriseCount(long priseCount) {
        mPriseCount = priseCount;
    }

    public long getCommentCount() {
        return mCommentCount;
    }

    public void setCommentCount(long commentCount) {
        mCommentCount = commentCount;
    }

    public int getUserIcon() {
        return mUserIcon;
    }

    public void setUserIcon(int userIcon) {
        mUserIcon = userIcon;
    }

    public List<Integer> getPicResIdList() {
        return mPicResIdList;
    }

    public void setPicResIdList(List<Integer> picResIdList) {
        mPicResIdList = picResIdList;
    }

    public ResourceType getResourceType() {
        return mResourceType;
    }

    public void setResourceType(ResourceType resourceType) {
        mResourceType = resourceType;
    }


}
