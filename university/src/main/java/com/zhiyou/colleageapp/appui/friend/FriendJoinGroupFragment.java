package com.zhiyou.colleageapp.appui.friend;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhiyou.colleageapp.R;
import com.zhiyou.colleageapp.appui.BaseFragment;

/**
 * Create by LongWeiHu on 2016/6/17.
 * 加入某个群的界面
 */
public class FriendJoinGroupFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_friend_join_group, container, false);
    }
}
