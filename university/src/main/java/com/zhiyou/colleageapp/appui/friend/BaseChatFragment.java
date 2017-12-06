package com.zhiyou.colleageapp.appui.friend;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.util.PathUtil;
import com.zhiyou.colleageapp.Helper;
import com.zhiyou.colleageapp.R;
import com.zhiyou.colleageapp.appui.BaseFragment;
import com.zhiyou.colleageapp.appui.adapter.MessageAdapter;
import com.zhiyou.colleageapp.appui.listener.ChatInputMenuListener;
import com.zhiyou.colleageapp.appui.listener.EaseChatExtendMenuItemClickListener;
import com.zhiyou.colleageapp.appui.listener.EaseVoiceRecorderCallback;
import com.zhiyou.colleageapp.appui.listener.MsgRawClickListener;
import com.zhiyou.colleageapp.appui.model.EaseUI;
import com.zhiyou.colleageapp.appui.mvpPresenter.PresenterConversation;
import com.zhiyou.colleageapp.appui.mvpView.ViewBase;
import com.zhiyou.colleageapp.appui.mvpView.ViewSuccess;
import com.zhiyou.colleageapp.appui.person.Fragment.PersonalInfoFragment;
import com.zhiyou.colleageapp.appui.widget.EaseChatInputMenu;
import com.zhiyou.colleageapp.appui.widget.EaseVoiceRecorderView;
import com.zhiyou.colleageapp.appui.widget.dialog.BaseDialog;
import com.zhiyou.colleageapp.appui.widget.dialog.TextDialog;
import com.zhiyou.colleageapp.appui.widget.emojicon.EaseEmojiconMenu;
import com.zhiyou.colleageapp.constants.EaseConstant;
import com.zhiyou.colleageapp.constants.FragmentTag;
import com.zhiyou.colleageapp.domain.EaseEmoIcon;
import com.zhiyou.colleageapp.domain.EmojiconExampleGroupData;
import com.zhiyou.colleageapp.utils.AppLog;
import com.zhiyou.colleageapp.utils.AppToast;
import com.zhiyou.colleageapp.utils.EaseCommonUtils;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.observers.SafeSubscriber;
import rx.schedulers.Schedulers;

/**
 * Author ： LongWeiHu on 2016/5/19.
 */
public class BaseChatFragment extends BaseFragment {
    protected static final String TAG = "BaseChatFragment";
    protected static final int REQUEST_CODE_MAP = 1;
    protected static final int REQUEST_CODE_CAMERA = 2;
    protected static final int REQUEST_CODE_LOCAL = 3;
    protected static final int REQUEST_CODE_SELECT_FILE = 12;
    /**
     * 传入fragment的参数
     */

    protected String mToChatUsername;
    protected EaseChatInputMenu mInputMenu;
    protected EMConversation mConversation;
    protected File mCameraFile;
    protected EaseVoiceRecorderView mVoiceRecorderView;
    protected PullToRefreshListView mListView;
    protected boolean mIsOnLoading = false, haveMoreData = true;
    protected int mPageSize = 20;

    protected EMMessage contextMenuMessage;
    static final int ITEM_TAKE_PICTURE = 1;
    static final int ITEM_PICTURE = 2;
    static final int ITEM_LOCATION = 3;


