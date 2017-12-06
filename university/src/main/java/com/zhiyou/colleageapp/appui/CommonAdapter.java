package com.zhiyou.colleageapp.appui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chuyh on 2016/6/6.
 */
public abstract class CommonAdapter<T> extends BaseAdapter implements View.OnClickListener {
    protected List<T> mList;
    protected LayoutInflater mInflater;
    protected int mItemLayoutId;
    protected Callback mCallback;
    protected Context mContext;

    /**
     * 自定义接口，用于回调按钮点击事件到Activity
     *
     * @author chuyh
     *         2014-11-26
     */
    public interface Callback {
        void click(View v);
    }

    public CommonAdapter(Context context, int itemLayoutId) {
        this(context, itemLayoutId, null, null);
    }

    public CommonAdapter(Context context, int itemLayoutId, Callback callback) {
        this(context, itemLayoutId, null, callback);
    }

    public CommonAdapter(Context context, int itemLayoutId, List<T> datas, Callback callback) {
        this.mContext = context;
        this.mItemLayoutId = itemLayoutId;
        this.mInflater = LayoutInflater.from(context);
        if (datas != null) {
            this.mList = datas;
        } else {
            this.mList = new ArrayList<>();
        }
        if (callback != null) {
            this.mCallback = callback;
        }
    }

    public void bindData(List<T> datas) {
        if (datas != null) {
            this.mList = datas;
            this.notifyDataSetChanged();
        }
    }

    public void addData(List<T> datas) {
        if (datas != null) {
            this.mList.clear();
            this.mList.addAll(datas);
            this.notifyDataSetChanged();
        }
    }

    public void cancleCallback() {
        this.mCallback = null;
    }

    @Override
    public void onClick(View v) {
        if (mCallback != null)
            mCallback.click(v);
    }

    @Override
    public int getCount() {
        return this.mList.size();
    }

    @Override
    public T getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = ViewHolder.get(mContext, convertView, parent, mItemLayoutId, position);
        conver(holder, getItem(position));
        return holder.getConvertView();
    }

    public abstract void conver(ViewHolder holder, T t);
}
