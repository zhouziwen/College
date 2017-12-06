package com.zhiyou.colleageapp.appui.mvpMode;

import com.zhiyou.colleageapp.R;
import com.zhiyou.colleageapp.appui.mvpMode.action.IActSuccess;
import com.zhiyou.colleageapp.appui.mvpMode.action.IActionBase;
import com.zhiyou.colleageapp.constants.HttpKey;
import com.zhiyou.colleageapp.httpservice.ApiFactory;
import com.zhiyou.colleageapp.httpservice.ApiResult;
import com.zhiyou.colleageapp.httpservice.UrlConstant;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Create by LongWeiHu on 2016/6/20.
 */
public class ModeUpload extends ModeBase {

    public ModeUpload(IActionBase IActionBase) {
        super(IActionBase);
    }

    public void upLoadFile(File file, final IActSuccess<List<String>> iActSuccess) {
        if (file == null ||!file.exists()) {
            mIActionBase.onActFail(R.string.upload_fail,"文件不存在");
            return;
        }
        RequestBody body = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        ApiFactory.getFartory().getFileService().uploadImage(UrlConstant.UserInfo.mFiledMap, file.getName(), body)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mIActionBase.onActStart(R.string.uploading);
                    }
                }).subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<ApiResult<List<String>>>() {
                       @Override
                       public void call(ApiResult<List<String>> listApiResult) {
                           if (listApiResult.getStatus() == HttpKey.SUCCESS) {
                               iActSuccess.onActSuccess(listApiResult.getData());
                           } else {
                               mIActionBase.onActFail(R.string.upload_fail,listApiResult.getMessage());
                           }
                       }
                   },
                new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        mIActionBase.onActError(R.string.upload_fail,throwable);
                    }
                });
    }
}
