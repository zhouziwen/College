package com.zhiyou.colleageapp.appui;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import com.zhiyou.colleageapp.Helper;
import com.zhiyou.colleageapp.R;
import com.zhiyou.colleageapp.appui.mvpView.ViewBase;
import com.zhiyou.colleageapp.appui.widget.AppMainTabLoading;
import com.zhiyou.colleageapp.appui.widget.AppTitleBar;
import com.zhiyou.colleageapp.utils.AppLog;
import com.zhiyou.colleageapp.utils.AppToast;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;
/**
 * Author ： LongWeiHu on 2016/5/17.
 */
public class BaseFragment extends Fragment {
    protected InputMethodManager inputMethodManager;
    protected BaseActivity mBaseActivity;
    protected Bundle mBundle;
    protected Context mContext;
    protected CompositeSubscription mCompositeSubscription;
    protected AppTitleBar mAppTitleBar;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBundle = getArguments();
        if (mBundle == null) {
            mBundle = new Bundle();
        }
        mCompositeSubscription = new CompositeSubscription();
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = getActivity().getApplicationContext();
        inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        mBaseActivity = (BaseActivity) getActivity();
        view.setClickable(true);
        initView(view);
        setListener();
    }
    @Override
    public void onResume() {
        super.onResume();
        showUnLoginCover();
    }
    protected void refresh() {
    }
    protected void addSubscription(Subscription subscription) {
        if (mCompositeSubscription != null) {
            mCompositeSubscription.add(subscription);
        }
    }
    /**
     * 当用户未登录时点击则跳转到登录界面
     */
    protected void showUnLoginCover() {
        if (Helper.getInstance().isTempUser()) {
            mBaseActivity.showAppCover();
        }
    }
    protected void initView(View view) {
        mAppTitleBar = (AppTitleBar) view.findViewById(R.id.app_title_bar);
    }
    protected void setListener() {
        if (mAppTitleBar != null) {
            mAppTitleBar.getBack().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hiddenLoading();
                    popSelf();
                }
            });
        }
    }
    protected void hideSoftKeyboard() {
        if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getActivity().getCurrentFocus() != null)
                if (inputMethodManager != null) {
                    inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }
        }
    }
    protected void setKeyboardAdjustSize() {
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }
    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
    }
    /**
     * 返回键处理
     *
     * @return
     */
    public boolean onBackPressed() {
        hiddenLoading();
        return false;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        hiddenLoading();
        if (!mCompositeSubscription.isUnsubscribed()) {
            mCompositeSubscription.unsubscribe();
        }
    }
    /**
     * 关闭Framgment
     */
    public void popSelf() {
        popBackStack();
        hideSoftKeyboard();
    }
    public void popSelf(String tag) {
        try {
            if (isDetached() || isRemoving() || getFragmentManager() == null) {
                return;
            }
            if (isResumed()) {
                getFragmentManager().popBackStackImmediate(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
            hideSoftKeyboard();
        } catch (Exception e) {
            AppLog.instance().e(e.getMessage());
        }
    }
    /**
     * 回退当前的Fragment并且不关闭软键盘
     */
    public void popSelfNoHiddenKeyBoard() {
        popBackStack();
    }

    private void popBackStack() {
        try {
            if (isDetached() || isRemoving() || getFragmentManager() == null) {
                return;
            }
            if (isResumed()) {
                getFragmentManager().popBackStackImmediate();
            }
        } catch (Exception e) {
            AppLog.instance().e(e.getMessage());
        }
    }
    //===================loading 相关=====================
    public void showLoading() {
        hiddenAppMainTabLoading();
        mBaseActivity.showLoading();
    }
    public void showLoading(int textResId) {
        hiddenAppMainTabLoading();
        mBaseActivity.showLoading(textResId);
    }
    public void hiddenLoading() {
       mBaseActivity.hiddenLoading();
    }
    private AppMainTabLoading mMainTabLoading;
    public void showAppMainTabLoading(int textId) {
        hiddenLoading();
        if (mMainTabLoading == null) {
            mMainTabLoading = new AppMainTabLoading(mBaseActivity);
        }
        mMainTabLoading.open(textId);
    }
    public void hiddenAppMainTabLoading() {
        if (mMainTabLoading != null) {
            mMainTabLoading.close();
        }
    }
    public class MyViewBase implements ViewBase {
        @Override
        public void onViewStart(int textId) {
            showLoading(textId);
        }
        @Override
        public void onViewComplete() {
            hiddenLoading();
        }
        @Override
        public void onViewError(int textId, Throwable e) {
            hiddenLoading();
            AppToast.showBottom(textId);
        }
        @Override
        public void onViewFail(int textId, String msg) {
            hiddenLoading();
            AppToast.showBottomText(textId,msg);
        }
    }
}
