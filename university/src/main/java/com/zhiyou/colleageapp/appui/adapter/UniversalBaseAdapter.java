package com.zhiyou.colleageapp.appui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.zhiyou.colleageapp.appui.adapter.viewholder.UniversalViewHolder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Create By LongWeiHu on 2016.6.8
 *
 * @param <T>
 */
public abstract class UniversalBaseAdapter<T> extends BaseAdapter {
    protected List<T> mDataList = new ArrayList<>();
    protected LayoutInflater mInflater;
    protected int mLayoutId;
    protected Context mContext;

    public UniversalBaseAdapter(Context context, int layoutId, List<T> dataList) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        this.mLayoutId = layoutId;
        if (dataList != null) {
            mDataList.addAll(dataList);
        } else {
            mDataList.clear();
        }
    }

    public UniversalBaseAdapter(Context context, int layoutId) {
        this(context, layoutId, null);
    }

    public UniversalBaseAdapter(Context context) {
        mContext = context;
    }

    public void updateData(Collection<T> newData) {
        mDataList.clear();
        if (newData != null) {
            mDataList.addAll(newData);
        }
        notifyDataSetChanged();
    }

    protected void append(T data) {
        mDataList.add(data);
        notifyDataSetChanged();
    }

    protected void append(List<T> dataList) {
        if (dataList == null) {
            return;
        }
        mDataList.addAll(dataList);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public T getItem(int position) {
        if (position < mDataList.size()) {
            return mDataList.get(position);
        }

        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        UniversalViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(mLayoutId, parent, false);
            holder = setViewHolder(convertView, parent);
            convertView.setTag(holder);
        } else {
            holder = (UniversalViewHolder) convertView.getTag();
        }

        holder.setPosition(position);
        setConvert(holder, holder.getPosition());
        return convertView;
    }


    public abstract void convert(UniversalViewHolder holder, T t);

    protected UniversalViewHolder setViewHolder(View convertView, ViewGroup parent) {
        return new UniversalViewHolder(convertView, null, null, null);
    }

    protected void setConvert(UniversalViewHolder holder, int position) {
        convert(holder, getItem(position));
    }

}
