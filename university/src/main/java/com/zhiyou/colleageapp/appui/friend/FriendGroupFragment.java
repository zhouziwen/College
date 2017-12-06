package com.zhiyou.colleageapp.appui.friend;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhiyou.colleageapp.R;
import com.zhiyou.colleageapp.appui.BaseFragment;
import com.zhiyou.colleageapp.appui.adapter.FriendGroupListAdapter;
import com.zhiyou.colleageapp.appui.widget.listview.StickyListHeadersListView;

/**
 * Created by LongWeiHu on 2016/5/20.
 *
 */
public class FriendGroupFragment extends BaseFragment{
    private StickyListHeadersListView mListView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_friend_group, container, false);
    }

    @Override
    protected void showUnLoginCover() {

    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        mListView = (StickyListHeadersListView) view.findViewById(R.id.group_listView);
    }

    @Override
    protected void setListener() {
        super.setListener();

    }


    public void refresh() {
        // TODO:By LongWeiHu 2016/6/2  
    }
}
