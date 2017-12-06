/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zhiyou.colleageapp.appui.widget;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zhiyou.colleageapp.R;
import com.zhiyou.colleageapp.appui.BaseActivity;
import com.zhiyou.colleageapp.appui.widget.photoview.EasePhotoView;

import java.util.ArrayList;

/**
 * 下载显示大图
 */
public class ShowBigImageActivity extends BaseActivity {
    private EasePhotoView image;
    private int default_res = R.drawable.ease_default_image;
    private AppTitleBar mTitleBar;
    private ArrayList<String> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.ease_activity_show_big_image);
        super.onCreate(savedInstanceState);
        mTitleBar = (AppTitleBar) findViewById(R.id.app_title_bar);
        mTitleBar.getBack().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mList = getIntent().getStringArrayListExtra("path");
        if (mList == null || mList.size() == 0) {
            finish();
        }
        TextView action = mTitleBar.getAction();
        action.setText("1/" + mList.size());

        image = (EasePhotoView) findViewById(R.id.image);
        default_res = getIntent().getIntExtra("default_image", R.drawable.user_default_avatar);

    }


    @Override
    public void onBackPressed() {
        finish();
    }
}
