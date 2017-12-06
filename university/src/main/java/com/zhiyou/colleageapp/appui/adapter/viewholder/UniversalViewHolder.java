package com.zhiyou.colleageapp.appui.adapter.viewholder;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.zhiyou.colleageapp.appui.listener.CheckedChangedCallback;
import com.zhiyou.colleageapp.appui.listener.ClickCallback;
import com.zhiyou.colleageapp.appui.listener.ClickLongCallback;
import com.zhiyou.colleageapp.utils.AppLog;
import com.zhiyou.colleageapp.utils.ResUtils;

/**
 * Create By LongWeiHu on 2016.6.8
 */
public class UniversalViewHolder extends BaseClickableViewHolder {

    /**
     * @param convertView :
     * @param clickListener : short click listener
     * @param longClickListener : long click listener
     * @param checkedChangeListener : checkedChangeListener
     */
    public UniversalViewHolder(View convertView, ClickCallback clickListener, ClickLongCallback longClickListener, CheckedChangedCallback checkedChangeListener) {
        super(convertView, clickListener, longClickListener, checkedChangeListener);
    }

    public static UniversalViewHolder getViewHolder(LayoutInflater mInflater, View convertView, ViewGroup parent, int layoutId, int position) {
        UniversalViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(layoutId, parent, false);
            holder = new UniversalViewHolder(convertView, null, null, null);
            holder.setPosition(position);
            convertView.setTag(holder);
        } else {
            holder = (UniversalViewHolder) convertView.getTag();
        }

        return holder;
    }
    /**
     * 通过viewId获取控件
     *
     * @param viewId :
     * @return the view for the viewId
     */
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public View getConvertView() {
        return mConvertView;
    }

    /**
     * 设置TextView的值
     *
     * @param viewId :
     * @param text   :
     * @return :
     */
    public UniversalViewHolder setText(int viewId, CharSequence text) {
        TextView tv = getView(viewId);
        tv.setText(text);
        return this;
    }

    public UniversalViewHolder setText(int viewId, int textId) {
        TextView tv = getView(viewId);
        if (textId > 0) {
            tv.setText(textId);
        }
        return this;
    }

    public UniversalViewHolder setImageResource(int viewId, int resId) {
        ImageView view = getView(viewId);
        view.setImageResource(resId);
        return this;
    }

    public UniversalViewHolder setImageBitmap(int viewId, Bitmap bitmap) {
        ImageView view = getView(viewId);
        view.setImageBitmap(bitmap);
        return this;
    }

    public UniversalViewHolder setImageDrawable(int viewId, Drawable drawable) {
        ImageView view = getView(viewId);
        view.setImageDrawable(drawable);
        return this;
    }

    /**
     * TextView 的方法
     */
    public UniversalViewHolder setCompoundDrawables(int viewId, Drawable left, Drawable top, Drawable right, Drawable bottom) {
        View view = getView(viewId);
        if (view instanceof TextView) {
            ((TextView) view).setCompoundDrawables(left, top, right, bottom);
        }

        return this;
    }

    public UniversalViewHolder setBackgroundColor(int viewId, int color) {
        View view = getView(viewId);
        view.setBackgroundColor(color);
        return this;
    }

    public UniversalViewHolder setBackgroundRes(int viewId, int backgroundRes) {
        View view = getView(viewId);
        view.setBackgroundResource(backgroundRes);
        return this;
    }

    public UniversalViewHolder setTextColor(int viewId, int textColor) {
        TextView view = getView(viewId);
        view.setTextColor(textColor);
        return this;
    }

    public UniversalViewHolder setTextColorRes(int viewId, int textColorRes) {
        TextView view = getView(viewId);
        view.setTextColor(ResUtils.getColor(textColorRes));
        return this;
    }

    @SuppressLint("NewApi")
    public UniversalViewHolder setAlpha(int viewId, float value) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getView(viewId).setAlpha(value);
        } else {
            // Pre-honeycomb hack to set Alpha value
            AlphaAnimation alpha = new AlphaAnimation(value, value);
            alpha.setDuration(0);
            alpha.setFillAfter(true);
            getView(viewId).startAnimation(alpha);
        }
        return this;
    }

    public UniversalViewHolder setVisible(int viewId, boolean visible) {
        View view = getView(viewId);
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    public UniversalViewHolder setVisible(int viewId, int visibility) {
        View view = getView(viewId);
        view.setVisibility(visibility);
        return this;
    }

    public UniversalViewHolder setEnable(int viewId, boolean enable) {
        View view = getView(viewId);
        view.setEnabled(enable);
        return this;
    }

    public UniversalViewHolder linkify(int viewId) {
        TextView view = getView(viewId);
        Linkify.addLinks(view, Linkify.ALL);
        return this;
    }

    public UniversalViewHolder setTypeface(Typeface typeface, int... viewIds) {
        for (int viewId : viewIds) {
            TextView view = getView(viewId);
            view.setTypeface(typeface);
            view.setPaintFlags(view.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        }
        return this;
    }

    public UniversalViewHolder setProgress(int viewId, int progress) {
        ProgressBar view = getView(viewId);
        view.setProgress(progress);
        return this;
    }

    public UniversalViewHolder setProgress(int viewId, int progress, int max) {
        ProgressBar view = getView(viewId);
        view.setMax(max);
        view.setProgress(progress);
        return this;
    }

    public UniversalViewHolder setMax(int viewId, int max) {
        ProgressBar view = getView(viewId);
        view.setMax(max);
        return this;
    }

    public UniversalViewHolder setRating(int viewId, float rating) {
        RatingBar view = getView(viewId);
        view.setRating(rating);
        return this;
    }

    public UniversalViewHolder setRating(int viewId, float rating, int max) {
        RatingBar view = getView(viewId);
        view.setMax(max);
        view.setRating(rating);
        return this;
    }

    public UniversalViewHolder setTag(int viewId, Object tag) {
        View view = getView(viewId);
        view.setTag(tag);
        return this;
    }

    public UniversalViewHolder setTag(int viewId, int key, Object tag) {
        View view = getView(viewId);
        view.setTag(key, tag);
        return this;
    }

    public UniversalViewHolder setChecked(int viewId, boolean checked) {
        Checkable view = getView(viewId);
        view.setChecked(checked);
        return this;
    }

    /**
     * 关于事件的
     */
    public UniversalViewHolder setClickListener(int viewId) {
        View view = getView(viewId);
        setClickListener(view);
        return this;
    }

    public UniversalViewHolder setClickListener(int... viewIds) {
        if (isViewIllegal(viewIds)) {
            return this;
        }
        for (int viewId : viewIds
                ) {
            View view = getView(viewId);
            if (view != null) {
                setClickListener(view);
            } else {
                AppLog.instance().e("UniversalViewHolder setViewListener null pointer !!!");
            }
        }

        return this;
    }

    public void setLongClickListener(int viewId) {
        setLongClickListener(getView(viewId));
    }

    public void setLongClickListener(int... viewIds) {
        for (int vId: viewIds
             ) {
            setLongClickListener(vId);
        }
    }

    public void setCheckedChangeListener(int checkableViewId) {
        View view = getView(checkableViewId);
        if (view != null && view instanceof CompoundButton) {
            ((CompoundButton) view).setOnCheckedChangeListener(this);
        } else {
            AppLog.instance().e("UniversalViewHolder setCheckedChangeListener null pointer !!!");
        }
    }

    public UniversalViewHolder setTouchListener(int viewId, View.OnTouchListener listener) {
        View view = getView(viewId);
        view.setOnTouchListener(listener);
        return this;
    }


    public int getLayoutId() {
        return mLayoutId;
    }


}
