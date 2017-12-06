package com.zhiyou.colleageapp.appui.widget;

import android.content.Context;
import android.graphics.drawable.PaintDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhiyou.colleageapp.R;
import com.zhiyou.colleageapp.application.ColleageApplication;
import com.zhiyou.colleageapp.appui.adapter.listitem.PopItem;
import com.zhiyou.colleageapp.utils.DisplayUtils;
import com.zhiyou.colleageapp.utils.ResUtils;

import java.util.ArrayList;
import java.util.List;

/**
 *use mListView to make the popWindow
 */
public class PopWindowView {

    private PopupWindow mPopupWindow;
    private LayoutInflater mInflater;
    private ListView mListView;
    private PopListAdapter mAdapter;
    private View mView;

    public PopWindowView(Context context, int popWidthPx) {
        this.mInflater = LayoutInflater.from(context);
        initView(popWidthPx);
    }

    public PopWindowView(Context context) {
        this.mInflater = LayoutInflater.from(context);
        initView(DisplayUtils.getWidthPx() / 2);
    }

    private void initView(int popWidthPx) {
        mView = mInflater.inflate(R.layout.operate_pop_layout, null, false);
        mListView = (ListView) mView.findViewById(R.id.pop_listView);
        mPopupWindow = new PopupWindow(mView, popWidthPx, WindowManager.LayoutParams.WRAP_CONTENT, true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setBackgroundDrawable(new PaintDrawable(ColleageApplication.getInstance().getResources().getColor(R.color.white)));
        mAdapter = new PopListAdapter();
        mListView.setAdapter(mAdapter);
        mView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mPopupWindow != null && mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                }
            }
        });
    }

    /**
     * 有arrow的
     */
    public void setBgWithArrow(){
        mView.setBackgroundResource(R.mipmap.pop_bg_with_arrow);
        setBackground();
    }

    /**
     * 没有尖角的背景
     */
    public void setBgNoArrow(){
        mView.setBackgroundResource(R.drawable.pop_bg_no_arrow);
        setBackground();

    }

    public void setBgFullScreen() {
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        mView.setLayoutParams(params);
        mView.setBackgroundResource(R.drawable.pop_bg_no_arrow);
    }

    private void setBackground() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.leftMargin = DisplayUtils.dip2px(4f);
        params.rightMargin = DisplayUtils.dip2px(4f);
        params.bottomMargin = DisplayUtils.dip2px(5f);
        params.topMargin = DisplayUtils.dip2px(10f);
        mListView.setLayoutParams(params);
    }

    public void setWidth(int popWidthPx) {
        mPopupWindow.setWidth(popWidthPx);
    }

    private boolean canBeShow(View parentView) {
        return parentView != null && mPopupWindow != null && !mPopupWindow.isShowing();
    }

    public void show(View parentView) {
        show(parentView,0,0);
    }


    public void show(View parentView, int xOff, int yOff) {
        if (canBeShow(parentView)) {
            mPopupWindow.showAsDropDown(parentView, xOff, yOff);
        }
    }


    public void showAtLocation(View parent, int xOff, int yOff) {
        if (canBeShow(parent)) {
            mPopupWindow.showAtLocation(parent, Gravity.CENTER_HORIZONTAL, xOff, yOff);
        }
    }


    public void setData(List<PopItem> popItems) {
        mAdapter.updateData(popItems);
        int size = popItems.size();
        if (size > 4) {
            size = 4;
        }
        mPopupWindow.setHeight(DisplayUtils.dip2px(52f) * size);
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        if (mListView != null) {
            mListView.setOnItemClickListener(onItemClickListener);
        }
    }

    public void dismiss() {
        if (mPopupWindow != null) {
            mPopupWindow.dismiss();
        }
    }

    public boolean isShowing() {
        if (mPopupWindow != null) {
            return mPopupWindow.isShowing();
        }
        return false;
    }

    public void setListViewHeight(int height) {
        if (mListView != null) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mListView.getWidth(), height);
            mListView.setLayoutParams(params);
        }
    }

    public void setPopHeight(int height) {
        if (mPopupWindow != null) {
            mPopupWindow.setHeight(height);
        }
    }


    public ListView getListView() {
        if (mListView != null) {
            return mListView;
        } else {
            mListView = (ListView) mView.findViewById(R.id.pop_listView);
        }
        return mListView;
    }


    private class PopListAdapter extends BaseAdapter {
        private List<PopItem> mPopItems;

        public PopListAdapter() {
            mPopItems = new ArrayList<>();
        }

        private void updateData(List<PopItem> newPopItems) {
            if (newPopItems != null) {
                mPopItems.clear();
                mPopItems.addAll(newPopItems);
                notifyDataSetChanged();
            }
        }


        @Override
        public int getCount() {
            return mPopItems.size();
        }

        @Override
        public PopItem getItem(int position) {
            return mPopItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.pop_list_item, parent, false);
                holder = new ViewHolder(convertView);
                holder.icon = (ImageView) convertView.findViewById(R.id.pop_list_item_icon);
                holder.content = (TextView) convertView.findViewById(R.id.pop_list_item_content);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            PopItem popItem = getItem(position);
            int resId = popItem.getImgResId();
            if (resId > 0) {
                holder.icon.setImageResource(resId);
                holder.icon.setVisibility(View.VISIBLE);
            } else {
                holder.icon.setVisibility(View.GONE);
            }
            String content = popItem.getContent();
            if (TextUtils.isEmpty(content)) {
                content = ResUtils.getString(popItem.getContentTextId());
            }
            holder.content.setText(content);
            return convertView;
        }
    }

    private static class ViewHolder {
        ImageView icon;
        TextView content;

        public ViewHolder(View convertView) {
            convertView.setTag(this);
        }
    }


}
