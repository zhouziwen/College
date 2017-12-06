package com.zhiyou.colleageapp.appui.adapter;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.zhiyou.colleageapp.appui.adapter.viewholder.UniversalViewHolder;
import com.zhiyou.colleageapp.appui.listener.CheckedChangedCallback;

import java.util.ArrayList;
import java.util.List;

/**Create by LongWeiHu on 2016.6.9
 * @param <T>
 */
public abstract class UniversalSelectAdapter<T> extends UniversalClickableAdapter<T> implements CheckedChangedCallback{

	/**
	 * a SparseBooleanArray to managed all selected item
	 */
	private SparseBooleanArray mBooleanArray = new SparseBooleanArray();
	/**
	 *
	 */
	private ChoiceMode mChoiceMode = ChoiceMode.single;

	public UniversalSelectAdapter(Context context, int layoutId) {
		super(context, layoutId);
	}

	public UniversalSelectAdapter(Context context, int layoutId, List<T> dataList) {
		super(context, layoutId, dataList);
	}

	public UniversalSelectAdapter(Context context, int layoutId, List<T> dataList, ChoiceMode choiceMode) {
		super(context, layoutId, dataList);
		this.mChoiceMode = choiceMode;
	}

	@Override
	protected UniversalViewHolder setViewHolder(View convertView, ViewGroup parent) {
		UniversalViewHolder holder = new UniversalViewHolder(convertView, this, this, this);
		setViewListener(holder);
		return holder;
	}


	/**listen the checkedChanged event
	 * @param v : CompoundButton
	 * @param isChecked : new checked state
	 * @param position : current position
	 */
	public abstract void onCheckedChangedCallback(CompoundButton v,boolean isChecked,int position);

//	@Override
//	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//		if (mCheckChangeCallback != null) {
//			mCheckChangeCallback.onCheckedChanged(buttonView, isChecked, mPosition);
//		} else {
//			onCheckedChangedCallback(buttonView,isChecked,mPosition);
//		}
//	}

	/**
	 * @param choiceMode :
	 */
	public void setChoiceMode(ChoiceMode choiceMode) {
		mChoiceMode = choiceMode;
	}


	/** add a position of the item
	 * @param position : the position of the item to be added
	 */
	public void addSelect(int position) {
		switch (mChoiceMode) {
			case single:
				mBooleanArray.clear();
				mBooleanArray.put(position, true);
				break;
			case multi:
				mBooleanArray.put(position, true);
				break;
			case none:
				mBooleanArray.clear();
				break;
		}

	}

	/** remove the selected position of the item
	 * @param position ：the position of the item to be unselected
	 */
	public void removeSelect(int position) {
		mBooleanArray.delete(position);
	}

	/**get the selected users
	 * @return : all selected users
	 */
	public List<T> getSelectedUser() {
		int size = mBooleanArray.size();
		if (size == 0) {
			return new ArrayList<T>();
		}

		List<T> list = new ArrayList<>();
		for (int index = 0; index < size; index++) {
			int selectPosition = mBooleanArray.keyAt(index);
			list.add(mDataList.get(selectPosition));
		}

		return list;
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean newState, int position) {
		onCheckedChangedCallback(buttonView,newState,position);
	}

	/**
	 * 选择类型
	 */
	public enum ChoiceMode{
		/**
		 * 单选
		 */
		single,
		/**
		 * 多选
		 */
		multi,
		/**
		 * 不可选择
		 */
		none
	}

}
