package com.zhiyou.colleageapp.appui.mvpPresenter;

import com.zhiyou.colleageapp.appui.mvpMode.ModeUpload;
import com.zhiyou.colleageapp.appui.mvpMode.action.IActSuccess;
import com.zhiyou.colleageapp.appui.mvpView.ViewBase;
import com.zhiyou.colleageapp.appui.mvpView.ViewSuccess;

import java.io.File;
import java.util.List;

/**
 * Create by LongWeiHu on 2016/6/20.
 */
public class PresenterUpload extends PresenterBase {

    private ModeUpload mModeUpload;
    public PresenterUpload(ViewBase viewBase) {
        super(viewBase);
        mModeUpload = new ModeUpload(new MyActions());
    }

    public void uploadFile(File imgFile, final ViewSuccess<List<String>> viewSuccess) {
        mModeUpload.upLoadFile(imgFile, new IActSuccess<List<String>>() {
            @Override
            public void onActSuccess(List<String> urls) {
                viewSuccess.onSuccess(urls);
            }
        });
    }



    @Override
    public void releaseAll() {
        mModeUpload.releaseAll();
    }
}
