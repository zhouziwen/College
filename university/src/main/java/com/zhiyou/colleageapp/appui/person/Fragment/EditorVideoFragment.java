package com.zhiyou.colleageapp.appui.person.Fragment;

import android.graphics.Bitmap;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.VideoView;

import com.zhiyou.colleageapp.R;
import com.zhiyou.colleageapp.appui.BaseFragment;
import com.zhiyou.colleageapp.constants.FragmentTag;
import com.zhiyou.colleageapp.utils.AppToast;

/**
 * Created by chuyh on 16/5/13.
 */
public class EditorVideoFragment extends BaseFragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_video_editor, container, false);
    }

    private String path;
    private VideoView mVideoView;

    @Override
    protected void initView(View view) {
        super.initView(view);
        mAppTitleBar.setTitle("发表表白");

        Bundle mBundle = this.getArguments();
        if (mBundle == null || mBundle.getString("path") == null) {
            AppToast.showCenterText("文件路径不存在");
            return;
        }
        mBaseActivity.removeFragment(FragmentTag.LIST_2_VIDEO);
        path = mBundle.getString("path");
        AppToast.showCenterText(path);

        mVideoView = (VideoView) view.findViewById(R.id.video_view);
        // 播放相应的视频
        mVideoView.setMediaController(new MediaController(getActivity()));
        mVideoView.setVideoURI(Uri.parse(path));
        mVideoView.start();
        //mVideoView.requestFocus();

        // 通过路径获取第一帧的缩略图并显示
//        Bitmap bitmap = Utils.createVideoThumbnail(path);
//        BitmapDrawable drawable = new BitmapDrawable(bitmap);
//        drawable.setTileModeXY(Shader.TileMode.REPEAT , Shader.TileMode.REPEAT);
//        drawable.setDither(true);
//        btnPlay.setBackgroundDrawable(drawable);
    }
}
