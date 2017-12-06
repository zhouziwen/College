package com.zhiyou.colleageapp.appui.person.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.zhiyou.colleageapp.R;
import com.zhiyou.colleageapp.appui.BaseFragment;
import com.zhiyou.colleageapp.constants.FragmentTag;
import com.zhiyou.colleageapp.utils.AppToast;

/**
 * Created by chuyh on 16/5/13.
 */
public class LoveFragment extends BaseFragment implements View.OnClickListener {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mine_love, container, false);
    }

    private LinearLayout love_card, love_video, love_study;

    @Override
    protected void initView(View view) {
        super.initView(view);
        mAppTitleBar.setTitle("表白墙");

        love_card = (LinearLayout) view.findViewById(R.id.love_card);
        love_video = (LinearLayout) view.findViewById(R.id.love_video);
        love_study = (LinearLayout) view.findViewById(R.id.love_study);
    }

    @Override
    protected void setListener() {
        super.setListener();
        love_card.setOnClickListener(this);
        love_video.setOnClickListener(this);
        love_study.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.love_card:
                mBaseActivity.showFragment(LoveCardFragment.class, FragmentTag.LOVE_2_CARD, null, true);
                break;
            case R.id.love_video:
                mBaseActivity.showFragment(VideoListFragment.class, FragmentTag.PLAY_2_SCHOOLACT, null, true);
                break;
            case R.id.love_study:
                AppToast.showCenterText("3");
                break;
        }
    }
}
