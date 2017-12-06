package com.zhiyou.colleageapp.appui.friend;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.zhiyou.colleageapp.Helper;
import com.zhiyou.colleageapp.R;
import com.zhiyou.colleageapp.appui.BaseFragment;
import com.zhiyou.colleageapp.appui.adapter.FriendGroupListAdapter;
import com.zhiyou.colleageapp.appui.adapter.listitem.GroupItem;
import com.zhiyou.colleageapp.appui.listener.IRegistEventBus;
import com.zhiyou.colleageapp.appui.mvpPresenter.PresenterGroupLoad;
import com.zhiyou.colleageapp.appui.mvpView.ViewBase;
import com.zhiyou.colleageapp.appui.mvpView.ViewSuccess;
import com.zhiyou.colleageapp.appui.widget.listview.StickyListHeadersListView;
import com.zhiyou.colleageapp.constants.EaseConstant;
import com.zhiyou.colleageapp.constants.FragmentTag;
import com.zhiyou.colleageapp.domain.FriendGroup;
import com.zhiyou.colleageapp.rxeventbus.RxEventBus;
import com.zhiyou.colleageapp.rxeventbus.event.EventCreateNewGroup;
import com.zhiyou.colleageapp.utils.AppToast;

import java.util.List;
import java.util.Map;

import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Create by LongWeiHu on 2016/5/26.
 *
 */
public class GroupsChatRecordFragment extends BaseFragment implements IRegistEventBus {

    private StickyListHeadersListView mListView;
    private FriendGroupListAdapter mAdapter;
    private PresenterGroupLoad mPresenterGroupLoad;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerEventBus();
    }

    @Override
    public void onStart() {
        super.onStart();
        registerEventBus();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_groups_chat_list, container, false);
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        mListView = (StickyListHeadersListView) view.findViewById(R.id.friend_group_chat_listView);
        mAdapter = new FriendGroupListAdapter(getContext(),R.layout.list_item_group_chat_record);
        mListView.setAdapter(mAdapter);
        mPresenterGroupLoad = new PresenterGroupLoad(new GroupViewBase());
        if (Helper.getInstance().isGroupSynced()) {
            mAdapter.updateData(Helper.getInstance().getAllGroups());
        } else {
            mPresenterGroupLoad.loadGroupList(new ViewSuccess<Map<String, List<FriendGroup>>>() {
                @Override
                public void onSuccess(Map<String, List<FriendGroup>> dataMap) {
                    Helper.getInstance().saveAllGroup(dataMap);
                    Helper.getInstance().setGroupSynced(true);
                    mAdapter.updateData(dataMap);
                    hiddenLoading();
                }
            });
        }
    }

    @Override
    protected void setListener() {
        super.setListener();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                GroupItem item = mAdapter.getItem(position);
                if (item != null) {
                    Bundle data = new Bundle();
                    data.putInt(EaseConstant.EXTRA_CHAT_TYPE,EaseConstant.CHATTYPE_GROUP);
                    data.putString(EaseConstant.CHAT_ENTITY_ID, item.getGroup().getId());
                    data.putParcelable(EaseConstant.GROUP,item.getGroup());
                    mBaseActivity.showFragment(GroupChatFragment.class, FragmentTag.GROUP_CHAT,data,true);
                }
            }
        });
    }

    @Override
    public void refresh() {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void registerEventBus() {
        Subscription mSubscription = RxEventBus.getDefault().onReceiveEvent()
                .map(new Func1<Object, EventCreateNewGroup>() {
            @Override
            public EventCreateNewGroup call(Object o) {
                return (EventCreateNewGroup) o;
            }
        }).subscribe(new Action1<EventCreateNewGroup>() {
            @Override
            public void call(EventCreateNewGroup eventCreateNewGroup) {
                Helper.getInstance().saveMyCreateGroup(eventCreateNewGroup.getGroup());
                mAdapter.updateData(Helper.getInstance().getAllGroups());
            }
        });
        mCompositeSubscription.add(mSubscription);
    }


    private class GroupViewBase implements ViewBase{

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
            Helper.getInstance().setGroupSynced(false);
        }

        @Override
        public void onViewFail(int textId, String msg) {
            hiddenLoading();
            AppToast.showBottomText(textId,msg);
            Helper.getInstance().setGroupSynced(false);
        }
    }
}
