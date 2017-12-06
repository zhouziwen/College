package com.zhiyou.colleageapp.appui.friend;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.hyphenate.EMChatRoomChangeListener;
import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMChatRoom;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMMessage;
import com.zhiyou.colleageapp.R;
import com.zhiyou.colleageapp.appui.listener.EaseGroupRemoveListener;
import com.zhiyou.colleageapp.appui.listener.IRegistEventBus;
import com.zhiyou.colleageapp.constants.EaseConstant;
import com.zhiyou.colleageapp.constants.FragmentTag;
import com.zhiyou.colleageapp.domain.FriendGroup;
import com.zhiyou.colleageapp.rxeventbus.RxEventBus;
import com.zhiyou.colleageapp.rxeventbus.event.EventChatRecordChanged;
import com.zhiyou.colleageapp.utils.AppLog;
import com.zhiyou.colleageapp.utils.AppToast;
import com.zhiyou.colleageapp.utils.EaseCommonUtils;

import java.util.List;

import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Author by LongWeiHu on 2016年6月19日
 */
public class GroupChatFragment extends BaseChatFragment implements IRegistEventBus{
    protected int mChatType;
    public static GroupChatFragment mInstance;
    private GroupListener mGroupListener;
    private EMChatRoomChangeListener mChatRoomChangeListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerEventBus();
        // 判断群聊的类型
        mChatType = mBundle.getInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_GROUP);
        mInstance = this;
    }


    @Override
    protected void initView(View view) {
        super.initView(view);
        mAppTitleBar.getAction().setVisibility(View.VISIBLE);
    }

    @Override
    protected void initTitleBar() {
        super.initTitleBar();
        if (mChatType == EaseConstant.CHATTYPE_GROUP) {
            // 群聊
            EMGroup group = EMClient.getInstance().groupManager().getGroup(mToChatUsername);
            if (group != null) {
                mAppTitleBar.setTitle(group.getGroupName());
            } else {
                FriendGroup friendGroup = mBundle.getParcelable(EaseConstant.GROUP);
                mAppTitleBar.setTitle(friendGroup != null ? friendGroup.getName() : "");
            }
            mGroupListener = new GroupListener();
            EMClient.getInstance().groupManager().addGroupChangeListener(mGroupListener);
        } else if (mChatType == EaseConstant.CHATTYPE_CHATROOM) {
            onChatRoomViewCreation();
        }
    }


    @Override
    public void setListener() {
        super.setListener();
        mAppTitleBar.getAction().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (mChatType) {
                    case EaseConstant.CHATTYPE_GROUP:
                        Bundle bundle = new Bundle();
                        bundle.putParcelable(EaseConstant.GROUP, mBundle.getParcelable(EaseConstant.GROUP));
                        mBaseActivity.showFragment(FriendGroupDetailFragment.class, FragmentTag.CHAT_FRAGMENT_2_GROUP_DETAIL, bundle, true);
                        break;
                    case EaseConstant.CHATTYPE_CHATROOM:
                        // TODO: 2016/6/19
                        break;
                }
            }
        });
    }


    protected void addChatRoomChangeListener() {
        mChatRoomChangeListener = new EMChatRoomChangeListener() {
            @Override
            public void onChatRoomDestroyed(String roomId, String roomName) {
                if (roomId.equals(mToChatUsername)) {
                    showChatRoomToast(" room : " + roomId + " with room name : " + roomName + " was destroyed");
                    popSelf();
                }
            }

            @Override
            public void onMemberJoined(String roomId, String participant) {
                showChatRoomToast("member : " + participant + " join the room : " + roomId);
            }

            @Override
            public void onMemberExited(String roomId, String roomName, String participant) {
                showChatRoomToast("member : " + participant + " leave the room : " + roomId + " room name : " + roomName);
            }

            @Override
            public void onMemberKicked(String roomId, String roomName, String participant) {
                if (roomId.equals(mToChatUsername)) {
                    String curUser = EMClient.getInstance().getCurrentUser();
                    if (curUser.equals(participant)) {
                        EMClient.getInstance().chatroomManager().leaveChatRoom(mToChatUsername);
                        popSelf();
                    } else {
                        showChatRoomToast("member : " + participant + " was kicked from the room : " + roomId + " room name : " + roomName);
                    }
                }
            }

        };

        EMClient.getInstance().chatroomManager().addChatRoomChangeListener(mChatRoomChangeListener);
    }

    protected void onChatRoomViewCreation() {
        showLoading();
        EMClient.getInstance().chatroomManager().joinChatRoom(mToChatUsername, new EMValueCallBack<EMChatRoom>() {

            @Override
            public void onSuccess(final EMChatRoom value) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hiddenLoading();
                        if (!mToChatUsername.equals(value.getId())) {
                            return;
                        }
                        EMChatRoom room = EMClient.getInstance().chatroomManager().getChatRoom(mToChatUsername);
                        if (room != null) {
                            mAppTitleBar.setTitle(room.getName());
                        } else {
                            mAppTitleBar.setTitle(mToChatUsername);
                        }
                        addChatRoomChangeListener();
                    }
                });
            }

            @Override
            public void onError(final int error, String errorMsg) {
                AppLog.instance().e("onChatRoomViewCreation  " + errorMsg);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hiddenLoading();
                    }
                });
                popSelf();
            }
        });
    }

    @Override
    public boolean onBackPressed() {
        if (mInputMenu.onBackPressed()) {
            if (mChatType == EaseConstant.CHATTYPE_CHATROOM) {
                EMClient.getInstance().chatroomManager().leaveChatRoom(mToChatUsername);
            }
        }
        return super.onBackPressed();
    }


    @Override
    protected void initData() {
        mConversation = EMClient.getInstance().chatManager().getConversation(mToChatUsername, EaseCommonUtils.getConversationType(mChatType), true);
    }

    @Override
    public void registerEventBus() {
        RxEventBus.getDefault().onReceiveEvent().map(new Func1<Object, EventChatRecordChanged>() {
            @Override
            public EventChatRecordChanged call(Object o) {
                return (EventChatRecordChanged) o;
            }
        }).subscribe(new Action1<EventChatRecordChanged>() {
            @Override
            public void call(EventChatRecordChanged eventChatRecordChanged) {
                EMMessage.ChatType type = eventChatRecordChanged.getChatType();
                if (type != EMMessage.ChatType.Chat) {
                    List<EMMessage> messages = mConversation.getAllMessages();
                    mAdapter.updateData(messages);
                }
            }
        });
    }

    /**
     * 监测群组解散或者被T事件
     */
    private class GroupListener extends EaseGroupRemoveListener {

        @Override
        public void onUserRemoved(final String groupId, String groupName) {
            getActivity().runOnUiThread(new Runnable() {

                public void run() {
                    if (mToChatUsername.equals(groupId)) {
                        AppToast.showCenterText(R.string.you_are_group);
                        popSelf();
                    }
                }
            });
        }

        @Override
        public void onGroupDestroy(final String groupId, String groupName) {
            // 群组解散正好在此页面，提示群组被解散，并finish此页面
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    if (mToChatUsername.equals(groupId)) {
                        AppToast.showCenterText(R.string.the_current_group);
                        popSelf();
                    }
                }
            });
        }

    }

    @Override
    protected void setMsgChatType(EMMessage msg) {
        super.setMsgChatType(msg);
        // 如果是群聊，设置chattype,默认是单聊
        if (mChatType == EaseConstant.CHATTYPE_GROUP) {
            msg.setChatType(EMMessage.ChatType.GroupChat);
        } else if (mChatType == EaseConstant.CHATTYPE_CHATROOM) {
            msg.setChatType(EMMessage.ChatType.ChatRoom);
        }
    }

    protected void showChatRoomToast(final String toastContent) {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(getActivity(), toastContent, Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mGroupListener != null) {
            EMClient.getInstance().groupManager().removeGroupChangeListener(mGroupListener);
        }

        if (mChatType == EaseConstant.CHATTYPE_CHATROOM) {
            EMClient.getInstance().chatroomManager().leaveChatRoom(mToChatUsername);
        }

        if (mChatRoomChangeListener != null) {
            EMClient.getInstance().chatroomManager().removeChatRoomChangeListener(mChatRoomChangeListener);
        }
    }
}
