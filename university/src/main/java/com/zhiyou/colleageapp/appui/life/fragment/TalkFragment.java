package com.zhiyou.colleageapp.appui.life.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zhiyou.colleageapp.R;
import com.zhiyou.colleageapp.appui.BaseFragment;
import com.zhiyou.colleageapp.appui.life.adapter.TalkAdapter;
import com.zhiyou.colleageapp.appui.life.adapter.TalkHorAdapter;
import com.zhiyou.colleageapp.appui.widget.listview.HorizontalListView;

/**
 * Created by Administrator on 2016/5/30.
 */
public class TalkFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_life_talk, container, false);
    }

    private ListView mListView;
    private HorizontalListView mHorizontalListView;

    @Override
    protected void initView(View view) {
        super.initView(view);
        mAppTitleBar.setTitle("话题圈");

        mListView = (ListView) view.findViewById(R.id.life_talk_listview);
        mHorizontalListView = (HorizontalListView) view.findViewById(R.id.life_talk_horizontalListView);

        mListView.setAdapter(new TalkAdapter(mContext, new TalkAdapter.Callback() {
            @Override
            public void click(View v) {

            }
        }));
        mHorizontalListView.setAdapter(new TalkHorAdapter(mContext, new TalkHorAdapter.Callback() {
            @Override
            public void click(View v) {

            }
        }));

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
    }
}
