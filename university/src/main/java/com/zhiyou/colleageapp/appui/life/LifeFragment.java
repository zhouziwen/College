package com.zhiyou.colleageapp.appui.life;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhiyou.colleageapp.R;
import com.zhiyou.colleageapp.appui.BaseFragment;
import com.zhiyou.colleageapp.appui.CommonAdapter;
import com.zhiyou.colleageapp.appui.life.fragment.OfferFragment;
import com.zhiyou.colleageapp.appui.life.fragment.PlayFragment;
import com.zhiyou.colleageapp.appui.life.fragment.SchoolActVoteFragment;
import com.zhiyou.colleageapp.appui.life.fragment.TalkFragment;
import com.zhiyou.colleageapp.appui.life.fragment.TaoyuFragment;
import com.zhiyou.colleageapp.appui.life.fragment.TreeHoleFragment;
import com.zhiyou.colleageapp.appui.listener.ImageCycleViewListener;
import com.zhiyou.colleageapp.appui.model.LifeBanner;
import com.zhiyou.colleageapp.appui.model.LifeInfo;
import com.zhiyou.colleageapp.appui.mvpPresenter.PresenterLife;
import com.zhiyou.colleageapp.appui.mvpView.ViewLife;
import com.zhiyou.colleageapp.appui.widget.CycleViewPager;
import com.zhiyou.colleageapp.constants.FragmentTag;
import com.zhiyou.colleageapp.utils.AppLog;
import com.zhiyou.colleageapp.utils.AppToast;
import com.zhiyou.colleageapp.utils.BannerImageViewUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;

/**
 * Created by wujiaolong on 16/5/13.
 */
