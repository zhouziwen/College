package com.zhiyou.colleageapp.appui.life.adapter;

import android.content.Context;

import com.zhiyou.colleageapp.R;
import com.zhiyou.colleageapp.appui.CommonAdapter;
import com.zhiyou.colleageapp.appui.ViewHolder;
import com.zhiyou.colleageapp.appui.model.SchoolActivity;

/**
 * Created by chuyh on 2015/12/23.
 */
public class SchoolActAdapter extends CommonAdapter<SchoolActivity> {

    public SchoolActAdapter(Context context, int itemLayoutId, Callback callback) {
        super(context, itemLayoutId, callback);
    }

    @Override
    public void conver(ViewHolder holder, SchoolActivity schoolActivity) {
        holder.setText(R.id.life_play_item_title, schoolActivity.getTitle())
                .setText(R.id.life_play_item_desc, schoolActivity.getDes())
                .setText(R.id.life_common_time, schoolActivity.getAddTime())
                .setText(R.id.life_common_nick, schoolActivity.getNickName())
                .setText(R.id.life_common_zannum, schoolActivity.getCommentCount() + "")
                .setText(R.id.life_common_commentnum, schoolActivity.getAppoint() + "");
        holder.setClick(R.id.life_common_zan, this);
        holder.setClick(R.id.life_common_avar, this);
        holder.setClick(R.id.life_common_share, this);

        holder.loadImage(R.id.life_play_item_bigiv, mContext, schoolActivity.getThumb(), R.mipmap.life_beautiful_girl);
    }
}
