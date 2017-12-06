package com.zhiyou.colleageapp.appui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.zhiyou.colleageapp.appui.BaseFragment;

import java.util.List;


/**
 * 校园tab对应的viewpager的adapter
 */
public class CampusPagerAdapter extends FragmentPagerAdapter {

    private List<String> titles;
    private List<BaseFragment> mFragments;

    public CampusPagerAdapter(FragmentManager fm, List<String> list, List<BaseFragment> fragments) {
        super(fm);
        this.titles = list;
        this.mFragments = fragments;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

    @Override
    public int getCount() {
        return titles.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

    }



    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }
}
