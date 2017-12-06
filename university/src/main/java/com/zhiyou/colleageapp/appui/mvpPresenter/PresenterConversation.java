package com.zhiyou.colleageapp.appui.mvpPresenter;

import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.zhiyou.colleageapp.appui.mvpMode.ModeConversation;
import com.zhiyou.colleageapp.appui.mvpMode.action.IActSuccess;
import com.zhiyou.colleageapp.appui.mvpMode.action.IActionBase;
import com.zhiyou.colleageapp.appui.mvpView.ViewBase;
import com.zhiyou.colleageapp.appui.mvpView.ViewSuccess;

import java.util.List;

/**
 * Create by LongWeiHu on 2016/6/2.
 * 最近会话
 */
public class PresenterConversation extends PresenterBase{

    private ModeConversation mModeConversation;
    public PresenterConversation(ViewBase viewBase) {
        super(viewBase);
        mModeConversation = new ModeConversation(new MyActions());
    }

    @Override
    public void releaseAll() {
        mModeConversation.releaseAll();
    }

    public void onLoadConversation(EMConversation.EMConversationType type, final ViewSuccess<List<EMConversation>> viewLoadSuccess) {
        mModeConversation.onLoadConversation(type, new IActSuccess<List<EMConversation>>() {
            @Override
            public void onActSuccess(List<EMConversation> conversations) {
                viewLoadSuccess.onSuccess(conversations);
            }
        });
    }

    public void search(CharSequence searchText, final ViewSuccess<List<EMConversation>> viewSearchSuccess) {
        mModeConversation.search(searchText, new IActSuccess<List<EMConversation>>() {
            @Override
            public void onActSuccess(List<EMConversation> conversations) {
                viewSearchSuccess.onSuccess(conversations);
            }
        });
    }

    /**根据传入的参数从db加载startMsgId之前(存储顺序)指定数量的message， 加载到的messages会加入到当前conversation的messages里
     * @param currentConversation : 当前的会话
     * @param msgId :加载这个id之前的message
     * @param pageSize :加载多少条
     * @param viewBase :
     * @param viewSuccess :
     */
    public void loadMoreMessage(EMConversation currentConversation, String msgId, int pageSize, final ViewBase viewBase, final ViewSuccess<List<EMMessage>> viewSuccess) {
        mModeConversation.loadMoreMessage(currentConversation, msgId, pageSize, new IActionBase() {
            @Override
            public void onActStart(int textId) {
                viewBase.onViewStart(textId);
            }

            @Override
            public void onActComplete() {
                viewBase.onViewComplete();
            }

            @Override
            public void onActError(int textId, Throwable e) {
                viewBase.onViewError(textId,e);
            }

            @Override
            public void onActFail(int textId, String msg) {
                viewBase.onViewFail(textId,msg);
            }
        }, new IActSuccess<List<EMMessage>>() {
            @Override
            public void onActSuccess(List<EMMessage> emMessages) {
                viewSuccess.onSuccess(emMessages);
            }
        });
    }
}
