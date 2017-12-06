package com.zhiyou.colleageapp.appui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;

import com.hyphenate.chat.EMClient;
import com.zhiyou.colleageapp.Helper;
import com.zhiyou.colleageapp.R;
import com.zhiyou.colleageapp.httpservice.UrlConstant;
import com.zhiyou.colleageapp.manager.PreferenceManager;
import com.zhiyou.colleageapp.utils.AppLog;
import com.zhiyou.colleageapp.utils.AppToast;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * 开屏页
 */
public class SplashActivity extends BaseActivity {
    private RelativeLayout rootLayout;


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_splash);
        rootLayout = (RelativeLayout) findViewById(R.id.splash_root);
        AlphaAnimation animation = new AlphaAnimation(0.3f, 1.0f);
        animation.setDuration(10);
        rootLayout.startAnimation(animation);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Subscription subscription = Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(final Subscriber<? super Integer> subscriber) {

                try {
                    String currentUser = PreferenceManager.getInstance().getCurrentUsername();
                    UrlConstant.UserInfo.mToken = PreferenceManager.getInstance().getToken();
                    UrlConstant.UserInfo.signature = PreferenceManager.getInstance().getSignature();
                    UrlConstant.UserInfo.setFiledMap();
                    if (TextUtils.isEmpty(currentUser) || PreferenceManager.TEMP.equals(currentUser)) {
                        PreferenceManager.getInstance().saveUserName(PreferenceManager.TEMP);
                        subscriber.onNext(1);
                    } else if (EMClient.getInstance().isLoggedInBefore()) {
                        if (!EMClient.getInstance().isConnected()) {
                            Helper.getInstance().notifyTokenExpired();
                        } else {
                            subscriber.onNext(1);
                        }
                    } else {
                        subscriber.onNext(1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        showLoading();
                    }
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        hiddenLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        hiddenLoading();
                        AppLog.instance().e(e);
                        AppToast.showBottom("初始化异常");
                    }

                    @Override
                    public void onNext(Integer action) {
                        hiddenLoading();
                        switch (action) {
                            case 0:
                                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                                break;
                            case 1:
                                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                                break;
                        }
                        finish();
                    }
                });

        mCompositeSubscription.add(subscription);

    }

    /**
     * 获取当前应用程序的版本号
     */
    private String getVersion() {
        return EMClient.getInstance().getChatConfig().getVersion();
    }
}
