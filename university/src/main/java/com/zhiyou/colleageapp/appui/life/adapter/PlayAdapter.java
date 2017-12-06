package com.zhiyou.colleageapp.appui.life.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhiyou.colleageapp.R;
import com.zhiyou.colleageapp.appui.widget.listview.HorizontalInterceptFalseListView;
import com.zhiyou.colleageapp.appui.widget.listview.HorizontalListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chuyh on 2015/12/23.
 */
public class PlayAdapter extends BaseAdapter implements View.OnClickListener {
    private List list;
    private LayoutInflater layoutInflater;
    private Callback mCallback;
    private HorizontalInterceptFalseListView groupListView;
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

    public PlayAdapter(Context context, Callback callback) {
        list = new ArrayList();
        list.add(1);
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
        if (position == 0) {
            convertView = layoutInflater.inflate(R.layout.life_play_item, null);

            ImageView avar = (ImageView) convertView.findViewById(R.id.life_common_avar);
            avar.setOnClickListener(this);
            avar.setTag(position);

            RelativeLayout zan = (RelativeLayout) convertView.findViewById(R.id.life_common_zan);
            zan.setOnClickListener(this);
            zan.setTag(position);

            ImageView share = (ImageView) convertView.findViewById(R.id.life_common_share);
            share.setOnClickListener(this);
            share.setTag(position);
        } else {
            convertView = layoutInflater.inflate(R.layout.life_play_item_stu, null);

            ImageView avar = (ImageView) convertView.findViewById(R.id.sh_group_item_group_icon);
            avar.setOnClickListener(this);
            avar.setTag(position);

            TextView btAdd = (TextView) convertView.findViewById(R.id.sh_group_item_add);
            btAdd.setOnClickListener(this);
            btAdd.setTag(position);

            groupListView = (HorizontalInterceptFalseListView) convertView.findViewById(R.id.sh_group_item_group_member_listView);
            groupListView.setAdapter(new GroupMemberAdapter(mContext));
        }

        LinearLayout lay = (LinearLayout) convertView.findViewById(R.id.life_play_item_lay);
        lay.setOnClickListener(this);
        lay.setTag(position);

        RelativeLayout more = (RelativeLayout) convertView.findViewById(R.id.life_play_item_more);
        more.setOnClickListener(this);
        more.setTag(position);
        return convertView;
    }

    @Override
    public void onClick(View view) {
        mCallback.click(view);
    }
}
