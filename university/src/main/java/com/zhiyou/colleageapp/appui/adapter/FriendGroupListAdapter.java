package com.zhiyou.colleageapp.appui.adapter;

import android.content.Context;
import android.text.Spannable;
import android.view.View;
import android.view.ViewGroup;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.zhiyou.colleageapp.R;
import com.zhiyou.colleageapp.appui.adapter.listitem.GroupItem;
import com.zhiyou.colleageapp.appui.adapter.viewholder.UniversalViewHolder;
import com.zhiyou.colleageapp.appui.widget.CircleImageView;
import com.zhiyou.colleageapp.appui.widget.listview.StickyListHeadersAdapter;
import com.zhiyou.colleageapp.constants.EaseConstant;
import com.zhiyou.colleageapp.constants.HttpKey;
import com.zhiyou.colleageapp.domain.FriendGroup;
import com.zhiyou.colleageapp.utils.EaseCommonUtils;
import com.zhiyou.colleageapp.utils.EaseSmileUtils;
import com.zhiyou.colleageapp.utils.EaseUserUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Create by LongWeiHu on 2016/5/25.
 */
public class FriendGroupListAdapter extends UniversalBaseAdapter<GroupItem> implements StickyListHeadersAdapter {

    private int mAddGroupCount, mManageGroupCount;
    public static final String MANAGE_BY_ME = "MANAGE_BY_ME";
    public static final String ADD_BY_ME = "ADD_BY_ME";

    public FriendGroupListAdapter(Context context, int layoutId) {
        super(context, layoutId);
    }

    public void updateData(Map<String, List<FriendGroup>> dataMap) {
        mDataList.clear();
        if (dataMap == null || dataMap.isEmpty()) {
            notifyDataSetChanged();
            return;
        }

        List<GroupItem> list = new ArrayList<>();
        List<FriendGroup> joinGroupList = dataMap.get(HttpKey.GROUP_JOIN);
        if (joinGroupList != null && !joinGroupList.isEmpty()) {
            for (FriendGroup joinGroup : joinGroupList) {
                GroupItem item = new GroupItem();
                item.setGroupType(FriendGroupListAdapter.ADD_BY_ME);
                item.setGroup(joinGroup);
                list.add(item);
            }
            mAddGroupCount = joinGroupList.size();
        }

        List<FriendGroup> manageGroupList = dataMap.get(HttpKey.GROUP_SELF);
        if (manageGroupList != null && !manageGroupList.isEmpty()) {
            for (FriendGroup createGroup : manageGroupList) {
                GroupItem item = new GroupItem();
                item.setGroupType(FriendGroupListAdapter.MANAGE_BY_ME);
                item.setGroup(createGroup);
                list.add(item);
            }
            mManageGroupCount = manageGroupList.size();
        }
        updateData(list);
        notifyDataSetChanged();
    }


    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        UniversalViewHolder headerHolder = UniversalViewHolder.getViewHolder(mInflater, convertView, parent, R.layout.list_item_sticky_header, position);
        final GroupItem item = getItem(position);
        if (item != null) {
            String groupType = item.getGroupType();
            if (MANAGE_BY_ME.equals(groupType)) {
                headerHolder.setText(R.id.list_item_header, String.format(Locale.getDefault(), "我管理的群( %d )", mManageGroupCount));
            } else {
                headerHolder.setText(R.id.list_item_header, String.format(Locale.getDefault(), "我加入的群( %d )", mAddGroupCount));
            }
        }
        return headerHolder.getConvertView();
    }

    @Override
    public long getHeaderId(int position) {
        final GroupItem item = getItem(position);
        if (item != null) {
            return item.getGroupType().charAt(0);
        }
        return -1;
    }

    @Override
    public void convert(UniversalViewHolder holder, GroupItem item) {
        if (item == null) {
            return;
        }
        FriendGroup group = item.getGroup();
        if (group == null) {
            holder.setImageResource(R.id.list_item_group_avatar, R.drawable.group_icon);
            holder.setText(R.id.list_item_group_name, "");
            return;
        }

        EaseUserUtils.setAvatar(holder.getConvertView().getContext(), group.getAvatar(), (CircleImageView) holder.getView(R.id.list_item_group_avatar), R.drawable.group_icon);
        holder.setText(R.id.list_item_group_name, group.getName());
        String groupId = group.getId();
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(groupId, EaseCommonUtils.getConversationType(EaseConstant.CHATTYPE_GROUP), true);
        if (conversation != null && conversation.getAllMsgCount() != 0) {
            String msgDigest = EaseCommonUtils.getMessageDigest(conversation.getLastMessage(), holder.getConvertView().getContext());
            Spannable msgContent = EaseSmileUtils.getSmiledText(holder.getConvertView().getContext(), msgDigest);
            holder.setText(R.id.list_item_group_last_msg, msgContent);
        } else {
            holder.setText(R.id.list_item_group_last_msg, "");
        }
    }

}
