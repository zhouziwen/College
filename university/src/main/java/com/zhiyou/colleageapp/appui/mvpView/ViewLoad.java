package com.zhiyou.colleageapp.appui.mvpView;

/**
 * Create by LongWeiHu on 2016/5/30.
 */
public interface ViewLoad<E> extends ViewBase {
    void onViewFail(int textId);
    void onViewLoadSuccess(int textId, E data);
}
