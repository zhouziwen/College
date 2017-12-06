package com.zhiyou.colleageapp.appui.mvpView;

/**
 * Create by LongWeiHu on 2016/5/27.
 */
public interface ViewGroupOperate extends ViewBase {
    void onSuccess(int textId);
    void onViewFail(int textId,String msg);
}
