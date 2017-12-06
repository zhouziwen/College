package com.zhiyou.colleageapp.appui.person.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.zhiyou.colleageapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chuyh on 2016/6/4.
 */
public class LoveCardAdapter extends RecyclerView.Adapter<LoveCardAdapter.ViewHolder> implements View.OnClickListener {

    public List mList;
    private LayoutInflater mInflater;
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


    public LoveCardAdapter(Context context, List data) {
        this.mContext = context;
        if (data != null)
            this.mList = data;
        else {
            this.mList = new ArrayList();
        }
        this.mInflater = LayoutInflater.from(context);
    }

    public void bindData(List datas) {
        if (datas != null) {
            this.mList.clear();
            this.mList = datas;
        }
    }

    public void addData(List datas) {
        if (datas != null) {
            this.mList.addAll(datas);
        }
    }

    //创建新View，被LayoutManager所调用
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_card_love, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mItemView.setOnClickListener(this);
        holder.mItemView.setTag(position);
        holder.zan.setOnClickListener(this);
        holder.zan.setTag(position);
        holder.iv.setImageResource((Integer) mList.get(position));
    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v, (int) v.getTag());
        }
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout zan;
        ImageView iv;
        View mItemView;

        public ViewHolder(View itemView) {
            super(itemView);
            mItemView = itemView;
            iv = (ImageView) itemView.findViewById(R.id.item_card_love_iv);
            zan = (RelativeLayout) itemView.findViewById(R.id.life_common_zan);
        }
    }

}
