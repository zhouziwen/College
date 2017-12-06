package com.zhiyou.colleageapp.appui.life.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhiyou.colleageapp.R;
import com.zhiyou.colleageapp.appui.BaseFragment;
import com.zhiyou.colleageapp.appui.model.PartTime;
import com.zhiyou.colleageapp.appui.mvpPresenter.PresenterLife;
import com.zhiyou.colleageapp.appui.mvpView.ViewSuccess;

/**
 * Created by Administrator on 2016/5/30.
 */
public class OfferDetailFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_offer_detail, container, false);
    }

    private PartTime mPartTime;
    private View mView;
    private String id;

    @Override
    protected void initView(View view) {
        super.initView(view);
        mView = view;
        mAppTitleBar.setTitle("兼职");

        id = getArguments().getString("id");
        PresenterLife presenterLife = new PresenterLife(new MyViewBase());

        presenterLife.getOfferDetail(id, new ViewSuccess<PartTime>() {
            @Override
            public void onSuccess(PartTime partTime) {
                hiddenLoading();
                mPartTime = partTime;
                if (partTime != null)
                    refresh();
            }
        });
    }

    @Override
    public void refresh() {
        (mView.findViewById(R.id.offer_detail_data_lay)).setVisibility(View.VISIBLE);

        ((TextView) mView.findViewById(R.id.offer_detail_title)).setText(mPartTime.getTitle());
        ((TextView) mView.findViewById(R.id.offer_detail_date)).setText(mPartTime.getDate());
        ((TextView) mView.findViewById(R.id.offer_detail_time)).setText(mPartTime.getTime());
        ((TextView) mView.findViewById(R.id.offer_detail_people)).setText("共" + mPartTime.getPeople() + "人");
        ((TextView) mView.findViewById(R.id.offer_detail_pos)).setText(mPartTime.getMap());
        ((TextView) mView.findViewById(R.id.offer_detail_endtime)).setText(mPartTime.getEndTime());
        ((TextView) mView.findViewById(R.id.offer_detail_desc)).setText(mPartTime.getContent());
        ((TextView) mView.findViewById(R.id.offer_detail_name)).setText(mPartTime.getName());
        ((TextView) mView.findViewById(R.id.offer_detail_tel)).setText(mPartTime.getTel());
        ((TextView) mView.findViewById(R.id.offer_detail_price)).setText(mPartTime.getSalary());
    }

}
