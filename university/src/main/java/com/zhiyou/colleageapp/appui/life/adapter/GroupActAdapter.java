package com.zhiyou.colleageapp.appui.life.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhiyou.colleageapp.R;
import com.zhiyou.colleageapp.appui.widget.listview.HorizontalInterceptFalseListView;
import com.zhiyou.colleageapp.appui.widget.listview.HorizontalListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chuyh on 2015/12/23.
 */
public class GroupActAdapter extends BaseAdapter implements View.OnClickListener {
    private List list;
    private LayoutInflater layoutInflater;
    private Callback mCallback;
    private Context mContext;

    /**
     * 自定义接口，用于回调按钮点击事件到Activity
     *
     * @author chuyh
     *         2014-11-26
     */
    public interface Callback {
        void click(View v);
    }

    public GroupActAdapter(Context context, Callback callback) {
        list = new ArrayList();
        list.add(1);
        list.add(2);
        list.add(2);
        list.add(2);
        list.add(2);
        list.add(2);
        list.add(2);
        layoutInflater = LayoutInflater.from(context);
        mCallback = callback;
        mContext = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.group_act_item, null);
            holder.groupListView = (HorizontalInterceptFalseListView) convertView.findViewById(R.id.sh_group_item_group_member_listView);
            holder.add = (TextView) convertView.findViewById(R.id.sh_group_item_add);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.add.setOnClickListener(this);
        holder.add.setTag(position);

        holder.groupListView = (HorizontalInterceptFalseListView) convertView.findViewById(R.id.sh_group_item_group_member_listView);
        holder.groupListView.setAdapter(new GroupMemberAdapter(mContext));
        return convertView;
    }

    private static class ViewHolder {
        HorizontalInterceptFalseListView groupListView;
        TextView add;
    }

    @Override
    public void onClick(View view) {
        mCallback.click(view);
    }
}
