package com.zhiyou.colleageapp.appui.life.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zhiyou.colleageapp.R;
import com.zhiyou.colleageapp.appui.CommonAdapter;
import com.zhiyou.colleageapp.appui.ViewHolder;
import com.zhiyou.colleageapp.appui.model.TaoyuCategory;
import com.zhiyou.colleageapp.utils.AppToast;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/6/17.
 */
public class TaoyuCategoryActivity extends Activity {


    private ListView mListView;
    private CommonAdapter<TaoyuCategory> mAdapter;
    private ArrayList<TaoyuCategory> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        mList = getIntent().getParcelableArrayListExtra("list");

        mListView = (ListView) findViewById(R.id.categoryListView);
        mAdapter = new CommonAdapter<TaoyuCategory>(this, R.layout.item_category, mList, null) {
            @Override
            public void conver(ViewHolder holder, TaoyuCategory taoyuCategory) {
                holder.setText(R.id.category_name, taoyuCategory.getName());
            }
        };
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.findViewById(R.id.category_select).setVisibility(View.VISIBLE);

                Intent intent = new Intent();
                intent.putExtra("id", mList.get(position).getId() + "");
                intent.putExtra("name", mList.get(position).getName());
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }
}
