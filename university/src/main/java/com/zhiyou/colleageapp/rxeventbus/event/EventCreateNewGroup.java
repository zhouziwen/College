package com.zhiyou.colleageapp.rxeventbus.event;

import com.zhiyou.colleageapp.domain.FriendGroup;

/**
 * Create by LongWeiHu on 2016/5/25.
 */
public class EventCreateNewGroup extends EventBase{
    private FriendGroup mGroup;

    public FriendGroup getGroup() {
        return mGroup;
    }

    public void setGroup(FriendGroup group) {
        mGroup = group;
    }
}
