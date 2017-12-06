package com.zhiyou.colleageapp.appui.mvpMode.action;

/**
 * Create by LongWeiHu on 2016/5/27.
 */
public interface IActionBase {
    void onActStart(int textId);
    void onActComplete();
    void onActError(int textId, Throwable e);
    void onActFail(int textId, String msg);
}
