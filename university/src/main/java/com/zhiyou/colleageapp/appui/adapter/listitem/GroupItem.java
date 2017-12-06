package com.zhiyou.colleageapp.appui.adapter.listitem;
import com.zhiyou.colleageapp.domain.FriendGroup;
/**
 * Author by LongWei Hu on 2016/5/25.
 */
public class GroupItem {
    private FriendGroup mGroup;
    private String mGroupType;

    public FriendGroup getGroup() {
        return mGroup;
    }

    public void setGroup(FriendGroup group) {
        mGroup = group;
    }

    public String getGroupType() {
        return mGroupType;
    }

    public void setGroupType(String groupType) {
        mGroupType = groupType;
    }
}
