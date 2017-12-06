package com.zhiyou.colleageapp.appui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.zhiyou.colleageapp.R;
import com.zhiyou.colleageapp.application.ColleageApplication;
import com.zhiyou.colleageapp.appui.friend.GroupChatFragment;
import com.zhiyou.colleageapp.appui.friend.SingleChatFragment;
import com.zhiyou.colleageapp.appui.widget.AppCover;
import com.zhiyou.colleageapp.appui.widget.AppLoading;
import com.zhiyou.colleageapp.appui.widget.dialog.BaseDialog;
import com.zhiyou.colleageapp.appui.widget.dialog.TextDialog;
import com.zhiyou.colleageapp.constants.EaseConstant;
import com.zhiyou.colleageapp.constants.FragmentTag;
import com.zhiyou.colleageapp.utils.AppLog;

import java.util.List;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by wujiaolong on 16/5/13.
 *
 */
public class BaseActivity extends FragmentActivity {
    protected InputMethodManager mInputMethodManager;
    protected CompositeSubscription mCompositeSubscription;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        //http://stackoverflow.com/questions/4341600/how-to-prevent-multiple-instances-of-an-activity-when-it-is-launched-with-differ/
        //理论上应该放在launcher activity,放在基类中所有集成此库的app都可以避免此问题
        registerBroadcastReceiver();
        if (!isTaskRoot()) {
            Intent intent = getIntent();
            String action = intent.getAction();
            if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && action.equals(Intent.ACTION_MAIN)) {
                finish();
                return;
            }
        }
        mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mCompositeSubscription = new CompositeSubscription();
    }
    @Override
    protected void onResume() {
        super.onResume();
        boolean isTokenExpired = ColleageApplication.getInstance().isTokenExpired();
        if (isTokenExpired) {
            showTokenExpireDialog();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCompositeSubscription != null && !mCompositeSubscription.isUnsubscribed()) {
            mCompositeSubscription.unsubscribe();
        }
        unregisterReceiver(mTokenExpireReceiver);
    }
    /**
     * @param beShowedFragment
     * @param fragmentTag ： 该fragment的tag
     * @param bundle :
     * @param addBackStack :
     */
    public void showFragment(Class<? extends BaseFragment> beShowedFragment, String fragmentTag, Bundle bundle, boolean addBackStack) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Fragment fragment = manager.findFragmentByTag(fragmentTag);
        transaction.setCustomAnimations(R.anim.fragment_enter, R.anim.fragment_exit, R.anim.fragment_pop_enter, R.anim.fragment_pop_exist);
        if (fragment != null) {
           omitCircleFragment(fragment,manager);
        }
        try {
            fragment = beShowedFragment.newInstance();
            omitCircleFragment(fragment,manager);
            if (bundle != null) {
                fragment.setArguments(bundle);
            }
            transaction.add(android.R.id.content, fragment, fragmentTag);
        } catch (Exception e) {
            AppLog.instance().e(" showFragment:  "+e);
        }
        if (addBackStack) {
            transaction.addToBackStack(fragmentTag);
        }
        transaction.commit();
        hideSoftKeyboard();
    }
    private void omitCircleFragment(Fragment fragment,FragmentManager manager) {
        if (fragment instanceof SingleChatFragment) {
            manager.popBackStack(FragmentTag.CONTACT_LIST, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            manager.popBackStack(FragmentTag.CHAT_SINGLE, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }else if (fragment instanceof GroupChatFragment) {
            manager.popBackStack(FragmentTag.GROUP_CHAT, 0);
        }
    }
    /**
     * @param activity : 将要被显示的Activity
     * @param data     ： extraData
     */
    public void showActivity(Class<? extends BaseActivity> activity, Bundle data) {
        Intent intent = new Intent(this, activity);
        if (data != null) {
            intent.putExtras(data);
        }
        startActivity(intent);
    }
    protected void hideSoftKeyboard() {
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN && getCurrentFocus() != null) {
            mInputMethodManager.hideSoftInputFromWindow(
                    getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    public Fragment getFragmentByTag(String tag) {
        return getSupportFragmentManager().findFragmentByTag(tag);
    }
    /**
     * 得到当前的fragment
     *
     * @return
     */
    public BaseFragment getCurrentFragment() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null) {
            int size = fragments.size();
            for (int i = size; i > 0; i--) {
                Fragment fragment = fragments.get(i - 1);
                if (fragment instanceof BaseFragment) {
                    return (BaseFragment) fragment;
                }
            }
        }
        return null;
    }
    public Fragment findFragmentByTag(String tag) {
        return getSupportFragmentManager().findFragmentByTag(tag);
    }
    public void removeFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().remove(fragment).commitAllowingStateLoss();
        }
    }
    /**
     * 根据tag移除某个Fragment的方法
     */
    public void removeFragment(String tag) {
        Fragment fragment = findFragmentByTag(tag);
        removeFragment(fragment);
    }
    private AppLoading mAppLoading;
    public void showLoading() {
        if (mAppLoading == null) {
            mAppLoading = new AppLoading(this);
        }
        mAppLoading.open();
    }
    public void showLoading(int textResId) {
        if (mAppLoading == null) {
            mAppLoading = new AppLoading(this);
        }
        mAppLoading.open(textResId);
    }
    public void hiddenLoading() {
        if (mAppLoading != null) {
            mAppLoading.close();
        }
    }
    private AppCover mAppCover;
    public void showAppCover() {
        if (mAppCover == null) {
            mAppCover = new AppCover(this);
        }
        mAppCover.open();
    }
    //=============================================================
    private void registerBroadcastReceiver() {
        IntentFilter tokenFilter = new IntentFilter();//token过期的广播
        tokenFilter.addAction(EaseConstant.ACTION_TOKEN_EXPIRED);
        registerReceiver(mTokenExpireReceiver, tokenFilter);
    }
    private final BroadcastReceiver mTokenExpireReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (!(context instanceof LoginActivity)) {
                showTokenExpireDialog();
            }
        }
    };
    private TextDialog mTokenDialog;
    private void showTokenExpireDialog() {
        if (mTokenDialog == null) {
            mTokenDialog = new TextDialog(this, R.string.g_prompt, BaseDialog.EDialogButtonType.BUTTON_ONE);

            mTokenDialog.setRightButtonListener(new BaseDialog.RightListener() {

                @Override
                public void onRightListener() {
                    reLogin();
                    mTokenDialog.dismiss();
                    mTokenDialog = null;
                    ColleageApplication.getInstance().setTokenExpired(false);
                }
            });
            mTokenDialog.setMsgText(R.string.n_state_unusual);
            mTokenDialog.setRight(R.string.l_relogin);
            mTokenDialog.setCancelable(false);
        }
        ColleageApplication.getInstance().setTokenExpired(true);
        mTokenDialog.show();
    }
    protected void reLogin() {
        showActivity(LoginActivity.class, null);
        finish();
    }
}
