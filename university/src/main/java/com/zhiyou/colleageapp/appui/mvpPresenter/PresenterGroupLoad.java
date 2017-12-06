package com.zhiyou.colleageapp.appui.mvpPresenter;

import com.zhiyou.colleageapp.appui.mvpMode.action.IActSuccess;
import com.zhiyou.colleageapp.appui.mvpMode.ModeGroupLoad;
import com.zhiyou.colleageapp.appui.mvpView.ViewBase;
import com.zhiyou.colleageapp.appui.mvpView.ViewSuccess;
import com.zhiyou.colleageapp.domain.FriendGroup;
import com.zhiyou.colleageapp.domain.Person;

import java.util.List;
import java.util.Map;

/**
 * Create by LongWeiHu on 2016/5/30.
 */
public class PresenterGroupLoad extends PresenterBase {

    private ModeGroupLoad mModeGroup;
    public PresenterGroupLoad(ViewBase viewBase) {
        super(viewBase);
        mModeGroup = new ModeGroupLoad(new MyActions());
    }

    public void loadGroupList(final ViewSuccess<Map<String,List<FriendGroup>>> mapViewSuccess) {
        mModeGroup.loadGroupList(new IActSuccess<Map<String, List<FriendGroup>>>() {
            @Override
            public void onActSuccess(Map<String, List<FriendGroup>> friendGroupMap) {
                mapViewSuccess.onSuccess(friendGroupMap);
            }
        });
    }


    public void search(String searchText, final ViewSuccess<List<FriendGroup>> viewSuccess) {
        // TODO:By LongWeiHu 2016/5/30
        mModeGroup.search(searchText, new IActSuccess< List<FriendGroup>>() {
            @Override
            public void onActSuccess( List<FriendGroup> friendGroups) {
                viewSuccess.onSuccess(friendGroups);
            }
        });
    }

    public void loadGroupMembers(String groupId, final ViewSuccess<List<Person>> actSuccess) {
        mModeGroup.loadGroupMembers(groupId, new IActSuccess<List<Person>>() {
            @Override
            public void onActSuccess(List<Person> persons) {
                actSuccess.onSuccess(persons);
            }
        });
    }


    @Override
    public void releaseAll() {
        mModeGroup.releaseAll();
    }


}
