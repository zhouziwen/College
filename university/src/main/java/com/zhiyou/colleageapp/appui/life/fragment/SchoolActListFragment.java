package com.zhiyou.colleageapp.appui.life.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.zhiyou.colleageapp.R;
import com.zhiyou.colleageapp.appui.BaseFragment;
import com.zhiyou.colleageapp.appui.life.adapter.SchoolActAdapter;
import com.zhiyou.colleageapp.appui.model.SchoolActivity;
import com.zhiyou.colleageapp.appui.mvpPresenter.PresenterLife;
import com.zhiyou.colleageapp.appui.mvpView.ViewLife;
import com.zhiyou.colleageapp.appui.widget.PopWindowView;
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
public class SchoolActListFragment extends BaseFragment implements View.OnClickListener {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_title_listview, container, false);
    }

    private ListView mListView;
    private SchoolActAdapter mAdapter;
    private PopWindowView mPopWindow;
    private Animation mAnimation;
    private TextView mZanAdd, mZanNum;

    @Override
    protected void initView(View view) {
        super.initView(view);
        mAppTitleBar.setTitle("校园活动列表");
//        mAppTitleBar.getAction().setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(mContext, R.mipmap.friend_more), null, null, null);
//        mAppTitleBar.getAction().setOnClickListener(this);
        mAnimation = AnimationUtils.loadAnimation(mContext, R.anim.zan);

        mListView = (ListView) view.findViewById(R.id.app_list_view);
        mAdapter = new SchoolActAdapter(mContext, R.layout.school_act_item, new SchoolActAdapter.Callback() {
            @Override
            public void click(View v) {
                int n = (int) v.getTag();
                switch (v.getId()) {
                    case R.id.life_common_zan:
                        zanAdd((ViewGroup) v);
                        break;
                    case R.id.life_common_share:
                        AppToast.showCenterText("share  点击 " + n);
                        break;
                    case R.id.life_common_avar:
                        AppToast.showCenterText("avar 点击 " + n);
                        break;
                }
            }
        });
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle mBundle = new Bundle();
                mBundle.putString("activity_id", mAdapter.getItem((int) id).getId() + "");
                mBaseActivity.showFragment(SchoolActVoteFragment.class, FragmentTag.PLAY_2_SCHOOLACT, mBundle, true);
            }
        });

        initData();
    }


    private void initData() {
        PresenterLife presenterLife = new PresenterLife<>(new ViewLife<List<SchoolActivity>>() {

            @Override
            public void onViewSuccess(List<SchoolActivity> schoolActivities) {
                hiddenLoading();
                mAdapter.bindData(schoolActivities);
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
        presenterLife.getSchoolActList();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.app_title_action:
//                showPopWindow(mAppTitleBar.getAction());
//                break;
        }
    }

//    private void showPopWindow(TextView action) {
//        if (mPopWindow == null) {
//            mPopWindow = new PopWindowView(getContext());
//            mPopWindow.setWidth(DisplayUtils.getWidthPx() / 3);
//            List<PopItem> popItemList = new ArrayList<>();
//            PopItem itemFriendAdd = new PopItem(getResources().getString(R.string.search), R.drawable.search);
//            PopItem itemShake = new PopItem(getResources().getString(R.string.create_groups), R.mipmap.create_group);
//
//            popItemList.add(itemFriendAdd);
//            popItemList.add(itemShake);
//
//            mPopWindow.setData(popItemList);
//            AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    PopItem popItem = (PopItem) parent.getAdapter().getItem(position);
//                    int key = popItem.getImgResId();
//                    switch (key) {
//                        case R.drawable.search:
////                            mBaseActivity.showFragment(AddContactFragment.class, FragmentTag.FRAGMENT_FRIEND_2_CREATE_GROUP, null, true);
//                            AppToast.showCenterText("search");
//                            break;
//                        case R.mipmap.create_group:
//                            AppToast.showCenterText("create group");
//                            break;
//                    }
//                    mPopWindow.dismiss();
//                }
//            };
//            mPopWindow.setOnItemClickListener(listener);
//        }
//        mPopWindow.show(action);
//    }
}
