package com.zhiyou.colleageapp.appui.adapter;

import android.content.Context;
import android.widget.CompoundButton;

import com.zhiyou.colleageapp.R;
import com.zhiyou.colleageapp.appui.adapter.viewholder.UniversalViewHolder;
import com.zhiyou.colleageapp.domain.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by LongWeiHu on 2016.6.9
 * 联系人选择的adapter
 */
public class ContactSelectAdapter extends ContactBaseAdapter {

    public ContactSelectAdapter(Context context, int layoutId, List<User> dataList) {
        super(context, layoutId, dataList);
    }

    public ContactSelectAdapter(Context context, int layoutId) {
        super(context, layoutId);
    }

    @Override
    public void setViewListener(UniversalViewHolder holder) {
        holder.setCheckedChangeListener(R.id.list_item_contact_check);
        holder.setVisible(R.id.list_item_contact_check, true);
    }

    @Override
    public void onCheckedChangedCallback(CompoundButton v, boolean isChecked, int position) {
        if (v.getId() == R.id.list_item_contact_check) {
            if (isChecked) {
                v.setButtonDrawable(R.mipmap.add_user_check);
                addSelect(position);
            } else {
                v.setButtonDrawable(R.mipmap.add_user_off);
                removeSelect(position);
            }
        }
    }

    @Override
    public void convert(UniversalViewHolder holder, User user) {
        super.convert(holder, user);
    }

    public List<String> getSelectUserNameList() {
        List<User> selectUsers = getSelectedUser();
        if (selectUsers == null || selectUsers.isEmpty()) {
            return new ArrayList<>();
        }

        List<String> nameList = new ArrayList<>();
        for (User user : selectUsers
                ) {
            nameList.add(user.getUsername());
        }

        return nameList;
    }

}
