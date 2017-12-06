package com.zhiyou.colleageapp.appui.widget.dialog;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhiyou.colleageapp.R;
import com.zhiyou.colleageapp.utils.DisplayUtils;
/**
 * 对话框基类 部分对话框的父类
 * 
 */
public class BaseDialog {

	public enum EDialogButtonType {
		BUTTON_ONE, BUTTON_TWO
	}
	protected AlertDialog dialog;
	protected Context mContext;
	protected LayoutInflater inflater;
	protected TextView left;
	protected TextView right;
	protected LinearLayout dialog_layout;
	protected TextView dialog_title;
	protected boolean cancelable = true;
	protected LeftListener leftListener;
	protected RightListener rightListener;

	public BaseDialog(Context context, String title, EDialogButtonType buttonType) {
		initView(context, buttonType);
		setTitle(title);
		setListener();
	}
	
	public BaseDialog(Context context, int title,EDialogButtonType buttonType) {
		initView(context, buttonType);
		setTitle(title);
		setListener();
	}
	

	private void initView(Context context, EDialogButtonType buttonType) {
		this.mContext = context;
		inflater = LayoutInflater.from(context);
		AlertDialog.Builder builder = new AlertDialog.Builder(context,AlertDialog.THEME_HOLO_LIGHT);
		View view = inflater.inflate(R.layout.dialog_layout, null);
		left = (TextView) view.findViewById(R.id.left_text);
		right = (TextView) view.findViewById(R.id.right_text);
		dialog_layout = (LinearLayout) view.findViewById(R.id.dialog_layout);
		dialog_title = (TextView) view.findViewById(R.id.dialog_title);
		View leftLine = view.findViewById(R.id.left_line);

		switch (buttonType) {
		case BUTTON_ONE: {
			leftLine.setVisibility(View.GONE);
			left.setVisibility(View.GONE);
		}
			break;
		case BUTTON_TWO: {
//			left.setVisibility(View.GONE);
//			leftLine.setVisibility(View.GONE);
		}
			break;

		default:
			break;
		}		

		builder.setView(view);
		builder.setCancelable(cancelable);
		dialog = builder.create();
	}

	/**
	 * 
	 */
	private void setListener() {
		left.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (leftListener != null) {
					leftListener.onLeftListener();
				}							
			}
		});

		right.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (rightListener != null) {
					rightListener.onRightListener();
				}
			}
		});
	}

	public AlertDialog getDialog() {
		return dialog;
	}

	public void setCancelable(boolean flag) {
		dialog.setCancelable(flag);
	}

	public LinearLayout getLayout() {
		return dialog_layout;
	}

	public void setDialogLayoutHeight(int height){
		int h = DisplayUtils.dip2px(height);
		dialog_layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, h));
	}

	public void show() {
		dialog.show();
	}

	public void dismiss() {
		dialog.dismiss();
	}

	public boolean isDialogShowing() {
		return dialog.isShowing();
	}
	/**
	 * 因为title是不固定的，所以没有使用dialog_title.setText(int);
	 * 
	 * @param title
	 */
	public void setTitle(String title) {
		dialog_title.setText(title);
	}

	public void setTitle(int titleId) {
		dialog_title.setText(titleId);
	}

	public void setLeft(int strId) {
		left.setText(strId);
	}

	public void setLeft(String str) {
		left.setText(str);
	}



	public void setRight(String str) {
		right.setText(str);
	}

	public void setRight(int strId) {
		right.setText(strId);
	}

	public void setLeftButtonListener(LeftListener leftListener) {
		this.leftListener = leftListener;
	}

	public void setRightButtonListener(RightListener rightListener) {
		this.rightListener = rightListener;
	}
	
	public interface LeftListener {
		 void onLeftListener();
	}

	public interface RightListener {
		 void onRightListener();
	}

	public interface MiddleListener {
		 void onMiddleListener();
	}
}
