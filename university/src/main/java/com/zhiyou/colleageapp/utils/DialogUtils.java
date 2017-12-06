package com.zhiyou.colleageapp.utils;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;

import com.zhiyou.colleageapp.R;
import com.zhiyou.colleageapp.appui.adapter.listitem.PopItem;
import com.zhiyou.colleageapp.appui.widget.dialog.DialogOperates;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by LongWeiHu on 2016/6/20.
 * 处理一些常见情况，比如选择图片，
 */
public class DialogUtils {
    private DialogOperates mDialogOperates;

    public void showGetPicDialog(final Fragment fragment) {
        if (mDialogOperates == null) {
            mDialogOperates = new DialogOperates(fragment.getContext(), 2);
            mDialogOperates.setTitle(R.string.chose);
            List<PopItem> list = new ArrayList<>();
            list.add(new PopItem(R.string.select_pic));
            list.add(new PopItem(R.string.make_photo));
            mDialogOperates.setData(list);
            AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    PopItem item = (PopItem) parent.getAdapter().getItem(position);
                    switch (item.getContentTextId()) {
                        case R.string.select_pic:
                            CameraHandler.startPickPicActivityForResult(fragment);
                            break;
                        case R.string.make_photo:
                            CameraHandler.startCameraActivityForResult(fragment);
                            break;
                    }
                    mDialogOperates.dismiss();
                }
            };
            mDialogOperates.setOnItemClickListener(listener);
        }
        mDialogOperates.show();
    }

    public void release() {
        mDialogOperates = null;
    }
}
