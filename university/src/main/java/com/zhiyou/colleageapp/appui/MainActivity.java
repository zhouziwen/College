package com.zhiyou.colleageapp.appui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.hyphenate.EMContactListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.zhiyou.colleageapp.Helper;
import com.zhiyou.colleageapp.R;
import com.zhiyou.colleageapp.application.ColleageApplication;
import com.zhiyou.colleageapp.appui.friend.SingleChatFragment;
import com.zhiyou.colleageapp.appui.friend.FriendFragment;
import com.zhiyou.colleageapp.appui.friend.GroupsChatRecordFragment;
import com.zhiyou.colleageapp.appui.life.LifeFragment;
import com.zhiyou.colleageapp.appui.message.QuickMessageFragment;
import com.zhiyou.colleageapp.appui.model.EaseUI;
import com.zhiyou.colleageapp.appui.person.MineFragment;
import com.zhiyou.colleageapp.appui.school.CampusFragment;
import com.zhiyou.colleageapp.appui.widget.dialog.BaseDialog;
import com.zhiyou.colleageapp.appui.widget.dialog.TextDialog;
import com.zhiyou.colleageapp.constants.EaseConstant;
import com.zhiyou.colleageapp.constants.HttpKey;
import com.zhiyou.colleageapp.httpservice.UrlConstant;
import com.zhiyou.colleageapp.manager.DBManager;
import com.zhiyou.colleageapp.manager.PreferenceManager;
import com.zhiyou.colleageapp.rxeventbus.RxEventBus;
import com.zhiyou.colleageapp.rxeventbus.event.EventContactChange;
import com.zhiyou.colleageapp.rxeventbus.event.EventGroupChanged;
import com.zhiyou.colleageapp.utils.AppToast;

import java.util.List;


/**
 * Created by wujiaolong on 16/5/13.
 *
 */
public class MainActivity extends BaseActivity implements View.OnClickListener {
    private CampusFragment mCampusFragment;
    private LifeFragment lifeFragment;
    private FriendFragment friendFragment;
    private QuickMessageFragment messageFragment;
    private MineFragment mineFragment;
    private FragmentManager mFragmentManager;
    private ImageView mFriendUnreadDot;

