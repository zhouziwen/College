package com.zhiyou.colleageapp.appui.person.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.zhiyou.colleageapp.R;
import com.zhiyou.colleageapp.appui.BaseFragment;
import com.zhiyou.colleageapp.appui.person.adapter.VideoAdapter;
import com.zhiyou.colleageapp.constants.FragmentTag;
import com.zhiyou.colleageapp.utils.AppToast;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;

public class VideoListFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_love_video, container, false);
    }


    private ListView mListView;
    private VideoAdapter mAdapter;
    private Animation mAnimation;
    private TextView mZanAdd, mZanNum;
    private ImageView btnRecordVideo;

    @Override
    protected void initView(View view) {
        super.initView(view);
        mAppTitleBar.setTitle("视频表白");
        mAnimation = AnimationUtils.loadAnimation(mContext, R.anim.zan);

        mListView = (ListView) view.findViewById(R.id.app_list_view);
        mAdapter = new VideoAdapter(mContext, new VideoAdapter.Callback() {
            @Override
            public void click(View v) {
                int n = (int) v.getTag();
                switch (v.getId()) {
                    case R.id.life_common_zan:
                        zanAdd((ViewGroup) v);
                        break;
                    case R.id.life_common_comment:
                        AppToast.showCenterText("  comment  -- " + n);
                        break;
                    case R.id.love_item_avar:
                        AppToast.showCenterText("  avar  -- " + n);
                        break;
                }
            }
        });
        mListView.setAdapter(mAdapter);

        btnRecordVideo = (ImageView) view.findViewById(R.id.love_video_start);
        btnRecordVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBaseActivity.showFragment(RecordVideoFragment.class, FragmentTag.LIST_2_VIDEO, null, true);
            }
        });
    }

    private void zanAdd(ViewGroup v) {
        mZanAdd = (TextView) v.getChildAt(1);
        mZanNum = (TextView) v.findViewById(R.id.life_common_zannum);
        Observable.timer(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mZanAdd.setVisibility(View.VISIBLE);
                        mZanAdd.startAnimation(mAnimation);
                    }
                }).subscribe(new Subscriber<Long>() {
            @Override
            public void onCompleted() {
                mZanNum.setText((Integer.valueOf(mZanNum.getText().toString()) + 1) + "");
                this.unsubscribe();
            }

            @Override
            public void onError(Throwable e) {
                this.unsubscribe();
            }

            @Override
            public void onNext(Long aLong) {
                mZanAdd.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void play(String videoPath, VideoView mVideoView) {
//        mVideoView = (VideoView) view.findViewById(R.id.video_view);
        if (videoPath == null) {
            AppToast.showCenterText("视频尚未录制");
            return;
        }
        // 播放相应的视频
        mVideoView.setMediaController(new MediaController(getActivity()));
        mVideoView.setVideoURI(Uri.parse(videoPath));
        mVideoView.start();
    }

}
