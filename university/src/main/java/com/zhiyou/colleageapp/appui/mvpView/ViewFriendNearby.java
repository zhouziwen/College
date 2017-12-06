package com.zhiyou.colleageapp.appui.mvpView;

import com.zhiyou.colleageapp.domain.UserNearby;

import java.util.List;

/**
 * Create by LongWeiHu on 2016/6/2.
 */
public interface ViewFriendNearby extends ViewBase {
    void onViewLoadNearbySuccess(List<UserNearby> userNearbyList);
    void onViewLoadRecommendSuccess(List<UserNearby> userRecommendList);

}
