package com.zhiyou.colleageapp.appui.friend;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zhiyou.colleageapp.R;
import com.zhiyou.colleageapp.appui.BaseFragment;
import com.zhiyou.colleageapp.appui.adapter.UniversalBaseAdapter;
import com.zhiyou.colleageapp.appui.adapter.listitem.PopItem;
import com.zhiyou.colleageapp.appui.adapter.viewholder.UniversalViewHolder;
import com.zhiyou.colleageapp.appui.mvpPresenter.PresenterFriendNearby;
import com.zhiyou.colleageapp.appui.mvpView.ViewSuccess;
import com.zhiyou.colleageapp.appui.widget.PopWindowView;
import com.zhiyou.colleageapp.appui.widget.listview.HorizontalListView;
import com.zhiyou.colleageapp.domain.UserNearby;
import com.zhiyou.colleageapp.eenum.Gender;
import com.zhiyou.colleageapp.utils.DisplayUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Author by LongWei Hu on 2016/5/24.
 * 附近的人的界面
 */
public class FriendNearbyPersonFragment extends BaseFragment implements View.OnClickListener {

    private HorizontalListView mHotListView;
    private ListView mNearbyListView;
    private PopWindowView mPopWindowView;
    private PresenterFriendNearby mPresenterFriendNearby;
    private UniversalBaseAdapter<UserNearby> mHotAdapter,mNearbyAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_nearby_person, container, false);
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        mHotListView = (HorizontalListView) view.findViewById(R.id.friend_person_hot_listView);
        mNearbyListView = (ListView) view.findViewById(R.id.friend_person_nearby_listView);
        mPresenterFriendNearby = new PresenterFriendNearby(new MyViewBase());
        initHotListView();
        initNearbyListView();

    }


    @Override
    protected void setListener() {
        super.setListener();
        mAppTitleBar.getAction().setOnClickListener(this);
    }

    private void initHotListView() {
        mHotListView.setDividerWidth(DisplayUtils.dip2px(10f));
        // TODO: 2016/5/15 test data
        List<UserNearby> persons = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            UserNearby person = new UserNearby("haha" + i);
            persons.add(person);
        }
      mHotAdapter = new UniversalBaseAdapter<UserNearby>(getContext(), R.layout.list_item_person_hot, persons) {
            @Override
            public void convert(UniversalViewHolder holder, UserNearby person) {
                holder.setText(R.id.list_item_person_hot_name, person.getUsername());
            }
        };

        mHotListView.setAdapter(mHotAdapter);
    }

    private void initNearbyListView() {
        // TODO: 2016/5/15 test data
        List<UserNearby> list = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            UserNearby userNearby = new UserNearby();
            userNearby.setUserName("学生aa" + i);
            userNearby.setGender(i % 2 == 0 ? Gender.Female : Gender.Male);
            userNearby.setBelong("郑州大学");
            userNearby.setTime(10 + i);
            userNearby.setDistance(1 + 0.1 * i);
            list.add(userNearby);
        }

       mNearbyAdapter = new UniversalBaseAdapter<UserNearby>(getContext(), R.layout.list_item_person_nearby, list) {
            @Override
            public void convert(UniversalViewHolder holder, UserNearby personNearby) {
                holder.setText(R.id.person_nearby_item_person_name, personNearby.getUsername());
                StringBuilder builder = new StringBuilder();
                builder.append(personNearby.getDistance());
                builder.append(" km | ");
                builder.append(personNearby.getTime());
                holder.setText(R.id.person_nearby_item_person_state, builder.toString());
                holder.setText(R.id.person_nearby_item_person_belong, personNearby.getBelong());
                holder.setImageResource(R.id.person_nearby_item_person_gender, personNearby.getGender() == Gender.Male ? R.drawable.male : R.drawable.female);
            }
        };

        mNearbyListView.setAdapter(mNearbyAdapter);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.app_title_back) {
            initPopWindow();
            mPopWindowView.show(mAppTitleBar.getAction());
        }
    }


    private void initPopWindow() {
        if (mPopWindowView == null) {
            mPopWindowView = new PopWindowView(getContext(), DisplayUtils.getWidthPx() / 3);
            List<PopItem> list = new ArrayList<>();
            PopItem itemAll = new PopItem("全部", R.mipmap.friend_nearby_all);
            PopItem itemFemale = new PopItem("只看女", R.mipmap.friend_female);
            PopItem itemMale = new PopItem("只看男", R.mipmap.friend_male);
            list.add(itemAll);
            list.add(itemFemale);
            list.add(itemMale);
            mPopWindowView.setData(list);
            mPopWindowView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    PopItem item = (PopItem) parent.getAdapter().getItem(position);
                    // TODO:By LongWeiHu 2016/6/2 调用presenter获取并刷新数据
                    mPresenterFriendNearby.loadFriendNearby(item.getContent(), new ViewSuccess<List<UserNearby>>() {
                        @Override
                        public void onSuccess(List<UserNearby> userNearbyList) {
                            hiddenLoading();
                            mNearbyAdapter.updateData(userNearbyList);
                        }
                    });
                    mPresenterFriendNearby.loadFriendRecommend(item.getContent(), new ViewSuccess<List<UserNearby>>() {
                        @Override
                        public void onSuccess(List<UserNearby> userNearbyList) {
                            hiddenLoading();
                            mHotAdapter.updateData(userNearbyList);
                        }
                    });
                    mPopWindowView.dismiss();
                }

            });
        }
    }

}
