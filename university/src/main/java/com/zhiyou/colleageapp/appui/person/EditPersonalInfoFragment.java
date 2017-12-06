package com.zhiyou.colleageapp.appui.person;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.zhiyou.colleageapp.R;
import com.zhiyou.colleageapp.appui.BaseFragment;
import com.zhiyou.colleageapp.appui.listener.IEditPersonalInfo;
import com.zhiyou.colleageapp.appui.widget.AntiEmoJiEditText;
import com.zhiyou.colleageapp.constants.EaseConstant;
import com.zhiyou.colleageapp.utils.AppLog;
import com.zhiyou.colleageapp.utils.EaseCommonUtils;

/**
 * Create by LongWeiHu on 2016/6/19.
 */
public class EditPersonalInfoFragment extends BaseFragment implements View.OnClickListener {

    private int mEditWhich;
    private String mFromFragmentTagName, mOldContent;
    private AntiEmoJiEditText mEditText;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mEditWhich = mBundle.getInt(EaseConstant.EDIT_WHICH);
        mFromFragmentTagName = mBundle.getString(EaseConstant.FRAGMENT_TAG);
        mOldContent = mBundle.getString(EaseConstant.EDIT_OLD_CONTENT);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_personal_info, container, false);
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        mAppTitleBar.getAction().setVisibility(View.GONE);
        mEditText = (AntiEmoJiEditText) view.findViewById(R.id.edit_person_info_edit);
        if (!TextUtils.isEmpty(mOldContent)) {
            mEditText.setText(mOldContent);
        }
        Button save = (Button) view.findViewById(R.id.edit_person_info_btn_save);
        save.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_person_info_btn_save:
                save();
                break;
        }
    }

    /**
     */
    private void save() {
        String newContent = mEditText.getText().toString();
        if (newContent.equals(mOldContent)) {
            popSelf();
            return;
        }
        Fragment fragment = mBaseActivity.findFragmentByTag(mFromFragmentTagName);
        if (fragment instanceof IEditPersonalInfo) {
            try {
                String methodName = "save";
                Object[] args = {mEditWhich,newContent, true};
                EaseCommonUtils.invoke(fragment, methodName, args);
            } catch (Exception e) {
                AppLog.instance().e("EditPersonalInfoFragment: " + e.getMessage());
            }
        }
        popSelf();
    }
}
