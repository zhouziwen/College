package com.zhiyou.colleageapp.appui.widget;

import android.widget.FrameLayout;

import com.zhiyou.colleageapp.appui.BaseActivity;
import com.zhiyou.colleageapp.utils.DisplayUtils;

/**
 * Create by LongWeiHu on 2016/6/16.
 * 主页面显示loading是不能遮挡下面的 tabLayout，此时使用AppMainTabLoading
 */
public class AppMainTabLoading extends AppLoading {
    public AppMainTabLoading(BaseActivity activity) {
        super(activity);
    }

    public AppMainTabLoading(BaseActivity activity, boolean outsideTouchCancelable) {
        super(activity, outsideTouchCancelable);
    }

    @Override
    protected void initHeight(int statusBarHeight, FrameLayout.LayoutParams params) {
        params.topMargin = statusBarHeight+ DisplayUtils.dip2px(60f); //loadingLayout 距离手机屏幕顶部的距离 = headView + statusBar 的高度之和，这样 headView 上面的按钮就可以点击了
        params.bottomMargin = DisplayUtils.dip2px(60f);//mainActivity底部tab的高度
    }
}
