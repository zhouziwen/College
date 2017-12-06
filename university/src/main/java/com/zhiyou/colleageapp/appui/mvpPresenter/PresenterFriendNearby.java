package com.zhiyou.colleageapp.appui.mvpPresenter;

import com.zhiyou.colleageapp.appui.mvpMode.action.IActSuccess;
import com.zhiyou.colleageapp.appui.mvpMode.ModeFriendNearby;
import com.zhiyou.colleageapp.appui.mvpView.ViewBase;
import com.zhiyou.colleageapp.appui.mvpView.ViewSuccess;
import com.zhiyou.colleageapp.domain.UserNearby;

import java.util.List;

/**朋友-群组tab-附近的人的presenter
 * Create by LongWeiHu on 2016/6/2.
 */
public class PresenterFriendNearby extends PresenterBase{

    private ModeFriendNearby mModeFriendNearby;
    public PresenterFriendNearby(ViewBase viewBase) {
        super(viewBase);
        mModeFriendNearby = new ModeFriendNearby(new MyActions());
    }


    public void loadFriendNearby(String type, final ViewSuccess<List<UserNearby>> viewSuccess) {
        mModeFriendNearby.loadFriendNearby(type, new IActSuccess<List<UserNearby>>() {
            @Override
            public void onActSuccess(List<UserNearby> userNearbyList) {
                viewSuccess.onSuccess(userNearbyList);
            }
        });
    }

    public void loadFriendRecommend(String type, final ViewSuccess<List<UserNearby>> viewSuccess) {
        mModeFriendNearby.loadFriendRecommend(type, new IActSuccess<List<UserNearby>>() {
            @Override
            public void onActSuccess(List<UserNearby> userNearbyList) {
                viewSuccess.onSuccess(userNearbyList);
            }
        });
    }

    @Override
    public void releaseAll() {
        mModeFriendNearby.releaseAll();
    }
}
