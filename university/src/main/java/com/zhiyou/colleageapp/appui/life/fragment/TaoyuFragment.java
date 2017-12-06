package com.zhiyou.colleageapp.appui.life.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.zhiyou.colleageapp.R;
import com.zhiyou.colleageapp.appui.BaseFragment;
import com.zhiyou.colleageapp.appui.life.adapter.TaoyuAdapter;
import com.zhiyou.colleageapp.appui.model.Taoyu;
import com.zhiyou.colleageapp.appui.mvpPresenter.PresenterLife;
import com.zhiyou.colleageapp.appui.mvpView.ViewLife;
import com.zhiyou.colleageapp.constants.FragmentTag;
import com.zhiyou.colleageapp.utils.AppLog;
import com.zhiyou.colleageapp.utils.AppToast;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;

/**
 * Created by Administrator on 2016/5/30.
 */
public class TaoyuFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.taoyu_fragment, container, false);
    }

    private ListView mListView;
    private TaoyuAdapter mAdapter;
    private Animation mAnimation;
    private TextView mZanAdd, mZanNum;
    private ImageView mPubIv;


    @Override
    protected void initView(View view) {
        super.initView(view);
        mAppTitleBar.setTitle("淘渔");
        mListView = (ListView) view.findViewById(R.id.app_list_view);
        mAnimation = AnimationUtils.loadAnimation(mContext, R.anim.zan1);
        mAdapter = new TaoyuAdapter(mContext, R.layout.life_taoyu_item, new TaoyuAdapter.Callback() {
            @Override
            public void click(View v) {
                int n = (int) v.getTag();
                zanAdd((ViewGroup) v);
            }
        });
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle mBundle = new Bundle();
                mBundle.putParcelable("taoyu", mAdapter.getItem((int) id));
                mBaseActivity.showFragment(TaoyuDetailFragment.class, FragmentTag.PLAY_2_TAOYUDETAIL, mBundle, true);
            }
        });

        mPubIv = (ImageView) view.findViewById(R.id.taou_fragment_pub);
        mPubIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBaseActivity.showFragment(TaoyuPubFragment.class, FragmentTag.TAOYU_2_PUB, null, true);
            }
        });
        initData();
    }

    private void initData() {
        PresenterLife<List<Taoyu>> mPresenter = new PresenterLife<>(new ViewLife<List<Taoyu>>() {
            @Override
            public void onViewSuccess(List<Taoyu> taoyus) {
                hiddenLoading();
                mAdapter.bindData(taoyus);
            }

            @Override
            public void onViewStart(int textId) {
                showLoading(textId);
            }


            public void onViewComplete() {
                hiddenLoading();
            }

            @Override
            public void onViewError(int textId, Throwable e) {
                hiddenLoading();
                AppLog.instance().e(e);
            }

            @Override
            public void onViewFail(int textId, String msg) {
                hiddenLoading();
                AppToast.showBottom(textId);
            }
        });
        mPresenter.getTaoyuList();
    }

    private void zanAdd(ViewGroup v) {
        mZanAdd = (TextView) v.getChildAt(1);
        mZanNum = (TextView) v.findViewById(R.id.life_common_zannum);
        Observable.timer(500, TimeUnit.MILLISECONDS)
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
}
