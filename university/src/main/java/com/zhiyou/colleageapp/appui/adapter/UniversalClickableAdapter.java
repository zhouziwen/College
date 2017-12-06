package com.zhiyou.colleageapp.appui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.zhiyou.colleageapp.appui.adapter.viewholder.UniversalViewHolder;
import com.zhiyou.colleageapp.appui.listener.ClickCallback;
import com.zhiyou.colleageapp.appui.listener.ClickLongCallback;

import java.util.List;

/**Create By LongWeiHu on 2016.6.8
 * a adapter which can deal with the click, longClick event
 */
public abstract class UniversalClickableAdapter<T> extends UniversalBaseAdapter<T> implements ClickCallback,ClickLongCallback{

	public UniversalClickableAdapter(Context context, int layoutId, List<T> dataList) {
		super(context, layoutId, dataList);
	}

	public UniversalClickableAdapter(Context context, int layoutId) {
		super(context, layoutId);
	}


	@Override
	protected UniversalViewHolder setViewHolder(View convertView,ViewGroup parent) {
		UniversalViewHolder holder = new UniversalViewHolder(convertView, this, this, null);
		setViewListener(holder);
		return holder;
	}

	/**
	 * @param holder：UniversalViewHolder
	 */
	public abstract void setViewListener(UniversalViewHolder holder);

	/**
	 * @param v ： the clicked view
	 * @param position ；the clicked position
	 */
	public abstract void onClickBack(View v,int position,UniversalViewHolder holder);
	public abstract void onLongClickBack(View v,int position,UniversalViewHolder holder);


	@Override
	public void onClick(View v, int position,UniversalViewHolder holder) {
		onClickBack(v,position,holder);
	}

	@Override
	public boolean onLongClick(View v, int position,UniversalViewHolder holder) {
		onLongClickBack(v,position,holder);
		return false;
	}
}
