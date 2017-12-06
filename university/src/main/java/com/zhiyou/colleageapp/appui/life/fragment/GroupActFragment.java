package com.zhiyou.colleageapp.appui.life.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.zhiyou.colleageapp.R;
import com.zhiyou.colleageapp.appui.BaseFragment;
import com.zhiyou.colleageapp.appui.life.adapter.GroupMemberAdapter;
import com.zhiyou.colleageapp.appui.life.adapter.GroupPicAdapter;
import com.zhiyou.colleageapp.appui.life.adapter.TaoyuAdapter;
import com.zhiyou.colleageapp.appui.widget.listview.HorizontalInterceptFalseListView;
import com.zhiyou.colleageapp.appui.widget.listview.HorizontalListView;

/**
 * Created by Administrator on 2016/5/30.
 */
public class GroupActFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_group_act_detail, container, false);
    }

    private HorizontalInterceptFalseListView mMemberListView, mPictureListView;

    @Override
    protected void initView(View view) {
        super.initView(view);
        mAppTitleBar.setTitle("群组详情");

        initMemberListView(view);
        initPicListView(view);
    }

    private void initMemberListView(View view) {
        mMemberListView = (HorizontalInterceptFalseListView) view.findViewById(R.id.group_act_detail_mem_listview);

        mMemberListView.setAdapter(new GroupMemberAdapter(mContext));
    }

    private void initPicListView(View view) {
        mPictureListView = (HorizontalInterceptFalseListView) view.findViewById(R.id.group_act_detail_pic_listview);
        mPictureListView.setAdapter(new GroupPicAdapter(mContext));
    }

}
