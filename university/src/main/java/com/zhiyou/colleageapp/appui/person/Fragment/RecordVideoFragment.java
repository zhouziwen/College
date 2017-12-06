package com.zhiyou.colleageapp.appui.person.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.zhiyou.colleageapp.R;
import com.zhiyou.colleageapp.appui.BaseFragment;
import com.zhiyou.colleageapp.appui.person.MovieRecorderView;
import com.zhiyou.colleageapp.constants.FragmentTag;
import com.zhiyou.colleageapp.utils.AppToast;

public class RecordVideoFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_record_video, container, false);
    }

    private MovieRecorderView mRecorderView;
    private Button mShootBtn;
    private boolean isFinish = true;

    @Override
    protected void initView(View view) {
        super.initView(view);
        mAppTitleBar.setTitle("视频表白");

        mRecorderView = (MovieRecorderView) view.findViewById(R.id.movieRecorderView);
        mShootBtn = (Button) view.findViewById(R.id.shoot_button);

        mShootBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mRecorderView.record(new MovieRecorderView.OnRecordFinishListener() {
                        @Override
                        public void onRecordFinish() {
                            handler.sendEmptyMessage(1);
                        }
                    });
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (mRecorderView.getTimeCount() > 1)
                        handler.sendEmptyMessage(1);
                    else {
                        if (mRecorderView.getmRecordFile() != null)
                            mRecorderView.getmRecordFile().delete();
                        mRecorderView.stop();
                        AppToast.showCenterText("视频录制时间太短");
                    }
                }
                return true;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        isFinish = true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        isFinish = false;
        mRecorderView.stop();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            finishActivity();
        }
    };

    private void finishActivity() {
        if (isFinish) {
            mRecorderView.stop();
            // 跳转到内容编辑播放页面

//            "path", mRecorderView.getmRecordFile().getAbsolutePath()
//            AppToast.showCenterText(mRecorderView.getmRecordFile().getAbsolutePath());
            Bundle mBundle = new Bundle();
            mBundle.putString("path", mRecorderView.getmRecordFile().getAbsolutePath());
            mBaseActivity.showFragment(EditorVideoFragment.class, FragmentTag.RECORDVIDEO_2_EDITOR, mBundle, true);
        }
    }
}
