package com.zhiyou.colleageapp.appui.mvpMode;

import com.zhiyou.colleageapp.appui.mvpMode.action.IActSuccess;
import com.zhiyou.colleageapp.appui.mvpMode.action.IActionBase;
import com.zhiyou.colleageapp.domain.UserNearby;

import java.util.List;

/**
 * Create by LongWeiHu on 2016/6/2.
 */
public class ModeFriendNearby extends ModeBase {


    public ModeFriendNearby(IActionBase iActionBase) {
        super(iActionBase);
    }

    public void loadFriendNearby(String type, IActSuccess<List<UserNearby>> nearbyCallback) {
        // TODO:By LongWeiHu 2016/6/2 服务器接口未提供
    }

    public void loadFriendRecommend(String type, IActSuccess<List<UserNearby>> recommendCallback) {
        // TODO:By LongWeiHu 2016/6/2  服务器接口未提供
    }
}
