package com.zhiyou.colleageapp.appui.listener;

import android.widget.CompoundButton;

/**
 * Created by Administrator on 2016/6/9.
 * 回调listItem中的checkable view
 */
public interface CheckedChangedCallback {
    void onCheckedChanged(CompoundButton buttonView, boolean newState,int position);
}
