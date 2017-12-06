package com.zhiyou.colleageapp.appui.friend;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.zhiyou.colleageapp.R;
import com.zhiyou.colleageapp.appui.BaseFragment;
import com.zhiyou.colleageapp.appui.adapter.listitem.PopItem;
import com.zhiyou.colleageapp.appui.mvpPresenter.PresenterContacts;
import com.zhiyou.colleageapp.appui.mvpView.ViewSuccess;
import com.zhiyou.colleageapp.appui.school.DynamicFragment;
import com.zhiyou.colleageapp.appui.widget.PopWindowView;
import com.zhiyou.colleageapp.appui.widget.dialog.BaseDialog;
import com.zhiyou.colleageapp.appui.widget.dialog.TextDialog;
import com.zhiyou.colleageapp.appui.widget.listview.HorizontalListView;
import com.zhiyou.colleageapp.constants.EaseConstant;
import com.zhiyou.colleageapp.constants.FragmentTag;
import com.zhiyou.colleageapp.utils.AppToast;
import com.zhiyou.colleageapp.utils.DisplayUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by LongWeiHu on 2016/6/16.
 * 朋友详情
 */
public class FragmentFriendInfo extends BaseFragment implements View.OnClickListener {
    private Button mBtnAddFriend, mBtnSendMsg;
    private String mUserId;
    private PresenterContacts mPresenterAddContact;
    private TextDialog mDialogEdit;
    private RelativeLayout mLayoutDynamic,mLayoutAddGroup,mLayoutConcernTopic;
    private HorizontalListView mListViewDynamic;
    private PopWindowView mPopWindowView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_friend_info, container, false);
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        mBtnAddFriend = (Button) view.findViewById(R.id.friend_detail_add_friend_btn);
        mBtnSendMsg = (Button) view.findViewById(R.id.friend_detail_sent_msg_btn);

        mLayoutDynamic = (RelativeLayout) view.findViewById(R.id.friend_detail_layout_dynamic);
        mLayoutAddGroup = (RelativeLayout) view.findViewById(R.id.friend_detail_layout_add_group);
        mLayoutConcernTopic = (RelativeLayout) view.findViewById(R.id.friend_detail_layout_concern_topic);

        mListViewDynamic = (HorizontalListView) view.findViewById(R.id.friend_detail_dynamic_listView);

        mUserId = mBundle.getString(EaseConstant.CHAT_ENTITY_ID);
        mPresenterAddContact = new PresenterContacts(new MyViewBase());
        if (EMClient.getInstance().getCurrentUser().equals(mUserId)) {
            mBtnSendMsg.setVisibility(View.GONE);
        }
    }

    @Override
    protected void setListener() {
        super.setListener();
        mBtnAddFriend.setOnClickListener(this);
        mBtnSendMsg.setOnClickListener(this);
        mAppTitleBar.getAction().setOnClickListener(this);
        mLayoutDynamic.setOnClickListener(this);
        mAppTitleBar.getAction().setOnClickListener(this);
    }

    private void setText(TextView view, int textId) {
        if (view != null) {
            view.setText(textId);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.friend_detail_add_friend_btn:
                showInviteDialog();
                break;

            case R.id.friend_detail_sent_msg_btn:
                mBaseActivity.showFragment(SingleChatFragment.class, FragmentTag.CHAT_SINGLE, getBundle(), true);
                break;
            case R.id.friend_detail_layout_dynamic:
                mBaseActivity.showFragment(DynamicFragment.class,FragmentTag.DYNAMIC, getBundle(),true);
                break;
            case R.id.app_title_action:
                showActionPopWindow();
        }
    }

    private Bundle getBundle() {
        Bundle bundle = new Bundle();
        bundle.putString(EaseConstant.CHAT_ENTITY_ID, mUserId);
        bundle.putString(EaseConstant.FRAGMENT_TAG,getTag());
        return bundle;
    }

    private void showInviteDialog() {
        if (mDialogEdit == null) {
            mDialogEdit = new TextDialog(getContext(), R.string.add_friend, BaseDialog.EDialogButtonType.BUTTON_TWO);
            mDialogEdit.setRightButtonListener(new BaseDialog.RightListener() {

                @Override
                public void onRightListener() {
                    EditText editText = (EditText) mDialogEdit.getLayout().getChildAt(0);
                    String msg = "";
                    if (editText != null) {
                        msg = editText.getText().toString();
                    }
                    mPresenterAddContact.sendInvite(mUserId, msg, new ViewSuccess<Integer>() {
                        @Override
                        public void onSuccess(Integer integer) {
                            hiddenLoading();
                            AppToast.showCenterText(integer);
                        }
                    });
                    mDialogEdit.dismiss();
                }
            });

            mDialogEdit.setLeftButtonListener(new BaseDialog.LeftListener() {
                @Override
                public void onLeftListener() {
                    mDialogEdit.dismiss();
                }
            });
            mDialogEdit.setEditTextView("");
            mDialogEdit.setRight(R.string.global_send);
            mDialogEdit.setLeft(R.string.cancel);
            mDialogEdit.setCancelable(true);
        }
        mDialogEdit.show();
    }

    private void showActionPopWindow() {
        if (mPopWindowView == null) {
            mPopWindowView = new PopWindowView(getContext(), DisplayUtils.getWidthPx() / 2);
            PopItem delete = new PopItem("删除", R.mipmap.contact_delete);
            PopItem addBlack = new PopItem("加入黑名单", R.mipmap.contact_add_black);
            List<PopItem> list = new ArrayList<>();
            list.add(delete);
            list.add(addBlack);
            mPopWindowView.setData(list);
            mPopWindowView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    PopItem item = (PopItem) parent.getAdapter().getItem(position);
                    int key = item.getImgResId();
                    switch (key) {
                        case R.mipmap.contact_delete:
                            if (!isSelf()) {
                                mPresenterAddContact.deleteContact(mUserId, new ViewSuccess<Integer>() {
                                    @Override
                                    public void onSuccess(Integer integer) {
                                        hiddenLoading();
                                        AppToast.showCenterText(integer);
                                    }
                                });
                            } else {
                                AppToast.showCenterText(R.string.contact_cannot_delete_self);
                            }
                            break;
                        case R.mipmap.contact_add_black:
                            if (!isSelf()) {
                                mPresenterAddContact.addContactToBlackList(mUserId, new ViewSuccess<Integer>() {
                                    @Override
                                    public void onSuccess(Integer integer) {
                                        hiddenLoading();
                                        AppToast.showCenterText(integer);
                                    }
                                });
                            } else {
                                AppToast.showCenterText(R.string.contact_cannot_add_black_self);
                            }
                            break;
                    }

                    mPopWindowView.dismiss();
                }
            });
        }

        mPopWindowView.show(mAppTitleBar.getAction());
    }


    private boolean isSelf() {
        String curUserId = EMClient.getInstance().getCurrentUser();
        return curUserId.equals(mUserId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDialogEdit = null;
    }
}
