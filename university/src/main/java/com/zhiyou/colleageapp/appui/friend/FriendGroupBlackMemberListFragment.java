package com.zhiyou.colleageapp.appui.friend;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.zhiyou.colleageapp.R;
import com.zhiyou.colleageapp.appui.BaseFragment;
import com.zhiyou.colleageapp.appui.adapter.ContactBaseAdapter;
import com.zhiyou.colleageapp.appui.adapter.viewholder.UniversalViewHolder;
import com.zhiyou.colleageapp.appui.mvpPresenter.PresenterContacts;
import com.zhiyou.colleageapp.constants.EaseConstant;
import com.zhiyou.colleageapp.domain.User;

/**
 *一个群的成员的黑名单列表界面
 */
public class FriendGroupBlackMemberListFragment extends BaseFragment {
    private ListView mListView;
    private String mGroupId;
    private PresenterContacts mPresenterContacts;
    private BlackUserAdapter mBlackUserAdapter;

    @Override
    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        if (mBundle != null) {
            mGroupId = mBundle.getString(EaseConstant.GROUP_ID);
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_group_black_member_list, container, false);
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        mListView = (ListView) view.findViewById(R.id.friend_group_black_member_listView);
        mPresenterContacts = new PresenterContacts(new MyViewBase());
        mBlackUserAdapter = new BlackUserAdapter(getContext(), R.layout.list_item_contact);
        mListView.setAdapter(mBlackUserAdapter);
        // TODO:By LongWeiHu 2016/6/14 等待接口
    }

    @Override
    protected void setListener() {
        super.setListener();
    }

    private class BlackUserAdapter extends ContactBaseAdapter{
        public BlackUserAdapter(Context context, int layoutId) {
            super(context, layoutId);
        }

        @Override
        public void setViewListener(UniversalViewHolder holder) {

        }

        @Override
        public void onClickBack(View v, int position, UniversalViewHolder holder) {

        }

        @Override
        public void onLongClickBack(View v, int position,UniversalViewHolder holder) {

        }

        @Override
        public void convert(UniversalViewHolder holder, User user) {
            super.convert(holder,user);
        }
    }
}
