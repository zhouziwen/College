package com.zhiyou.colleageapp.appui.friend;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.zhiyou.colleageapp.Helper;
import com.zhiyou.colleageapp.R;
import com.zhiyou.colleageapp.appui.BaseFragment;
import com.zhiyou.colleageapp.appui.LoginActivity;
import com.zhiyou.colleageapp.appui.adapter.listitem.PopItem;
import com.zhiyou.colleageapp.appui.widget.PopWindowView;
import com.zhiyou.colleageapp.constants.FragmentTag;
import com.zhiyou.colleageapp.utils.DisplayUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wujiaolong on 16/5/13.
 *
 */
public class FriendFragment extends BaseFragment implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {

    private ImageView mScan, mAdd;
    private RadioGroup mTabGroup;
    private ViewPager mViewPager;
    private List<BaseFragment> mFragments = new ArrayList<>();
    private RadioButton mFriendTab, mGroupTab;
    private PopWindowView mPopWindowGroup, mPopWindowFriend;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_friend_layout, container, false);
    }


    @Override
    protected void initView(View view) {
        super.initView(view);
        //顶部标题栏右侧按钮popwindow
        mAdd = (ImageView) view.findViewById(R.id.right_image);
        mScan = (ImageView) view.findViewById(R.id.left_image);

        mViewPager = (ViewPager) view.findViewById(R.id.friend_viewPager);
        mFriendTab = (RadioButton) view.findViewById(R.id.friend_tab);
        mGroupTab = (RadioButton) view.findViewById(R.id.friend_group_tab);
        //好友和群聊fragment切换
        mTabGroup = (RadioGroup) view.findViewById(R.id.friend_radio_group);
        mTabGroup.setOnCheckedChangeListener(this);
        onCheckedChanged(mTabGroup, R.id.friend_tab);
        mFragments.add(new FriendConversationFragment());
        mFragments.add(new FriendGroupFragment());
        mViewPager.setAdapter(new ViewPagerAdapter(getChildFragmentManager()));

    }


    @Override
    protected void setListener() {
        super.setListener();
        mScan.setOnClickListener(this);
        mAdd.setOnClickListener(this);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        mTabGroup.check(R.id.friend_tab);

                        break;
                    case 1:
                        mTabGroup.check(R.id.friend_group_tab);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    protected void showUnLoginCover() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.left_image:
                if (Helper.getInstance().isTempUser()) {
                    mBaseActivity.showActivity(LoginActivity.class, null);
                    break;
                }


                break;
            case R.id.right_image:
                if (Helper.getInstance().isTempUser()) {
                    mBaseActivity.showActivity(LoginActivity.class, null);
                    break;
                }
                int currentPosition = mViewPager.getCurrentItem();
                switch (currentPosition) {
                    case 0:
                        initFriendAddPop();
                        mPopWindowFriend.show(mAdd);
                        break;
                    case 1:
                        initGroupPop();
                        mPopWindowGroup.show(mAdd);
                        break;
                }

                break;
        }
    }

    /**
     *
     */
    private void initFriendAddPop() {
        if (mPopWindowFriend == null) {
            mPopWindowFriend = new PopWindowView(getContext());
            mPopWindowFriend.setWidth(DisplayUtils.getWidthPx() / 5*2);
            List<PopItem> popItemList = new ArrayList<>();
            PopItem itemFriendAdd = new PopItem(getResources().getString(R.string.add_friend), R.mipmap.friend_memu_add);
            PopItem itemShake = new PopItem(getResources().getString(R.string.friend_shake_it_off), R.mipmap.friend_memu_shake);
            PopItem itemCreateFriendCircle = new PopItem(getResources().getString(R.string.friend_create_circle), R.mipmap.friend_memu_quanzi);
            popItemList.add(itemFriendAdd);
            popItemList.add(itemShake);
            popItemList.add(itemCreateFriendCircle);

            mPopWindowFriend.setData(popItemList);
            AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    PopItem popItem = (PopItem) parent.getAdapter().getItem(position);
                    int key = popItem.getImgResId();
                    switch (key) {
                        case R.mipmap.friend_memu_add:
                            mBaseActivity.showFragment(AddContactFragment.class, FragmentTag.FRAGMENT_FRIEND_2_CREATE_GROUP, null, true);
                            break;
                        case R.mipmap.friend_memu_shake:
                            // TODO: By LongWeiHu 2016/5/24
                            break;
                        case R.mipmap.friend_memu_quanzi:
                            // TODO: By LongWeiHu 2016/5/24
                            break;
                    }
                    mPopWindowFriend.dismiss();
                }
            };
            mPopWindowFriend.setOnItemClickListener(listener);
        }

    }

    /**
     *
     */
    private void initGroupPop() {
        if (mPopWindowGroup == null) {
            mPopWindowGroup = new PopWindowView(getContext());
            mPopWindowGroup.setWidth(DisplayUtils.getWidthPx() / 5*2);
            List<PopItem> popItemList = new ArrayList<>();
            PopItem itemCreate = new PopItem(getResources().getString(R.string.create_group), R.mipmap.create_group);
            PopItem itemAdd = new PopItem(getResources().getString(R.string.add_group), R.mipmap.add_group);
            popItemList.add(itemCreate);
            popItemList.add(itemAdd);
            mPopWindowGroup.setData(popItemList);
            AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    PopItem popItem = (PopItem) parent.getAdapter().getItem(position);
                    int key = popItem.getImgResId();
                    switch (key) {
                        case R.mipmap.create_group:
                            mBaseActivity.showFragment(CreateGroupFragment.class, FragmentTag.FRAGMENT_FRIEND_2_CREATE_GROUP, null, true);
                            break;
                        case R.mipmap.add_group:
                            mBaseActivity.showFragment(FriendJoinGroupFragment.class,FragmentTag.JOIN_GROUP,null,true);
                            break;
                    }
                    mPopWindowGroup.dismiss();
                }
            };
            mPopWindowGroup.setOnItemClickListener(listener);
        }

    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.friend_tab:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.friend_group_tab:
                mViewPager.setCurrentItem(1);
                break;
        }
    }


    @Override
    public boolean onBackPressed() {
        return super.onBackPressed();
    }

    @Override
    public void refresh() {
        ((FriendConversationFragment) mFragments.get(0)).refresh();
    }

    public void refreshGroupFragment() {
        ((FriendGroupFragment) mFragments.get(1)).refresh();
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            return super.instantiateItem(container, position);
        }


    }
}
