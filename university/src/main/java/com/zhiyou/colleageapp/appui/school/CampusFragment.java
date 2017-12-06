package com.zhiyou.colleageapp.appui.school;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhiyou.colleageapp.R;
import com.zhiyou.colleageapp.appui.BaseFragment;
import com.zhiyou.colleageapp.appui.adapter.CampusPagerAdapter;
import com.zhiyou.colleageapp.appui.widget.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wujiaolong on 16/5/13.
 *
 */
public class CampusFragment extends BaseFragment{
    private PagerSlidingTabStrip mPagerSlidingTabStrip;
    private ViewPager mViewPager;
    private List<BaseFragment> mFragments = new ArrayList<>();
    private List<String> mTabNames =new ArrayList<String>();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_campus, container, false);
    }
    @Override
    protected void initView(View view) {
        mTabNames.add(getResources().getString(R.string.school));
        mTabNames.add(getResources().getString(R.string.society));
        mFragments.add(new SchoolFragment());
        mFragments.add(new SocietyFragment());
        mPagerSlidingTabStrip = (PagerSlidingTabStrip) view.findViewById(R.id.campus_sub_tab_strip);
        mViewPager = (ViewPager) view.findViewById(R.id.campus_viewpager);
        mViewPager.setAdapter(new CampusPagerAdapter(getChildFragmentManager(),mTabNames,mFragments));
        mPagerSlidingTabStrip.setViewPager(mViewPager);
        setTabsValue();
        mViewPager.setCurrentItem(0);
    }
    @Override
    protected void setListener() {
        super.setListener();
    }
    @Override
    protected void showUnLoginCover() {
    }
    /**
     * 对PagerSlidingTabStrip的各项属性进行赋值。
     */
    private void setTabsValue() {
        // 设置Tab Indicator的颜色
//        mPagerSlidingTabStrip.setIndicatorColor(getResources().getColor(R.color.main_tab_on));
        //  设置点击Tab时的背景色
        //是否支持动画渐变(颜色渐变和文字大小渐变)
        mPagerSlidingTabStrip.setFadeEnabled(true);
        // 设置最大缩放,是正常状态的0.1倍
//        mPagerSlidingTabStrip.setZoomMax(0.1F);
    }
}
