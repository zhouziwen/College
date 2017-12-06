package com.zhiyou.colleageapp.appui.mvpMode;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.zhiyou.colleageapp.Helper;
import com.zhiyou.colleageapp.R;
import com.zhiyou.colleageapp.appui.model.EaseUI;
import com.zhiyou.colleageapp.appui.mvpMode.action.IActSuccess;
import com.zhiyou.colleageapp.appui.mvpMode.action.IActionBase;
import com.zhiyou.colleageapp.appui.mvpMode.action.IActionRegister;
import com.zhiyou.colleageapp.constants.HttpKey;
import com.zhiyou.colleageapp.domain.LoginUser;
import com.zhiyou.colleageapp.domain.PersonalProfile;
import com.zhiyou.colleageapp.httpservice.ApiFactory;
import com.zhiyou.colleageapp.httpservice.ApiResult;
import com.zhiyou.colleageapp.httpservice.UrlConstant;
import com.zhiyou.colleageapp.manager.DBManager;
import com.zhiyou.colleageapp.manager.PreferenceManager;
import com.zhiyou.colleageapp.utils.AppLog;
import com.zhiyou.colleageapp.utils.RxCountDown;

import java.util.Map;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.observers.SafeSubscriber;
import rx.schedulers.Schedulers;

/**
 * Create by LongWeiHu on 2016/5/30.
 */
public class ModeUser extends ModeBase{
    private final String mTag = "ModeUser  : ";
    public ModeUser(IActionBase iActionLogin) {
        super(iActionLogin);
    }

