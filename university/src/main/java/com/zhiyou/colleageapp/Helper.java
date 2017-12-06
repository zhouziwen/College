package com.zhiyou.colleageapp;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMContactListener;
import com.hyphenate.EMError;
import com.hyphenate.EMGroupChangeListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessage.ChatType;
import com.hyphenate.chat.EMMessage.Type;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.util.EMLog;
import com.zhiyou.colleageapp.application.ColleageApplication;
import com.zhiyou.colleageapp.appui.MainActivity;
import com.zhiyou.colleageapp.appui.friend.SingleChatFragment;
import com.zhiyou.colleageapp.appui.listener.EaseNotifier;
import com.zhiyou.colleageapp.appui.listener.EaseSettingsProvider;
import com.zhiyou.colleageapp.appui.model.EaseUI;
import com.zhiyou.colleageapp.appui.widget.emojicon.EaseEmoIconGroupEntity;
import com.zhiyou.colleageapp.constants.EaseConstant;
import com.zhiyou.colleageapp.constants.HttpKey;
import com.zhiyou.colleageapp.domain.EaseEmoIcon;
import com.zhiyou.colleageapp.domain.EmojiconExampleGroupData;
import com.zhiyou.colleageapp.domain.FriendGroup;
import com.zhiyou.colleageapp.domain.InviteMessage;
import com.zhiyou.colleageapp.domain.User;
import com.zhiyou.colleageapp.eenum.InviteMsgStatus;
import com.zhiyou.colleageapp.manager.DBManager;
import com.zhiyou.colleageapp.manager.PreferenceManager;
import com.zhiyou.colleageapp.utils.AppLog;
import com.zhiyou.colleageapp.utils.AppToast;
import com.zhiyou.colleageapp.utils.DisplayUtils;
import com.zhiyou.colleageapp.utils.EaseCommonUtils;
import com.zhiyou.colleageapp.utils.ResUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;
public class Helper {
    protected static final String TAG = "Helper";
    /**
     * EMEventListener
     */
    protected EMMessageListener mMessageListener;
    private SortedMap<String, User> mContactMap = new TreeMap<>();
    private SortedMap<String, List<FriendGroup>> mGroupMap = new TreeMap<>();
    private static Helper instance;
    private Model mModel;
    private boolean mIsGroupSynced;
    private boolean mIsWhiteContactSynced;
    private boolean alreadyNotified;
    private boolean misBlackContactSynced;
    private boolean mIsGroupAndContactListenerRegistered;
    private LocalBroadcastManager mBroadcastManager;
    public synchronized static Helper getInstance() {
        if (instance == null) {
            instance = new Helper();
        }
        return instance;
    }
    /**
     * initEase helper
     *
     * @param context application context
     */
    public void initEase(Context context) {
        mModel = new Model();
        EaseUI.getInstance().init(context);
        EMOptions options = new EMOptions();
        // 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);
        //初始化
        EMClient.getInstance().init(context, options);
        //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(true);
        // 设置是否需要已读回执
        options.setRequireAck(true);
        // 设置是否需要已送达回执
        options.setRequireDeliveryAck(false);
        DisplayUtils.init();
    }
    public void initAfterLogin(final String username, String pwd) {
        //初始化PreferenceManager
        PreferenceManager.getInstance().saveUserName(username);
        PreferenceManager.getInstance().savePwd(pwd);
        setEaseUIProviders();
        //设置全局监听
        setGlobalListeners();
        mBroadcastManager = LocalBroadcastManager.getInstance(ColleageApplication.getInstance());
        mGroupMap.put(HttpKey.GROUP_JOIN, new ArrayList<FriendGroup>());
        mGroupMap.put(HttpKey.GROUP_SELF, new ArrayList<FriendGroup>());
    }
    protected void setEaseUIProviders() {
        //不设置，则使用easeui默认的
        EaseUI.getInstance().setSettingsProvider(new EaseSettingsProvider() {
            @Override
            public boolean isSpeakerOpened() {
                return mModel.getSettingMsgSpeaker();
            }
            @Override
            public boolean isMsgVibrateAllowed(EMMessage message) {
                return mModel.getSettingMsgVibrate();
            }
            @Override
            public boolean isMsgSoundAllowed(EMMessage message) {
                return mModel.getSettingMsgSound();
            }
            @Override
            public boolean isMsgNotifyAllowed(EMMessage message) {
                if (message == null) {
                    return mModel.getSettingMsgNotification();
                }
                if (!mModel.getSettingMsgNotification()) {
                    return false;
                } else {
                    //如果允许新消息提示
                    //屏蔽的用户和群组不提示用户
                    String chatUsername;
                    List<String> notNotifyIds;
                    // 获取设置的不提示新消息的用户或者群组ids
                    if (message.getChatType() == ChatType.Chat) {
                        chatUsername = message.getFrom();
                        notNotifyIds = mModel.getDisabledIds();
                    } else {
                        chatUsername = message.getTo();
                        notNotifyIds = mModel.getDisabledGroups();
                    }

                    return notNotifyIds == null || !notNotifyIds.contains(chatUsername);
                }
            }
        });
        //设置表情provider
        EaseUI.getInstance().setEmoIconInfoProvider(new EaseUI.EaseEmoIconInfoProvider() {
            @Override
            public EaseEmoIcon getEmoIconInfo(String emoIconIdentityCode) {
                EaseEmoIconGroupEntity data = EmojiconExampleGroupData.getData();
                for (EaseEmoIcon emoIcon : data.getEmojiconList()) {
                    if (emoIcon.getIdentityCode().equals(emoIconIdentityCode)) {
                        return emoIcon;
                    }
                }
                return null;
            }
            @Override
            public Map<String, Object> getTextEmoIconMapping() {
                //返回文字表情emoji文本和图片(resource id或者本地路径)的映射map
                return null;
            }
        });
        //不设置，则使用easeui默认的
        EaseUI.getInstance().getNotifier().setNotificationInfoProvider(new EaseNotifier.EaseNotificationInfoProvider() {
            @Override
            public String getTitle(EMMessage message) {
                //修改标题,这里使用默认
                return null;
            }
            @Override
            public int getSmallIcon(EMMessage message) {
                //设置小图标，这里为默认
                return 0;
            }
            @Override
            public String getDisplayedText(EMMessage message) {
                // 设置状态栏的消息提示，可以根据message的类型做相应提示
                String ticker = EaseCommonUtils.getMessageDigest(message, ColleageApplication.getInstance());
                if (message.getType() == Type.TXT) {
                    ticker = ticker.replaceAll("\\[.{2,3}\\]", "[表情]");
                }
                User user = getUser(message.getFrom());
                if (user != null) {
                    return getUser(message.getFrom()).getNick() + ": " + ticker;
                } else {
                    return message.getFrom() + ": " + ticker;
                }
            }
            @Override
            public String getLatestText(EMMessage message, int fromUsersNum, int messageNum) {
                return null;
                // return fromUsersNum + "个基友，发来了" + messageNum + "条消息";
            }
            @Override
            public Intent getLaunchIntent(EMMessage message) {
                //设置点击通知栏跳转事件
                Intent intent = new Intent(ColleageApplication.getInstance(), SingleChatFragment.class);

                ChatType chatType = message.getChatType();
                if (chatType == ChatType.Chat) { // 单聊信息
                    intent.putExtra(EaseConstant.CHAT_ENTITY_ID, message.getFrom());
                    intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE);
                } else { // 群聊信息
                    // message.getTo()为群聊id
                    intent.putExtra(EaseConstant.CHAT_ENTITY_ID, message.getTo());
                    if (chatType == ChatType.GroupChat) {
                        intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_GROUP);
                    } else {
                        intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_CHATROOM);
                    }

                }
                return intent;
            }
        });
    }
    /**
     * @return 获取好友邀请消息
     */
    public List<InviteMessage> getInviteMsgList() {
        return mModel.getInviteMsgList();
    }

    /**
     * 设置全局事件监听
     */
    protected void setGlobalListeners() {
        mIsGroupSynced = mModel.isGroupsSynced();
        mIsWhiteContactSynced = mModel.isContactSynced();
        misBlackContactSynced = mModel.isBacklistSynced();
        EMConnectionListener connectionListener = new EMConnectionListener() {
            @Override
            public void onDisconnected(int error) {
                if (error == EMError.USER_REMOVED) {
                    onCurrentAccountRemoved();
                } else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                    onConnectionConflict();
                }
            }
            @Override
            public void onConnected() {

                // in case group and contact were already synced, we supposed to notify sdk we are ready to receive the events
                if (mIsGroupSynced && mIsWhiteContactSynced) {
                    notifyForReceivingEvents();
                }
            }
        };
        //注册连接监听
        EMClient.getInstance().addConnectionListener(connectionListener);
        //注册群组和联系人监听
        registerGroupAndContactListener();
        //注册消息事件监听
        registerEventListener();
    }
    /**
     * 注册群组和联系人监听，由于logout的时候会被sdk清除掉，再次登录的时候需要再注册一下
     */
    public void registerGroupAndContactListener() {
        if (!mIsGroupAndContactListenerRegistered) {
            //注册群组变动监听
            EMClient.getInstance().groupManager().addGroupChangeListener(new MyGroupChangeListener());
            //注册联系人变动监听
            EMClient.getInstance().contactManager().setContactListener(new FriendContactChangeListener());
            mIsGroupAndContactListenerRegistered = true;
        }
    }
    private InviteMessage setInviteMsg(String from, String groupId, String groupName, String reason, InviteMsgStatus status) {
        InviteMessage msg = new InviteMessage();
        msg.setFrom(from);
        msg.setMsgId(UUID.randomUUID().toString());
        msg.setTimeStamp(System.currentTimeMillis());
        msg.setGroupId(groupId);
        msg.setGroupName(groupName);
        msg.setReason(reason);
        msg.setInviteeId(from);
        msg.setMsgStatus(status);
        return msg;
    }
    private class MyGroupChangeListener implements EMGroupChangeListener {
        @Override
        public void onInvitationReceived(String groupId, String groupName, String invitePersonId, String reason) {

            DBManager.getInstance().getInviteMessageDao().deleteByKey(groupId);

            // 用户申请加入群聊
            AppLog.instance().d("收到邀请加入群聊：" + groupName);
            InviteMessage msg = setInviteMsg(invitePersonId, groupId, groupName, reason, InviteMsgStatus.GROUPINVITATION);
            notifyNewInviteMessage(msg);
            mBroadcastManager.sendBroadcast(new Intent(EaseConstant.ACTION_GROUP_CHANAGED));
        }

        @Override
        public void onInvitationAccpted(String groupId, String invitee, String reason) {
            DBManager.getInstance().getInviteMessageDao().deleteByKey(groupId);
            // 对方同意加群邀请
            boolean hasGroup = false;
            EMGroup _group = null;
            for (EMGroup group : EMClient.getInstance().groupManager().getAllGroups()) {
                if (group.getGroupId().equals(groupId)) {
                    hasGroup = true;
                    _group = group;
                    break;
                }
            }
            if (!hasGroup) {
                return;
            }

            InviteMessage msg = setInviteMsg(invitee, groupId, _group.getGroupName(), reason, InviteMsgStatus.GROUPINVITATION_ACCEPTED);
            FriendGroup fg = setFriendGroup(groupId, _group.getGroupName(), false);
            DBManager.getInstance().getGroupDao().insertOrReplace(fg);

            AppLog.instance().d(TAG + "groupId:  " + groupId + "groupName:   " + _group.getGroupName());
            notifyNewInviteMessage(msg);
            mBroadcastManager.sendBroadcast(new Intent(EaseConstant.ACTION_GROUP_CHANAGED));
        }

        @Override
        public void onInvitationDeclined(String groupId, String invitee, String reason) {
            DBManager.getInstance().getInviteMessageDao().deleteByKey(groupId);
            // 对方同意加群邀请
            boolean hasGroup = false;
            EMGroup group = null;
            List<EMGroup> all = EMClient.getInstance().groupManager().getAllGroups();
            for (EMGroup _group : all) {
                if (_group.getGroupId().equals(groupId)) {
                    group = _group;
                    hasGroup = true;
                    break;
                }
            }
            if (!hasGroup) {
                return;
            }

            InviteMessage msg = setInviteMsg(invitee, groupId, group.getGroupName(), reason, InviteMsgStatus.GROUPINVITATION_DECLINED);
            notifyNewInviteMessage(msg);
            mBroadcastManager.sendBroadcast(new Intent(EaseConstant.ACTION_GROUP_CHANAGED));
        }

        @Override
        public void onUserRemoved(String groupId, String groupName) {
            mBroadcastManager.sendBroadcast(new Intent(EaseConstant.ACTION_GROUP_CHANAGED));
        }

        @Override
        public void onGroupDestroy(String groupId, String groupName) {
            // 群被解散
            mBroadcastManager.sendBroadcast(new Intent(EaseConstant.ACTION_GROUP_CHANAGED));
            DBManager.getInstance().getGroupDao().deleteByKey(groupId);
        }

        /**
         * @param groupId       :
         * @param groupName     :
         * @param applyUserName : 申请人的名字
         * @param reason        :
         */
        @Override
        public void onApplicationReceived(String groupId, String groupName, String applyUserName, String reason) {

            // 用户申请加入群聊
            InviteMessage msg = setInviteMsg(applyUserName, groupId, groupName, reason, InviteMsgStatus.BEAPPLYED);

            AppLog.instance().d(TAG + applyUserName + " 申请加入群聊：" + groupName);
            notifyNewInviteMessage(msg);
            mBroadcastManager.sendBroadcast(new Intent(EaseConstant.ACTION_GROUP_CHANAGED));
        }

        /**
         * @param groupId        :
         * @param groupName      :
         * @param acceptUserName 接受人的name
         */
        @Override
        public void onApplicationAccept(String groupId, String groupName, String acceptUserName) {

            String string = ResUtils.getString(R.string.Agreed_to_your_group_chat_application);
            // 加群申请被同意
            EMMessage msg = setMsg(ChatType.GroupChat, acceptUserName, groupId, string);
            // 保存同意消息
            EMClient.getInstance().chatManager().saveMessage(msg);
            // 提醒新消息
            getNotifier().vibrateAndPlayTone(msg);
            DBManager.getInstance().getGroupDao().insert(setFriendGroup(groupId, groupName, false));

            mBroadcastManager.sendBroadcast(new Intent(EaseConstant.ACTION_GROUP_CHANAGED));
        }

        private FriendGroup setFriendGroup(String id, String name, boolean isShield) {
            FriendGroup group = new FriendGroup();
            group.setId(id);
            group.setName(name);
            group.setShield(isShield);
            return group;
        }

        /**
         * @param groupId      :
         * @param groupName    :
         * @param declinerName 拒绝人的名字
         * @param reason       :
         */
        @Override
        public void onApplicationDeclined(String groupId, String groupName, String declinerName, String reason) {
            String str = ResUtils.getString(R.string.decline_to_your_group_chat_application);
            // 加群申请被拒绝
            EMMessage msg = setMsg(ChatType.GroupChat, declinerName, groupId, str);
            // 保存同意消息
            EMClient.getInstance().chatManager().saveMessage(msg);
            // 提醒新消息
            getNotifier().vibrateAndPlayTone(msg);
            mBroadcastManager.sendBroadcast(new Intent(EaseConstant.ACTION_GROUP_CHANAGED));
        }

        @Override
        public void onAutoAcceptInvitationFromGroup(String groupId, String invitePersonId, String inviteMessage) {
            // 被邀请
            EMMessage msg = setMsg(ChatType.GroupChat, invitePersonId, groupId, ResUtils.getString(R.string.Invite_you_to_join_a_group_chat));
            // 保存邀请消息
            EMClient.getInstance().chatManager().saveMessage(msg);
            // 提醒新消息
            getNotifier().vibrateAndPlayTone(msg);
            FriendGroup group = setFriendGroup(groupId, null, false);
            DBManager.getInstance().getGroupDao().insertOrReplace(group);
            //发送local广播
            mBroadcastManager.sendBroadcast(new Intent(EaseConstant.ACTION_GROUP_CHANAGED));
            EMLog.d(TAG, "onAutoAcceptInvitationFromGroup groupId:" + groupId);
        }
    }


    private EMMessage setMsg(ChatType chatType, String fromPersonName, String groupId, String extra) {
        EMMessage msg = EMMessage.createReceiveMessage(Type.TXT);
        msg.setChatType(chatType);
        msg.setFrom(fromPersonName);
        msg.setTo(groupId);
        msg.setMsgId(UUID.randomUUID().toString());
        msg.addBody(new EMTextMessageBody(fromPersonName + " " + extra));
        msg.setStatus(EMMessage.Status.SUCCESS);

        return msg;
    }

    /***
     * 好友变化listener
     */
    public class FriendContactChangeListener implements EMContactListener {

        @Override
        public void onContactAdded(String username) {
            // 保存增加的联系人
            User user = new User(username);
            // 添加好友时可能会回调added方法两次
            if (!mContactMap.containsKey(username)) {
                DBManager.getInstance().getUserDao().insertOrReplaceInTx(user);
            }
            mContactMap.put(username, user);

            //发送好友变动广播
            mBroadcastManager.sendBroadcast(new Intent(EaseConstant.ACTION_CONTACT_CHANAGED));
        }

        @Override
        public void onContactDeleted(String username) {
            // 被删除
            DBManager.getInstance().getUserDao().delete(mContactMap.get(username));
            mContactMap.remove(username);
            DBManager.getInstance().getInviteMessageDao().deleteByFromName(username);
            //发送好友变动广播
            mBroadcastManager.sendBroadcast(new Intent(EaseConstant.ACTION_CONTACT_CHANAGED));
        }

        @Override
        public void onContactInvited(String username, String reason) {
            // 接到邀请的消息，如果不处理(同意或拒绝)，掉线后，服务器会自动再发过来，所以客户端不需要重复提醒
            List<InviteMessage> msgList = mModel.getInviteMsgList();
            for (InviteMessage inviteMessage : msgList) {
                if (inviteMessage.getGroupId() == null && inviteMessage.getFrom().equals(username)) {
                    DBManager.getInstance().getInviteMessageDao().deleteByFromName(username);
                }
            }
            InviteMessage msg = setInviteMsg(username, null, null, reason, InviteMsgStatus.BEINVITEED);
            notifyNewInviteMessage(msg);
            mBroadcastManager.sendBroadcast(new Intent(EaseConstant.ACTION_CONTACT_CHANAGED));
            AppLog.instance().d(TAG + reason);
        }

        @Override
        public void onContactAgreed(String username) {
            List<InviteMessage> msgList = mModel.getInviteMsgList();
            for (InviteMessage inviteMessage : msgList) {
                if (inviteMessage.getFrom().equals(username)) {
                    return;
                }
            }
            // 自己封装的javabean
            InviteMessage msg = setInviteMsg(username, null, null, null, InviteMsgStatus.BEAGREED);
            AppLog.instance().d(TAG + username + "同意了你的好友请求");
            notifyNewInviteMessage(msg);
            mBroadcastManager.sendBroadcast(new Intent(EaseConstant.ACTION_CONTACT_CHANAGED));
        }

        @Override
        public void onContactRefused(String username) {
            InviteMessage msg = setInviteMsg(username, null, null, null, InviteMsgStatus.BEREFUSED);
            notifyNewInviteMessage(msg);
            mBroadcastManager.sendBroadcast(new Intent(EaseConstant.ACTION_CONTACT_CHANAGED));
        }
    }

    /**
     * 保存并提示消息的邀请消息
     *
     * @param msg :
     */
    private void notifyNewInviteMessage(InviteMessage msg) {
        msg.setMsgUnreadCount(1);
        DBManager.getInstance().getInviteMessageDao().insertOrReplaceInTx(msg);
//        //保存未读数，这里没有精确计算
//        DBManager.getInstance().saveUnreadMessageCount(1); // TODO: 2016/5/18 这里需要计算邀请消息的数量
        // 提示有新消息
        getNotifier().vibrateAndPlayTone(null);
    }

    /**
     * 账号在别的设备登录
     */
    protected void onConnectionConflict() {
        Intent intent = new Intent(ColleageApplication.getInstance(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(EaseConstant.ACCOUNT_CONFLICT, true);
        ColleageApplication.getInstance().startActivity(intent);
    }

    /**
     * 账号被移除
     */
    protected void onCurrentAccountRemoved() {
        Intent intent = new Intent(ColleageApplication.getInstance(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(EaseConstant.ACCOUNT_REMOVED, true);
        ColleageApplication.getInstance().startActivity(intent);
    }

    public User getUser(String username) {
        return mContactMap.get(username);
    }


    /**
     * 全局事件监听
     * 因为可能会有UI页面先处理到这个消息，所以一般如果UI页面已经处理，这里就不需要再次处理
     * activityList.size() <= 0 意味着所有页面都已经在后台运行，或者已经离开Activity Stack
     */
    protected void registerEventListener() {
        mMessageListener = new EMMessageListener() {
            private BroadcastReceiver mBroadCastReceiver = null;

            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                for (EMMessage message : messages) {
                    AppLog.instance().d(TAG + "onMessageReceived id : " + message.getMsgId());
                    //应用在后台，不需要刷新UI,通知栏提示新消息
                    if (!EaseUI.getInstance().hasForegroundActivity()) {
                        getNotifier().onNewMsg(message);
                    }
                }
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> messages) {
                for (EMMessage message : messages) {
                    AppLog.instance().d(TAG + "收到透传消息");
                    //获取消息body
                    EMCmdMessageBody cmdMsgBody = (EMCmdMessageBody) message.getBody();
                    final String action = cmdMsgBody.action();//获取自定义action

                    //获取扩展属性 此处省略
                    //message.getStringAttribute("");

                    final String str = ResUtils.getString(R.string.receive_the_passthrough);

                    final String CMD_TOAST_BROADCAST = "hyphenate.demo.cmd.toast";
                    IntentFilter cmdFilter = new IntentFilter(CMD_TOAST_BROADCAST);

                    if (mBroadCastReceiver == null) {
                        mBroadCastReceiver = new BroadcastReceiver() {

                            @Override
                            public void onReceive(Context context, Intent intent) {
                                AppToast.showCenterText(intent.getStringExtra("cmd_value"));
                            }
                        };

                        //注册广播接收者
                        ColleageApplication.getInstance().registerReceiver(mBroadCastReceiver, cmdFilter);
                    }

                    Intent broadcastIntent = new Intent(CMD_TOAST_BROADCAST);
                    broadcastIntent.putExtra("cmd_value", str + action);
                    ColleageApplication.getInstance().sendBroadcast(broadcastIntent, null);
                    AppLog.instance().d(TAG + String.format("透传消息：action:%s,message:%s", action, message.toString()));
                }
            }

            @Override
            public void onMessageReadAckReceived(List<EMMessage> messages) {
                // TODO:By LongWeiHu 2016/6/13
            }

            @Override
            public void onMessageDeliveryAckReceived(List<EMMessage> message) {
            }

            @Override
            public void onMessageChanged(EMMessage message, Object change) {

            }
        };

        EMClient.getInstance().chatManager().addMessageListener(mMessageListener);
    }

    /**
     * 是否登录成功过
     *
     * @return
     */
    public boolean isLoggedIn() {
        return EMClient.getInstance().isLoggedInBefore();
    }

    /**
     * 退出登录
     *
     * @param unbindDeviceToken 是否解绑设备token(使用GCM才有)
     * @param callback          callback
     */
    public void logout(boolean unbindDeviceToken, final EMCallBack callback) {
        AppLog.instance().d(TAG + "logout: " + unbindDeviceToken);
        EMClient.getInstance().logout(unbindDeviceToken, new EMCallBack() {

            @Override
            public void onSuccess() {
                reset();
                if (callback != null) {
                    callback.onSuccess();
                }
                AppLog.instance().d(TAG + "logout: onActSuccess");

            }

            @Override
            public void onProgress(int progress, String status) {
                if (callback != null) {
                    callback.onProgress(progress, status);
                }
            }

            @Override
            public void onError(int code, String error) {
                reset();
                if (callback != null) {
                    callback.onError(code, error);
                }
                AppLog.instance().d(TAG + "logout: onActSuccess");
            }
        });
    }

    /**
     * 获取消息通知类
     *
     * @return
     */
    public EaseNotifier getNotifier() {
        return EaseUI.getInstance().getNotifier();
    }


    /**
     * 设置好友user list到内存中
     */
    public void saveAllContact(List<User> users) {
        if (users == null || users.isEmpty()) {
            return;
        }
        Map<String, User> userList = new HashMap<>();
        for (User user : users) {
            EaseCommonUtils.setUserInitialLetter(user);
            userList.put(user.getUsername(), user);
        }
        // 存入内存
        mContactMap.putAll(userList);
        mModel.saveContactList(users);
        AppLog.instance().d("set contact syn status to true");
    }


    /**
     * 保存单个user
     */
    public synchronized void saveContact(User user) {
        if (user == null) {
            return;
        }
        EaseCommonUtils.setUserInitialLetter(user);
        mContactMap.put(user.getUsername(), user);
        mModel.saveContact(user);
    }

    /**
     * 获取好友list
     *
     * @return :
     */
    public synchronized SortedMap<String, User> getAllContact() {
        return mContactMap;
    }

    public SortedMap<String, User> getAllContactFromDb() {
        mContactMap.putAll(mModel.getContactList());
        return mContactMap;
    }

    public synchronized SortedMap<String, List<FriendGroup>> getAllGroups() {
        return mGroupMap;
    }

    public synchronized void saveAllGroup(Map<String, List<FriendGroup>> map) {
        if (map == null || map.isEmpty()) {
            mGroupMap.clear();
            return;
        }
        mGroupMap.putAll(map);
        if (map.containsKey(HttpKey.GROUP_JOIN)) {
            List<FriendGroup> joinGroups = map.get(HttpKey.GROUP_JOIN);
            if (joinGroups != null) {
                DBManager.getInstance().getGroupDao().insertOrReplaceInTx(joinGroups);
            }
        }
        if (map.containsKey(HttpKey.GROUP_SELF)) {
            List<FriendGroup> manageGroups = map.get(HttpKey.GROUP_SELF);
            if (manageGroups != null) {
                DBManager.getInstance().getGroupDao().insertOrReplaceInTx(manageGroups);
            }
        }
    }

    public void saveMyCreateGroup(FriendGroup group) {
        Helper.getInstance().getAllGroups().get(HttpKey.GROUP_SELF).add(group);
        DBManager.getInstance().getGroupDao().insertOrReplaceInTx(group);
    }

    public synchronized void notifyForReceivingEvents() {
        if (alreadyNotified) {
            return;
        }

        // 通知sdk，UI 已经初始化完毕，注册了相应的receiver和listener, 可以接受broadcast了
        alreadyNotified = true;
    }


    public boolean isGroupSynced() {
        return mIsGroupSynced;
    }

    public void setGroupSynced(boolean groupSynced) {
        mIsGroupSynced = groupSynced;
    }

    public boolean isWhiteContactSynced() {
        return mIsWhiteContactSynced;
    }

    public void setWhiteContactSynced(boolean whiteContactSynced) {
        mIsWhiteContactSynced = whiteContactSynced;
    }

    public boolean isBlackContactSynced() {
        return misBlackContactSynced;
    }

    public void setBlackContactSynced(boolean misBlackContactSynced) {
        this.misBlackContactSynced = misBlackContactSynced;
    }

    /**
     * 提醒用户token过期
     */
    public synchronized void notifyTokenExpired() {
        Context context = ColleageApplication.getInstance();
        Intent myIntent = new Intent();
        myIntent.setPackage(context.getPackageName());
        myIntent.setAction(EaseConstant.ACTION_TOKEN_EXPIRED);
        context.sendBroadcast(myIntent);
    }

    public synchronized void reset() {

        mModel.setGroupsSynced(false);
        mModel.setContactSynced(false);
        mModel.setBlacklistSynced(false);

        mIsGroupSynced = false;
        mIsWhiteContactSynced = false;
        misBlackContactSynced = false;

        alreadyNotified = false;
        mIsGroupAndContactListenerRegistered = false;

        mContactMap.clear();
        mGroupMap.clear();
        DBManager.getInstance().clear();
        PreferenceManager.clear();
    }

    public boolean isTempUser() {
        String currentUser = PreferenceManager.getInstance().getCurrentUsername();
        return PreferenceManager.TEMP.equals(currentUser) || TextUtils.isEmpty(currentUser);
    }

    public boolean isSelf(String userName) {
        return EMClient.getInstance().getCurrentUser().equals(userName);
    }

}
