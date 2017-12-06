package com.zhiyou.colleageapp.appui.widget;

import android.view.View;
import android.widget.FrameLayout;

import com.zhiyou.colleageapp.R;
import com.zhiyou.colleageapp.appui.BaseActivity;
import com.zhiyou.colleageapp.appui.LoginActivity;
import com.zhiyou.colleageapp.utils.DisplayUtils;


/**
 * PROJECT NAME: FishBone
 * PACKAGE NAME: com.yugusoft.fishbone.ui.libary
 * CREATED BY Longwh ON 2015/9/14 0014.
 * 加载的loading
 */
public class AppCover extends AppLoading{

    private BaseActivity mBaseActivity;
    private View mCenterLayout;
    /**
     * 点击loading的任何位置关闭loading
     */

    public AppCover(BaseActivity activity) {
        super(activity);
        mBaseActivity = activity;
    }

    public AppCover(BaseActivity activity, boolean outsideTouchCancelable) {
        super(activity,outsideTouchCancelable);
        mBaseActivity = activity;
    }


    @Override
    protected void initHeight(int statusBarHeight, FrameLayout.LayoutParams params) {
        params.topMargin = statusBarHeight+DisplayUtils.dip2px(60f); //loadingLayout 距离手机屏幕顶部的距离 = headView + statusBar 的高度之和，这样 headView 上面的按钮就可以点击了
        params.bottomMargin = DisplayUtils.dip2px(60f);//mainActivity底部tab的高度
        mCenterLayout = mLoadingLayout.findViewById(R.id.loading_center_layout);
        mCenterLayout.setVisibility(View.GONE);
    }

    @Override
    protected void setListener() {
        mLoadingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBaseActivity.showActivity(LoginActivity.class, null);
                close();
            }
        });
    }


    /**
     * 打开loading
     */
    @Override
    public void open() {
        mLoadingLayout.setVisibility(View.VISIBLE);
        showLoadingAnim(false);
    }


    /**
     * 关闭loading
     */
    public void close() {
        mLoadingLayout.setVisibility(View.GONE);
    }

}
