package com.zhiyou.colleageapp.appui.widget.dialog;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.zhiyou.colleageapp.R;
import com.zhiyou.colleageapp.utils.DisplayUtils;
import com.zhiyou.colleageapp.utils.ResUtils;


/**
 * 文本对话框 设置一或二行文本的对话框
 *
 */
public class TextDialog extends BaseDialog {
	private LayoutParams params;
		
	public TextDialog(Context context, String title, EDialogButtonType buttonType) {
		super(context, title, buttonType);
		initTextStyle();
	}

	
	public TextDialog(Context context, int title, EDialogButtonType buttonType) {
		super(context, title,buttonType);
		initTextStyle();
	}	

	private void initTextStyle() {
		int margin = DisplayUtils.dip2px(34);
		params = new LayoutParams(LayoutParams.MATCH_PARENT, 0, 1);
		params.setMargins(margin, 0, margin, 0);

	}

	public void setMsgTextAndImg(String msg, int resId) {
		getLayout().removeAllViews();
		TextView view = initTextView(msg);
		Drawable drawable = ResUtils.getDrawable(resId);
		setDrawableSize(drawable);
		view.setCompoundDrawables(drawable, null, null, null);
		getLayout().addView(view);
	}

	private void setDrawableSize(Drawable... drawables) {
		if (drawables != null) {
			int len = drawables.length;
			for (int i = 0; i < len; i++) {
				drawables[i].setBounds(0, 0, drawables[i].getIntrinsicWidth(), drawables[i].getIntrinsicWidth());
			}
		}

	}

	 /**
	 * @param msg
	 */
	public void setMsgText(String msg) { 
		getLayout().removeAllViews();
		if (msg.length()>37) { //超过27个字以后最后三个显示为...
			msg = msg.substring(0,34)+"...";
		}
		TextView view = initTextView(msg);
		//设置行距
		view.setLineSpacing(DisplayUtils.dip2px(12f), 0.5f);
		getLayout().addView(view);
		
	}
	 
	 
	 /**
	 * @param textId
	 */
	public void setMsgText(int textId) { 
		 getLayout().removeAllViews();
		 TextView view = initTextView(textId);
		 //设置行距
		 view.setLineSpacing(DisplayUtils.dip2px(12f), 0.5f);
		 getLayout().addView(view);
		 
	 }

	public void setMsgText(String text1, String text2) { 
		getLayout().removeAllViews();
		TextView view1 = initTextView(text1);
		TextView view2 = initTextView(text2);
		getLayout().addView(view1);
		getLayout().addView(view2);
	}
	public void setMsgText(int text1, int text2) { 
		getLayout().removeAllViews();
		TextView view1 = initTextView(text1);
		TextView view2 = initTextView(text2);
		getLayout().addView(view1);
		getLayout().addView(view2);
	}

	public TextView initTextView(String msg) {
		TextView textView = new TextView(mContext);
		textView.setText(msg);
		textView.setLayoutParams(params);
		textView.setGravity(Gravity.CENTER_VERTICAL);
		textView.setTextSize(15f);
		textView.setTextColor(ResUtils.getColor(R.color.font_black_light));
		return textView;
	}
	
	public TextView initTextView(int msg) {
		TextView textView = new TextView(mContext);
		textView.setText(msg);
		textView.setLayoutParams(params);
		textView.setGravity(Gravity.CENTER_VERTICAL);
		textView.setTextSize(15f);
		textView.setTextColor(ResUtils.getColor(R.color.font_black_light));
		return textView;
	}
	
	/**设置3条消息（包括3条）以上的方法，
	 * @param msgs
	 */
	public void setMultiMsg(String[] msgs){
		getLayout().removeAllViews();
		for (int i = 0; i < msgs.length; i++) {
			TextView textView = initTextView(msgs[i]);
			textView.setSingleLine(true);
			getLayout().addView(textView);
			View view = initLine(); //可以添加分割线，或者不添加
			getLayout().addView(view);
		}
		int oneMsgHeight = DisplayUtils.px2dip(100f); 
		params = new LayoutParams(LayoutParams.MATCH_PARENT, oneMsgHeight*msgs.length);
		getLayout().setLayoutParams(params);
	}

	private View initLine() {
		View view = new View(mContext);
		view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, DisplayUtils.px2dip(1)));
		view.setBackgroundColor(ResUtils.getColor(R.color.divider));
		return view;
	}
	
	/**设置dialog显示的位置
	 */
	public void setDialogPosition(int x,int y){
		Window window = getDialog().getWindow();
		WindowManager.LayoutParams params = window.getAttributes();
		window.setGravity(Gravity.CENTER);
		params.x = x;
		params.y = y;
		window.setAttributes(params);
	}
	
	/**
	 */
	public void setEditTextView(String text){
		getLayout().removeAllViews();
		params = new LayoutParams(LayoutParams.MATCH_PARENT, DisplayUtils.dip2px(70f));
		getLayout().setLayoutParams(params);
		getLayout().setGravity(Gravity.CENTER);
		EditText editText = new EditText(mContext);
		editText.setBackgroundColor(ResUtils.getColor(R.color.transparent));
//		params = new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		params = new LayoutParams(LayoutParams.MATCH_PARENT, 0, 1);
		params.gravity = Gravity.CENTER;
		params.leftMargin = DisplayUtils.dip2px(20f);
		params.rightMargin = DisplayUtils.dip2px(20f);
		params.bottomMargin = DisplayUtils.dip2px(20f);
		
		editText.setGravity(Gravity.BOTTOM);
		editText.setSingleLine(true);
		editText.setLayoutParams(params);
		editText.setTextSize(14f);
		editText.requestFocus();
		editText.setText(text);
		editText.setSelection(text.length());
		editText.setImeOptions(EditorInfo.IME_ACTION_NONE);
		getLayout().addView(editText);
	}

}
