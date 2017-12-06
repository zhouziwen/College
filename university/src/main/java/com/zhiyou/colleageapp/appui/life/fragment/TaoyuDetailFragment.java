package com.zhiyou.colleageapp.appui.life.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.zhiyou.colleageapp.R;
import com.zhiyou.colleageapp.appui.BaseFragment;
import com.zhiyou.colleageapp.appui.CommonAdapter;
import com.zhiyou.colleageapp.appui.ViewHolder;
import com.zhiyou.colleageapp.appui.model.Taoyu;
import com.zhiyou.colleageapp.appui.model.TaoyuComment;
import com.zhiyou.colleageapp.appui.model.TaoyuDetail;
import com.zhiyou.colleageapp.appui.mvpPresenter.PresenterLife;
import com.zhiyou.colleageapp.appui.mvpView.ViewLife;
import com.zhiyou.colleageapp.utils.AppLog;
import com.zhiyou.colleageapp.utils.AppToast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/30.
 */
public class TaoyuDetailFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_taoyu_detail, container, false);
    }

    private ListView mListView;
    private CommonAdapter<TaoyuComment> mAdapter;
    private Taoyu mTaoyu;

    @Override
    protected void initView(View view) {
        super.initView(view);
        mAppTitleBar.setTitle("淘渔");

        Bundle mBundle = this.getArguments();
        mTaoyu = mBundle.getParcelable("taoyu");

        mListView = (ListView) view.findViewById(R.id.app_list_view);
        mAdapter = new CommonAdapter<TaoyuComment>(mContext, R.layout.common_detail_item) {
            @Override
            public void conver(ViewHolder holder, TaoyuComment taoyuComment) {

            }
        };
        initHeadView();
        mListView.setAdapter(mAdapter);

        PresenterLife<TaoyuDetail> mPresenter = new PresenterLife<>(new ViewLife<TaoyuDetail>() {
            @Override
            public void onViewSuccess(TaoyuDetail taoyuDetail) {
                hiddenLoading();
                mAdapter.bindData(taoyuDetail.getTaoyuComments());
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
        mPresenter.getTaoyuDetail(mTaoyu.getId() + "");
    }

    private void initHeadView() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View mHeadView = inflater.inflate(R.layout.taoyu_detail_head, null);
        mListView.addHeaderView(mHeadView);

        AppLog.instance().d(mTaoyu.toString());
        ((TextView) mHeadView.findViewById(R.id.taoyu_detail_head_nick)).setText(mTaoyu.getUserName());
        ((TextView) mHeadView.findViewById(R.id.taoyu_detail_head_view)).setText(mTaoyu.getComment() + "浏览");
        ((TextView) mHeadView.findViewById(R.id.taoyu_detail_head_time)).setText(mTaoyu.getTime());
        if (mTaoyu.getPrice() != null)
            ((TextView) mHeadView.findViewById(R.id.taoyu_detail_head_price)).setText("￥ " + mTaoyu.getPrice());
        if (mTaoyu.getDes() != null)
            ((TextView) mHeadView.findViewById(R.id.taoyu_detail_head_desc)).setText(mTaoyu.getDes());
        if (mTaoyu.getSchool() != null)
            ((TextView) mHeadView.findViewById(R.id.taoyu_detail_head_school)).setText(mTaoyu.getSchool());
    }
}
