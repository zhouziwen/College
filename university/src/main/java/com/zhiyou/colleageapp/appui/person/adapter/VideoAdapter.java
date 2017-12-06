package com.zhiyou.colleageapp.appui.person.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhiyou.colleageapp.R;
import com.zhiyou.colleageapp.appui.model.TreeHole;
import com.zhiyou.colleageapp.utils.DisplayUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chuyh on 2015/12/23.
 */
public class VideoAdapter extends BaseAdapter implements View.OnClickListener {
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

    public VideoAdapter(Context context, Callback callback) {
        list = new ArrayList();
        list.add(1);
        list.add(1);
        list.add(1);
        list.add(1);
        list.add(1);
        list.add(1);
        layoutInflater = LayoutInflater.from(context);
        mCallback = callback;
        mContext = context;
    }

    public void bindData(List data) {
        if (data != null) {
            this.list = data;
            this.notifyDataSetChanged();
        }
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
            convertView = layoutInflater.inflate(R.layout.love_item_video, null);
            holder.zan = (RelativeLayout) convertView.findViewById(R.id.life_common_zan);
            holder.comment = (LinearLayout) convertView.findViewById(R.id.life_common_comment);
            holder.avar = (ImageView) convertView.findViewById(R.id.love_item_avar);
            convertView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, DisplayUtils.getHeightPx() - 80 - (int) (mContext.getResources().getDimension(R.dimen.title_bar_height))));
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.zan.setOnClickListener(this);
        holder.zan.setTag(position);
        holder.comment.setOnClickListener(this);
        holder.comment.setTag(position);
        holder.avar.setOnClickListener(this);
        holder.avar.setTag(position);
        return convertView;
    }

    @Override
    public void onClick(View view) {
        mCallback.click(view);
    }

    private static class ViewHolder {
        RelativeLayout zan;
        LinearLayout comment;
        ImageView avar;
    }
}
