package com.zhiyou.colleageapp.appui.mvpView;

/**
 * Create by LongWeiHu on 2016/5/31.
 */
public interface ViewRegister {
    void onViewDuringCountDown(int time);
    void onViewCountDownComplete();
    void onViewCountDownStart();
    void onViewError(Throwable e);
}
