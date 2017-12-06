package com.zhiyou.colleageapp.appui.mvpPresenter;

import com.zhiyou.colleageapp.appui.mvpMode.action.IActSuccess;
import com.zhiyou.colleageapp.appui.mvpMode.ModeContacts;
import com.zhiyou.colleageapp.appui.mvpView.ViewBase;
import com.zhiyou.colleageapp.appui.mvpView.ViewSuccess;
import com.zhiyou.colleageapp.domain.InviteMessage;
import com.zhiyou.colleageapp.domain.PersonalProfile;
import com.zhiyou.colleageapp.domain.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by LongWeiHu on 2016/6/1.
 */
public class PresenterContacts extends PresenterBase {

    private ModeContacts mModeContacts;

    public PresenterContacts(ViewBase viewBase) {
        super(viewBase);
        mModeContacts = new ModeContacts(new MyActions());
    }

    public PresenterContacts() {
        this(null);

    }

    public void deleteContact(String who, final ViewSuccess<Integer> viewSuccess) {
        mModeContacts.deleteContact(who, new IActSuccess<Integer>() {
            @Override
            public void onActSuccess(Integer textId) {
                viewSuccess.onSuccess(textId);
            }
        });
    }

    public void loadWhiteContactFromServer(final ViewSuccess<List<User>> viewLoadSuccess) {
        mModeContacts.loadWhiteContactFromServer(new IActSuccess<List<User>>() {
            @Override
            public void onActSuccess(List<User> users) {
                viewLoadSuccess.onSuccess(users);
            }
        });
    }

    public void loadWhiteContactFromLocal(final ViewSuccess<List<User>> viewLoadSuccess) {
        mModeContacts.loadWhiteContactFromLocal(new IActSuccess<List<User>>() {
            @Override
            public void onActSuccess(List<User> users) {
                viewLoadSuccess.onSuccess(users);
            }
        });
    }

    public void search(CharSequence searchText) {
        mModeContacts.search(searchText);
    }

    public void agreeInvitation(InviteMessage msg, final ViewSuccess<Integer> viewSuccess) {
        mModeContacts.agreeInvitation(msg, new IActSuccess<Integer>() {
            @Override
            public void onActSuccess(Integer textId) {
                viewSuccess.onSuccess(textId);
            }
        });
    }

    public void refuseInvitation(InviteMessage msg, final ViewSuccess<Integer> viewSuccess) {
        mModeContacts.refuseInvitation(msg, new IActSuccess<Integer>() {
            @Override
            public void onActSuccess(Integer textId) {
                viewSuccess.onSuccess(textId);
            }
        });
    }


    public void sendInvite(String who, String msg, final ViewSuccess<Integer> viewTextIdSuccess) {
        mModeContacts.sendInvite(who, msg, new IActSuccess<Integer>() {
            @Override
            public void onActSuccess(Integer textId) {
                viewTextIdSuccess.onSuccess(textId);
            }
        });
    }

    public void isContactExists(String userName, final ViewSuccess<User> viewSearchUserSuccess) {
        mModeContacts.isContactExists(userName, new IActSuccess<User>() {
            @Override
            public void onActSuccess(User user) {
                viewSearchUserSuccess.onSuccess(user);
            }
        });
    }


    public void loadBlackNameList(final ViewSuccess<List<User>> viewLoadBlackSuccess) {
        mModeContacts.loadBlackNameList(new IActSuccess<List<User>>() {
            @Override
            public void onActSuccess(List<User> users) {
                viewLoadBlackSuccess.onSuccess(users);
            }
        });
    }


    @Override
    public void releaseAll() {
        mModeContacts.releaseAll();
    }


    public void addContactToBlackList(String userName, ViewSuccess<Integer> viewSuccess) {
        List<String> userNames = new ArrayList<>();
        userNames.add(userName);
        addContactToBlackList(userNames,viewSuccess);
    }

    public void addContactToBlackList(List<String> userNames,final ViewSuccess<Integer> viewSuccess) {
        mModeContacts.moveContactsToBlackList(userNames, new IActSuccess<Integer>() {
            @Override
            public void onActSuccess(Integer textId) {
                viewSuccess.onSuccess(textId);
            }
        });
    }


    public void getPersonalProfile(final ViewSuccess<PersonalProfile> viewSuccess) {
        getContactProfile(null,viewSuccess);
    }


    public void getContactProfile(String contactName, final ViewSuccess<PersonalProfile> viewSuccess) {
        mModeContacts.getContactProfile(contactName, new IActSuccess<PersonalProfile>() {
            @Override
            public void onActSuccess(PersonalProfile personalProfile) {
                viewSuccess.onSuccess(personalProfile);
            }
        });
    }
}
