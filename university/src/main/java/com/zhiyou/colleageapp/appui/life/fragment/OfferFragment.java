package com.zhiyou.colleageapp.appui.life.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zhiyou.colleageapp.R;
import com.zhiyou.colleageapp.appui.BaseFragment;
import com.zhiyou.colleageapp.appui.CommonAdapter;
import com.zhiyou.colleageapp.appui.ViewHolder;
import com.zhiyou.colleageapp.appui.model.PartTime;
import com.zhiyou.colleageapp.appui.mvpPresenter.PresenterLife;
import com.zhiyou.colleageapp.appui.mvpView.ViewLife;
import com.zhiyou.colleageapp.constants.FragmentTag;
import com.zhiyou.colleageapp.utils.AppLog;
import com.zhiyou.colleageapp.utils.AppToast;

import java.util.List;

/**
 * Created by Administrator on 2016/5/30.
 */
public class OfferFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_title_listview, container, false);
    }

    private ListView mListView;
    private CommonAdapter<PartTime> mAdapter;

    @Override
    protected void initView(View view) {
        super.initView(view);
        mAppTitleBar.setTitle("兼职");
        mListView = (ListView) view.findViewById(R.id.app_list_view);

        mAdapter = new CommonAdapter<PartTime>(mContext, R.layout.life_offer_item) {
            @Override
            public void conver(ViewHolder holder, PartTime partTime) {
                holder.setText(R.id.life_offer_item_title, partTime.getTitle())
                        .setText(R.id.life_offer_item_price, partTime.getSalary())
                        .setText(R.id.life_offer_item_date, partTime.getDate())
                        .setText(R.id.life_offer_item_pos, partTime.getMap());
            }
        };

        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bd = new Bundle();
                bd.putString("id", mAdapter.getItem(position).getID());
                mBaseActivity.showFragment(OfferDetailFragment.class, FragmentTag.PLAY_2_OFFERDETAIL, bd, true);
            }
        });

        PresenterLife presenterLife = new PresenterLife<>(new ViewLife<List<PartTime>>() {

            @Override
            public void onViewSuccess(List<PartTime> partTimes) {
                hiddenLoading();
                mAdapter.bindData(partTimes);
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
//                AppToast.showCenterText(textId, e);
            }

            @Override
            public void onViewFail(int textId, String msg) {
                hiddenLoading();
                AppToast.showBottom(textId);
            }
        });
        presenterLife.getOfferList();
    }
}
