package com.zhiyou.colleageapp.appui.friend;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMConversationListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMChatRoom;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.util.DateUtils;
import com.hyphenate.util.NetUtils;
import com.zhiyou.colleageapp.Helper;
import com.zhiyou.colleageapp.R;
import com.zhiyou.colleageapp.appui.BaseFragment;
import com.zhiyou.colleageapp.appui.LoginActivity;
import com.zhiyou.colleageapp.appui.MainActivity;
import com.zhiyou.colleageapp.appui.adapter.UniversalClickableAdapter;
import com.zhiyou.colleageapp.appui.adapter.listitem.PopItem;
import com.zhiyou.colleageapp.appui.adapter.viewholder.UniversalViewHolder;
import com.zhiyou.colleageapp.appui.mvpPresenter.PresenterConversation;
import com.zhiyou.colleageapp.appui.mvpView.ViewBase;
import com.zhiyou.colleageapp.appui.mvpView.ViewSuccess;
import com.zhiyou.colleageapp.appui.school.DynamicFragment;
import com.zhiyou.colleageapp.appui.widget.dialog.BaseDialog;
import com.zhiyou.colleageapp.appui.widget.dialog.DialogOperates;
import com.zhiyou.colleageapp.appui.widget.dialog.TextDialog;
import com.zhiyou.colleageapp.constants.EaseConstant;
import com.zhiyou.colleageapp.constants.FragmentTag;
import com.zhiyou.colleageapp.manager.DBManager;
import com.zhiyou.colleageapp.utils.AppToast;
import com.zhiyou.colleageapp.utils.EaseCommonUtils;
import com.zhiyou.colleageapp.utils.EaseSmileUtils;
import com.zhiyou.colleageapp.utils.EaseUserUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.observers.SafeSubscriber;

/**
 * 会话列表的fragment
 */
public class FriendConversationFragment extends BaseFragment {
    private TextView mErrorText;
    private EditText query;
    private ImageButton clearSearch;
    private PullToRefreshListView mListView;
    private View mNetErrorView;
    private boolean mIsConflict;
    private View mHeaderDynamicView, mHeaderContactView;
    private PresenterConversation mPresenterConversation;
    private MyConversationAdapter mContactAdapter;
    private DialogOperates mDialogOperates;
    private TextDialog mNetErrorDialog;
    private EMConversationListener convListener = new EMConversationListener() {

        @Override
        public void onCoversationUpdate() {
            refresh();
        }

    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_conversation_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.getBoolean("mIsConflict", false)) {
            return;
        }
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected void showUnLoginCover() {

    }


    @Override
    protected void initView(View view) {
        mNetErrorView = view.findViewById(R.id.net_error_layout);
        mNetErrorView.setVisibility(View.GONE);
        mErrorText = (TextView) view.findViewById(R.id.tv_connect_error);
        clearSearch = (ImageButton) view.findViewById(R.id.search_clear);
        // 搜索框
        query = (EditText) view.findViewById(R.id.query);
        // 搜索框中清除button
        mPresenterConversation = new PresenterConversation(new ConViewBase());
        initListView(view);
    }

    private void initListView(View view) {
        mListView = (PullToRefreshListView) view.findViewById(R.id.friend_good_contact_listView);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View headerView = inflater.inflate(R.layout.contact_head_view, null);
        mListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        mHeaderContactView = headerView.findViewById(R.id.contact_item_contact);
        mHeaderDynamicView = headerView.findViewById(R.id.contact_item_dynamic);

        mListView.getRefreshableView().addHeaderView(headerView, null, false);
        mContactAdapter = new MyConversationAdapter(getContext(), R.layout.list_item_conversation);
        mListView.setAdapter(mContactAdapter);
        loadConversation(null);
    }

    private void loadConversation(final PullToRefreshBase<ListView> refreshView) {
        mPresenterConversation.onLoadConversation(EMConversation.EMConversationType.Chat, new ViewSuccess<List<EMConversation>>() {
            @Override
            public void onSuccess(List<EMConversation> conversations) {
                hiddenAppMainTabLoading();
                if (refreshView != null) {
                    refreshView.onRefreshComplete();
                }
                mContactAdapter.updateData(conversations);
            }
        });
    }

    @Override
    protected void setListener() {
        super.setListener();

        EMClient.getInstance().addConnectionListener(connectionListener);
        query.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mPresenterConversation.search(s, new ViewSuccess<List<EMConversation>>() {
                    @Override
                    public void onSuccess(List<EMConversation> conversations) {
                        mContactAdapter.updateData(conversations);
                    }
                });
                if (s.length() > 0) {
                    clearSearch.setVisibility(View.VISIBLE);
                } else {
                    clearSearch.setVisibility(View.INVISIBLE);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        });

