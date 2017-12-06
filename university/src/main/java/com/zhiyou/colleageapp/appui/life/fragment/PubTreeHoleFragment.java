package com.zhiyou.colleageapp.appui.life.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.zhiyou.colleageapp.R;
import com.zhiyou.colleageapp.appui.BaseFragment;
import com.zhiyou.colleageapp.appui.life.adapter.TaoyuAdapter;
import com.zhiyou.colleageapp.appui.model.Taoyu;
import com.zhiyou.colleageapp.appui.mvpPresenter.PresenterLife;
import com.zhiyou.colleageapp.appui.mvpView.ViewLife;
import com.zhiyou.colleageapp.constants.FragmentTag;
import com.zhiyou.colleageapp.constants.HttpKey;
import com.zhiyou.colleageapp.httpservice.ApiFactory;
import com.zhiyou.colleageapp.httpservice.ApiResult;
import com.zhiyou.colleageapp.httpservice.UrlConstant;
import com.zhiyou.colleageapp.utils.AppLog;
import com.zhiyou.colleageapp.utils.AppToast;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/5/30.
 */
public class PubTreeHoleFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_editor_treehole, container, false);
    }

    private EditText mContent;
    private String content;

    @Override
    protected void initView(View view) {
        super.initView(view);
        mAppTitleBar.setTitle("发布吐槽");
        mAppTitleBar.getAction().setText("发送");
        mAppTitleBar.getAction().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content = mContent.getText().toString().trim();
                if (!TextUtils.isEmpty(content)) {
                    pubContent(content);
                } else
                    AppToast.showCenterText("请输入吐槽内容");
            }
        });
        mContent = (EditText) view.findViewById(R.id.treehole_edit_content);
    }

    private void pubContent(String content) {
        ApiFactory.getFartory().getLifeService().pubTreeHole(UrlConstant.UserInfo.mFiledMap, content)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ApiResult>() {
                    @Override
                    public void onCompleted() {
                        popSelf();
                    }

                    @Override
                    public void onError(Throwable e) {
                        AppLog.instance().e(e);
                    }

                    @Override
                    public void onNext(ApiResult apiResult) {
                        AppToast.showCenterText(apiResult.getMessage());
                    }
                });
    }
}
