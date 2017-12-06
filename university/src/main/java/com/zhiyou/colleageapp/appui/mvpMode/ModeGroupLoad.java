package com.zhiyou.colleageapp.appui.mvpMode;

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

import java.util.List;
import java.util.Map;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Create by LongWeiHu on 2016/5/30.
 */
public class ModeGroupLoad extends ModeBase{

    public ModeGroupLoad(IActionBase actionBase) {
        super(actionBase);
    }


    public void loadGroupList(final IActSuccess<Map<String,List<FriendGroup>>> actSuccess) {
        Subscription subscription = ApiFactory.getFartory().getFriendService().getGroupList(UrlConstant.UserInfo.mFiledMap)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mIActionBase.onActStart(R.string.on_loading);
                    }
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ApiResult<Map<String,List<FriendGroup>>>>() {
                               @Override
                               public void call(ApiResult<Map<String,List<FriendGroup>>> apiResult) {
                                   if (apiResult == null) {
                                       return;
                                   }
                                   int status = apiResult.getStatus();
                                   if (status == HttpKey.SUCCESS) {
                                       actSuccess.onActSuccess(apiResult.getData());
                                   } else {
                                       mIActionBase.onActFail(R.string.group_list_fail,apiResult.getMessage());
                                   }
                               }
                           }, new Action1<Throwable>() {
                               @Override
                               public void call(Throwable e) {
                                   AppLog.instance().e("Get FriendGroup List Error !!! " + e);
                                   mIActionBase.onActError(R.string.on_loading_fail, e);
                               }
                           }
                );

        addSubscription(subscription);
    }

    public void search(String searchText,IActSuccess<List<FriendGroup>> actSuccess) {
        // TODO:By LongWeiHu 2016/6/8  
    }

    


    public void loadGroupMembers(String groupId, final IActSuccess<List<Person>> actSuccess) {
        ApiFactory.getFartory().getFriendService().getGroupMembers(UrlConstant.UserInfo.mFiledMap, groupId)
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
                        if (status == HttpKey.SUCCESS) {
                            actSuccess.onActSuccess(listApiResult.getData());
                        } else {
                            mIActionBase.onActFail(R.string.group_list_fail,"");
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mIActionBase.onActError(R.string.group_list_fail, throwable);
                    }
                });
    }

}
