package com.zhiyou.colleageapp.appui.widget.listview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 *FishBone
 *
 *@author Longwh on 2015-7-15 
 *Copyright (c) 2015 Longwh. All rights reserved.
 *不可滚动的listview
 */
public class AntiScrollListView extends ListView {
	private int mPosition;

	public AntiScrollListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public AntiScrollListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public AntiScrollListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		final int actionMasked = ev.getActionMasked() & MotionEvent.ACTION_MASK;

		if (actionMasked == MotionEvent.ACTION_DOWN) {
			// 记录手指按下时的位置
			mPosition = pointToPosition((int) ev.getX(), (int) ev.getY());
			return super.dispatchTouchEvent(ev);
		}

		if (actionMasked == MotionEvent.ACTION_MOVE) {
			// 最关键的地方，忽略MOVE 事件
			// ListView onTouch获取不到MOVE事件所以不会发生滚动处理
			return true;
		}


		return super.dispatchTouchEvent(ev);
	}
}
