package com.zhiyou.colleageapp.appui.mvpPresenter;

import com.zhiyou.colleageapp.appui.mvpMode.action.IActionBase;
import com.zhiyou.colleageapp.appui.mvpView.ViewBase;

/**
 * Create by LongWeiHu on 2016/6/4.
 */
public abstract class PresenterBase {
    protected ViewBase mViewBase;

    public PresenterBase(ViewBase viewBase) {
        mViewBase = viewBase;
    }

    public abstract void releaseAll() ;


    protected class MyActions implements IActionBase {

        @Override
        public void onActFail(int textId, String msg) {
            if (mViewBase != null) {
                mViewBase.onViewFail(textId,msg);
            }
        }

        @Override
        public void onActStart(int textId) {
            if (mViewBase != null) {
                mViewBase.onViewStart(textId);
            }
        }

        @Override
        public void onActComplete() {
            if (mViewBase != null) {
                mViewBase.onViewComplete();
            }
        }

        @Override
        public void onActError(int textId, Throwable e) {
            if (mViewBase != null) {
                mViewBase.onViewError(textId,e);
            }
        }
    }
}
