package com.zhiyou.colleageapp.appui.life.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhiyou.colleageapp.R;
import com.zhiyou.colleageapp.appui.BaseFragment;
import com.zhiyou.colleageapp.appui.CommonAdapter;
import com.zhiyou.colleageapp.appui.ViewHolder;
import com.zhiyou.colleageapp.appui.life.adapter.VoteAdapter;
import com.zhiyou.colleageapp.appui.model.BlankModel;
import com.zhiyou.colleageapp.appui.model.SchoolActComment;
import com.zhiyou.colleageapp.appui.model.SchoolActDetail;
import com.zhiyou.colleageapp.appui.model.SchoolActivity;
import com.zhiyou.colleageapp.appui.mvpPresenter.PresenterLife;
import com.zhiyou.colleageapp.appui.mvpView.ViewLife;
import com.zhiyou.colleageapp.appui.widget.EaseExpandGridView;
import com.zhiyou.colleageapp.utils.AppLog;
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
public class SchoolActVoteFragment extends BaseFragment implements View.OnClickListener {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_title_input_listview, container, false);
    }

    private ListView mListView;
    private CommonAdapter<SchoolActComment> mAdapter;
    private LayoutInflater mInflater;
    private View mHeadView;
    //头部主要你点击控件
    private ImageView mHeadAvar, mHeadShare;
    private RelativeLayout mHeadZan;
    private Button btVote;
    //评论
    private EditText input_lay_edit;
    private TextView input_lay_submit;
    private SchoolActivity mSchoolActivity;
    //评论赞动画
    private Animation mAnimation;
    private TextView mZanAdd, mZanNum;

    @Override
    protected void initView(View view) {
        super.initView(view);
        mAppTitleBar.setTitle("热门活动");
        mAnimation = AnimationUtils.loadAnimation(mContext, R.anim.zan);
        mListView = (ListView) view.findViewById(R.id.app_list_view);

        Bundle mBundle = this.getArguments();
        if (mBundle == null || mBundle.getString("activity_id") == null) {
            popSelf();
            return;
        }
        String activity_id = mBundle.getString("activity_id");
        initData(activity_id);
    }

    private void initData(String activity_id) {
        PresenterLife<SchoolActDetail> mPresenter = new PresenterLife<>(new ViewLife<SchoolActDetail>() {
            @Override
            public void onViewSuccess(SchoolActDetail schoolActDetail) {
                hiddenLoading();
                mSchoolActivity = schoolActDetail.getSchoolActivity();
                initHeadView();
                initListener();
                mAdapter = new CommonAdapter<SchoolActComment>(mContext, R.layout.common_detail_item) {
                    @Override
                    public void conver(ViewHolder holder, SchoolActComment schoolActComment) {

                    }
                };
                mListView.setAdapter(mAdapter);
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
        mPresenter.getSchoolActDetail(activity_id);
    }


    public List<BlankModel> createData() {
        List<BlankModel> list = new ArrayList<>();
        list.add(new BlankModel());
        list.add(new BlankModel());
        list.add(new BlankModel());
        list.add(new BlankModel());
        list.add(new BlankModel());
        return list;
    }


    //评论作为listview的item，主要内容区以headview的形式添加
    //投票部分使用代码加载
    //投票选项是图片还是文字
    //投票是图片的container，一个gridvire
    private EaseExpandGridView mVoteContainer;
    //投票是文字的container，包含一张大图及两个投票选项,tv1,tv2表示两个文字投票选项
    private LinearLayout mVoteContainer2;
    private ImageView mVoteContainer2Bigiv;
    //文字描述
    private TextView mVoteContainer2Tv1, mVoteContainer2Tv2;
    //投票框
    private ImageView mVoteContainer2Iv1, mVoteContainer2Iv2;
    //进度条layout
    private LinearLayout mVoteContainer2ProLay1, mVoteContainer2ProLay2;
    //进度条
    private View mVoteContainer2Pro1, mVoteContainer2Pro2;
    //投票人数
    private TextView mVoteContainer2Count1, mVoteContainer2Count2;

    private Boolean isVote = false;
    private int choiceVote = -1;
    private VoteAdapter mVoteAdapter;
    private ImageView tempImageView;

    private void initHeadView() {
        mInflater = LayoutInflater.from(mContext);
        mHeadView = mInflater.inflate(R.layout.school_actvote_head, null);
        //等待添加的投票部分layout
        btVote = (Button) mHeadView.findViewById(R.id.school_actvote_head_btvote);
        if (isVote) {
            btVote.setVisibility(View.GONE);
        } else {
            btVote.setOnClickListener(this);
        }

        mVoteContainer = (EaseExpandGridView) mHeadView.findViewById(R.id.school_actvote_head_votecontainer);
        mVoteContainer2 = (LinearLayout) mHeadView.findViewById(R.id.school_actvote_head_votecontainer2);
        mVoteContainer2Bigiv = (ImageView) mHeadView.findViewById(R.id.school_actvote_head_bigiv);
        if (mSchoolActivity.getType().equals("1")) {
            //图片式投票
            mVoteContainer2.setVisibility(View.GONE);
            mVoteContainer2Bigiv.setVisibility(View.GONE);
            mVoteAdapter = new VoteAdapter(mContext, R.layout.school_actvote_grid_item, isVote, choiceVote, createData(), new VoteAdapter.Callback() {
                @Override
                public void click(View v) {
                    int n = (int) v.getTag();
                    if (choiceVote != n) {
                        if (tempImageView != null)
                            tempImageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.life_play_select_shape3));
                        tempImageView = (ImageView) v.findViewById(R.id.vote_item_choice);
                        tempImageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.life_play_select_shape4));
                        choiceVote = n;
                    } else {
                        if (tempImageView != null) {
                            tempImageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.life_play_select_shape3));
                            choiceVote = -1;
                        }
                    }
                }
            });
            mVoteContainer.setAdapter(mVoteAdapter);
            mVoteContainer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    AppToast.showCenterText(" --  " + position);
                }
            });
        } else {
            Glide.with(mContext).load(mSchoolActivity.getThumb())
                    .centerCrop()
                    .placeholder(R.mipmap.life_beautiful_girl)
                    .crossFade().into(mVoteContainer2Bigiv);
            //文字式投票
            mVoteContainer.setVisibility(View.GONE);
            //文字描述
            mVoteContainer2Tv1 = (TextView) mHeadView.findViewById(R.id.school_actvote_head_votecontainer2_tv1);
            mVoteContainer2Tv2 = (TextView) mHeadView.findViewById(R.id.school_actvote_head_votecontainer2_tv2);
            //投票框
            mVoteContainer2Iv1 = (ImageView) mHeadView.findViewById(R.id.school_actvote_head_votecontainer2_iv1);
            mVoteContainer2Iv2 = (ImageView) mHeadView.findViewById(R.id.school_actvote_head_votecontainer2_iv2);
            //进度条layout
            mVoteContainer2ProLay1 = (LinearLayout) mHeadView.findViewById(R.id.school_actvote_head_votecontainer2_progresslay1);
            mVoteContainer2ProLay2 = (LinearLayout) mHeadView.findViewById(R.id.school_actvote_head_votecontainer2_progresslay2);
            //投票人数
            mVoteContainer2Count1 = (TextView) mHeadView.findViewById(R.id.school_actvote_head_votecontainer2_count1);
            mVoteContainer2Count2 = (TextView) mHeadView.findViewById(R.id.school_actvote_head_votecontainer2_count2);
            //进度条
            mVoteContainer2Pro1 = mHeadView.findViewById(R.id.school_actvote_head_votecontainer2_progresslay1);
            mVoteContainer2Pro2 = mHeadView.findViewById(R.id.school_actvote_head_votecontainer2_progresslay2);

            mVoteContainer2Iv1.setOnClickListener(this);
            mVoteContainer2Iv2.setOnClickListener(this);
        }
        mListView.addHeaderView(mHeadView);

        mHeadAvar = (ImageView) mHeadView.findViewById(R.id.life_common_avar);
        mHeadShare = (ImageView) mHeadView.findViewById(R.id.life_common_share);
        mHeadZan = (RelativeLayout) mHeadView.findViewById(R.id.life_common_zan);

        ((TextView) mHeadView.findViewById(R.id.life_common_nick)).setText(mSchoolActivity.getNickName());
        ((TextView) mHeadView.findViewById(R.id.life_common_time)).setText(mSchoolActivity.getAddTime());
        ((TextView) mHeadView.findViewById(R.id.life_common_commentnum)).setText(mSchoolActivity.getCommentCount() + "");
        ((TextView) mHeadView.findViewById(R.id.life_common_zannum)).setText(mSchoolActivity.getAppoint() + "");

        ((TextView) mHeadView.findViewById(R.id.school_actvote_head_title)).setText(mSchoolActivity.getTitle());
        ((TextView) mHeadView.findViewById(R.id.school_actvote_head_desc)).setText(mSchoolActivity.getDes());
    }

    private void initListener() {
        mHeadAvar.setOnClickListener(this);
        mHeadShare.setOnClickListener(this);
        mHeadZan.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.school_actvote_head_btvote:
                if (choiceVote != -1) {
                    if (mSchoolActivity.getType().equals("1")) {
                        //图片投票
                        mVoteAdapter.setVote(choiceVote);
                        mVoteAdapter.cancleCallback();
                    } else {
                        //文字投票
                        mVoteContainer2ProLay1.setVisibility(View.VISIBLE);
                        mVoteContainer2ProLay2.setVisibility(View.VISIBLE);
                        mVoteContainer2Iv1.setVisibility(View.INVISIBLE);
                        mVoteContainer2Iv2.setVisibility(View.INVISIBLE);
                        mVoteContainer2Tv1.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                        mVoteContainer2Tv2.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    }
                    btVote.setVisibility(View.GONE);
                } else {
                    AppToast.showCenterText("请选择您要投票的选项");
                }
                break;
            case R.id.life_common_avar:
                AppToast.showCenterText("avar");
                break;
            case R.id.life_common_share:
                AppToast.showCenterText("share");
                break;
            case R.id.life_common_zan:
                zanAdd((ViewGroup) v);
                break;
            //俩个文字投票选项
            case R.id.school_actvote_head_votecontainer2_iv1:
                if (choiceVote != 1) {
                    mVoteContainer2Iv1.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.life_play_select_shape2));
                    mVoteContainer2Iv2.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.life_play_select_shape1));
                    choiceVote = 1;
                } else {
                    choiceVote = -1;
                    mVoteContainer2Iv1.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.life_play_select_shape1));
                }
                break;
            case R.id.school_actvote_head_votecontainer2_iv2:
                if (choiceVote != 2) {
                    mVoteContainer2Iv2.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.life_play_select_shape2));
                    mVoteContainer2Iv1.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.life_play_select_shape1));
                    choiceVote = 2;
                } else {
                    choiceVote = -1;
                    mVoteContainer2Iv2.setImageDrawable(ContextCompat.getDrawable(mContext, R.mipmap.life_play_select_shape1));
                }
                break;
        }
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
}