    public void loginEaseServer(final String userName, final String userPwd) {
        final String tag = "Login Ease Server ";
        Subscription subscription = Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(final Subscriber<? super Boolean> subscriber) {
                EMClient.getInstance().login(userName, userPwd, new EMCallBack() {

                    @Override
                    public void onSuccess() {
                        // ** 第一次登录或者之前logout后再登录，加载所有本地群和回话
                        // ** manually load all local groups and
                        try {
                            subscriber.onNext(true);
                        } catch (Exception e) {
                            e.printStackTrace();
                            subscriber.onNext(false);
                        }
                    }

                    @Override
                    public void onProgress(int progress, String status) {

                    }

                    @Override
                    public void onError(final int code, final String message) {
                        subscriber.onError(new Throwable(code + message));
                    }
                });


            }
        }).subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        AppLog.instance().e(tag + e);
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        AppLog.instance().d(tag + aBoolean);
                    }
                });

        addSubscription(subscription);

    }

    public void loginAppServer(final String userName, final String userPwd, final IActSuccess<Integer> actSuccess) {

        Subscription subscription = ApiFactory.getFartory().getUserService().login(HttpKey.APPSECRET, userName, userPwd)
                .map(new Func1<ApiResult<LoginUser>, ApiResult<LoginUser>>() {
                    @Override
                    public ApiResult<LoginUser> call(ApiResult<LoginUser> apiResult) {
                        int status = apiResult.getStatus();
                        if (status == HttpKey.SUCCESS) {
                            initApp(userName,userPwd);
                            loginEaseServer(userName,userPwd);
                            LoginUser loginUser = apiResult.getData();
                            UrlConstant.UserInfo.mToken = loginUser.getToken();
                            UrlConstant.UserInfo.signature = HttpKey.APPSECRET;
                            UrlConstant.UserInfo.setFiledMap();
                            PreferenceManager.getInstance().saveToken(loginUser.getToken());
                            PreferenceManager.getInstance().saveSignature(HttpKey.APPSECRET);

                        }


                        return apiResult;
                    }
                })
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mIActionBase.onActStart(R.string.Is_loading);
                    }
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SafeSubscriber<ApiResult<LoginUser>>(new Subscriber<ApiResult<LoginUser>>() {
                    @Override
                    public void onCompleted() {
                        mIActionBase.onActComplete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mIActionBase.onActError(R.string.Login_failed, e);
                        AppLog.instance().e(e);
                    }

                    @Override
                    public void onNext(ApiResult<LoginUser> apiResult) {
                        if (apiResult.getStatus() == HttpKey.SUCCESS) {
                            actSuccess.onActSuccess(R.string.login_success);
                        } else {
                            mIActionBase.onActFail(R.string.Login_failed,apiResult.getMessage());
                        }
                    }
                }));

        addSubscription(subscription);

    }

    //===========================================register==================================================

    public void onRegister(String userName, String pwd, String smsCode, final IActSuccess<Integer> actSuccess) {
        Subscription subscription = ApiFactory.getFartory().getUserService().register(HttpKey.APPSECRET, userName, pwd, smsCode)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mIActionBase.onActStart(R.string.registering);
                    }
                }).subscribeOn(AndroidSchedulers.mainThread())

                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ApiResult>() {
                               @Override
                               public void onCompleted() {
                                   mIActionBase.onActComplete();
                               }

                               @Override
                               public void onError(Throwable e) {
                                   mIActionBase.onActError(R.string.register_fail, e);
                                   AppLog.instance().e(mTag + " onRegister : " + e);
                               }

                               @Override
                               public void onNext(ApiResult apiResult) {
                                   if (apiResult.getStatus() == HttpKey.SUCCESS) {
                                       actSuccess.onActSuccess(R.string.register_success);
                                   } else {
                                       mIActionBase.onActFail(R.string.register_fail, apiResult.getMessage());
                                   }
                               }
                           }
                );

        addSubscription(subscription);

    }


    public void onSetSMS(final IActSuccess<Integer> actSuccess) {
        Subscription subscription =  Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(final Subscriber<? super Boolean> subscriber) {
                EventHandler eh = new EventHandler() {
                    @Override
                    public void afterEvent(int event, int result, Object data) {

                        if (result == SMSSDK.RESULT_COMPLETE) {
                            //回调完成
                            if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                                //提交验证码成功 empty
                            } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                                //获取验证码成功
                                subscriber.onNext(true);
                            } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                                //返回支持发送验证码的国家列表
                                subscriber.onCompleted();
                            }
                        } else if (result == SMSSDK.RESULT_ERROR) {
                            subscriber.onError((Throwable) data);

                        }
                        subscriber.onCompleted();
                        AppLog.instance().d(mTag +"onSetSMS: " +data.toString());
                    }
                };
                SMSSDK.registerEventHandler(eh);
            }
        })
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mIActionBase.onActStart(R.string.register_request_code);
                    }
                }).subscribeOn(AndroidSchedulers.mainThread())

                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                        mIActionBase.onActComplete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mIActionBase.onActError(R.string.register_verify_code_error, e);
                        AppLog.instance().e(mTag + "  onSetSMS: " + e);
                    }

                    @Override
                    public void onNext(Boolean result) {
                        if (result) {
                            actSuccess.onActSuccess(R.string.register_verify_code_send);
                        } else {
                            mIActionBase.onActFail(R.string.register_verify_code_error, "");
                        }
                    }
                });

        addSubscription(subscription);
    }

    public void onCountDown(int timeSeconds, final String phoneNum, final IActionRegister iActionRegister) {
        Subscription subscription = RxCountDown.countdown(timeSeconds)
                .subscribeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        iActionRegister.onCountDownStart();
//                        SMSSDK.getVerificationCode("86", phoneNum, new OnSendMessageHandler() {
//                            @Override
//                            public boolean onSendMessage(String s, String s1) {
//                                return false;
//                            }
//                        });
                    }
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {
                        AppLog.instance().d(mTag+ "计时完成");
                        iActionRegister.onCountDownComplete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        iActionRegister.onActError(e);
                        AppLog.instance().e("onCountDown" + e);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        AppLog.instance().d("当前计时：" + integer);
                        iActionRegister.onDuringCountDown(integer);
                    }
                });

        addSubscription(subscription);
    }

    public void onCancelRegister() {
        releaseAll();
    }

    public void cancelLogin() {
        releaseAll();
    }

    private void initApp(String userName,String pwd) {
        UrlConstant.UserInfo.signature = HttpKey.APPSECRET;
        DBManager.getInstance().init(userName);
        Helper.getInstance().initAfterLogin(userName,pwd);
        EaseUI.getInstance().getNotifier().reset();
        EMClient.getInstance().groupManager().loadAllGroups();
        EMClient.getInstance().chatManager().loadAllConversations();
    }
    //============================================================================
    //                               modify personal info
    //=============================================================================

    public void modifyPersonalInfo(Map<String,String> beChangeContent, final IActSuccess<PersonalProfile> iActSuccess) {
        ApiFactory.getFartory().getUserService().modifyPersonalInfo(UrlConstant.UserInfo.mFiledMap,beChangeContent)
        .subscribeOn(Schedulers.io())
        .doOnSubscribe(new Action0() {
            @Override
            public void call() {
                mIActionBase.onActStart(R.string.on_modify);
            }
        }).subscribeOn(AndroidSchedulers.mainThread())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<ApiResult<PersonalProfile>>() {
                       @Override
                       public void call(ApiResult<PersonalProfile> apiResult) {
                           if (apiResult.getStatus() == HttpKey.SUCCESS) {
                                iActSuccess.onActSuccess(apiResult.getData());
                           } else {
                               mIActionBase.onActFail(R.string.on_modify_fail,apiResult.getMessage());
                           }
                       }
                   },
                new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mIActionBase.onActError(R.string.on_modify_fail,throwable);
                    }
                });
    }

}
