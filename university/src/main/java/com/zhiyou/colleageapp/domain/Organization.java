package com.zhiyou.colleageapp.domain;

import com.zhiyou.colleageapp.eenum.ResourceType;

/**
 * Author ： LongWeiHu on 2016/5/16.
 * 组织类，群组属于一个组织，方便以后扩展
 */
public class Organization extends BaseEntity{
    private String mId;
    private String mSummary;
    private String mAddress;
    private ResourceType mResourceType;

    public ResourceType getResourceType() {
        return mResourceType;
    }

    public void setResourceType(ResourceType resourceType) {
        mResourceType = resourceType;
    }
    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }


    public String getSummary() {
        return mSummary;
    }

    public void setSummary(String summary) {
        mSummary = summary;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        mAddress = address;
    }
}
