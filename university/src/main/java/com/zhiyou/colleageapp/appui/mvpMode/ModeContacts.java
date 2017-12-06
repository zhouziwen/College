package com.zhiyou.colleageapp.appui.mvpMode;

import android.text.TextUtils;

import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.zhiyou.colleageapp.Helper;
import com.zhiyou.colleageapp.R;
import com.zhiyou.colleageapp.appui.mvpMode.action.IActSuccess;
import com.zhiyou.colleageapp.appui.mvpMode.action.IActionBase;
import com.zhiyou.colleageapp.constants.HttpKey;
import com.zhiyou.colleageapp.domain.InviteMessage;
import com.zhiyou.colleageapp.domain.PersonalProfile;
import com.zhiyou.colleageapp.domain.User;
import com.zhiyou.colleageapp.eenum.InviteMsgStatus;
import com.zhiyou.colleageapp.httpservice.ApiFactory;
import com.zhiyou.colleageapp.httpservice.ApiResult;
import com.zhiyou.colleageapp.httpservice.UrlConstant;
import com.zhiyou.colleageapp.manager.DBManager;
import com.zhiyou.colleageapp.utils.AppLog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
 * Create by LongWeiHu on 2016/6/1.
 */
public class ModeContacts extends ModeBase {
    private final String mTag = "ModeContacts";

    public ModeContacts(IActionBase IActionContacts) {
        super(IActionContacts);
    }

