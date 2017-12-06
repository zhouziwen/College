package com.zhiyou.colleageapp.appui.mvpView;

/**
 * Create by LongWeiHu on 2016/5/30.
 */
public interface ViewBase {
    void onViewStart(int textId);
    void onViewComplete();
    void onViewError(int textId, Throwable e);
    void onViewFail(int textId, String msg);
}
