package com.zhiyou.colleageapp.appui.mvpMode;

import android.util.Pair;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.zhiyou.colleageapp.R;
import com.zhiyou.colleageapp.appui.mvpMode.action.IActSuccess;
import com.zhiyou.colleageapp.appui.mvpMode.action.IActionBase;
import com.zhiyou.colleageapp.utils.AppLog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Create by LongWeiHu on 2016/6/2.
 */
public class ModeConversation extends ModeBase {

    public ModeConversation(IActionBase iActionBase) {
        super(iActionBase);
    }

    public void onLoadConversation(final EMConversation.EMConversationType type, final IActSuccess<List<EMConversation>> iActSuccess) {
        Subscription subscription = Observable.create(new Observable.OnSubscribe<List<EMConversation>>() {
            @Override
            public void call(Subscriber<? super List<EMConversation>> subscriber) {
                List<EMConversation> conversations = EMClient.getInstance().chatManager().getConversationsByType(type);
                List<Pair<Long, EMConversation>> sortList = new ArrayList<>();

                for (EMConversation conversation : conversations) {
                    EMMessage message = conversation.getLastMessage();
                    if (message != null) {
                        Pair<Long, EMConversation> pair = new Pair<>(message.getMsgTime(), conversation);
                        sortList.add(pair);
                    }
                }

                try {
                    // Internal is TimSort algorithm, has bug
                    sortConversationByLastChatTime(sortList);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                List<EMConversation> list = new ArrayList<EMConversation>();
                for (Pair<Long, EMConversation> sortItem : sortList) {
                    list.add(sortItem.second);
                }

                subscriber.onNext(list);
            }
        }).subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mIActionBase.onActStart(R.string.g_wait_loading);
                    }
                }).subscribeOn(AndroidSchedulers.mainThread())

                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<EMConversation>>() {
                    @Override
                    public void onCompleted() {
                        mIActionBase.onActComplete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mIActionBase.onActError(R.string.on_loading_fail, e);
                        AppLog.instance().e(e);
                    }

                    @Override
                    public void onNext(List<EMConversation> list) {
                        iActSuccess.onActSuccess(list);
                    }
                });

        addSubscription(subscription);
    }

    public void loadMoreMessage(final EMConversation currentConversation, final String msgId, final int pageSize, final IActionBase iActionBase, final IActSuccess<List<EMMessage>> success) {
      Subscription subscription = Observable.create(new Observable.OnSubscribe<List<EMMessage>>() {
            @Override
            public void call(Subscriber<? super List<EMMessage>> subscriber) {
                try {
                    List<EMMessage> list = currentConversation.loadMoreMsgFromDB(msgId, pageSize);
                    subscriber.onNext(list);
                } catch (Exception e) {
                    AppLog.instance().e("loadMoreMessage: "+e);
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io())
        .doOnSubscribe(new Action0() {
            @Override
            public void call() {
                iActionBase.onActStart(R.string.on_loading);
            }
        }).subscribeOn(AndroidSchedulers.mainThread())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<List<EMMessage>>() {
                       @Override
                       public void call(List<EMMessage> emMessages) {
                        success.onActSuccess(emMessages);
                       }
                   },
                new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        iActionBase.onActError(R.string.on_loading_fail,throwable);
                    }
                });

        addSubscription(subscription);
    }



    public void search(CharSequence searchText,IActSuccess<List<EMConversation>> searchSuccess) {
        // TODO:By LongWeiHu 2016/6/2
    }


    /**
     * 根据最后一条消息的时间排序
     */
    private void sortConversationByLastChatTime(List<Pair<Long, EMConversation>> conversationList) {
        Collections.sort(conversationList, new Comparator<Pair<Long, EMConversation>>() {
            @Override
            public int compare(final Pair<Long, EMConversation> con1, final Pair<Long, EMConversation> con2) {

                if (con1.first == con2.first) {
                    return 0;
                } else if (con2.first > con1.first) {
                    return 1;
                } else {
                    return -1;
                }
            }

        });
    }
}
