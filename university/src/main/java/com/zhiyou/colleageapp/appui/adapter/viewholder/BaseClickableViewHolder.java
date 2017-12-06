package com.zhiyou.colleageapp.appui.adapter.viewholder;

import android.util.SparseArray;
import android.view.View;
import android.widget.Checkable;
import android.widget.CompoundButton;

import com.zhiyou.colleageapp.appui.listener.CheckedChangedCallback;
import com.zhiyou.colleageapp.appui.listener.ClickCallback;
import com.zhiyou.colleageapp.appui.listener.ClickLongCallback;
import com.zhiyou.colleageapp.utils.AppLog;

/**
 * Create by LongWeiHu on 2016/6/7.
 * this is a ViewHolder that can deal with clickListener, longClickListener or both of them;
 * When you want to set listener only in ViewHolder, just extends it
 */
public class BaseClickableViewHolder implements View.OnClickListener,View.OnLongClickListener,CompoundButton.OnCheckedChangeListener{
    private final String mTag = "BaseClickableViewHolder";
    protected SparseArray<View> mViews;
    protected View mConvertView;
    protected static int mLayoutId;
    /**
     * the clicked item's position, call setPosition(position) in getView(...) method in the adapter;
     */
    protected int mPosition;
    protected CheckedChangedCallback mOnCheckedChangeListener;
    /**
     *  short click listener callback
     */
    protected ClickCallback mClickCallback;
    /**
     *  long click listener callback
     */
    protected ClickLongCallback mClickLongCallback;

    /**set the short click callback
     * @param clickCallback :
     */
    public void setClickCallback(ClickCallback clickCallback) {
        mClickCallback = clickCallback;
    }

    /**set the longClick callback
     * @param clickLongCallback :
     */
    public void setLongClickCallback(ClickLongCallback clickLongCallback) {
        mClickLongCallback = clickLongCallback;
    }

    public void setOnCheckedChangeListener(CheckedChangedCallback onCheckedChangeListener) {
        mOnCheckedChangeListener = onCheckedChangeListener;
    }

    public BaseClickableViewHolder(View convertView) {
        this(convertView, null, null, null);
    }

    public BaseClickableViewHolder(View convertView, ClickCallback onClickListener) {
        this(convertView, onClickListener, null, null);
    }

    public BaseClickableViewHolder(View convertView, ClickCallback clickListener, ClickLongCallback longClickListener) {
        this(convertView, clickListener, longClickListener, null);
    }

    public BaseClickableViewHolder(View convertView, ClickCallback clickListener, ClickLongCallback longClickListener, CheckedChangedCallback checkedChangeListener) {
        mClickCallback = clickListener;
        mClickLongCallback = longClickListener;
        mOnCheckedChangeListener = checkedChangeListener;
        mConvertView = convertView;
        mViews = new SparseArray<>();
        if (mConvertView != null) {
            mConvertView.setTag(this);
            convertView.setTag(this);
        }
    }



    public int getPosition() {
        return mPosition;
    }

    public void setPosition(int position) {
        mPosition = position;
    }


    /**
     * set a short clickListener for a view
     *
     * @param view: the view need set a short clickListener
     */
    public void setClickListener(View view) {
        setClickListener(ClickType.click, view);
    }

    /**
     * set a short clickListener for views
     *
     * @param views: the views need set a short clickListener
     */
    public void setClickListener(View... views) {
        setClickListener(ClickType.click, views);
    }

    /**
     * set a long clickListener for view
     *
     * @param view: the views need set a long clickListener
     */
    public void setLongClickListener(View view) {
        setClickListener(ClickType.longClick, view);
    }

    /**
     * set a long clickListener for views
     *
     * @param views: the views need set a long clickListener
     */
    public void setLongClickListener(View... views) {
        setClickListener(ClickType.longClick, views);
    }


    /**
     * set short and long clickListener for views
     *
     * @param views: the views need set both short and long clickListener
     */
    public void setBothClickListener(View... views) {
        setClickListener(ClickType.both, views);
    }

    public void setCheckedChangeListener(Checkable checkable) {
        if (isViewIllegal(checkable)) {
            return;
        }
        if (checkable instanceof CompoundButton) {
            ((CompoundButton) checkable).setOnCheckedChangeListener(this);
        } else {
            AppLog.instance().e(mTag+"setCheckedChangeListener : Currently only support CompoundButton !!!");
        }
    }



    public void setCheckedChangeListener(Checkable... checkableList) {
        if (isViewIllegal(checkableList)) {
            for (Checkable checkable : checkableList
                    ) {
                setCheckedChangeListener(checkable);
            }
        }
    }

    /**
     * @param clickType : 点击事件的类型
     * @param views     : 需要设置点击事件的数组
     */
    public void setClickListener(ClickType clickType, View... views) {
        if (isViewIllegal(views)) {
            AppLog.instance().e("setViewListener Views Error!!! Null Pointer!!!");
            return;
        }

        for (View v : views
                ) {
            setClickListener(clickType, v);
        }
    }


    private void setClickListener(ClickType clickType, View view) {
        if (isViewIllegal(view)) {
            return;
        }
        switch (clickType) {
            case click:
                view.setOnClickListener(this);
                break;
            case longClick:
                view.setOnLongClickListener(this);
                break;
            case both:
                view.setOnClickListener(this);
                view.setOnLongClickListener(this);
                break;
            case none:
                AppLog.instance().d("ClickType none");
                break;
        }
    }

    private boolean isViewIllegal(View... views) {
        boolean result;
        if (views == null || views.length == 0) {
            AppLog.instance().e(mTag + "Error : views... is Empty !!!");
            result = true;
        } else {
            result = false;
        }
        return result;
    }

    private boolean isViewIllegal(Checkable... views) {
        boolean result;
        if (views == null || views.length == 0) {
            AppLog.instance().e(mTag + "Error : Checkable... is Empty !!!");
            result = true;
        } else {
            result = false;
        }
        return result;
    }

    protected boolean isViewIllegal(int... viewIds) {
        boolean result;
        if (viewIds.length == 0) {
            AppLog.instance().e(mTag + "Error : viewIds... is Empty !!!");
            result = true;
        } else {
            result = false;
        }
        return result;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (mOnCheckedChangeListener != null) {
            mOnCheckedChangeListener.onCheckedChanged(buttonView,isChecked,mPosition);
        }
    }

    @Override
    public void onClick(View v) {
        if (mClickCallback != null) {
            mClickCallback.onClick(v,mPosition, (UniversalViewHolder) this);
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (mClickLongCallback != null) {
            mClickLongCallback.onLongClick(v, mPosition, (UniversalViewHolder) this);
        }
        return false;
    }

    /**
     * 点击事件的类型
     */
    public enum ClickType {
        /**
         * 短按
         */
        click,
        /**
         * 长按
         */
        longClick,
        /**
         * 短按和长按都有
         */
        both,
        /**
         * 没有点击事件
         */
        none
    }
}
