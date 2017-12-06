package com.zhiyou.colleageapp.appui.listener;

import com.zhiyou.colleageapp.domain.User;

/**
 * Author ： LongWeiHu on 2016/5/19.
 */
public interface EaseContactListItemClickListener {
    /**
     * 联系人listview item点击事件
     * @param user 被点击item所对应的user对象
     */
    void onListItemClicked(User user);
}
