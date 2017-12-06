package com.zhiyou.colleageapp.appui.mvpMode;

import com.zhiyou.colleageapp.appui.mvpMode.action.IActionBase;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Create by LongWeiHu on 2016/5/30.
 */
public class ModeBase {

    protected IActionBase mIActionBase;
    private CompositeSubscription mCompositeSubscription = new CompositeSubscription();

    public ModeBase(IActionBase IActionBase) {
        mIActionBase = IActionBase;
    }

    public void addSubscription(Subscription subscription){
        mCompositeSubscription.add(subscription);
    }

    public void releaseAll() {
        if (mCompositeSubscription != null && !mCompositeSubscription.isUnsubscribed()) {
            mCompositeSubscription.unsubscribe();
        }
    }
}
