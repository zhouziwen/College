package com.zhiyou.colleageapp.appui.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.zhiyou.colleageapp.R;
import com.zhiyou.colleageapp.appui.adapter.UniversalBaseAdapter;
import com.zhiyou.colleageapp.appui.adapter.listitem.PopItem;
import com.zhiyou.colleageapp.appui.adapter.viewholder.UniversalViewHolder;
import com.zhiyou.colleageapp.utils.DisplayUtils;

import java.util.List;

/**
 * Create By LongWeiHu on 2016.6.12
 * 显示操作项列表的dialog
 */
public class DialogOperates {
    private int mDialogHeightInDp = 310;
    private OperateDialog mOperateDialog;

    public DialogOperates(Context context, int operateItemCount) {
        mDialogHeightInDp = 45 + operateItemCount * 51;
        mOperateDialog = new OperateDialog(context, R.style.Loadingdialog, R.layout.operate_list_dialog);
    }

    public DialogOperates(Context context) {
        this(context, 0);
    }

    public void setData(List<PopItem> operateItems) {
        mOperateDialog.setOperateItems(operateItems);
    }

    public void updateData(List<PopItem> operateItems) {
        mOperateDialog.updateData(operateItems);
        setItemCount(operateItems.size());
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        mOperateDialog.getListView().setOnItemClickListener(listener);
    }

    public void dismiss() {
        mOperateDialog.dismiss();
    }

    public void show() {
        mOperateDialog.show();
    }


    public void setTitle(int title) {
        mOperateDialog.setDialogTitle(title);
    }

    public void setConfirmText(int title) {
        mOperateDialog.setDialogConfirm(title);
    }


    public void setIsShowConfirm(boolean show) {
        mOperateDialog.mConfirm.setVisibility(show ? View.VISIBLE : View.GONE);
        mOperateDialog.mListView.setPadding(0, 0, 0, 0);
    }

    public void setItemCount(int count) {
        mDialogHeightInDp = 45 + count * 51;
        mOperateDialog.refreshWindow();
    }

    public void setOnConfirmClickListener(OnClickListener listener) {
        if (listener != null) {
            mOperateDialog.mConfirm.setOnClickListener(listener);
        }
    }

    /**
     * 设置该Dialog点击back键时是否可以dismiss
     *
     * @param cancelable :
     */
    public void setCancleable(boolean cancelable) {
        mOperateDialog.setCancelable(cancelable);
    }

    /**
     * 设置点击Dialog外部区域该Dialog是否可以dismiss
     *
     * @param cancelable :
     */
    public void setCanceledOnTouchOutside(boolean cancelable) {
        mOperateDialog.setCanceledOnTouchOutside(cancelable);
    }

    public void setIconType(EIconType type) {
        mOperateDialog.mAdapter.setIconType(type);
    }

    /**
     */
    private class OperateDialog extends Dialog {
        private TextView mTitle, mConfirm;
        private ListView mListView;
        private OperateListAdapter mAdapter;

        public OperateDialog(Context context, int theme, int layout) {
            super(context, theme);
            setContentView(layout);
            initView(context);
        }

        private void initView(Context context) {
            refreshWindow();
            mTitle = (TextView) findViewById(R.id.operate_list_title);
            mConfirm = (TextView) findViewById(R.id.operate_list_confirm);
            mListView = (ListView) findViewById(R.id.operate_listview);
            mListView.setOverScrollMode(View.OVER_SCROLL_NEVER);
            mAdapter = new OperateListAdapter(context,R.layout.operate_list_item,EIconType.DEFAULT);
            mListView.setAdapter(mAdapter);
        }

        public void refreshWindow() {
            Window window = getWindow();
            WindowManager.LayoutParams params = window.getAttributes();
            params.gravity = Gravity.CENTER;
            params.width = DisplayUtils.getWidthPx() * 8 / 10;
            // dialog 的最大高度为屏幕高度的 8/10；
            int maxHeightInPx = DisplayUtils.getHeightPx() * 6 / 10;
            int dialogHeightInPx = DisplayUtils.dip2px(mDialogHeightInDp);
            params.height = Math.min(maxHeightInPx, dialogHeightInPx);
            window.setAttributes(params);
        }


        private ListView getListView() {
            return mListView;
        }

        private void updateData(List<PopItem> operateItems) {
            mAdapter.updateData(operateItems);
        }

        private void setDialogTitle(int title) {
            mTitle.setText(title);
        }

        private void setDialogConfirm(int title) {
            mConfirm.setText(title);
        }

        public void setOperateItems(List<PopItem> operateItems) {
            updateData(operateItems);
        }
    }


    /**
     * dialog中listview 的适配器
     */
    private class OperateListAdapter extends UniversalBaseAdapter<PopItem> {
        private EIconType mIconType = EIconType.DEFAULT;

        public OperateListAdapter(Context context, int layoutId,EIconType iconType) {
            super(context, layoutId);
            mIconType = iconType;
        }


        private void setIconType(EIconType type) {
            mIconType = type;
        }

        @Override
        public void convert(UniversalViewHolder holder, PopItem item) {
            switch (mIconType) {
                case DEFAULT:
                case LEFT:
                    holder.setVisible(R.id.operate_list_item_img_right, View.GONE);
                    if (item.getImgResId() <0) {
                        holder.setVisible(R.id.operate_list_item_img, View.GONE);
                    } else {
                        holder.setVisible(R.id.operate_list_item_img, View.VISIBLE);
                        holder.setImageResource(R.id.operate_list_item_img, item.getImgResId());
                    }
                    break;
                case RIGHT:
                    holder.setVisible(R.id.operate_list_item_img, View.GONE);
                    if (item.getImgResId() <0) {
                        holder.setVisible(R.id.operate_list_item_img_right, View.GONE);
                    } else {
                        holder.setVisible(R.id.operate_list_item_img_right, View.VISIBLE);
                        holder.setImageResource(R.id.operate_list_item_img_right, item.getImgResId());
                    }
                    break;

                default:
                    break;
            }

            holder.setText(R.id.operate_list_item_name, item.getContentTextId());
        }
    }

    public enum EIconType {
        DEFAULT, RIGHT, LEFT
    }

}