    protected boolean mIsMessageListInit;
    protected MyItemClickListener extendMenuItemClickListener;
    private PresenterConversation mPresenterConversation;
    private TextDialog mDialogResendMsg;
    protected MessageAdapter mAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        // 会话人或群组id
        mToChatUsername = mBundle.getString(EaseConstant.CHAT_ENTITY_ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    /**
     * initEase view
     */
    @Override
    protected void initView(View view) {
        super.initView(view);
        final String fromFragmentTag = mBundle.getString(EaseConstant.FRAGMENT_TAG);
        if (!TextUtils.isEmpty(fromFragmentTag) && FragmentTag.FRIEND_INFO.equals(fromFragmentTag)) {
            Subscription subscription = Observable.create(new Observable.OnSubscribe<Object>() {
                @Override
                public void call(Subscriber<? super Object> subscriber) {
                    subscriber.onNext("");
                }
            }).subscribeOn(Schedulers.io())
                    .delay(2100, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<Object>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(Object o) {
                            mBaseActivity.removeFragment(fromFragmentTag);
                        }
                    });
            mCompositeSubscription.add(subscription);
        }
        initTitleBar();
        // 按住说话录音控件
        mVoiceRecorderView = (EaseVoiceRecorderView) view.findViewById(R.id.voice_recorder);
        initListView(view);
        registerExtendMenuItem(view);
        mPresenterConversation = new PresenterConversation(new MyViewBase());
    }

    protected void initTitleBar() {
        mAppTitleBar.setTitle(mToChatUsername);
    }

