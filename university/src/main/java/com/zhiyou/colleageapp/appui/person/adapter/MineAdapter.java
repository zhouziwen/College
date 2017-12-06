package com.zhiyou.colleageapp.appui.person.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhiyou.colleageapp.R;
import com.zhiyou.colleageapp.appui.person.MineItem;

import java.util.List;

/**
 * Created by zhang on 16/5/13.
 *
 */
public class MineAdapter extends BaseAdapter {
    private LayoutInflater mLayoutInflater;
    private List<MineItem> mSettingItemList;

    public MineAdapter(Context context, List<MineItem> settingItemList) {
        mLayoutInflater = LayoutInflater.from(context);
        mSettingItemList = settingItemList;
    }

    @Override
    public int getCount() {
        return mSettingItemList == null ? 0 : mSettingItemList.size();
    }

    @Override
    public MineItem getItem(int position) {
        return mSettingItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        final MineItem settingItem = mSettingItemList.get(position);
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.list_item_person_normal, parent, false);
            holder = new ViewHolder();
            holder.img = ((ImageView) convertView.findViewById(R.id.img_icon_person_item));
            holder.mame = ((TextView) convertView.findViewById(R.id.txt_name_person_item));
            holder.divider = convertView.findViewById(R.id.view_divider_person_item);
            holder.space = convertView.findViewById(R.id.mine_space);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.img.setImageResource(settingItem.getImgResId());
        holder.mame.setText(settingItem.getName());
        switch (settingItem.getImgResId()) {
            case R.drawable.mine_dynamic:
                holder.divider.setVisibility(View.GONE);
                holder.space.setVisibility(View.VISIBLE);
                break;
            case R.drawable.mine_task:
                holder.divider.setVisibility(View.GONE);
                holder.space.setVisibility(View.VISIBLE);
            case R.drawable.mine_setting:
                holder.space.setVisibility(View.VISIBLE);
                holder.divider.setVisibility(View.GONE);
                break;
            default:
                holder.space.setVisibility(View.GONE);
                holder.divider.setVisibility(View.VISIBLE);
                break;
        }

        return convertView;
    }

    public void append(MineItem item) {
        mSettingItemList.add(item);
        notifyDataSetChanged();
    }

    private static class ViewHolder {
        private ImageView img;
        private TextView mame;
        private View divider;
        private View space;
    }


}
