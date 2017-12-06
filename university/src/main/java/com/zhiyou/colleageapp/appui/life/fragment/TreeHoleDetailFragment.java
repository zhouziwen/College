package com.zhiyou.colleageapp.appui.life.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhiyou.colleageapp.R;
import com.zhiyou.colleageapp.appui.BaseFragment;
import com.zhiyou.colleageapp.appui.CommonAdapter;
import com.zhiyou.colleageapp.appui.ViewHolder;
import com.zhiyou.colleageapp.appui.model.BlankModel;
import com.zhiyou.colleageapp.appui.model.TreeHole;
import com.zhiyou.colleageapp.appui.model.TreeHoleComment;
import com.zhiyou.colleageapp.appui.model.TreeHoleDetailInfo;
import com.zhiyou.colleageapp.appui.mvpPresenter.PresenterLife;
import com.zhiyou.colleageapp.appui.mvpView.ViewSuccess;
import com.zhiyou.colleageapp.appui.person.BaseRecycleAdapter;
import com.zhiyou.colleageapp.appui.person.LoadMoreRecyclerView;
import com.zhiyou.colleageapp.appui.person.SpacesItemDecoration;
import com.zhiyou.colleageapp.constants.FragmentTag;
import com.zhiyou.colleageapp.utils.AppToast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;

/**
 * Created by Administrator on 2016/5/30.
 */
public class TreeHoleDetailFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_treehole_detail, container, false);
    }

    //输入框两个空间
    private TextView mImputSubmit;
    private TreeHole mTreeHole;
    private Animation mAnimation;
    private TextView mZanAdd, mZanNum;

    private LoadMoreRecyclerView mRecyclerView;
    private BaseRecycleAdapter<TreeHoleComment> mAdapter;
    private PresenterLife mPresenter;

    @Override
    protected void initView(View view) {
        super.initView(view);
        mAppTitleBar.setTitle("树洞详情");
        mAnimation = AnimationUtils.loadAnimation(mContext, R.anim.zan1);
        mPresenter = new PresenterLife(new MyViewBase());
        Bundle mBundle = this.getArguments();
        mTreeHole = mBundle.getParcelable("treehole");

        initHeadView(view);
        mImputSubmit = (TextView) view.findViewById(R.id.input_lay_submit);
        mImputSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppToast.showCenterText("tijiao");
            }
        });

        mRecyclerView = (LoadMoreRecyclerView) view.findViewById(R.id.recycler_view);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mAdapter = new BaseRecycleAdapter<TreeHoleComment>(mContext, R.layout.life_treehole_detail_item) {
            @Override
            protected void convert(ViewHolder holder, TreeHoleComment comment, int position) {
                holder.setClick(R.id.life_common_zan, this, position);
                if (position == 0) {
                    holder.setVisiable(R.id.tree_hole_detail_item_flooriv, View.VISIBLE);
                    holder.setVisiable(R.id.tree_hole_detail_item_floortv, View.INVISIBLE);
                } else {
                    holder.setText(R.id.tree_hole_detail_item_floortv, (position + 1) + "L");
                }
                holder.setText(R.id.tree_detail_item_nick,comment.getName());
                holder.setText(R.id.tree_detail_item_comment,comment.getContent());
            }
        };

        //设置item之间的间隔
//        SpacesItemDecoration decoration = new SpacesItemDecoration(16);
//        mRecyclerView.addItemDecoration(decoration);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseRecycleAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (view.getId()) {
                    case R.id.life_common_zan:
                        zanAdd((ViewGroup) view);
                        break;
                    default:
                        break;
                }
            }
        });

        mPresenter.getTreeHoleCommentList(mTreeHole.getId() + "", new ViewSuccess<TreeHoleDetailInfo>() {
            @Override
            public void onSuccess(TreeHoleDetailInfo treeHoleDetailInfo) {
                mAdapter.bindData(treeHoleDetailInfo.getComments());
                mRecyclerView.notifyMoreFinish(false);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void initHeadView(View view) {
        TextView title = (TextView) view.findViewById(R.id.treehole_detail_head_title);
        TextView nick = (TextView) view.findViewById(R.id.treehole_detail_head_nick);
        TextView zannum = (TextView) view.findViewById(R.id.life_common_zannum);
        TextView commentnum = (TextView) view.findViewById(R.id.life_common_commentnum);
        RelativeLayout zan = (RelativeLayout) view.findViewById(R.id.life_common_zan);
        zan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zanAdd((ViewGroup) v);
            }
        });

        title.setText(mTreeHole.getTitle());
        if (mTreeHole.getAuthor() != null)
            nick.setText(mTreeHole.getAuthor());
        zannum.setText(mTreeHole.getLoveCount() + "");
        commentnum.setText(mTreeHole.getComment() + "");
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
            @SuppressLint("SetTextI18n")
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
