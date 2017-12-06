package com.zhiyou.colleageapp.appui.mvpPresenter;

import com.zhiyou.colleageapp.appui.mvpMode.action.IActSuccess;
import com.zhiyou.colleageapp.appui.mvpMode.ModeUser;
import com.zhiyou.colleageapp.appui.mvpMode.action.IActionRegister;
import com.zhiyou.colleageapp.appui.mvpView.ViewBase;
import com.zhiyou.colleageapp.appui.mvpView.ViewRegister;
import com.zhiyou.colleageapp.appui.mvpView.ViewSuccess;
import com.zhiyou.colleageapp.domain.PersonalProfile;

import java.util.Map;

/**
 * Create by LongWeiHu on 2016/5/30.
 */
public class PresenterUser extends PresenterBase {

    private ModeUser mModeUser;

    public PresenterUser(ViewBase viewLogin) {
        super(viewLogin);
        mModeUser = new ModeUser(new MyActions());
    }


    public void loginEaseServer(String userName, String userPwd) {
        mModeUser.loginEaseServer(userName, userPwd);
    }

    public void loginAppServer(String userName, String userPwd, final ViewSuccess<Integer> viewSuccess) {
        mModeUser.loginAppServer(userName, userPwd, new IActSuccess<Integer>() {
            @Override
            public void onActSuccess(Integer integer) {
                viewSuccess.onSuccess(integer);
            }
        });
    }

    //=============================register==========================================

    public void onRegister(String userName, String pwd, String smsCode, final ViewSuccess<Integer> viewSuccess) {
        mModeUser.onRegister(userName, pwd, smsCode, new IActSuccess<Integer>() {
            @Override
            public void onActSuccess(Integer integer) {
                viewSuccess.onSuccess(integer);
            }
        });
    }



    public void onSetSMS(final ViewSuccess<Integer> viewSuccess) {
        mModeUser.onSetSMS(new IActSuccess<Integer>() {
            @Override
            public void onActSuccess(Integer integer) {
                viewSuccess.onSuccess(integer);
            }
        });
    }


    public void onCountDown(int timeSeconds, String phoneNum, final ViewRegister viewRegister) {
        mModeUser.onCountDown(timeSeconds, phoneNum, new IActionRegister() {
            @Override
            public void onDuringCountDown(int time) {
                viewRegister.onViewDuringCountDown(time);
            }

            @Override
            public void onCountDownComplete() {
                viewRegister.onViewCountDownComplete();
            }

            @Override
            public void onCountDownStart() {
                viewRegister.onViewCountDownStart();
            }

            @Override
            public void onActError(Throwable e) {
                viewRegister.onViewError(e);
            }
        });
    }

    public void cancelLogin() {
        mModeUser.cancelLogin();
    }

    public void onCancelRegister() {
        mModeUser.onCancelRegister();
    }

    public void modifyUserInfo(Map<String,String> beChangedContent,final ViewSuccess<PersonalProfile> viewSuccess) {
        mModeUser.modifyPersonalInfo(beChangedContent, new IActSuccess<PersonalProfile>() {
            @Override
            public void onActSuccess(PersonalProfile personalProfile) {
                viewSuccess.onSuccess(personalProfile);
            }
        });
    }




    @Override
    public void releaseAll() {
        mModeUser.releaseAll();
    }
}
