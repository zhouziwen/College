package com.zhiyou.colleageapp.appui.mvpMode;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.exceptions.HyphenateException;
import com.zhiyou.colleageapp.Helper;
import com.zhiyou.colleageapp.R;
import com.zhiyou.colleageapp.appui.mvpMode.action.IActSuccess;
import com.zhiyou.colleageapp.appui.mvpMode.action.IActionBase;
import com.zhiyou.colleageapp.constants.HttpKey;
import com.zhiyou.colleageapp.domain.FriendGroup;
import com.zhiyou.colleageapp.domain.Person;
import com.zhiyou.colleageapp.httpservice.ApiFactory;
import com.zhiyou.colleageapp.httpservice.ApiResult;
import com.zhiyou.colleageapp.httpservice.UrlConstant;
import com.zhiyou.colleageapp.utils.AppLog;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Create by LongWeiHu on 2016/5/27.
 */
public class ModelGroupOperate extends ModeBase {

    private final String mTag = "ModelGroupOperate:  ";

    public ModelGroupOperate(IActionBase actionBase) {
        super(actionBase);
    }


    public void actChangeGroupName(final String groupId, final String newName, final IActSuccess<Integer> actSuccess) {
        Subscription subscription = Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                try {
                    EMClient.getInstance().groupManager().changeGroupName(groupId, newName);
                    subscriber.onNext(true);
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    subscriber.onNext(false);
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io()) //联网操作在io线程
                .observeOn(AndroidSchedulers.mainThread())//被观察在主线程
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        //显示dialog或loading
                        mIActionBase.onActStart(R.string.is_modify_the_group_name);
                    }
                }).subscribeOn(AndroidSchedulers.mainThread()) //显示loading在主线程中

                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                        mIActionBase.onActComplete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mIActionBase.onActError(R.string.change_the_group_name_failed_please, e);
                        AppLog.instance().e(e);
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            actSuccess.onActSuccess(R.string.Modify_the_group_name_successful);
                        }
                    }
                });

        //如果没有取消订阅则取消订阅，几十释放资源
        addSubscription(subscription);
    }


    public void actAddMembers(final String groupOwner, final String currentUser, final String groupId,
                              final List<String> newMembers, final IActSuccess<Integer> actSuccess) {
        Subscription subscription = Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {

                try {
                    if (currentUser.equals(groupOwner)) {
                        EMClient.getInstance().groupManager().addUsersToGroup(groupId, newMembers.toArray(new String[newMembers.size()]));
                    } else {
                        // 一般成员调用invite方法
                        EMClient.getInstance().groupManager().inviteUser(groupId, newMembers.toArray(new String[newMembers.size()]), null);
                    }
                    subscriber.onNext(true);
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    subscriber.onNext(false);
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mIActionBase.onActStart(R.string.being_added);
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
                        mIActionBase.onActError(R.string.Add_group_members_fail, e);
                        AppLog.instance().e(mTag + e);
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            actSuccess.onActSuccess(R.string.Add_group_members_fail);
                        } else {
                            mIActionBase.onActFail(R.string.Add_group_members_fail, "");
                        }
                    }
                });
        addSubscription(subscription);
    }

    public void actQuitGroup(final String groupId, String who, final IActSuccess<Integer> actSuccess) {
        Subscription subscription = ApiFactory.getFartory().getFriendService().quitGroup(UrlConstant.UserInfo.mFiledMap, groupId,who)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mIActionBase.onActStart(R.string.is_quit_the_group_chat);
                    }
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ApiResult>() {
                               @Override
                               public void call(ApiResult apiResult) {
                                   if (apiResult.getStatus() == HttpKey.SUCCESS) {
                                       actSuccess.onActSuccess(R.string.exit_group_success);
                                   } else {
                                       mIActionBase.onActFail(R.string.Exit_the_group_chat_failure, "");
                                   }
                               }
                           },
                        new Action1<Throwable>() {
                            @Override
                            public void call(Throwable e) {
                                mIActionBase.onActError(R.string.Exit_the_group_chat_failure, e);
                                AppLog.instance().e("actQuitGroup: " + e);
                            }
                        });
        addSubscription(subscription);
    }


    public void actDismissGroup(final String groupId,final IActSuccess<Integer> actSuccess) {
        Subscription subscription = ApiFactory.getFartory().getFriendService().deleteGroup(UrlConstant.UserInfo.mFiledMap, groupId)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mIActionBase.onActStart(R.string.chatting_is_dissolution);
                    }
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Action1<ApiResult>() {
                            @Override
                            public void call(ApiResult apiResult) {
                                if (apiResult.getStatus() == HttpKey.SUCCESS) {
                                    actSuccess.onActSuccess(R.string.exit_group_success);
                                } else {
                                    mIActionBase.onActFail(R.string.Exit_the_group_chat_failure, "");
                                }
                            }
                        },

                        new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                mIActionBase.onActError(R.string.Exit_the_group_chat_failure, throwable);
                            }
                        });

        addSubscription(subscription);
    }

    public void actClearGroupHistoryRecord(String groupId,IActSuccess<Integer> actSuccess) {
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(groupId, EMConversation.EMConversationType.GroupChat);
        if (conversation != null) {
            conversation.clearAllMessages();
            actSuccess.onActSuccess(R.string.messages_are_empty);
        }
    }

    public void actBlockGroup(final String groupId,final IActSuccess<Integer> actSuccess) {
        Subscription subscription = Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                try {
                    EMClient.getInstance().groupManager().blockGroupMessage(groupId);
                    AppLog.instance().d("actBlockGroup " + groupId);
                    subscriber.onNext(true);
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    subscriber.onNext(false);
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io())

                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mIActionBase.onActStart(R.string.Is_block);
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
                        mIActionBase.onActError(R.string.bloc_group_fail, e);
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            actSuccess.onActSuccess(R.string.block_group_success);
                        } else {
                            mIActionBase.onActFail(R.string.bloc_group_fail, "");
                        }
                    }
                });

        addSubscription(subscription);
    }

    public void actUnBlockGroup(final String groupId,final IActSuccess<Integer> actSuccess) {
        Subscription subscription = Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                try {
                    EMClient.getInstance().groupManager().unblockGroupMessage(groupId);

                    subscriber.onNext(true);
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    subscriber.onNext(false);
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io())

                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {

                        mIActionBase.onActStart(R.string.Is_unblock);
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

                        mIActionBase.onActError(R.string.unblock_group_fail, e);
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            actSuccess.onActSuccess(R.string.unblock_group_success);
                        } else {
                            mIActionBase.onActFail(R.string.unblock_group_fail, "");
                        }
                    }
                });

        addSubscription(subscription);
    }


    public void actAddMemberToBlackList(String groupId, String who,IActSuccess<Integer> actSuccess) {
        // TODO:By LongWeiHu 2016/5/27  
    }

    public void actUpdateGroup(final String groupId, final IActSuccess<Integer> actSuccess) {
        Subscription subscription = Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                try {
                    EMClient.getInstance().groupManager().getGroupFromServer(groupId);
                    subscriber.onNext(true);
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    subscriber.onNext(false);
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mIActionBase.onActStart(R.string.on_loading);
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
                        mIActionBase.onActError(-1, e);
                        AppLog.instance().e(e);
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            actSuccess.onActSuccess(R.string.g_update_success);
                        } else {
                            mIActionBase.onActFail(R.string.g_update_fail, "");
                        }
                    }
                });

        addSubscription(subscription);
    }

    public void createFriendGroup(String groupName, String groupDesc, boolean isPublic, boolean isNeedApproval,
                                  ArrayList<String> selectedUserNames,final IActSuccess<FriendGroup> actSuccess) {
        Subscription subscription = ApiFactory.getFartory().getFriendService().createGroup(UrlConstant.UserInfo.mFiledMap, groupName, groupDesc, isPublic, isNeedApproval, selectedUserNames)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mIActionBase.onActStart(R.string.Is_to_create_a_group_chat);
                    }
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ApiResult<FriendGroup>>() {
                    @Override
                    public void onCompleted() {
                        mIActionBase.onActComplete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mIActionBase.onActError(R.string.Failed_to_create_groups, e);
                    }

                    @Override
                    public void onNext(ApiResult<FriendGroup> apiResult) {
                        if (apiResult == null) {
                            mIActionBase.onActFail(R.string.Failed_to_create_groups, "");
                        } else {
                            int status = apiResult.getStatus();
                            if (status == HttpKey.SUCCESS) {
                                actSuccess.onActSuccess(apiResult.getData());
                            } else if (status == HttpKey.TOKE_EXPIRED) {
                                Helper.getInstance().notifyTokenExpired();
                            } else {
                                mIActionBase.onActFail(R.string.Failed_to_create_groups, apiResult.getMessage());
                            }
                        }
                    }
                });

        addSubscription(subscription);
    }


    public void loadGroupMembers(String groupId, final IActSuccess<List<Person>> actSuccess) {
      Subscription subscription = ApiFactory.getFartory().getFriendService().getGroupMembers(UrlConstant.UserInfo.mFiledMap, groupId)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mIActionBase.onActStart(R.string.start);
                    }
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ApiResult<List<Person>>>() {
                    @Override
                    public void call(ApiResult<List<Person>> listApiResult) {
                        if (listApiResult == null) {
                            return;
                        }

                        int status = listApiResult.getStatus();
                        if (status == HttpKey.SUCCESS || status == 200) {
                            actSuccess.onActSuccess(listApiResult.getData());
                        } else {
                            mIActionBase.onActFail(R.string.group_member_list_fail,"");
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mIActionBase.onActError(R.string.empty, throwable);
                    }
                });

        addSubscription(subscription);
    }



}
