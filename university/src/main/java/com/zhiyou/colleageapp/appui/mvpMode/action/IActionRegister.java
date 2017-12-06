package com.zhiyou.colleageapp.appui.mvpMode.action;

/**
 * Create by LongWeiHu on 2016/5/31.
 */
public interface IActionRegister {
    void onDuringCountDown(int time);
    void onCountDownComplete();
    void onCountDownStart();
    void onActError(Throwable e);
}
