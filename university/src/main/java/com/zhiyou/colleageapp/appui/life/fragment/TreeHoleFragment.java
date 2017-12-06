package com.zhiyou.colleageapp.appui.life.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.zhiyou.colleageapp.R;
import com.zhiyou.colleageapp.appui.BaseFragment;
import com.zhiyou.colleageapp.appui.adapter.listitem.PopItem;
import com.zhiyou.colleageapp.appui.CommonAdapter;
import com.zhiyou.colleageapp.appui.ViewHolder;
import com.zhiyou.colleageapp.appui.life.LinearLayoutManagerWrapper;
import com.zhiyou.colleageapp.appui.model.TreeHole;
import com.zhiyou.colleageapp.appui.mvpPresenter.PresenterLife;
import com.zhiyou.colleageapp.appui.mvpView.ViewLife;
import com.zhiyou.colleageapp.appui.mvpView.ViewSuccess;
import com.zhiyou.colleageapp.appui.person.BaseRecycleAdapter;
import com.zhiyou.colleageapp.appui.person.LoadMoreRecyclerView;
import com.zhiyou.colleageapp.appui.person.SpacesItemDecoration;
import com.zhiyou.colleageapp.appui.widget.PopWindowView;
import com.zhiyou.colleageapp.constants.FragmentTag;
import com.zhiyou.colleageapp.utils.AppLog;
import com.zhiyou.colleageapp.utils.AppToast;
import com.zhiyou.colleageapp.utils.DisplayUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by chuyh on 2016/5/30.
 */
public class TreeHoleFragment extends BaseFragment implements View.OnClickListener {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tree_hole, container, false);
    }

    private PopWindowView mPopWindow;
    private BaseRecycleAdapter<TreeHole> mAdapter;
    private Animation mAnimation;
    private TextView mZanAdd, mZanNum;
    private ImageView mPubIv;

    private LoadMoreRecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private PresenterLife mPresenter;

    @Override
    protected void initView(View view) {
        super.initView(view);
        mAppTitleBar.setTitle("树洞");
        mAppTitleBar.getAction().setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(mContext, R.mipmap.friend_more), null, null, null);
        mAppTitleBar.getAction().setOnClickListener(this);
        mAnimation = AnimationUtils.loadAnimation(mContext, R.anim.zan1);
        mPubIv = (ImageView) view.findViewById(R.id.tree_fragment_pub);
        mPubIv.setOnClickListener(this);
        mPresenter = new PresenterLife(new MyViewBase());

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
        mRecyclerView.setLayoutManager(new LinearLayoutManagerWrapper(mContext, LinearLayoutManager.VERTICAL, false));
        mAdapter = new BaseRecycleAdapter<TreeHole>(mContext, R.layout.life_treehole_item) {
            @Override
            protected void convert(ViewHolder holder, TreeHole treeHole, int position) {
                holder.setClick(R.id.life_common_zan, this, position);

                holder.setText(R.id.item_tree_title, treeHole.getTitle());
                holder.setText(R.id.life_common_zannum, treeHole.getLoveCount() + "");
                holder.setText(R.id.life_common_commentnum, treeHole.getComment() + "");
                if (treeHole.getAuthor() != null)
                    holder.setText(R.id.item_tree_nickname, treeHole.getAuthor());
            }
        };

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
                        Bundle mBundle = new Bundle();
                        mBundle.putParcelable("treehole", mAdapter.getItem(position));
                        mBaseActivity.showFragment(TreeHoleDetailFragment.class, FragmentTag.TREEHOLE_2_DETAIL, mBundle, true);
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
                        mRecyclerView.notifyMoreFinish(false);
                    }
                }, 3000);
            }
        });

        mPresenter.getTreeHoleList(new ViewSuccess<List<TreeHole>>() {
            @Override
            public void onSuccess(List<TreeHole> treeHoles) {
                mAdapter.bindData(treeHoles);
                mRecyclerView.notifyMoreFinish(false);
            }
        });
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.app_title_action:
                showPopWindow(mAppTitleBar.getAction());
                break;
            case R.id.tree_fragment_pub:
                mBaseActivity.showFragment(PubTreeHoleFragment.class, FragmentTag.TREEHOLE_2_PUB, null, true);
                break;
        }
    }

    private void showPopWindow(TextView action) {
        if (mPopWindow == null) {
            mPopWindow = new PopWindowView(getContext());
            mPopWindow.setWidth(DisplayUtils.getWidthPx() / 3);
            List<PopItem> popItemList = new ArrayList<>();
            PopItem itemFriendAdd = new PopItem(getResources().getString(R.string.publish), R.mipmap.publish);
            PopItem itemShake = new PopItem(getResources().getString(R.string.comment), R.mipmap.comment);

            popItemList.add(itemFriendAdd);
            popItemList.add(itemShake);

            mPopWindow.setData(popItemList);
            AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    PopItem popItem = (PopItem) parent.getAdapter().getItem(position);
                    int key = popItem.getImgResId();
                    switch (key) {
                        case R.mipmap.publish:
                            mPresenter.getMyTreeholeList(new ViewSuccess<List<TreeHole>>() {
                                @Override
                                public void onSuccess(List<TreeHole> treeHoles) {
                                    mAdapter.bindData(treeHoles);
                                    mRecyclerView.notifyMoreFinish(false);
                                }
                            });

                            break;
                        case R.mipmap.comment:
                            mPresenter.getComTreeholeList(new ViewSuccess<List<TreeHole>>() {
                                @Override
                                public void onSuccess(List<TreeHole> treeHoles) {
                                    mAdapter.bindData(treeHoles);
                                    mRecyclerView.notifyMoreFinish(false);
                                }
                            });

                            break;
                    }
                    mPopWindow.dismiss();
                }
            };
            mPopWindow.setOnItemClickListener(listener);
        }
        mPopWindow.show(action);
    }
}