public class LifeFragment extends BaseFragment implements View.OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_life_layout, container, false);
    }


    private CycleViewPager mCycleViewPager;
    private LifeAdapter mAdapter;
    private Animation mAnimation;
    private TextView mZanAdd, mZanNum;

    @Override
    protected void initView(View view) {
        super.initView(view);
        mAppTitleBar.setTitle("生活");
        mAppTitleBar.getBack().setVisibility(View.INVISIBLE);
        mAnimation = AnimationUtils.loadAnimation(mContext, R.anim.zan);

        ListView listView = (ListView) view.findViewById(R.id.life_listview);
        listView.addHeaderView(LayoutInflater.from(getContext()).inflate(R.layout.life_fragment_head, null));
        mAdapter = new LifeAdapter(mContext, R.layout.life_fragment_item, new CommonAdapter.Callback() {
            @Override
            public void click(View v) {
                int n = (int) v.getTag();
                switch (v.getId()) {
                    case R.id.life_common_zan:
                        zanAdd((ViewGroup) v);
                        break;
                    case R.id.life_common_share:
                        AppToast.showCenterText("share  position  " + n);
                        break;
                }
            }
        });

        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle mBundle = new Bundle();
                mBundle.putString("activity_id", mAdapter.getItem((int) id).getId() + "");
                mBaseActivity.showFragment(SchoolActVoteFragment.class, FragmentTag.PLAY_2_SCHOOLACT, mBundle, true);
            }
        });

        //listview头部轮播初始化
        mCycleViewPager = (CycleViewPager) getFragmentManager().findFragmentById(R.id.life_fragment_banner);
        initBanner();
        //listview头部part空间初始化点击
        initHeadView(view);

        PresenterLife presenterLife = new PresenterLife<>(new ViewLife<LifeInfo>() {
            @Override
            public void onViewSuccess(LifeInfo lifeInfo) {
                //设置轮播
                setBanner(lifeInfo.getBanners());
                //设置活动首页活动列表
                mAdapter.bindData(lifeInfo.getActivities());
            }

            @Override
            public void onViewStart(int textId) {
                showAppMainTabLoading(textId);
            }


            public void onViewComplete() {
                hiddenAppMainTabLoading();
            }

            @Override
            public void onViewError(int textId, Throwable e) {
                hiddenAppMainTabLoading();
                AppLog.instance().e(e);
//                AppToast.showCenterText(textId, e);
            }

            @Override
            public void onViewFail(int textId, String msg) {
                hiddenAppMainTabLoading();
                AppToast.showBottom(textId);
            }
        });
        presenterLife.getLifeInfo();
    }

    @Override
    protected void showUnLoginCover() {

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

    private void initHeadView(View view) {
        RelativeLayout taoyu = (RelativeLayout) view.findViewById(R.id.life_head_taoyu);
        RelativeLayout play = (RelativeLayout) view.findViewById(R.id.life_head_play);
        RelativeLayout talk = (RelativeLayout) view.findViewById(R.id.life_head_talk);
        RelativeLayout tree = (RelativeLayout) view.findViewById(R.id.life_head_tree);
        RelativeLayout offer = (RelativeLayout) view.findViewById(R.id.life_head_offer);
        taoyu.setOnClickListener(this);
        play.setOnClickListener(this);
        talk.setOnClickListener(this);
        tree.setOnClickListener(this);
        offer.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.life_head_taoyu:
                mBaseActivity.showFragment(TaoyuFragment.class, FragmentTag.LIFE_2_TAOYU, null, true);
                break;
            case R.id.life_head_play:
                mBaseActivity.showFragment(PlayFragment.class, FragmentTag.LIFE_2_PLAY, null, true);
                break;
            case R.id.life_head_talk:
                mBaseActivity.showFragment(TalkFragment.class, FragmentTag.LIFE_2_TALK, null, true);
                break;
            case R.id.life_head_tree:
                mBaseActivity.showFragment(TreeHoleFragment.class, FragmentTag.LIFE_2_TREEHOEL, null, true);
                break;
            case R.id.life_head_offer:
                mBaseActivity.showFragment(OfferFragment.class, FragmentTag.LIFE_2_OFFER, null, true);
                break;
        }
    }

    //由banner工厂生产出来的imageview
    private ImageView bannerImageView;
    private List<ImageView> mImageViews = new ArrayList<>();

    private void initBanner() {
        bannerImageView = BannerImageViewUtils.getImageView(mContext);
        bannerImageView.setImageResource(R.drawable.life_banner);
        mImageViews.add(bannerImageView);

        // 设置循环，在调用setData方法前调用
        mCycleViewPager.setCycle(false);
        // 在加载数据前设置是否循环
        mCycleViewPager.setData(mImageViews, null, null);
        //设置轮播
        mCycleViewPager.setWheel(false);
        //设置圆点指示图标组居中显示，默认靠右
        mCycleViewPager.setIndicatorCenter();
        mImageViews.clear();
    }

    private void setBanner(List<LifeBanner> banners) {
        mImageViews.clear();

        for (LifeBanner banner : banners) {
            bannerImageView = BannerImageViewUtils.getImageView(mContext);
            Glide.with(mContext).load(banner.getImageUrl()).centerCrop()
                    .placeholder(R.mipmap.life_banner)
                    .crossFade().into(bannerImageView);
            mImageViews.add(bannerImageView);
        }

        //重新添加第一个view
        bannerImageView = BannerImageViewUtils.getImageView(mContext);
        Glide.with(mContext).load(banners.get(banners.size() - 1).getImageUrl()).centerCrop()
                .placeholder(R.mipmap.life_banner)
                .crossFade().into(bannerImageView);
        mImageViews.add(0, bannerImageView);
        //重新添加最后一个view
        bannerImageView = BannerImageViewUtils.getImageView(mContext);
        Glide.with(mContext).load(banners.get(0).getImageUrl()).centerCrop()
                .placeholder(R.mipmap.life_banner)
                .crossFade().into(bannerImageView);
        mImageViews.add(bannerImageView);

        // 设置循环，在调用setData方法前调用
        mCycleViewPager.setCycle(true);
        // 在加载数据前设置是否循环
        mCycleViewPager.setData(mImageViews, banners, new ImageCycleViewListener() {
            @Override
            public void onImageClick(Object info, int position, View imageView) {

            }
        });
        //设置轮播
        mCycleViewPager.setWheel(true);
        // 设置轮播时间，默认5000ms
        mCycleViewPager.setTime(2000);
        //设置圆点指示图标组居中显示，默认靠右
        mCycleViewPager.setIndicatorCenter();
    }
}
