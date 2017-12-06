package com.zhiyou.colleageapp.appui.friend;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zhiyou.colleageapp.R;
import com.zhiyou.colleageapp.appui.BaseFragment;
import com.zhiyou.colleageapp.constants.EaseConstant;

/**
 * Create By LongWeiHu on 2016年5月27日
 */
public class EditFragment extends BaseFragment {
	private EditText editText;
	private String title,data;
	private Button mSave;

	@Override
	public void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		if (mBundle != null) {
			title=mBundle.getString(EaseConstant.TITLE);
			data = mBundle.getString(EaseConstant.DATA);
		}

	}


	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.em_activity_edit, container, false);
	}

	@Override
	protected void initView(View view) {
		super.initView(view);
		editText = (EditText) view.findViewById(R.id.edittext);
		if (title != null) {
			((TextView)view.findViewById(R.id.tv_title)).setText(title);
		}
		if (data != null) {
			editText.setText(data);
			editText.setSelection(editText.length());
		}
		mSave = (Button) view.findViewById(R.id.save_btn);
	}

	@Override
	protected void setListener() {
		super.setListener();
		mSave.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO: 2016/5/27
			}
		});
	}
}
