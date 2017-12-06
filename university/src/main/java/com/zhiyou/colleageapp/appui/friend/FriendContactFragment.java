package com.zhiyou.colleageapp.appui.friend;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;

import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.zhiyou.colleageapp.Helper;
import com.zhiyou.colleageapp.R;
import com.zhiyou.colleageapp.appui.BaseFragment;
import com.zhiyou.colleageapp.appui.MainActivity;
import com.zhiyou.colleageapp.appui.adapter.ContactBaseAdapter;
import com.zhiyou.colleageapp.appui.listener.IRegistEventBus;
import com.zhiyou.colleageapp.appui.mvpPresenter.PresenterContacts;
import com.zhiyou.colleageapp.appui.mvpView.ViewContacts;
import com.zhiyou.colleageapp.appui.mvpView.ViewSuccess;
import com.zhiyou.colleageapp.appui.person.Fragment.PersonalInfoFragment;
import com.zhiyou.colleageapp.appui.widget.ContactItemView;
import com.zhiyou.colleageapp.appui.widget.dialog.DialogOperates;
import com.zhiyou.colleageapp.appui.widget.listview.SideBar;
import com.zhiyou.colleageapp.appui.widget.listview.StickyListHeadersListView;
import com.zhiyou.colleageapp.constants.EaseConstant;
import com.zhiyou.colleageapp.constants.FragmentTag;
import com.zhiyou.colleageapp.domain.User;
import com.zhiyou.colleageapp.manager.DBManager;
import com.zhiyou.colleageapp.rxeventbus.RxEventBus;
import com.zhiyou.colleageapp.rxeventbus.event.EventContactChange;
import com.zhiyou.colleageapp.utils.AppToast;
import com.zhiyou.colleageapp.utils.EaseCommonUtils;

import java.util.List;
import java.util.Locale;

import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Author ： LongWeiHu on 2016/5/18.
 * 朋友-联系人界面
 */
public class FriendContactFragment extends BaseFragment implements IRegistEventBus {
    private StickyListHeadersListView mListView;
    private SideBar mSideBar;
    private boolean hidden, isConflict;
    private ImageButton mClearSearchBtn;
    private EditText query;
    private ContactBaseAdapter mAdapter;
    private PresenterContacts mPresenterContacts;
    private ContactItemView applicationItem;
    private DialogOperates mDialogOperates;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerEventBus();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contact_list, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        //防止被T后，没点确定按钮然后按了home键，长期在后台又进app导致的crash
        if (savedInstanceState != null && savedInstanceState.getBoolean(EaseConstant.IS_CONFLICT, false)) {
            return;
        }
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected void initView(View view) {
        super.initView(view);

        //搜索框
        query = (EditText) view.findViewById(R.id.query);
        mClearSearchBtn = (ImageButton) view.findViewById(R.id.search_clear);
        initListView(view);

    }

    private void initListView(View view) {

        mSideBar = (SideBar) view.findViewById(R.id.contact_sidebar);
        mListView = (StickyListHeadersListView) view.findViewById(R.id.friend_contact_list);
        HeaderItemClickListener clickListener = new HeaderItemClickListener();
        View headerView = LayoutInflater.from(getActivity()).inflate(R.layout.em_contacts_header, null);

        applicationItem = (ContactItemView) headerView.findViewById(R.id.chat_apply_and_notify_item);
        applicationItem.setOnClickListener(clickListener);

        headerView.findViewById(R.id.chat_nearby_person).setOnClickListener(clickListener);
        headerView.findViewById(R.id.chat_group_item).setOnClickListener(clickListener);
        headerView.findViewById(R.id.chat_school_motto).setOnClickListener(clickListener);
        //添加headerView
        mListView.addHeaderView(headerView);
        mAdapter = new ContactBaseAdapter(getContext(), R.layout.list_item_contact, null);
        mListView.setAdapter(mAdapter);
        mPresenterContacts = new PresenterContacts(mViewContacts);
        boolean isConnect = EMClient.getInstance().isConnected();
        if (!isConnect) {
            mAdapter.updateData(Helper.getInstance().getAllContactFromDb().values());
        } else {
            if (Helper.getInstance().isWhiteContactSynced()) {
                mAdapter.updateData(Helper.getInstance().getAllContact().values());
            } else {
                mPresenterContacts.loadWhiteContactFromServer(new ViewSuccess<List<User>>() {
                    @Override
                    public void onSuccess(List<User> users) {
                       updateContacts(users);
                    }
                });
            }
        }

        int unReadInviteCount = DBManager.getInstance().getInviteMessageDao().getUnreadMessagesCount();
        applicationItem.setUnreadCount(unReadInviteCount);
    }