    @Override
    protected void setListener() {
        super.setListener();
        ((EaseEmojiconMenu) mInputMenu.getEmojiconMenu()).addEmojiconGroup(EmojiconExampleGroupData.getData());
        setInputMenuListener();
        setRefreshLayoutListener();
        // show forward message if the message is not null
        String forwardMsgId = mBundle.getString(EaseConstant.FORWARD_MSG_ID);
        if (forwardMsgId != null) {
            // 发送要转发的消息
            forwardMessage(forwardMsgId);
        }
        //设置list item里的控件的点击事件
        setListItemClickListener();
        mListView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideSoftKeyboard();
                mInputMenu.hideExtendMenuContainer();
                return false;
            }
        });
    }

    private void initListView(View view) {
        mListView = (PullToRefreshListView) view.findViewById(R.id.message_listView);
        mListView.getLoadingLayoutProxy().setReleaseLabel("松开以加载");
        mListView.getLoadingLayoutProxy().setPullLabel("下拉以加载");
        mAdapter = new MessageAdapter(getContext());
        mAdapter.setShowAvatar(true);
        mAdapter.setShowUserNick(true);
        mListView.setAdapter(mAdapter);
        // 获取当前conversation对象
        initData();
        if (mConversation != null) {
            mConversation.markAllMessagesAsRead();
            // 把此会话的未读数置为0
            // 初始化db时，每个conversation加载数目是getChatOptions().getNumberOfMessagesLoaded
            // 这个数目如果比用户期望进入会话界面时显示的个数不一样，就多加载一些
            final List<EMMessage> messages = mConversation.getAllMessages();
            mAdapter.updateData(messages);
        }
        mIsMessageListInit = true;
    }

    protected void initData() {
        mConversation = EMClient.getInstance().chatManager().getConversation(mToChatUsername, EaseCommonUtils.getConversationType(EaseConstant.CHATTYPE_SINGLE), true);
    }


    /**
     * 注册底部菜单扩展栏item; 覆盖此方法时如果不覆盖已有item，item的id需大于3
     */
    protected void registerExtendMenuItem(View view) {
        extendMenuItemClickListener = new MyItemClickListener();
        mInputMenu = (EaseChatInputMenu) view.findViewById(R.id.input_menu);
        // initEase input menu
        mInputMenu.init(null);
        int[] itemStrings = {R.string.attach_take_pic, R.string.attach_picture, R.string.attach_location};
        int[] itemDrawables = {R.drawable.ease_chat_takepic_selector, R.drawable.ease_chat_image_selector,
                R.drawable.ease_chat_location_selector};
        int[] itemIds = {ITEM_TAKE_PICTURE, ITEM_PICTURE, ITEM_LOCATION};
        for (int i = 0; i < itemStrings.length; i++) {
            mInputMenu.registerExtendMenuItem(itemStrings[i], itemDrawables[i], itemIds[i], extendMenuItemClickListener);
        }
    }


    private void setInputMenuListener() {
        mInputMenu.setChatInputMenuListener(new ChatInputMenuListener() {
            @Override
            public void onSendMessage(String content) {
                // 发送文本消息
                sendTextMessage(content);
            }

            @Override
            public boolean onPressToSpeakBtnTouch(View v, MotionEvent event) {
                return mVoiceRecorderView.onPressToSpeakBtnTouch(v, event, new EaseVoiceRecorderCallback() {

                    @Override
                    public void onVoiceRecordComplete(String voiceFilePath, int voiceTimeLength) {
                        // 发送语音消息
                        sendVoiceMessage(voiceFilePath, voiceTimeLength);
                    }
                });
            }

            @Override
            public void onBigExpressionClicked(EaseEmoIcon emojicon) {
                //发送大表情(动态表情)
                sendBigExpressionMessage(emojicon.getName(), emojicon.getIdentityCode());
            }
        });
    }


    protected void setListItemClickListener() {
        mAdapter.setChatRawClickListener(new MsgRawClickListener() {

            @Override
            public void onUserAvatarClick(String username) {
                if (!Helper.getInstance().isSelf(username)) {
                    Bundle bundle = new Bundle();
                    bundle.putString(EaseConstant.CHAT_ENTITY_ID, username);
                    mBaseActivity.showFragment(FragmentFriendInfo.class, FragmentTag.FRIEND_INFO, bundle, true);
                } else {
                    mBaseActivity.showFragment(PersonalInfoFragment.class, FragmentTag.PERSONAL_INFO, null, true);
                }

            }

            @Override
            public void onResendClick(final EMMessage message) {
                if (mDialogResendMsg == null) {
                    mDialogResendMsg = new TextDialog(getContext(), R.string.resend, BaseDialog.EDialogButtonType.BUTTON_TWO);
                    mDialogResendMsg.setMsgText(R.string.confirm_resend);
                    mDialogResendMsg.setLeft(R.string.cancel);
                    mDialogResendMsg.setRight(R.string.ok);
                    mDialogResendMsg.setLeftButtonListener(new BaseDialog.LeftListener() {
                        @Override
                        public void onLeftListener() {
                            mDialogResendMsg.dismiss();
                        }
                    });

                    mDialogResendMsg.setRightButtonListener(new BaseDialog.RightListener() {
                        @Override
                        public void onRightListener() {
                            resendMessage(message);
                            mDialogResendMsg.dismiss();
                        }
                    });
                }

                mDialogResendMsg.show();
            }

            @Override
            public void onBubbleLongClick(EMMessage message) {
                contextMenuMessage = message;
                // TODO:By LongWeiHu 2016/6/15  
            }

            @Override
            public boolean onBubbleClick(EMMessage message) {
                // TODO:By LongWeiHu 2016/6/15  
                return false;
            }
        });
    }


    /**
     * @param msgId :
     */
    private void loadMore(final String msgId, final int pageSize, final PullToRefreshBase<ListView> refreshView) {

        mPresenterConversation.loadMoreMessage(mConversation, msgId, pageSize, new ViewBase() {
            @Override
            public void onViewStart(int textId) {
                mIsOnLoading = true;
            }

            @Override
            public void onViewComplete() {
                if (refreshView != null) {
                    refreshView.onRefreshComplete();
                }
            }

            @Override
            public void onViewError(int textId, Throwable e) {
                if (refreshView != null) {
                    refreshView.onRefreshComplete();
                }
                AppToast.showBottomText(R.string.on_loading_fail, e);
                mIsOnLoading = false;
            }

            @Override
            public void onViewFail(int textId, String msg) {
                if (refreshView != null) {
                    refreshView.onRefreshComplete();
                }
                AppToast.showBottomText(textId, msg);
            }
        }, new ViewSuccess<List<EMMessage>>() {
            @Override
            public void onSuccess(List<EMMessage> messages) {
                if (refreshView != null) {
                    refreshView.onRefreshComplete();
                }
                int size = messages.size();
                if (size > 0) {
                    refreshSeekTo(0);
                }
                haveMoreData = (size >= mPageSize);
                mIsOnLoading = false;
            }
        });
    }

    protected void setRefreshLayoutListener() {

        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                EMMessage message = mAdapter.getItem(0);
                if (message == null) {
                    refreshView.onRefreshComplete();
                    AppLog.instance().e("BaseChatFragment: Error message is Null !!!");
                    return;
                }
                final String msgId = message.getMsgId();
                if (mListView.getRefreshableView().getFirstVisiblePosition() == 0 && !mIsOnLoading && haveMoreData) {
                    loadMore(msgId, mPageSize, refreshView);
                } else {
                    AppToast.showBottom(R.string.no_more_messages);
                }
                refreshView.onRefreshComplete();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_CAMERA) { // 发送照片
                if (mCameraFile != null && mCameraFile.exists())
                    sendImageMessage(mCameraFile.getAbsolutePath());
            } else if (requestCode == REQUEST_CODE_LOCAL) { // 发送本地图片
                if (data != null) {
                    Uri selectedImage = data.getData();
                    if (selectedImage != null) {
                        sendPicByUri(selectedImage);
                    }
                }
            } else if (requestCode == REQUEST_CODE_MAP) { // 地图
                double latitude = data.getDoubleExtra("latitude", 0);
                double longitude = data.getDoubleExtra("longitude", 0);
                String locationAddress = data.getStringExtra("address");
                if (locationAddress != null && !locationAddress.equals("")) {
                    sendLocationMessage(latitude, longitude, locationAddress);
                } else {
                    AppToast.showCenterText(R.string.unable_to_get_loaction);
                }

            }
        }
    }


        EMMessageListener msgListener = new EMMessageListener() {

        @Override
        public void onMessageReceived(List<EMMessage> messages) {

            for (EMMessage message : messages) {
                String username;
                // 群组消息
                if (message.getChatType() == EMMessage.ChatType.GroupChat || message.getChatType() == EMMessage.ChatType.ChatRoom) {
                    username = message.getTo();
                } else {
                    // 单聊消息
                    username = message.getFrom();
                }

                // 如果是当前会话的消息，刷新聊天页面
                if (username.equals(mToChatUsername)) {
                    refreshSelectLast();
                    // 声音和震动提示有新消息
                    EaseUI.getInstance().getNotifier().vibrateAndPlayTone(message);
                } else {
                    // 如果消息不是和当前聊天ID的消息
                    EaseUI.getInstance().getNotifier().onNewMsg(message);
                }
            }
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {

        }

        @Override
        public void onMessageReadAckReceived(List<EMMessage> messages) {
            if (mIsMessageListInit) {
                refreshListView();
            }
        }

        @Override
        public void onMessageDeliveryAckReceived(List<EMMessage> message) {
            if (mIsMessageListInit) {
                refreshListView();
            }
        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {
            if (mIsMessageListInit) {
                refreshListView();
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        if (mIsMessageListInit) {
            refreshListView();
        }
        EMClient.getInstance().chatManager().addMessageListener(msgListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        // unregister this event listener when this activity enters the
        // background
        EMClient.getInstance().chatManager().removeMessageListener(msgListener);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();


    }
    /**
     * 扩展菜单栏item点击事件
     */
    private class MyItemClickListener implements EaseChatExtendMenuItemClickListener {

        @Override
        public void onClick(int itemId, View view) {
            switch (itemId) {
                case ITEM_TAKE_PICTURE: // 拍照
                    selectPicFromCamera();
                    break;
                case ITEM_PICTURE:
                    selectPicFromLocal(); // 图库选择图片
                    break;
                case ITEM_LOCATION: // 位置
                    // TODO: 2016/5/19 地图的activity
//                    startActivityForResult(new Intent(getActivity(), EaseBaiduMapActivity.class), REQUEST_CODE_MAP);
                    break;

                default:
                    break;
            }
        }

    }


    //发送消息方法
    //==========================================================================
    protected void sendTextMessage(String content) {
        System.out.println(content);
        EMMessage message = EMMessage.createTxtSendMessage(content, mToChatUsername);
        sendMessage(message);
    }

    protected void sendBigExpressionMessage(String name, String identityCode) {
        EMMessage message = EaseCommonUtils.createExpressionMessage(mToChatUsername, name, identityCode);
        sendMessage(message);
    }

    protected void sendVoiceMessage(String filePath, int length) {
        EMMessage message = EMMessage.createVoiceSendMessage(filePath, length, mToChatUsername);
        sendMessage(message);
    }

    protected void sendImageMessage(String imagePath) {
        EMMessage message = EMMessage.createImageSendMessage(imagePath, false, mToChatUsername);
        sendMessage(message);
    }

    protected void sendLocationMessage(double latitude, double longitude, String locationAddress) {
        EMMessage message = EMMessage.createLocationSendMessage(latitude, longitude, locationAddress, mToChatUsername);
        sendMessage(message);
    }

    protected void sendVideoMessage(String videoPath, String thumbPath, int videoLength) {
        EMMessage message = EMMessage.createVideoSendMessage(videoPath, thumbPath, videoLength, mToChatUsername);
        sendMessage(message);
    }

    protected void sendFileMessage(String filePath) {
        EMMessage message = EMMessage.createFileSendMessage(filePath, mToChatUsername);
        sendMessage(message);
    }

    protected void sendMessage(EMMessage message) {
        if (message == null) {
            return;
        }
       setMsgChatType(message);
        //发送消息
        EMClient.getInstance().chatManager().sendMessage(message);
        //刷新ui
        if (mIsMessageListInit) {
            refreshSelectLast();
        }
    }

    protected void setMsgChatType(EMMessage msg) {
        msg.setChatType(EMMessage.ChatType.Chat);
    }


    public void resendMessage(EMMessage message) {
        message.setStatus(EMMessage.Status.CREATE);
        EMClient.getInstance().chatManager().sendMessage(message);
        refreshListView();
    }

    //===================================================================================


    /**
     * 根据图库图片uri发送图片
     *
     * @param selectedImage :
     */
    protected void sendPicByUri(Uri selectedImage) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            if (picturePath == null || picturePath.equals("null")) {
                Toast toast = Toast.makeText(getActivity(), R.string.cant_find_pictures, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return;
            }
            sendImageMessage(picturePath);
        } else {
            File file = new File(selectedImage.getPath());
            if (!file.exists()) {
                Toast toast = Toast.makeText(getActivity(), R.string.cant_find_pictures, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return;

            }
            sendImageMessage(file.getAbsolutePath());
        }

    }

    /**
     * 根据uri发送文件
     *
     * @param uri :
     */
    protected void sendFileByUri(Uri uri) {
        String filePath = null;
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor;

            try {
                cursor = getActivity().getContentResolver().query(uri, filePathColumn, null, null, null);
                if (cursor != null) {
                    int column_index = cursor.getColumnIndexOrThrow("_data");
                    if (cursor.moveToFirst()) {
                        filePath = cursor.getString(column_index);
                    }
                    cursor.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            filePath = uri.getPath();
        }
        if (TextUtils.isEmpty(filePath)) {
            return;
        }
        File file = new File(filePath);
        if (!file.exists()) {
            AppToast.showCenterText(R.string.File_does_not_exist);
            return;
        }
        //大于10M不让发送
        if (file.length() > 10 * 1024 * 1024) {
            AppToast.showCenterText(R.string.The_file_is_not_greater_than_10_m);
            return;
        }
        sendFileMessage(filePath);
    }

    /**
     * 照相获取图片
     */
    protected void selectPicFromCamera() {
        if (!EaseCommonUtils.isExitsSdcard()) {
            AppToast.showCenterText(R.string.sd_card_does_not_exist);
            return;
        }

        mCameraFile = new File(PathUtil.getInstance().getImagePath(), EMClient.getInstance().getCurrentUser()
                + System.currentTimeMillis() + ".jpg");
        mCameraFile.getParentFile().mkdirs();
        startActivityForResult(
                new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mCameraFile)),
                REQUEST_CODE_CAMERA);
    }

    /**
     * 从图库获取图片
     */
    protected void selectPicFromLocal() {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");

        } else {
            intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        startActivityForResult(intent, REQUEST_CODE_LOCAL);
    }

    /**
     * 转发消息
     *
     * @param forwardMsgId :
     */
    protected void forwardMessage(String forwardMsgId) {
        final EMMessage forward_msg = EMClient.getInstance().chatManager().getMessage(forwardMsgId);
        EMMessage.Type type = forward_msg.getType();
        switch (type) {
            case TXT:
                if (forward_msg.getBooleanAttribute(EaseConstant.MESSAGE_ATTR_IS_BIG_EXPRESSION, false)) {
                    sendBigExpressionMessage(((EMTextMessageBody) forward_msg.getBody()).getMessage(),
                            forward_msg.getStringAttribute(EaseConstant.MESSAGE_ATTR_EXPRESSION_ID, null));
                } else {
                    // 获取消息内容，发送消息
                    String content = ((EMTextMessageBody) forward_msg.getBody()).getMessage();
                    sendTextMessage(content);
                }
                break;
            case IMAGE:
                // 发送图片
                String filePath = ((EMImageMessageBody) forward_msg.getBody()).getLocalUrl();
                if (filePath != null) {
                    File file = new File(filePath);
                    if (!file.exists()) {
                        // 不存在大图发送缩略图
                        filePath = ((EMImageMessageBody) forward_msg.getBody()).thumbnailLocalPath();
                    }
                    sendImageMessage(filePath);
                }
                break;
            default:
                break;
        }

        if (forward_msg.getChatType() == EMMessage.ChatType.ChatRoom) {
            EMClient.getInstance().chatroomManager().leaveChatRoom(forward_msg.getTo());
        }
    }

    public String getToChatUsername() {
        return mToChatUsername;
    }

    public void refreshListView() {
        refreshSeekTo(-1);
    }

    /**
     * 刷新页面, 选择最后一个
     */
    public void refreshSelectLast() {
        refreshSeekTo(mAdapter.getCount() - 1);
    }

    /**
     * 刷新页面, 选择Position位置
     */
    public void refreshSeekTo(final int position) {
        // UI线程不能直接使用conversation.getAllMessages()
        // 否则在UI刷新过程中，如果收到新的消息，会导致并发问题
        Observable.create(new Observable.OnSubscribe<List<EMMessage>>() {
            @Override
            public void call(Subscriber<? super List<EMMessage>> subscriber) {
                List<EMMessage> mMessageList = mConversation.getAllMessages(); // TODO:By LongWeiHu 2016/6/15
                subscriber.onNext(mMessageList);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SafeSubscriber<>(new Subscriber<List<EMMessage>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        AppLog.instance().e("MessageAdapter: " + e);
                    }

                    @Override
                    public void onNext(List<EMMessage> emMessages) {
                        mConversation.markAllMessagesAsRead();
                        mAdapter.updateData(emMessages);
                        if (position >= 0) {
                            mListView.getRefreshableView().setSelection(position);
                        }
                    }
                }));
    }

    /**
     * 选择文件
     */
    protected void selectFileFromLocal() {
        Intent intent = null;
        if (Build.VERSION.SDK_INT < 19) { //19以后这个api不可用，demo这里简单处理成图库选择图片
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);

        } else {
            intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        startActivityForResult(intent, REQUEST_CODE_SELECT_FILE);
    }
}
