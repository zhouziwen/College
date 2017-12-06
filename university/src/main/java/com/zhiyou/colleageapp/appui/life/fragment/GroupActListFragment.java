package com.zhiyou.colleageapp.appui.life.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zhiyou.colleageapp.R;
import com.zhiyou.colleageapp.appui.BaseFragment;
import com.zhiyou.colleageapp.appui.life.adapter.GroupActAdapter;
import com.zhiyou.colleageapp.appui.life.adapter.GroupMemberAdapter;
import com.zhiyou.colleageapp.appui.life.adapter.GroupPicAdapter;
import com.zhiyou.colleageapp.appui.widget.listview.HorizontalListView;
import com.zhiyou.colleageapp.constants.FragmentTag;
import com.zhiyou.colleageapp.utils.AppToast;

/**
 * Created by Administrator on 2016/5/30.
 */
public class GroupActListFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_title_listview, container, false);
    }

    private ListView mListView;

    @Override
    protected void initView(View view) {
        super.initView(view);
        mAppTitleBar.setTitle("群组列表");

        mListView = (ListView) view.findViewById(R.id.app_list_view);
        mListView.setAdapter(new GroupActAdapter(mContext, new GroupActAdapter.Callback() {
            @Override
            public void click(View v) {
                int n = (int) v.getTag();
                AppToast.showCenterText("  join in -- " + n);
            }
        }));

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mBaseActivity.showFragment(GroupActFragment.class, FragmentTag.LIFE_2_GROUPACT, null, true);
            }
        });
    }
}
