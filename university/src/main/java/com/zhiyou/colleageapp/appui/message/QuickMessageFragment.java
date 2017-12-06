package com.zhiyou.colleageapp.appui.message;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.zhiyou.colleageapp.R;
import com.zhiyou.colleageapp.appui.BaseFragment;

/**
 * Created by wujiaolong on 16/5/13.
 */
public class QuickMessageFragment extends BaseFragment {
    private ListView listView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_message_layout, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    protected void showUnLoginCover() {

    }

    @Override
    public void onResume() {
        super.onResume();
        hiddenAppMainTabLoading();
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        listView = (ListView) view.findViewById(R.id.message_listView);

        listView.setAdapter(new MessageAdapter(getContext(), new MessageAdapter.Callback() {
            @Override
            public void click(View v) {
            }
        }));
    }
}
