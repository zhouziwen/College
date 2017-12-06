package com.zhiyou.colleageapp.domain;

/**
 * Author ： LongWeiHu on 2016/5/16.
 * 群组类型
 *
 */
public class GroupType extends BaseEntity {

    public GroupType(String id,String name) {
        mId = id;
        mName = name;
    }

    private String mId;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }
}
