package com.zhiyou.colleageapp.appui.school;

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
import com.zhiyou.colleageapp.appui.model.SocietyNews;
import com.zhiyou.colleageapp.appui.mvpPresenter.PresenterSchool;
import com.zhiyou.colleageapp.appui.mvpView.ViewSchool;
import com.zhiyou.colleageapp.constants.FragmentTag;
import com.zhiyou.colleageapp.httpservice.UrlConstant;
import com.zhiyou.colleageapp.utils.AppLog;
import com.zhiyou.colleageapp.utils.AppToast;

import java.util.List;

/**
 * Author by LongWei Hu on 2016/5/23.
 */
public class SocietyFragment extends BaseFragment {
    private ListView mListView;
    private CommonAdapter<SocietyNews> mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_society, container, false);
    }

    @Override
    protected void showUnLoginCover() {

    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        mListView = (ListView) view.findViewById(R.id.school_sicuety_listview);
        mAdapter = new CommonAdapter<SocietyNews>(mContext, R.layout.life_society_item) {
            @Override
            public void conver(ViewHolder holder, SocietyNews societyNews) {
                holder.setText(R.id.society_item_title, societyNews.getNewsTitle());
                if (societyNews.getNewsAddTime() != null)
                    holder.setText(R.id.society_item_time, societyNews.getNewsAddTime());
                String[] urls = societyNews.getNewsThumb();
                if ("3".equals(societyNews.getNewsType())) {
                    holder.setVisiable(R.id.society_item_tp1, View.GONE);
                    holder.setVisiable(R.id.society_item_tp2, View.VISIBLE);
                    holder.loadImage(R.id.society_ivtp2_left1, mContext, UrlConstant.BASE + urls[0], R.mipmap.life_beautiful_girl);
                    holder.loadImage(R.id.society_ivtp2_left2, mContext, UrlConstant.BASE + urls[1], R.mipmap.life_beautiful_girl);
                    holder.loadImage(R.id.society_ivtp2_center, mContext, UrlConstant.BASE + urls[2], R.mipmap.life_beautiful_girl);
                } else if ("2".equals(societyNews.getNewsType())) {
                    holder.setVisiable(R.id.society_item_tp1, View.GONE);
                    holder.setVisiable(R.id.society_item_tp2, View.VISIBLE);
                    holder.setVisiable(R.id.society_ivtp2_leftlay, View.GONE);
                    holder.loadImage(R.id.society_ivtp2_center, mContext, UrlConstant.BASE + urls[0], R.mipmap.life_beautiful_girl);
                } else if ("1".equals(societyNews.getNewsType())) {
                    holder.loadImage(R.id.society_item_tp1, mContext, UrlConstant.BASE + urls[0], R.mipmap.life_beautiful_girl);
                }
            }
        };
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle mBundle = new Bundle();
                mBundle.putString("url", UrlConstant.BASE + mAdapter.getItem(position).getNewsDetail());
                mBaseActivity.showFragment(WebViewFragment.class, FragmentTag.SOCIETY_2_WEBVIEW, mBundle, true);
            }
        });

        PresenterSchool<List<SocietyNews>> mPresenter = new PresenterSchool<>(new ViewSchool<List<SocietyNews>>() {
            @Override
            public void onViewSuccess(List<SocietyNews> societyNewses) {
                mAdapter.bindData(societyNewses);
            }

            @Override
            public void onViewStart(int textId) {
//                showAppMainTabLoading(textId);
            }


            public void onViewComplete() {
//                hiddenAppMainTabLoading();
            }

            @Override
            public void onViewError(int textId, Throwable e) {
//                hiddenAppMainTabLoading();
                AppLog.instance().e(e);
            }

            @Override
            public void onViewFail(int textId, String msg) {
                AppToast.showBottom(textId);
            }
        });
        mPresenter.getSocietyNewsList();
    }
}