    private void updateContacts(List<User> users) {
        for (User user : users) {
            EaseCommonUtils.setUserInitialLetter(user);
        }
        Helper.getInstance().saveAllContact(users);
        mAdapter.updateData(Helper.getInstance().getAllContact().values());
        hiddenLoading();
        Helper.getInstance().setWhiteContactSynced(true);
    }

    @Override
    protected void setListener() {
        super.setListener();
        EMClient.getInstance().addConnectionListener(connectionListener);
        query.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    mClearSearchBtn.setVisibility(View.VISIBLE);
                    mPresenterContacts.search(s);
                } else {
                    mClearSearchBtn.setVisibility(View.INVISIBLE);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
            }
        });
        mClearSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                query.getText().clear();
                hideSoftKeyboard();
            }
        });

        mListView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 隐藏软键盘
                hideSoftKeyboard();
                return false;
            }
        });

        mSideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                char c = s.toLowerCase(Locale.getDefault()).charAt(0);
                int position = mAdapter.getPositionForPinyin(c);
                if (position != -1) {
                    mListView.setSelection(position);
                }
            }

            @Override
            public void onTouchUp() {

            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User user = (User) parent.getAdapter().getItem(position);
                if (user == null) {
                    return;
                }
                if (user.getUsername().equals(EMClient.getInstance().getCurrentUser())) {
                    mBaseActivity.showFragment(PersonalInfoFragment.class, FragmentTag.PERSONAL_INFO, null, true);
                } else {
                    Bundle bundle = new Bundle();
                    String username = user.getUsername();
                    bundle.putString(EaseConstant.CHAT_ENTITY_ID, username);
                    mBaseActivity.showFragment(FragmentFriendInfo.class, FragmentTag.FRIEND_INFO, bundle, true);
                }
            }
        });

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EMClient.getInstance().removeConnectionListener(connectionListener);
        mPresenterContacts.releaseAll();
    }


    private EMConnectionListener connectionListener = new EMConnectionListener() {

        @Override
        public void onDisconnected(int error) {
            if (error == EMError.USER_REMOVED || error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                isConflict = true;
            } else {
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        onConnectionDisconnected();
                    }

                });
            }
        }

        @Override
        public void onConnected() {
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    onConnectionConnected();
                }

            });
        }
    };

    private void onConnectionDisconnected() {
        // TODO:By LongWeiHu 2016/6/12
    }

    private void onConnectionConnected() {
        // TODO:By LongWeiHu 2016/6/12
    }

    @Override
    public void registerEventBus() {
        Subscription subscription = RxEventBus.getDefault().onReceiveEvent()
                .map(new Func1<Object, EventContactChange>() {
                    @Override
                    public EventContactChange call(Object o) {
                        mPresenterContacts.loadWhiteContactFromServer(new ViewSuccess<List<User>>() {
                            @Override
                            public void onSuccess(List<User> users) {
                                updateContacts(users);
                            }
                        });
                        return null;
                    }
                }).subscribe(new Action1<EventContactChange>() {
                    @Override
                    public void call(EventContactChange eventContactChange) {

                    }
                });
        mCompositeSubscription.add(subscription);
    }


    private class HeaderItemClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            // TODO: By LongWeiHu 2016/5/23
            switch (v.getId()) {
                case R.id.chat_apply_and_notify_item:
                    // 进入申请与通知页面
                    mBaseActivity.showFragment(FriendApplyAndNotifyFragment.class, FragmentTag.CONTACT_LIST_2_NEW_FRIEND, null, true);
                    DBManager.getInstance().getInviteMessageDao().markAllInviteMsgAsRead();
                    applicationItem.setUnreadCount(0);
                    ((MainActivity) getActivity()).updateUnreadLabel();
                    break;
                case R.id.chat_nearby_person:
                    mBaseActivity.showFragment(FriendNearbyPersonFragment.class, FragmentTag.CONTACT_LIST_2_NEARBY_PERSON, null, true);
                    break;
                case R.id.chat_group_item:
                    // 进入群聊列表页面
                    mBaseActivity.showFragment(GroupsChatRecordFragment.class, FragmentTag.GROUP_CHAT, null, true);
                    break;
                case R.id.chat_school_motto:
                    //进入校训录页面
//                    startActivity(new Intent(getActivity(), PublicChatRoomsActivity.class));
                    break;


                default:
                    break;
            }
        }

    }

    private ViewContacts mViewContacts = new ViewContacts() {
        @Override
        public void onViewFail(int textId, String msg) {
            hiddenLoading();
            AppToast.showCenterText(textId, msg);
            Helper.getInstance().setWhiteContactSynced(false);
        }

        @Override
        public void onViewStart(int textId) {
            showLoading(textId);
        }

        @Override
        public void onViewComplete() {
            hiddenLoading();
        }

        @Override
        public void onViewError(int textId, Throwable e) {
            hiddenLoading();
            AppToast.showBottom(textId);
            Helper.getInstance().setWhiteContactSynced(false);
        }
    };
}
