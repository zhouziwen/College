package com.zhiyou.colleageapp.appui.life.adapter;

import android.content.Context;
import android.view.View;

import com.zhiyou.colleageapp.R;
import com.zhiyou.colleageapp.appui.CommonAdapter;
import com.zhiyou.colleageapp.appui.ViewHolder;
import com.zhiyou.colleageapp.appui.model.Taoyu;
import com.zhiyou.colleageapp.httpservice.UrlConstant;
import com.zhiyou.colleageapp.utils.AppLog;

/**
 * Created by chuyh on 2015/12/23.
 */
public class TaoyuAdapter extends CommonAdapter<Taoyu> {

    public TaoyuAdapter(Context context, int itemLayoutId, Callback callback) {
        super(context, itemLayoutId, callback);
    }

    @Override
    public void conver(ViewHolder holder, Taoyu taoyu) {
        holder.setText(R.id.taoyu_item_nick, taoyu.getUserName())
                .setText(R.id.taoyu_item_time, taoyu.getTime())
                .setText(R.id.life_common_zannum, taoyu.getAppoint() + "")
                .setText(R.id.life_common_commentnum, taoyu.getComment() + "");
        if (taoyu.getSchool() != null)
            holder.setText(R.id.taoyu_item_school, taoyu.getSchool());
        holder.setClick(R.id.life_common_zan, this);

        if (taoyu.getDes() != null)
            holder.setText(R.id.taoyu_item_des, taoyu.getDes());
        else holder.setText(R.id.taoyu_item_des, "暂无评论");

        if (taoyu.getPrice() != null)
            holder.setText(R.id.taoyu_item_price, taoyu.getPrice());
        else holder.setText(R.id.taoyu_item_price, "0.00");

        if (!"".equals(taoyu.getThumb()[0])) {
            holder.setVisiable(R.id.taoyu_item_imgs, View.VISIBLE);
            holder.setVisiable(R.id.taoyu_item_img2, View.INVISIBLE);
            holder.setVisiable(R.id.taoyu_item_img3, View.INVISIBLE);
            if (taoyu.getThumb().length == 3) {
                holder.loadImage(R.id.taoyu_item_img3, mContext, UrlConstant.BASE + taoyu.getThumb()[2], R.mipmap.loading);
                holder.loadImage(R.id.taoyu_item_img2, mContext, UrlConstant.BASE + taoyu.getThumb()[1], R.mipmap.loading);
                holder.setVisiable(R.id.taoyu_item_img2, View.VISIBLE);
                holder.setVisiable(R.id.taoyu_item_img3, View.VISIBLE);
            }
            if (taoyu.getThumb().length == 2) {
                holder.loadImage(R.id.taoyu_item_img2, mContext, UrlConstant.BASE + taoyu.getThumb()[1], R.mipmap.loading);
                holder.setVisiable(R.id.taoyu_item_img2, View.VISIBLE);
            }
            holder.loadImage(R.id.taoyu_item_img1, mContext, UrlConstant.BASE + taoyu.getThumb()[0], R.mipmap.loading);
        } else {
            holder.setVisiable(R.id.taoyu_item_imgs, View.GONE);
        }
    }
}
