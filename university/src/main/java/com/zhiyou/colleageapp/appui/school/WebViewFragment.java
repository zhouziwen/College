package com.zhiyou.colleageapp.appui.school;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.zhiyou.colleageapp.R;
import com.zhiyou.colleageapp.appui.BaseFragment;
import com.zhiyou.colleageapp.utils.AppLog;

/**
 * Author by LongWei Hu on 2016/5/23.
 */
public class WebViewFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_webview, container, false);
    }

    WebView wv;

    @Override
    protected void initView(View view) {
        super.initView(view);
        mAppTitleBar.setTitle("社会新闻");
        wv = (WebView) view.findViewById(R.id.web_view);

        Bundle mBundle = this.getArguments();
        if (mBundle == null || mBundle.getString("url") == null) {
            popSelf();
            return;
        }
        if (mBundle.getString("name") != null)
            mAppTitleBar.setTitle(mBundle.getString("name"));
        wv.loadUrl(mBundle.getString("url"));
        showLoading(R.string.g_wait_loading);
        wv.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                AppLog.instance().d("jump to :" + url);
                wv.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                AppLog.instance().d("pageStarted:" + url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                hiddenLoading();
                AppLog.instance().d("pageStarted:" + url);
            }

        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        wv.stopLoading();
        wv.removeAllViews();
        wv.destroy();
        wv = null;
    }
}
