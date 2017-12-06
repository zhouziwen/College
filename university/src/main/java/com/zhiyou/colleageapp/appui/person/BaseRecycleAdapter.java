package com.zhiyou.colleageapp.appui.person;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chuyh on 2016/6/8.
 */
public abstract class BaseRecycleAdapter<T> extends RecyclerView.Adapter<BaseRecycleAdapter.ViewHolder> implements View.OnClickListener {

    protected List<T> mList;
    protected LayoutInflater mInflater;
    protected int mItemLayoutId;

    private OnRecyclerViewItemClickListener mOnItemClickListener;
    protected Context mContext;

    /**
     * 自定义接口，用于回调按钮点击事件
     *
     * @author chuyh
     *         2014-11-26
     */

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v, (int) v.getTag());
        }
    }

    public BaseRecycleAdapter(Context context, int itemLayoutId) {
        this(context, itemLayoutId, null);
    }

    public BaseRecycleAdapter(Context context, int itemLayoutId, List<T> datas) {
        this.mContext = context;
        this.mItemLayoutId = itemLayoutId;
        this.mInflater = LayoutInflater.from(context);
        if (datas != null) {
            this.mList = datas;
        } else {
            this.mList = new ArrayList<>();
        }
    }

    public void bindData(List<T> datas) {
        if (datas != null) {
            this.mList.clear();
            this.mList = datas;
        }
    }

    public void addData(List<T> datas) {
        if (datas != null) {
            this.mList.addAll(datas);
        }
    }

    public T getItem(int position) {
        return mList.get(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(mItemLayoutId, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mItemView.setOnClickListener(this);
        holder.mItemView.setTag(position);
        convert(holder, mList.get(position), position);
    }

    protected abstract void convert(ViewHolder holder, T t, int position);

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder<T> extends RecyclerView.ViewHolder {
        private SparseArray<View> mViews;
        private View mItemView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.mItemView = itemView;
            mViews = new SparseArray<>();
        }

        /**
         * 通过viewId获取控件
         */
        public <S extends View> S getView(int viewId) {
            View view = mViews.get(viewId);
            if (view == null) {
                view = mItemView.findViewById(viewId);
                mViews.put(viewId, view);
            }
            return (S) view;
        }

        public ViewHolder setClick(int viewId, View.OnClickListener listener, int position) {
            View view = getView(viewId);
            view.setOnClickListener(listener);
            view.setTag(position);
            return this;
        }

        public ViewHolder setText(int viewId, String text) {
            TextView view = getView(viewId);
            view.setText(text);
            return this;
        }

        public ViewHolder setVisiable(int viewId, int visiable) {
            getView(viewId).setVisibility(visiable);
            return this;
        }
    }
}
