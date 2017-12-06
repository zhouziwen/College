package com.zhiyou.colleageapp.appui.school;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.zhiyou.colleageapp.R;
import com.zhiyou.colleageapp.appui.BaseFragment;
import com.zhiyou.colleageapp.appui.adapter.DynamicAdapter;
import com.zhiyou.colleageapp.appui.adapter.UniversalBaseAdapter;
import com.zhiyou.colleageapp.appui.adapter.viewholder.UniversalViewHolder;
import com.zhiyou.colleageapp.appui.widget.listview.HorizontalListView;
import com.zhiyou.colleageapp.domain.Dynamic;
import com.zhiyou.colleageapp.domain.UserNearby;
import com.zhiyou.colleageapp.eenum.Gender;
import com.zhiyou.colleageapp.eenum.ResourceType;

import java.util.ArrayList;
import java.util.List;

/**
 * Author ： LongWeiHu on 2016/5/16.
 */
public class DynamicFragment extends BaseFragment {
    private ListView mListView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dynamic, container, false);
    }

    @Override
    protected void initView(View view) {
        mListView = (ListView) view.findViewById(R.id.dynamic_listView);

        // TODO: 2016/5/16 test data
        List<Dynamic> data = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            Dynamic dynamic = new Dynamic();
            UserNearby sender = new UserNearby("hehe" + i);
            sender.setUserName("hehe" + i);
            sender.setGender(i % 2 == 0 ? Gender.Male : Gender.Female);
            sender.setBelong("郑大");
            sender.setTime(i);
            dynamic.setSender(sender);
            dynamic.setUserIcon(R.drawable.test_person_01 + i % 5);
            dynamic.setContent("好好学习，天天向上！");
            dynamic.setCommentCount(i);
            dynamic.setPriseCount(i * 10);
            if (i % 5 == 0) {
                List<Integer> pic = new ArrayList<>();
                pic.add(R.drawable.test_img_01);
                pic.add(R.drawable.test_img_02);
                pic.add(R.drawable.test_img_03);
                pic.add(R.drawable.test_img_01);
                pic.add(R.drawable.test_img_02);
                dynamic.setPicResIdList(pic);
                dynamic.setResourceType(ResourceType.pic);
            } else {
                dynamic.setResourceType(ResourceType.text);
            }

            data.add(dynamic);
        }

        DynamicAdapter adapter = new DynamicAdapter(
                getContext(),
                R.layout.list_item_dynamic_text,
                R.layout.list_item_dynamic_pic,
                data) {
            @Override
            public void convertText(UniversalViewHolder holder, Dynamic dynamic) {
                setItemBase(holder, dynamic);
            }

            @Override
            public void convertPic(UniversalViewHolder holder, Dynamic dynamic) {
                setItemBase(holder, dynamic);
                HorizontalListView listView = holder.getView(R.id.dynamic_item_pic_listView);
                UniversalBaseAdapter picAdapter = new UniversalBaseAdapter<Integer>(getContext(), R.layout.dynamic_item_pic_item, dynamic.getPicResIdList()) {
                    @Override
                    public void convert(UniversalViewHolder holder, Integer resId) {
                        holder.setImageResource(R.id.dynamic_item_pic_item_img, resId);
                    }
                };

                listView.setAdapter(picAdapter);
            }
        };

        mListView.setAdapter(adapter);

    }


    private void setItemBase(UniversalViewHolder holder, Dynamic dynamic) {
        holder.setImageResource(R.id.dynamic_item_user_icon, dynamic.getUserIcon());
        UserNearby user = dynamic.getSender();
        holder.setText(R.id.dynamic_item_user_name, user.getUsername());
        holder.setText(R.id.dynamic_item_belong, user.getBelong());
        holder.setText(R.id.dynamic_item_content, dynamic.getContent());
        holder.setText(R.id.dynamic_item_tv_time, user.getTime() + "分钟前");// TODO: 2016/5/16 这里以后看服务器返回的数据是什么样的
        holder.setText(R.id.dynamic_item_tv_prise, String.valueOf(dynamic.getPriseCount()));
        holder.setText(R.id.dynamic_item_tv_comment, String.valueOf(dynamic.getCommentCount()));
    }


}
