package com.zhiyou.colleageapp.appui.life.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.zhiyou.colleageapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chuyh on 2015/12/23.
 */
public class TalkAdapter extends BaseAdapter implements View.OnClickListener {
    private List list;
    private LayoutInflater layoutInflater;
    private Callback mCallback;

    /**
     * 自定义接口，用于回调按钮点击事件到Activity
     *
     * @author chuyh
     *         2014-11-26
     */
    public interface Callback {
        void click(View v);
    }

    public TalkAdapter(Context context, Callback callback) {
        list = new ArrayList();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        list.add(5);
        list.add(5);
        list.add(5);
        list.add(5);
        list.add(5);
        layoutInflater = LayoutInflater.from(context);
        mCallback = callback;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.life_talk_item, null);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }

    @Override
    public void onClick(View view) {
        mCallback.click(view);
    }

    private static class ViewHolder {
    }
}
