package com.zhiyou.colleageapp.appui.life.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.zhiyou.colleageapp.R;
import com.zhiyou.colleageapp.appui.CommonAdapter;
import com.zhiyou.colleageapp.appui.ViewHolder;
import com.zhiyou.colleageapp.appui.model.BlankModel;

import java.util.List;

/**
 * Created by chuyh on 2015/12/23.
 */
public class VoteAdapter extends CommonAdapter<BlankModel> {
    private Boolean isVote;
    private int choice;

    public VoteAdapter(Context context, int itemLayoutId, boolean isVote, int choice, List<BlankModel> datas, Callback callback) {
        super(context, itemLayoutId, datas, callback);
        this.isVote = isVote;
        this.choice = choice;
        this.mContext = context;

    }

    public void setVote(int choice) {
        this.isVote = true;
        this.choice = choice;
        notifyDataSetChanged();
    }

    @Override
    public void conver(ViewHolder holder, BlankModel blankModel) {
        //如果已经投票显示进度条以及投票人数
        if (isVote) {
            //进度条以及人数layout
            holder.setVisiable(R.id.vote_item_bottom_lay2, View.VISIBLE);
            if (choice == holder.getPosition())
                holder.setImgDrawable(R.id.vote_item_choice, ContextCompat.getDrawable(mContext, R.mipmap.life_play_select_shape4));
            else
                holder.setVisiable(R.id.vote_item_choice, View.VISIBLE);
        }

        holder.setClick(R.id.vote_item_bottom_lay, this);
    }
}