    protected static final String TAG = "MainActivity";
    private LocalBroadcastManager mBroadcastManager;
    private BroadcastReceiver mGroupAndContactChangeBroadcastReceiver;
    private RadioButton mTabFriend, mTabCampus, mTabLife, mTabMsg, mTabMine;
    public boolean isConflict = false;
    // 账号被移除
    private boolean isCurrentAccountRemoved = false;
    private TextDialog mAccountConflictDialog, mAccountRemovedDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.getBoolean(EaseConstant.ACCOUNT_REMOVED, false)) {
            // 防止被移除后，没点确定按钮然后按了home键，长期在后台又进app导致的crash
            // 三个fragment里加的判断同理
            Helper.getInstance().logout(false, null);
            finish();
            startActivity(new Intent(this, LoginActivity.class));
            return;
        } else if (savedInstanceState != null && savedInstanceState.getBoolean(EaseConstant.ACCOUNT_CONFLICT, false)) {
            // 防止被T后，没点确定按钮然后按了home键，长期在后台又进app导致的crash
            // 三个fragment里加的判断同理
            finish();
            startActivity(new Intent(this, LoginActivity.class));
            return;
        }
        setContentView(R.layout.activity_main);
        mFragmentManager = getSupportFragmentManager();
        if (getIntent().getBooleanExtra(EaseConstant.ACCOUNT_CONFLICT, false)) {
            showAccountConflictDialog();
        } else if (getIntent().getBooleanExtra(EaseConstant.ACCOUNT_REMOVED, false)) {
            showAccountRemovedDialog();
        }
        initView();
        setListener();
        DBManager.getInstance().update(PreferenceManager.getInstance().getCurrentUsername());
        UrlConstant.UserInfo.signature = HttpKey.APPSECRET;
        EaseUI.getInstance().pushActivity(this);
    }


    private void initView() {
        mFriendUnreadDot = (ImageView) findViewById(R.id.friend_tab_unread_dot);
        mTabCampus = (RadioButton) findViewById(R.id.main_tab_campus);
        mTabLife = (RadioButton) findViewById(R.id.main_tab_life);
        mTabFriend = (RadioButton) findViewById(R.id.main_tab_friend);
        mTabMsg = (RadioButton) findViewById(R.id.main_tab_msg);
        mTabMine = (RadioButton) findViewById(R.id.main_tab_mine);
        mCampusFragment = new CampusFragment();
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.add(R.id.activity_main_framelayout, mCampusFragment);
        transaction.commit();
    }

    private void setListener() {
        mTabCampus.setOnClickListener(this);
        mTabLife.setOnClickListener(this);
        mTabFriend.setOnClickListener(this);
        mTabMsg.setOnClickListener(this);
        mTabMine.setOnClickListener(this);

        initReceiver();
    }

    private void hideAllFragment(FragmentTransaction transaction) {
        transaction.hide(mCampusFragment);
        if (lifeFragment != null) {
            transaction.hide(lifeFragment);
        }
        if (friendFragment != null) {
            transaction.hide(friendFragment);
        }
        if (messageFragment != null) {
            transaction.hide(messageFragment);
        }
        if (mineFragment != null) {
            transaction.hide(mineFragment);
        }
        mTabCampus.setChecked(false);
        mTabLife.setChecked(false);
        mTabFriend.setChecked(false);
        mTabMsg.setChecked(false);
        mTabMine.setChecked(false);
    }


    private void initReceiver() {
        //注册local广播接收者，用于接收helper中发出的群组联系人的变动通知
        registerGroupAndContactChangeBroadcastReceiver();
        EMClient.getInstance().contactManager().setContactListener(new MyContactListener());

    }

    private void registerGroupAndContactChangeBroadcastReceiver() {
        mBroadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(EaseConstant.ACTION_CONTACT_CHANAGED);
        intentFilter.addAction(EaseConstant.ACTION_GROUP_CHANAGED);
        mGroupAndContactChangeBroadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action.equals(EaseConstant.ACTION_GROUP_CHANAGED)) {
                    RxEventBus.getDefault().postEvent(new EventGroupChanged());
                    updateUnreadLabel();
                } else if (action.equals(EaseConstant.ACTION_CONTACT_CHANAGED)) {
                    updateUnreadLabel();
                    RxEventBus.getDefault().postEvent(new EventContactChange());
                }
            }
        };
        mBroadcastManager.registerReceiver(mGroupAndContactChangeBroadcastReceiver, intentFilter);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBroadcastManager.unregisterReceiver(mGroupAndContactChangeBroadcastReceiver);
        mAccountRemovedDialog = null;
        mAccountConflictDialog = null;
    }


    @Override
    protected void onResume() {
        super.onResume();
        EMClient.getInstance().chatManager().addMessageListener(messageListener);
        if (!isConflict && !isCurrentAccountRemoved) {
            updateUnreadLabel();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(EaseConstant.ACCOUNT_CONFLICT, isConflict);
        outState.putBoolean(EaseConstant.ACCOUNT_REMOVED, isCurrentAccountRemoved);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        hiddenLoading();
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            final FragmentManager manager = getSupportFragmentManager();
            List<Fragment> list = manager.getFragments();

            if (list != null && !list.isEmpty()) {
                for (Fragment fg: list
                     ) {
                    if (fg instanceof FriendFragment) {
                        ((FriendFragment) fg).refresh();
                    }
                    if (fg instanceof GroupsChatRecordFragment) {
                        ((GroupsChatRecordFragment) fg).refresh();
                    }
                }
            }

            if (manager.getBackStackEntryCount() == 0) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);

                return true;
            }

        }

        return super.onKeyDown(keyCode, event);
    }


    public void updateUnreadLabel() {
        /**
         * 刷新未读消息数
         */
        boolean isShowNotifyDot = isShowNotifyDot();
        mFriendUnreadDot.setVisibility(isShowNotifyDot ? View.VISIBLE : View.GONE);
    }

    /**
     * 获取未读消息数
     *
     * @return
     */
    public boolean isShowNotifyDot() {
        int unreadCount = EMClient.getInstance().chatManager().getUnreadMsgsCount();
        if (unreadCount > 0) {
            return true;
        }

        unreadCount += DBManager.getInstance().getInviteMessageDao().getUnreadMessagesCount();

        return unreadCount > 0;

    }


    @Override
    public void onClick(View v) {
        clickTab(v.getId());
    }

    public class MyContactListener implements EMContactListener {
        @Override
        public void onContactAdded(String username) {
        }

        @Override
        public void onContactDeleted(final String username) {
            runOnUiThread(new Runnable() {
                public void run() {

                    if (SingleChatFragment.mInstance != null) {
                        String toChatUserName = SingleChatFragment.mInstance.getToChatUsername();
                        if (!TextUtils.isEmpty(toChatUserName) && username.equals(toChatUserName))
                            // TODO:By LongWeiHu 2016/5/27
                            AppToast.showCenterText(R.string.have_you_removed);
                    }
                }
            });
        }

        @Override
        public void onContactInvited(String username, String reason) {
        }

        @Override
        public void onContactAgreed(String username) {
        }

        @Override
        public void onContactRefused(String username) {
        }
    }

    EMMessageListener messageListener = new EMMessageListener() {

        @Override
        public void onMessageReceived(List<EMMessage> messages) {

            // 提示新消息
            for (EMMessage message : messages) {
                Helper.getInstance().getNotifier().onNewMsg(message);

            }
            refreshUIWithMessage();
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
        }

        @Override
        public void onMessageReadAckReceived(List<EMMessage> messages) {
        }

        @Override
        public void onMessageDeliveryAckReceived(List<EMMessage> message) {
        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {
        }
    };

    private void refreshUIWithMessage() {
        runOnUiThread(new Runnable() {
            public void run() {
                // 刷新bottom bar消息未读数
                // 当前页面如果为聊天历史页面，刷新此页面
                if (friendFragment != null) {
                    friendFragment.refresh();
                }
            }
        });
    }

    private void clickTab(int checkId) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        hideAllFragment(transaction);
        //点击某个按钮的时候显示对应fragment
        switch (checkId) {
            case R.id.main_tab_campus:
                transaction.show(mCampusFragment);
                mTabCampus.setChecked(true);
                break;
            case R.id.main_tab_life:
                if (lifeFragment == null) {
                    lifeFragment = new LifeFragment();
                    transaction.add(R.id.activity_main_framelayout, lifeFragment);
                } else {
                    transaction.show(lifeFragment);
                }
                mTabLife.setChecked(true);
                break;
            case R.id.main_tab_friend:
                if (friendFragment == null) {
                    friendFragment = new FriendFragment();
                    transaction.add(R.id.activity_main_framelayout, friendFragment);
                } else {
                    transaction.show(friendFragment);
                }
                mTabFriend.setChecked(true);
                friendFragment.hiddenAppMainTabLoading();
                break;
            case R.id.main_tab_msg:
                if (messageFragment == null) {
                    messageFragment = new QuickMessageFragment();
                    transaction.add(R.id.activity_main_framelayout, messageFragment);
                } else {
                    transaction.show(messageFragment);
                }
                mTabMsg.setChecked(true);
                messageFragment.hiddenAppMainTabLoading();
                break;
            case R.id.main_tab_mine:
                if (mineFragment == null) {
                    mineFragment = new MineFragment();
                    transaction.add(R.id.activity_main_framelayout, mineFragment);
                } else {
                    transaction.show(mineFragment);
                }
                mTabMine.setChecked(true);
                mineFragment.hiddenAppMainTabLoading();
                break;
        }
        transaction.commit();
    }

    private void showAccountConflictDialog() {
        if (mAccountConflictDialog == null) {
            mAccountConflictDialog = new TextDialog(getApplicationContext(), R.string.g_prompt, BaseDialog.EDialogButtonType.BUTTON_ONE);
            mAccountConflictDialog.setRightButtonListener(new BaseDialog.RightListener() {

                @Override
                public void onRightListener() {
                    reLogin();
                    mAccountConflictDialog.dismiss();
                    ColleageApplication.getInstance().setTokenExpired(false);
                }
            });
            mAccountConflictDialog.setMsgText(R.string.global_account_login_in_other_place);
            mAccountConflictDialog.setRight(R.string.l_relogin);
            mAccountConflictDialog.setCancelable(false);
        }

        ColleageApplication.getInstance().setTokenExpired(true);

        if (!mAccountConflictDialog.isDialogShowing()) {
            mAccountConflictDialog.show();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getBooleanExtra(EaseConstant.ACCOUNT_CONFLICT, false)) {
            showAccountConflictDialog();
        } else if (intent.getBooleanExtra(EaseConstant.ACCOUNT_REMOVED, false)) {
            showAccountRemovedDialog();
        }
    }

    /**
     * 帐号被移除的dialog
     */
    private void showAccountRemovedDialog() {
        Helper.getInstance().logout(false, null);
        if (!MainActivity.this.isFinishing()) {
            if (mAccountRemovedDialog == null) {
                mAccountRemovedDialog = new TextDialog(getApplicationContext(), R.string.Remove_the_notification, BaseDialog.EDialogButtonType.BUTTON_ONE);
                mAccountRemovedDialog.setTitle(R.string.em_user_remove);
                mAccountRemovedDialog.setRightButtonListener(new BaseDialog.RightListener() {
                    @Override
                    public void onRightListener() {
                        mAccountRemovedDialog.dismiss();
                        finish();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    }
                });
                mAccountRemovedDialog.setCancelable(false);
                isCurrentAccountRemoved = true;
            }
        }

        if (!mAccountRemovedDialog.isDialogShowing()) {
            mAccountRemovedDialog.show();
        }
    }
}
