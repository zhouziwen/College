package com.zhiyou.colleageapp.appui.school;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.zhiyou.colleageapp.R;
import com.zhiyou.colleageapp.appui.BaseFragment;
import com.zhiyou.colleageapp.appui.adapter.ShGroupAdapter;
import com.zhiyou.colleageapp.appui.adapter.UniversalBaseAdapter;
import com.zhiyou.colleageapp.appui.adapter.viewholder.UniversalViewHolder;
import com.zhiyou.colleageapp.appui.widget.listview.HorizontalListView;
import com.zhiyou.colleageapp.domain.GroupType;
import com.zhiyou.colleageapp.domain.SchoolGroup;
import com.zhiyou.colleageapp.domain.User;
import com.zhiyou.colleageapp.eenum.ResourceType;
import com.zhiyou.colleageapp.eenum.Gender;

import java.util.ArrayList;
import java.util.List;

/**
 * Author ： LongWeiHu on 2016/5/16.
 */
public class SchoolGroupFragment extends BaseFragment {

    private EditText mSearchEdit;
    private HorizontalListView mGroupTypeListView;
    private ListView mGroupListView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sh_group, container, false);
    }

    @Override
    protected void initView(View view) {
        mSearchEdit = (EditText) view.findViewById(R.id.sh_group_edit);
        mGroupTypeListView = (HorizontalListView) view.findViewById(R.id.sh_group_listView_groupType);
        mGroupListView = (ListView) view.findViewById(R.id.sh_group_listView_recruit);


        // TODO: 2016/5/16 test data

        List<GroupType> typeList = new ArrayList<>();
        typeList.add(new GroupType("0", "生活"));
        typeList.add(new GroupType("1", "户外"));
        typeList.add(new GroupType("2", "运动"));
        typeList.add(new GroupType("3", "兴趣"));
        typeList.add(new GroupType("4", "交友"));
        typeList.add(new GroupType("5", "玩乐"));

        UniversalBaseAdapter<GroupType> groupTypeAdapter = new UniversalBaseAdapter<GroupType>(getContext(), R.layout.list_item_sh_group_type, typeList) {
            @Override
            public void convert(UniversalViewHolder holder, GroupType groupType) {
                holder.setText(R.id.sh_group_item_group_type, groupType.getName());
            }
        };
        mGroupTypeListView.setAdapter(groupTypeAdapter);


        List<SchoolGroup> groupList = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            SchoolGroup group = new SchoolGroup();
            //群组状态
            List<String> groupState = new ArrayList<>();
            if (i % 2 == 0) {
                groupState.add("新群");
            } else {
                groupState.add("新群");
                groupState.add("招募中");
            }
            group.setGroupState(groupState);

            //群组名字和介绍
            group.setName("群组" + i);
            group.setIconResId(R.drawable.test_group_01 + i % 10);
            group.setSummary("这是一个团结向上的群组");


            //群组活动图片资源
            if (i % 4 == 1) {
                List<Integer> groupPicList = new ArrayList<>();
                groupPicList.add(R.drawable.test_group_01);
                groupPicList.add(R.drawable.test_group_02);
                groupPicList.add(R.drawable.test_group_03);
                groupPicList.add(R.drawable.test_group_02);
                group.setPicResList(groupPicList);
                group.setResourceType(ResourceType.pic);
            } else {
                group.setResourceType(ResourceType.text);
            }

            //群组成员数据
            List<User> members = new ArrayList<>();
            int femaleCount = 0;
            for (int j = 30; j > 10; j--) {
                User user = new User();
                user.setUserName("haha");
                if (j % 3 == 0) {
                    user.setGender(Gender.Male);
                } else if (j % 4 == 0) {
                    user.setGender(Gender.Female);
                    femaleCount++;
                } else {
                    user.setGender(Gender.Male);
                }

                members.add(user);
            }
            group.setTotalCount(30);
            group.setMembers(members);
            group.setFemaleCount(femaleCount);

            groupList.add(group);
        }

        ShGroupAdapter groupAdapter = new ShGroupAdapter(
                getContext(),
                R.layout.list_item_sh_group_recruit_text,
                R.layout.list_item_sh_group_recruit_pic,
                groupList) {
            @Override
            public void convertText(UniversalViewHolder holder, SchoolGroup group) {
                setGroupBaseInfo(holder, group);



            }

            @Override
            public void convertPic(UniversalViewHolder holder, SchoolGroup group) {
                setGroupBaseInfo(holder, group);
                HorizontalListView groupPicListView = holder.getView(R.id.sh_group_item_group_pic_listView);
                setGroupPicInfo(groupPicListView, group.getPicResList());
            }
        };

        mGroupListView.setAdapter(groupAdapter);

    }

    private void setGroupPicInfo(HorizontalListView groupPicListView, List<Integer> imgResList) {
        UniversalBaseAdapter<Integer> picListAdapter = new UniversalBaseAdapter<Integer>(
                getContext(),
                R.layout.list_item_sh_group_pic,
                imgResList) {
            @Override
            public void convert(UniversalViewHolder holder, Integer integer) {
                holder.setImageResource(R.id.sh_group_pic_item_img, integer);
            }
        };

        if (groupPicListView != null) {

            groupPicListView.setAdapter(picListAdapter);
        }
    }

    private void setGroupBaseInfo(UniversalViewHolder holder, SchoolGroup group) {
        //设置群名称，人数等
        holder.setImageResource(R.id.sh_group_item_group_icon, group.getIconResId());
        holder.setText(R.id.sh_group_item_name, group.getName());
        StringBuilder builder = new StringBuilder();
        builder.append("本群共");
        builder.append(group.getTotalCount());
        builder.append("人");
        builder.append("(女生");
        builder.append(group.getFemaleCount());
        builder.append("人)");
        holder.setText(R.id.sh_group_item_total, builder.toString());

        //设置群状态
        HorizontalListView stateListView = holder.getView(R.id.sh_group_item_group_state_listView);
        UniversalBaseAdapter<String> groupStateAdapter = new UniversalBaseAdapter<String>(
                getContext(),
                R.layout.list_item_sh_group_state,
                group.getGroupState()) {
            @Override
            public void convert(UniversalViewHolder holder, String s) {
                TextView state = holder.getView(R.id.sh_group_item_group_state);
                state.setText(s);
                if ("新群".equals(s)) {
                    state.setBackgroundResource(R.drawable.bg_sh_group_state_item_blue);
                    state.setTextColor(getResources().getColor(R.color.main_tab_on));

                } else if ("招募中".equals(s)) {
                    state.setBackgroundResource(R.drawable.bg_sh_group_state_item_red);
                    state.setTextColor(getResources().getColor(R.color._ff7a9a));
                }

            }
        };

        if (stateListView != null) {

            stateListView.setAdapter(groupStateAdapter);
        }


        holder.setText(R.id.sh_group_item_group_summary, group.getSummary());

        HorizontalListView memberListView = holder.getView(R.id.sh_group_item_group_member_listView);
        UniversalBaseAdapter<User> memberAdapter = new UniversalBaseAdapter<User>(
                getContext(),
                R.layout.list_item_sh_group_member_item,
                group.getMembers()) {
            @Override
            public void convert(UniversalViewHolder holder, User user) {
                holder.setImageResource(R.id.sh_group_member_item_gender_icon, user.getGender() == Gender.Male ? R.drawable.male_in_group : R.drawable.female_in_group);
            }
        };
        if (memberListView != null) {

            memberListView.setAdapter(memberAdapter);
        }
        holder.setText(R.id.sh_group_item_location, "篮球场1km");
    }


}
