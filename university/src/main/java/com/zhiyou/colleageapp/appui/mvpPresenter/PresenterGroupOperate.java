package com.zhiyou.colleageapp.appui.mvpPresenter;

import com.zhiyou.colleageapp.appui.mvpMode.ModelGroupOperate;
import com.zhiyou.colleageapp.appui.mvpMode.action.IActSuccess;
import com.zhiyou.colleageapp.appui.mvpView.ViewBase;
import com.zhiyou.colleageapp.appui.mvpView.ViewSuccess;
import com.zhiyou.colleageapp.domain.FriendGroup;
import com.zhiyou.colleageapp.domain.Person;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by LongWeiHu on 2016/5/27.
 */
public class PresenterGroupOperate extends PresenterBase {
    
    private ModelGroupOperate mModelGroupOperate;
    public PresenterGroupOperate(ViewBase viewBase) {
        super(viewBase);
        mModelGroupOperate = new ModelGroupOperate(new MyActions());
    }

    public void actChangeGroupName(String groupId, String newName, final ViewSuccess<Integer> viewSuccess) {
        mModelGroupOperate.actChangeGroupName(groupId, newName, new IActSuccess<Integer>() {
            @Override
            public void onActSuccess(Integer textId) {
                viewSuccess.onSuccess(textId);
            }
        });
    }

    public void actAddMembers(String groupOwner, String currentUser, String groupId, List<String> newMembers, final ViewSuccess<Integer> viewSuccess) {
        mModelGroupOperate.actAddMembers(groupOwner, currentUser, groupId, newMembers, new IActSuccess<Integer>() {
            @Override
            public void onActSuccess(Integer textId) {
                viewSuccess.onSuccess(textId);
            }
        });
    }

    public void actQuitGroup(String groupId,String who ,final ViewSuccess<Integer> viewSuccess) {
        mModelGroupOperate.actQuitGroup(groupId, who,new IActSuccess<Integer>() {
            @Override
            public void onActSuccess(Integer textId) {
                viewSuccess.onSuccess(textId);
            }
        });
    }

    public void actDismissGroup(String groupId, final ViewSuccess<Integer> viewSuccess) {
        mModelGroupOperate.actDismissGroup(groupId, new IActSuccess<Integer>() {
            @Override
            public void onActSuccess(Integer textId) {
                viewSuccess.onSuccess(textId);
            }
        });
    }

    public void actClearGroupHistoryRecord(String groupId, final ViewSuccess<Integer> viewSuccess) {
        mModelGroupOperate.actClearGroupHistoryRecord(groupId, new IActSuccess<Integer>() {
            @Override
            public void onActSuccess(Integer textId) {
                viewSuccess.onSuccess(textId);
            }
        });
    }

    public void actBlockGroup(String groupId, final ViewSuccess<Integer> viewSuccess) {
        mModelGroupOperate.actBlockGroup(groupId, new IActSuccess<Integer>() {
            @Override
            public void onActSuccess(Integer textId) {
                viewSuccess.onSuccess(textId);
            }
        });
    }

    public void actUnBlockGroup(String groupId, final ViewSuccess<Integer> viewSuccess) {
        mModelGroupOperate.actUnBlockGroup(groupId, new IActSuccess<Integer>() {
            @Override
            public void onActSuccess(Integer textId) {
                viewSuccess.onSuccess(textId);
            }
        });
    }


    public void actAddMemberToBlackList(String groupId, String who, final ViewSuccess<Integer> viewSuccess) {
        mModelGroupOperate.actAddMemberToBlackList(groupId, who, new IActSuccess<Integer>() {
            @Override
            public void onActSuccess(Integer textId) {
                viewSuccess.onSuccess(textId);
            }
        });
    }

    public void actUpdateGroup(String groupId, final ViewSuccess<Integer> viewSuccess) {
        mModelGroupOperate.actUpdateGroup(groupId, new IActSuccess<Integer>() {
            @Override
            public void onActSuccess(Integer textId) {
                viewSuccess.onSuccess(textId);
            }
        });
    }

    public void createFriendGroup(String groupName, String groupDesc, boolean isPublic, boolean isNeedApproval, 
                                  ArrayList<String> selectedUserNames, final ViewSuccess<FriendGroup> viewSuccess) {
        mModelGroupOperate.createFriendGroup(groupName, groupDesc, isPublic, isNeedApproval, selectedUserNames, new IActSuccess<FriendGroup>() {
            @Override
            public void onActSuccess(FriendGroup newGroup) {
                viewSuccess.onSuccess(newGroup);
            }
        });
    }


    public void loadGroupMembers(String groupId, final ViewSuccess<List<Person>>viewSuccess) {
        mModelGroupOperate.loadGroupMembers(groupId, new IActSuccess<List<Person>>() {
            @Override
            public void onActSuccess(List<Person> persons) {
                viewSuccess.onSuccess(persons);
            }
        });
    }


    public void deleteGroupMember() {

    }
    @Override
    public void releaseAll() {
        mModelGroupOperate.releaseAll();
    }



}
