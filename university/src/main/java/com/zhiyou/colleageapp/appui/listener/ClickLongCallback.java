package com.zhiyou.colleageapp.appui.listener;

import android.view.View;

import com.zhiyou.colleageapp.appui.adapter.viewholder.UniversalViewHolder;

/**
 * Create by LongWeiHu on 2016/6/8.
 */
public interface ClickLongCallback {
    boolean onLongClick(View v, int position, UniversalViewHolder holder);
}
