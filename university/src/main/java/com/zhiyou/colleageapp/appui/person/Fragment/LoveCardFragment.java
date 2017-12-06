package com.zhiyou.colleageapp.appui.person.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhiyou.colleageapp.R;
import com.zhiyou.colleageapp.appui.BaseFragment;
import com.zhiyou.colleageapp.appui.person.BaseRecycleAdapter;
import com.zhiyou.colleageapp.appui.person.LoadMoreRecyclerView;
import com.zhiyou.colleageapp.appui.person.SpacesItemDecoration;
import com.zhiyou.colleageapp.appui.person.adapter.LoveCardAdapter;
import com.zhiyou.colleageapp.utils.AppToast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;

/**
 * Created by chuyh on 16/5/13.
 *
 */
public class LoveCardFragment extends BaseFragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_card_love, container, false);
    }

    private LoadMoreRecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private BaseRecycleAdapter<Integer> mAdapter;
    //    private LoveCardAdapter mAdapter;
    private Animation mAnimation;
    private TextView mZanAdd, mZanNum;

    @Override
    protected void initView(View view) {
        super.initView(view);
        mAppTitleBar.setTitle("表白卡");
        mAnimation = AnimationUtils.loadAnimation(mContext, R.anim.zan);

        //下拉刷新，v4包控件SwipeRefreshLayout及下拉刷新事件
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //下拉刷新加载数据，刷新完毕调动  mSwipeRefreshLayout.setRefreshing(false);mRecyclerView.notifyMoreFinish(true);结束刷新
//                mAdapter.addData(createData());
//                mSwipeRefreshLayout.setRefreshing(false);
//                mRecyclerView.notifyMoreFinish(true);
            }
        });

        mRecyclerView = (LoadMoreRecyclerView) view.findViewById(R.id.recycler_view);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mAdapter = new BaseRecycleAdapter<Integer>(mContext, R.layout.item_card_love, createData()) {

            @Override
            protected void convert(ViewHolder holder, Integer integer, int position) {
                ((ImageView) holder.getView(R.id.item_card_love_iv)).setImageResource(integer);
                holder.setClick(R.id.life_common_zan, this, position);
            }
        };
//        mAdapter = new LoveCardAdapter(mContext, createData());
        //设置item之间的间隔
        SpacesItemDecoration decoration = new SpacesItemDecoration(16);
        mRecyclerView.addItemDecoration(decoration);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new BaseRecycleAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (view.getId()) {
                    case R.id.life_common_zan:
                        zanAdd((ViewGroup) view);
                        break;
                    default:
                        AppToast.showCenterText(position + "");
                        break;
                }
            }
        });

        //底部加载，加载完毕调用  mRecyclerView.notifyMoreFinish(true);刷新列表
        mRecyclerView.setAutoLoadMoreEnable(true);
        mRecyclerView.setLoadMoreListener(new LoadMoreRecyclerView.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                mSwipeRefreshLayout.setRefreshing(false);

                mRecyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.addData(createData());
                        mRecyclerView.notifyMoreFinish(true);
                    }
                }, 3000);
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

    private List<Integer> createData() {
        List<Integer> list = new ArrayList<>();
        list.add(R.mipmap.love_card_img1);
        list.add(R.mipmap.love_card_img2);
        list.add(R.mipmap.love_card_img3);
        list.add(R.mipmap.love_card_img4);
        return list;
    }
}