        clearSearch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                query.getText().clear();
                hideSoftKeyboard();
            }
        });

        mListView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideSoftKeyboard();
                return false;
            }
        });

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.contact_item_contact:
                        if (Helper.getInstance().isTempUser()) {
                            mBaseActivity.showActivity(LoginActivity.class, null);
                        } else {
                            mBaseActivity.showFragment(FriendContactFragment.class, FragmentTag.CONTACT_LIST, null, true);
                        }
                        break;
                    case R.id.contact_item_dynamic:
                        mBaseActivity.showFragment(DynamicFragment.class, FragmentTag.FRAGMENT_CONVERSATION_2_DYNAMIC, null, true);
                        break;
                }
            }
        };

        mHeaderDynamicView.setOnClickListener(listener);
        mHeaderContactView.setOnClickListener(listener);

        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                loadConversation(refreshView);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

            }
        });

        mNetErrorView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mNetErrorDialog == null) {
                    mNetErrorDialog = new TextDialog(getContext(), R.string.prompt, BaseDialog.EDialogButtonType.BUTTON_TWO);
                    mNetErrorDialog.setMsgText("请检查网络,并重新登录");
                    mNetErrorDialog.setLeft(R.string.cancel);
                    mNetErrorDialog.setLeftButtonListener(new BaseDialog.LeftListener() {
                        @Override
                        public void onLeftListener() {
                            mNetErrorDialog.dismiss();
                        }
                    });

                    mNetErrorDialog.setRight(R.string.ok);
                    mNetErrorDialog.setRightButtonListener(new BaseDialog.RightListener() {
                        @Override
                        public void onRightListener() {
                            mBaseActivity.showActivity(LoginActivity.class, null);
                            mNetErrorDialog.dismiss();
                        }
                    });
                }

                mNetErrorDialog.show();
            }
        });

    }

    private void clickItem(EMConversation conversation) {
        String username = conversation.getUserName();
        // 进入聊天页面
        Bundle bundle = new Bundle();
        bundle.putInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
        // it's single chat
        bundle.putString(EaseConstant.CHAT_ENTITY_ID, username);
        mBaseActivity.showFragment(SingleChatFragment.class, FragmentTag.CHAT_SINGLE, bundle, true);
        conversation.markAllMessagesAsRead();
        mContactAdapter.notifyDataSetChanged();
    }

    private boolean mIsDeleteMsg = false;

    public void onLongClickItem(final EMConversation item) {
        if (item == null) {
            return;
        }

        if (mDialogOperates == null) {
            mDialogOperates = new DialogOperates(getContext(), 2);
            mDialogOperates.setIsShowConfirm(false);
            mDialogOperates.setTitle(R.string.delete);
            List<PopItem> items = new ArrayList<>();
            PopItem deleteConItem = new PopItem(R.string.delete_conversation);
            items.add(deleteConItem);
            PopItem deleteConItemAndMsg = new PopItem(R.string.delete_conversation_messages);
            items.add(deleteConItemAndMsg);
            mDialogOperates.setData(items);
            mDialogOperates.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    PopItem popItem = (PopItem) parent.getAdapter().getItem(position);
                    int textId = popItem.getContentTextId();
                    switch (textId) {
                        case R.string.delete_conversation:
                            mIsDeleteMsg = false;
                            break;
                        case R.string.delete_conversation_messages:
                            mIsDeleteMsg = true;
                            break;
                    }
                    try {
                        // 删除此会话
                        String userName = item.getUserName();

                        EMClient.getInstance().chatManager().deleteConversation(userName, mIsDeleteMsg);
                        DBManager.getInstance().getInviteMessageDao().deleteByFromName(userName);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    refresh();
//                    mContactAdapter.notifyDataSetChanged();
                    // 更新消息未读数
                    ((MainActivity) getActivity()).updateUnreadLabel();
                    mDialogOperates.dismiss();
                }
            });
        }

        mDialogOperates.show();

    }

    protected EMConnectionListener connectionListener = new EMConnectionListener() {

        @Override
        public void onDisconnected(int error) {
            if (error == EMError.USER_REMOVED || error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                mIsConflict = true;
            } else {
                Subscription subscription = Observable.create(new Observable.OnSubscribe<Object>() {
                    @Override
                    public void call(Subscriber<? super Object> subscriber) {
                        onConnectionDisconnected();
                    }
                }).subscribeOn(AndroidSchedulers.mainThread()).subscribe(new SafeSubscriber<Object>(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Object o) {

                    }
                }));

                mCompositeSubscription.add(subscription);
            }
        }

        @Override
        public void onConnected() {
            Subscription subscription = Observable.create(new Observable.OnSubscribe<Object>() {
                @Override
                public void call(Subscriber<? super Object> subscriber) {
                    onConnectionConnected();
                }
            }).subscribeOn(AndroidSchedulers.mainThread()).subscribe(new SafeSubscriber<Object>(new Subscriber<Object>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onNext(Object o) {

                }
            }));
            mCompositeSubscription.add(subscription);
        }
    };

    /**
     * 连接到服务器
     */
    protected void onConnectionConnected() {
        mNetErrorView.setVisibility(View.GONE);
    }

    /**
     * 连接断开
     */
    protected void onConnectionDisconnected() {
        mNetErrorView.setVisibility(View.VISIBLE);
        if (NetUtils.hasNetwork(getActivity())) {
            mErrorText.setText(R.string.can_not_connect_chat_server_connection);
        } else {
            mErrorText.setText(R.string.the_current_network);
        }
    }


    /**
     * 刷新页面
     */
    public void refresh() {
        loadConversation(null);
    }


    @Override
    public void onResume() {
        super.onResume();
        refresh();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EMClient.getInstance().removeConnectionListener(connectionListener);
        mDialogOperates = null;
        mNetErrorDialog = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mIsConflict) {
            outState.putBoolean("mIsConflict", true);
        }
    }


    private class MyConversationAdapter extends UniversalClickableAdapter<EMConversation> {

        public MyConversationAdapter(Context context, int layoutId) {
            super(context, layoutId);
        }

        @Override
        public void setViewListener(UniversalViewHolder holder) {
            holder.setClickListener(holder.getConvertView());
            holder.setLongClickListener(holder.getConvertView());
        }

        @Override
        public void onClickBack(View v, int position, UniversalViewHolder holder) {
            clickItem(mContactAdapter.getItem(position));
        }

        @Override
        public void onLongClickBack(View v, int position, UniversalViewHolder holder) {
            onLongClickItem(mContactAdapter.getItem(position));
        }

        @Override
        public void convert(UniversalViewHolder holder, EMConversation conversation) {

            holder.setBackgroundRes(R.id.list_item_contact, R.drawable.bg_selector_item_common);

            // 获取与此用户/群组的会话
            // 获取用户username或者群组groupid
            String username = conversation.getUserName();

            if (conversation.getType() == EMConversation.EMConversationType.GroupChat) {
                // 群聊消息，显示群聊头像
                holder.setImageResource(R.id.avatar, R.drawable.group_icon);
                EMGroup group = EMClient.getInstance().groupManager().getGroup(username);
                holder.setText(R.id.name, group != null ? group.getGroupName() : username);
            } else if (conversation.getType() == EMConversation.EMConversationType.ChatRoom) {
                holder.setImageResource(R.id.avatar, R.drawable.group_icon);
                EMChatRoom room = EMClient.getInstance().chatroomManager().getChatRoom(username);
                holder.setText(R.id.name, room != null && !TextUtils.isEmpty(room.getName()) ? room.getName() : username);
            } else {
                EaseUserUtils.setUserAvatar(getContext(), username, (ImageView) holder.getView(R.id.avatar));
                EaseUserUtils.setUserNick(username, (TextView) holder.getView(R.id.name));
            }

            if (conversation.getUnreadMsgCount() > 0) {
                // 显示与此用户的消息未读数
                holder.setText(R.id.unread_msg_number, String.valueOf(conversation.getUnreadMsgCount()));
                holder.setVisible(R.id.unread_msg_number, View.VISIBLE);
                int unReadMsgCount = conversation.getUnreadMsgCount();
                if (unReadMsgCount > 99) {
                    holder.setText(R.id.unread_msg_number, String.format(Locale.getDefault(), "%s", "99+"));
                } else {
                    holder.setText(R.id.unread_msg_number, String.format(Locale.getDefault(), "%s", unReadMsgCount));
                }
            } else {
                holder.setVisible(R.id.unread_msg_number, View.INVISIBLE);
            }

            if (conversation.getAllMsgCount() != 0) {
                // 把最后一条消息的内容作为item的message内容
                EMMessage lastMessage = conversation.getLastMessage();
                Spannable msgContent = EaseSmileUtils.getSmiledText(getContext(), EaseCommonUtils.getMessageDigest(lastMessage, (getContext())));
                holder.setText(R.id.message,msgContent);

                holder.setText(R.id.time, DateUtils.getTimestampString(new Date(lastMessage.getMsgTime())));
                if (lastMessage.direct() == EMMessage.Direct.SEND && lastMessage.status() == EMMessage.Status.FAIL) {
                    holder.setVisible(R.id.msg_state, View.VISIBLE);
                } else {
                    holder.setVisible(R.id.msg_state, View.GONE);
                }
            }
        }
    }

    private class ConViewBase implements ViewBase {
        @Override
        public void onViewStart(int textId) {
        }

        @Override
        public void onViewComplete() {
            hiddenAppMainTabLoading();
        }

        @Override
        public void onViewError(int textId, Throwable e) {
            hiddenAppMainTabLoading();
            AppToast.showBottom(textId);
        }

        @Override
        public void onViewFail(int textId, String msg) {
            hiddenAppMainTabLoading();
            AppToast.showBottomText(textId, msg);
        }
    }

}
