package com.zhiyou.colleageapp.appui.person.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.hyphenate.EMCallBack;
import com.zhiyou.colleageapp.Helper;
import com.zhiyou.colleageapp.R;
import com.zhiyou.colleageapp.appui.BaseFragment;
import com.zhiyou.colleageapp.appui.LoginActivity;
import com.zhiyou.colleageapp.utils.AppToast;

/**
 * Create by LongWeiHu on 2016/5/27.
 */
public class SettingFragment extends BaseFragment {
    private Button mLogoutBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        mLogoutBtn = (Button) view.findViewById(R.id.setting_btn_logout);
    }


    @Override
    protected void setListener() {
        super.setListener();

        mLogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

    }

    private void logout() {
        showLoading(R.string.Are_logged_out);
        Helper.getInstance().logout(false, new EMCallBack() {

            @Override
            public void onSuccess() {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        hiddenLoading();
                        // 重新显示登陆页面
                        // TODO:By LongWeiHu 2016/5/27 处理一些清理工作
                        getActivity().finish();
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                    }
                });
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        hiddenLoading();
                        AppToast.showCenterText("unbind device tokens failed");

                    }
                });
            }
        });
    }
}
