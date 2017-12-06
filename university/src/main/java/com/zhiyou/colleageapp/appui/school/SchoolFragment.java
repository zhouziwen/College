package com.zhiyou.colleageapp.appui.school;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.zhiyou.colleageapp.R;
import com.zhiyou.colleageapp.appui.BaseFragment;
import com.zhiyou.colleageapp.appui.CommonAdapter;
import com.zhiyou.colleageapp.appui.ViewHolder;
import com.zhiyou.colleageapp.appui.adapter.UniversalBaseAdapter;
import com.zhiyou.colleageapp.appui.adapter.listitem.SchoolInnerListItem;
import com.zhiyou.colleageapp.appui.adapter.viewholder.UniversalViewHolder;
import com.zhiyou.colleageapp.appui.listener.ImageCycleViewListener;
import com.zhiyou.colleageapp.appui.model.SchoolSlider;
import com.zhiyou.colleageapp.appui.model.SchoolSmallNews;
import com.zhiyou.colleageapp.appui.mvpPresenter.PresenterSchool;
import com.zhiyou.colleageapp.appui.mvpView.ViewSchool;
import com.zhiyou.colleageapp.appui.widget.CycleViewPager;
import com.zhiyou.colleageapp.appui.widget.listview.HorizontalListView;
import com.zhiyou.colleageapp.constants.FragmentTag;
import com.zhiyou.colleageapp.constants.HttpKey;
import com.zhiyou.colleageapp.httpservice.ApiFactory;
import com.zhiyou.colleageapp.httpservice.ApiResult;
import com.zhiyou.colleageapp.httpservice.UrlConstant;
import com.zhiyou.colleageapp.utils.AppLog;
import com.zhiyou.colleageapp.utils.AppToast;
import com.zhiyou.colleageapp.utils.BannerImageViewUtils;
import java.util.ArrayList;
import java.util.List;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
/**
 * Created by LongWeiHu on 2016/5/12.
 * 校园tab下的学校的fragment
 */
