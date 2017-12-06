package com.zhiyou.colleageapp.appui.life;

import android.content.Context;

import com.zhiyou.colleageapp.R;
import com.zhiyou.colleageapp.appui.CommonAdapter;
import com.zhiyou.colleageapp.appui.ViewHolder;
import com.zhiyou.colleageapp.appui.model.SchoolActivity;

/**
 * Created by chuyh on 2015/12/23.
 */
public class LifeAdapter extends CommonAdapter<SchoolActivity> {

    public LifeAdapter(Context context, int itemLayoutId, Callback callback) {
        super(context, itemLayoutId, callback);
    }

    @Override
    public void conver(ViewHolder holder, SchoolActivity schoolActivity) {
        holder.setClick(R.id.life_common_zan, this);
        holder.setClick(R.id.life_common_share, this);

        holder.setText(R.id.life_item_title, schoolActivity.getTitle());
        holder.setText(R.id.life_common_nick, schoolActivity.getNickName());
        holder.setText(R.id.life_common_time, schoolActivity.getAddTime());
        holder.setText(R.id.life_common_zannum, schoolActivity.getAppoint() + "");
        holder.setText(R.id.life_common_commentnum, schoolActivity.getCommentCount() + "");
        holder.setText(R.id.life_item_title, schoolActivity.getTitle());

        holder.loadImage(R.id.life_item_bigiv, mContext, schoolActivity.getThumb(), R.mipmap.life_beautiful_girl);
    }
}