    public void deleteContact(final String who, final IActSuccess<Integer> iActSuccess) {
        Subscription subscription = ApiFactory.getFartory().getFriendService().deleteFriend(UrlConstant.UserInfo.mFiledMap, who)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mIActionBase.onActStart(R.string.contact_delete_begin);
                    }
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SafeSubscriber<ApiResult>(new Subscriber<ApiResult>() {
                    @Override
                    public void onCompleted() {
                        mIActionBase.onActComplete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mIActionBase.onActError(R.string.global_delete_fail, e);
                        AppLog.instance().e(e);
                    }

                    @Override
                    public void onNext(ApiResult apiResult) {
                        if (apiResult != null) {
                            int status = apiResult.getStatus();
                            if (status == HttpKey.SUCCESS) {
                                iActSuccess.onActSuccess(R.string.global_delete_success);
                                Helper.getInstance().getAllContact().remove(who);
                                DBManager.getInstance().getUserDao().deleteByKey(who);
                            } else if (status == HttpKey.TOKE_EXPIRED) {
                                Helper.getInstance().notifyTokenExpired();
                            } else {
                                mIActionBase.onActFail(R.string.global_delete_fail, apiResult.getMessage());
                                AppLog.instance().e(mTag + apiResult.getMessage());
                            }
                        }
                    }
                }));

        addSubscription(subscription);
    }

    public void loadWhiteContactFromServer(final IActSuccess<List<User>> iActSuccess) {

        Subscription subscription = ApiFactory.getFartory().getFriendService().getFriendList(UrlConstant.UserInfo.mFiledMap)
                .map(new Func1<ApiResult<List<User>>, List<User>>() {

                    @Override
                    public List<User> call(ApiResult<List<User>> listApiResult) {
                        if (listApiResult == null) {
                            return null;
                        }
                        int status = listApiResult.getStatus();
                        if (status == HttpKey.SUCCESS) {
                            return listApiResult.getData();
                        } else if (status == HttpKey._2042) {
                            AppLog.instance().e(listApiResult.getMessage());
                            return null;
                        } else if (status == HttpKey.TOKE_EXPIRED) {
                            Helper.getInstance().notifyTokenExpired();
                        }
                        return null;
                    }
                }).map(new Func1<List<User>, List<User>>() {
                    @Override
                    public List<User> call(List<User> users) {

                        if (users == null || users.isEmpty()) {
                            return null;
                        }

                        sortContacts(users);
                        return users;
                    }

                })
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        if (mIActionBase != null) {
                            mIActionBase.onActStart(R.string.on_loading);
                        }
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SafeSubscriber<List<User>>(new Subscriber<List<User>>() {
                    @Override
                    public void onCompleted() {
                        if (mIActionBase != null) {
                            mIActionBase.onActComplete();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (mIActionBase != null) {
                            mIActionBase.onActError(R.string.failed_to_load_data, e);
                        }
                        AppLog.instance().e(mTag + e);
                    }

                    @Override
                    public void onNext(List<User> users) {
                        iActSuccess.onActSuccess(users);
                    }
                }));


        addSubscription(subscription);
    }

    public void loadWhiteContactFromLocal(final IActSuccess<List<User>> iActSuccess) {

        Subscription subscription = Observable.create(new Observable.OnSubscribe<List<User>>() {
            @Override
            public void call(Subscriber<? super List<User>> subscriber) {
                try {
                    Map<String, User> map = Helper.getInstance().getAllContact();
                    Set<String> userNameSet = map.keySet();
                    List<User> allUsers = new ArrayList<User>();
                    for (String name : userNameSet) {
                        if (TextUtils.isEmpty(name)) {
                            continue;
                        }
                        allUsers.add(map.get(name));
                    }

                    subscriber.onNext(allUsers);
                } catch (Exception e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        }).map(new Func1<List<User>, List<User>>() {
            @Override
            public List<User> call(List<User> users) {
                sortContacts(users);
                return users;
            }
        }).subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mIActionBase.onActStart(R.string.on_loading);
                    }
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SafeSubscriber<List<User>>(new Subscriber<List<User>>() {
                    @Override
                    public void onCompleted() {
                        mIActionBase.onActComplete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mIActionBase.onActError(R.string.on_loading_fail, e);
                    }

                    @Override
                    public void onNext(List<User> users) {
                        iActSuccess.onActSuccess(users);
                    }
                }));


        addSubscription(subscription);
    }

    public void search(CharSequence searchText) {
        // TODO:By LongWeiHu 2016/6/2
    }

    public void agreeInvitation(final InviteMessage msg, final IActSuccess<Integer> iActSuccess) {
        String userName = msg.getFrom();
        Subscription subscription = ApiFactory.getFartory().getFriendService().addFriend(UrlConstant.UserInfo.mFiledMap, userName).map(new Func1<ApiResult, ApiResult>() {
            @Override
            public ApiResult call(ApiResult apiResult) {
                if (apiResult.getStatus() == HttpKey.SUCCESS) {
                    try {
                        InviteMsgStatus status = msg.getMsgStatus();
                        switch (status) {
                            case BEINVITEED:
                                EMClient.getInstance().contactManager().acceptInvitation(msg.getFrom());
                                break;
                            case BEAPPLYED:
                                EMClient.getInstance().groupManager().acceptApplication(msg.getFrom(), msg.getGroupId());
                                break;
                            case GROUPINVITATION:
                                EMClient.getInstance().groupManager().acceptInvitation(msg.getGroupId(), msg.getInviteeId());
                                break;
                        }

                        msg.setMsgStatus(InviteMsgStatus.AGREED);
                        DBManager.getInstance().getInviteMessageDao().updateInviteMessage(msg);
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                        AppLog.instance().d("agreeInvitation" + e);

                    }

                }

                return apiResult;
            }
        })
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mIActionBase.onActStart(R.string.g_wait_loading);
                    }
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ApiResult>() {
                    @Override
                    public void call(ApiResult result) {
                        if (result.getStatus() == HttpKey.SUCCESS) {
                            iActSuccess.onActSuccess(R.string.contact_add_success);
                        } else {
                            mIActionBase.onActFail(R.string.contact_add_fail, result.getMessage());
                        }

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mIActionBase.onActError(R.string.contact_add_fail, throwable);
                    }
                });
        addSubscription(subscription);
    }

    public void refuseInvitation(final InviteMessage msg, final IActSuccess<Integer> iActSuccess) {
        Subscription subscription = Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {

                InviteMsgStatus status = msg.getMsgStatus();
                try {
                    switch (status) {
                        case BEINVITEED:
                            EMClient.getInstance().contactManager().declineInvitation(msg.getFrom());
                            break;
                        case BEAPPLYED:
                            EMClient.getInstance().groupManager().declineApplication(msg.getFrom(), msg.getGroupId(), "");
                            break;
                        case GROUPINVITATION:
                            EMClient.getInstance().groupManager().declineInvitation(msg.getGroupId(), msg.getInviteeId(), "");
                            break;
                    }
                    msg.setMsgStatus(InviteMsgStatus.REFUSED);
                    // 更新db
                    DBManager.getInstance().getInviteMessageDao().updateInviteMessage(msg);
                    subscriber.onNext(true);
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    subscriber.onNext(false);
                    AppLog.instance().e("refuseInvitation Error " + e);
                }
            }
        }).subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mIActionBase.onActStart(R.string.Are_refuse_with);
                    }
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Boolean>() {
                               @Override
                               public void call(Boolean aBoolean) {
                                   if (aBoolean) {
                                       iActSuccess.onActSuccess(R.string.Has_refused_to);
                                   } else {
                                       mIActionBase.onActFail(R.string.Refuse_with_failure, "");
                                   }
                               }
                           },
                        new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                mIActionBase.onActError(R.string.Refuse_with_failure, throwable);
                            }
                        });

        addSubscription(subscription);
    }

    public void sendInvite(final String who, final String msg, final IActSuccess<Integer> textIdSuccess) {
        Subscription subscription = Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                AppLog.instance().d("sendInvite who:" + who + "|msg  " + msg);
                try {
                    EMClient.getInstance().contactManager().addContact(who, msg);
                    subscriber.onNext(true);
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    subscriber.onNext(false);
                    AppLog.instance().e("sendInvite Error:" + e);
                }
            }
        }).subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mIActionBase.onActStart(R.string.Is_sending_a_request);
                    }
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Boolean>() {
                               @Override
                               public void call(Boolean aBoolean) {
                                   if (aBoolean) {
                                       textIdSuccess.onActSuccess(R.string.send_successful);
                                   } else {
                                       mIActionBase.onActFail(R.string.Request_add_buddy_failure, "");
                                   }
                               }
                           },
                        new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                mIActionBase.onActError(R.string.Request_add_buddy_failure, throwable);
                            }
                        });

        addSubscription(subscription);
    }

    public void isContactExists(String userName, final IActSuccess<User> iActSuccess) {
        Subscription subscription = ApiFactory.getFartory().getFriendService().isUserExist(userName)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mIActionBase.onActStart(R.string.contact_is_finding);
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ApiResult<User>>() {
                               @Override
                               public void call(ApiResult<User> userApiResult) {
                                   if (userApiResult == null) {
                                       mIActionBase.onActFail(R.string.contact_finding_fail, "");
                                       return;
                                   }
                                   if (userApiResult.getStatus() == HttpKey.SUCCESS) {
                                       iActSuccess.onActSuccess(userApiResult.getData());
                                   } else {
                                       mIActionBase.onActFail(R.string.contact_finding_fail, userApiResult.getMessage());
                                   }
                               }
                           },

                        new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                mIActionBase.onActError(R.string.contact_finding_fail, throwable);
                            }
                        });

        addSubscription(subscription);
    }

    public void loadBlackNameList(IActSuccess<List<User>> iActSuccess) {
        // TODO:By LongWeiHu 2016/6/3 服务器为提供接口
    }

    private void sortContacts(List<User> contactList) {
        // 排序
        Collections.sort(contactList, new Comparator<User>() {

            @Override
            public int compare(User lhs, User rhs) {

                if (lhs.getInitialLetter().equals(rhs.getInitialLetter())) {
                    String leftNick = lhs.getNick();
                    String rightNick = rhs.getNick();
                    String leftName = lhs.getUsername();
                    String rightName = rhs.getUsername();

                    if ((TextUtils.isEmpty(leftNick) | TextUtils.isEmpty(rightNick)) || (TextUtils.isEmpty(leftName) | TextUtils.isEmpty(rightName))) {
                        return -1;
                    } else if (!TextUtils.isEmpty(leftNick) && !TextUtils.isEmpty(rightNick)) {
                        return leftNick.compareTo(rightNick);
                    } else if (!TextUtils.isEmpty(leftName) && !TextUtils.isEmpty(rightName)) {
                        return leftName.compareTo(rightName);
                    }
                } else {
                    if ("#".equals(lhs.getInitialLetter())) {
                        return 1;
                    } else if ("#".equals(rhs.getInitialLetter())) {
                        return -1;
                    }
                }
                return -1;
            }
        });
    }

    /**把人移入到黑名单列表
     * @param userNames : 被添加到黑名单的人员的名单数组
     * @param actSuccess :
     */
    public void moveContactsToBlackList(List<String> userNames, final IActSuccess<Integer> actSuccess) {
       Subscription subscription =  ApiFactory.getFartory().getFriendService().moveContactsToBlackList(UrlConstant.UserInfo.mFiledMap, userNames)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mIActionBase.onActStart(R.string.g_wait_loading);
                    }
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ApiResult>() {
                               @Override
                               public void call(ApiResult apiResult) {
                                   if (apiResult.getStatus() == HttpKey.SUCCESS) {
                                       actSuccess.onActSuccess(R.string.global_operate_success);
                                   } else {
                                       mIActionBase.onActFail(R.string.global_operate_fail, apiResult.getMessage());
                                   }
                               }
                           },
                        new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                mIActionBase.onActError(R.string.global_operate_fail, throwable);
                            }
                        });
        addSubscription(subscription);
    }


    /**获取个人资料
     * @param actSuccess :
     */
    public void getPersonalProfile(IActSuccess<PersonalProfile> actSuccess) {
        getContactProfile("",actSuccess);
    }


    /**获取联系人资料
     * @param friendName :
     * @param actSuccess
     */
    public void getContactProfile(String friendName, final IActSuccess<PersonalProfile> actSuccess) {
        ApiFactory.getFartory().getFriendService().getPersonalProfile(UrlConstant.UserInfo.mFiledMap, friendName)
        .subscribeOn(Schedulers.io())
        .doOnSubscribe(new Action0() {
            @Override
            public void call() {
                mIActionBase.onActStart(R.string.on_loading);
            }
        }).subscribeOn(AndroidSchedulers.mainThread())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<ApiResult<PersonalProfile>>() {
                       @Override
                       public void call(ApiResult<PersonalProfile> apiResult) {

                           if (apiResult.getStatus() == HttpKey.SUCCESS) {
                               actSuccess.onActSuccess(apiResult.getData());
                           } else {
                               mIActionBase.onActFail(R.string.on_loading_fail,apiResult.getMessage());
                           }
                       }
                   },
                new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mIActionBase.onActError(R.string.on_loading_fail,throwable);
                    }
                });
    }

}