public class SchoolFragment extends BaseFragment {
    private Context context;
    private CycleViewPager mCycleViewPager;
    private List<ImageView> mImageViews = new ArrayList<>();
    private List<SchoolSlider> mBannerInfoList = new ArrayList<>();
    private HorizontalListView mListView;
    private GridView mGridView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_school, container, false);
    }
    @Override
    protected void initView(View view) {
        context = getActivity().getApplicationContext();
        mCycleViewPager = (CycleViewPager) getChildFragmentManager().findFragmentById(R.id.school_fragment_banner);
        mListView = (HorizontalListView) view.findViewById(R.id.school_horizontalListView);
        mGridView = (GridView) view.findViewById(R.id.school_newsletter_gridV);
        initViewpager();
        initListView();
        initGridView();
        // 首页banner可以写在presenter里面也可以在下面直接调用apifactory
        PresenterSchool<List<SchoolSmallNews>> mPresenter = new PresenterSchool<>(new ViewSchool<List<SchoolSmallNews>>() {
            @Override
            public void onViewSuccess(List<SchoolSmallNews> societyNewses) {
                hiddenAppMainTabLoading();
                mAdapter.bindData(societyNewses);
            }
            @Override
            public void onViewStart(int textId) {
                showAppMainTabLoading(textId);
            }
            @Override
            public void onViewComplete() {
                hiddenAppMainTabLoading();
            }
            @Override
            public void onViewError(int textId, Throwable e) {
                hiddenAppMainTabLoading();
                AppLog.instance().e(e);
            }
            @Override
            public void onViewFail(int textId, String msg) {
                hiddenAppMainTabLoading();
                AppToast.showBottom(textId);
            }
        });
        mPresenter.getSchoolSimgnewsList();
    }
    private void initListView() {
        int[] imgResId = {R.drawable.campus_overview,
                R.drawable.campus_mien,
                R.drawable.campus_celebrity,
                R.drawable.campus_};
        int[] bgResId = {getResources().getColor(R.color._65b6FE),
                getResources().getColor(R.color._FA8A89),
                getResources().getColor(R.color._61D77E),
                getResources().getColor(R.color._FCCD46)};
        String[] names = getResources().getStringArray(R.array.school_inner);
        List<SchoolInnerListItem> items = new ArrayList<>();
        for (int i = 0; i < imgResId.length; i++) {
            SchoolInnerListItem item = new SchoolInnerListItem();
            item.setImgResId(imgResId[i]);
            item.setBgResId(bgResId[i]);
            item.setName(names[i]);
            items.add(item);
        }
        mListView.setAdapter(new UniversalBaseAdapter<SchoolInnerListItem>(getContext(), R.layout.list_item_school_inner_, items) {
            @Override
            public void convert(UniversalViewHolder holder, SchoolInnerListItem schoolInnerListItem) {
                if (schoolInnerListItem != null) {
                    holder.setText(R.id.school_overview_list_tV_item, schoolInnerListItem.getName());
                    // TODO: 2016/5/13 test data
                    Drawable drawable = mInflater.getContext().getResources().getDrawable(schoolInnerListItem.getImgResId());
                    if (drawable != null){
                        drawable.setBounds(0, 0, drawable.getMinimumHeight(), drawable.getMinimumWidth());
                        holder.setCompoundDrawables(R.id.school_overview_list_tV_item, drawable, null, null, null);
                    }
                    holder.setBackgroundColor(R.id.school_overview_list_tV_item, schoolInnerListItem.getBgResId());
                }
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle mBundle = new Bundle();
                switch (position) {
                    case 0:
                        mBundle.putString("url", UrlConstant.SCHOOLINFO1);
                        mBundle.putString("name", "校园概况");
                        break;
                    case 1:
                        mBundle.putString("url", UrlConstant.SCHOOLINFO2);
                        mBundle.putString("name", "校园风采");
                        break;
                    case 2:
                        mBundle.putString("url", UrlConstant.SCHOOLINFO3);
                        mBundle.putString("name", "名人榜");
                        break;
                    case 3:
                        mBundle.putString("url", UrlConstant.SCHOOLINFO3);
                        mBundle.putString("name", "校园资讯");
                        break;
                }
                mBaseActivity.showFragment(WebViewFragment.class, FragmentTag.SOCIETY_2_WEBVIEW, mBundle, true);
            }
        });
    }
    //由banner工厂生产出来的imageview
    private ImageView bannerImageView;
    private void initViewpager() {
        bannerImageView = BannerImageViewUtils.getImageView(context);
        bannerImageView.setImageResource(R.drawable.test_banner);
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
        ApiFactory.getFartory().getSchoolService().getSchoolSlider(HttpKey.APPSECRET)
                //所有异步操作都在io线程上完成
                .subscribeOn(Schedulers.io())
                //最终结果返回到主线程处理
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<ApiResult<List<SchoolSlider>>, List<SchoolSlider>>() {
                    @Override
                    public List<SchoolSlider> call(ApiResult<List<SchoolSlider>> listApiResult) {
                        //第一次转化请求结果，这里可以根据服务器返回的status做不同的处理，异常或者直接返回data数据
                        return listApiResult.getData();
                    }
                })
                .flatMap(new Func1<List<SchoolSlider>, Observable<SchoolSlider>>() {
                    @Override
                    public Observable<SchoolSlider> call(List<SchoolSlider> schoolSliders) {
                        //第二次转化，将请求结果List<SchoolSliders>依次发送出去
                        mBannerInfoList = schoolSliders;
                        return Observable.from(schoolSliders);
                    }
                })
                .map(new Func1<SchoolSlider, ImageView>() {
                    @Override
                    public ImageView call(SchoolSlider schoolSlider) {
                        //第三次转化，下载轮播图片，返回imageview，让UI主线程上直接使用mImageViews.add(imageView)添加结果
                        bannerImageView = BannerImageViewUtils.getImageView(context);
                        Glide.with(context).load(schoolSlider.getSliderUrlAb()).into(bannerImageView);
                        return bannerImageView;
                    }
                })
                .subscribe(new Subscriber<ImageView>() {
                    @Override
                    public void onCompleted() {
                        //重新添加第一个view
                        bannerImageView = BannerImageViewUtils.getImageView(context);
                        Glide.with(context).load(mBannerInfoList.get(mBannerInfoList.size() - 1).getSliderUrlAb()).into(bannerImageView);
                        mImageViews.add(0, bannerImageView);
                        //重新添加最后一个view
                        bannerImageView = BannerImageViewUtils.getImageView(context);
                        Glide.with(context).load(mBannerInfoList.get(0).getSliderUrlAb()).into(bannerImageView);
                        mImageViews.add(bannerImageView);

                        // 设置循环，在调用setData方法前调用
                        mCycleViewPager.setCycle(true);
                        // 在加载数据前设置是否循环
                        mCycleViewPager.setData(mImageViews, mBannerInfoList, new ImageCycleViewListener() {
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
                    @Override
                    public void onError(Throwable e) {
                        //处理异常
                        AppToast.showCenterText(e.getMessage());
                        AppLog.instance().e("School Sliders: " + e.getMessage());
                    }
                    @Override
                    public void onNext(ImageView imageView) {
                        mImageViews.add(imageView);
                    }
                });
    }
    @Override
    protected void showUnLoginCover() {
    }
    private CommonAdapter<SchoolSmallNews> mAdapter;
    private void initGridView() {
//        UniversalAdapter<SchoolInnerListItem> adapter = new UniversalAdapter<SchoolInnerListItem>(getContext(), R.layout.gridv_school_item, data) {
//            @Override
//            public void convert(UniversalViewHolder holder, SchoolInnerListItem item) {
//                holder.setImageResource(R.id.gridView_school_item_imgV, item.getImgResId());
//                holder.setText(R.id.griView_school_item_tv, item.getName());
//            }
//        };
        mAdapter = new CommonAdapter<SchoolSmallNews>(mContext, R.layout.gridv_school_item) {
            @Override
            public void conver(ViewHolder holder, SchoolSmallNews schoolSmallNews) {
                holder.setText(R.id.griView_school_item_tv, schoolSmallNews.getNewsTitle());
                holder.loadImage(R.id.gridView_school_item_imgV, mContext, schoolSmallNews.getNewsThumbAd(), R.mipmap.life_beautiful_girl);
            }
        };
        mGridView.setAdapter(mAdapter);
    }
}
