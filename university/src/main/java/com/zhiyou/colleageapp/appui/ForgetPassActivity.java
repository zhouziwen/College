/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 * <p>
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
package com.zhiyou.colleageapp.appui;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.zhiyou.colleageapp.R;
import com.zhiyou.colleageapp.appui.BaseActivity;

/**
 * 注册页
 */
public class ForgetPassActivity extends BaseActivity {
    private EditText userNameEditText;
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pass);
        userNameEditText = (EditText) findViewById(R.id.phoneNumEdit);
        passwordEditText = (EditText) findViewById(R.id.eidt_password);

        // TODO:By LongWeiHu 2016/6/1 忘记密码未处理
    }

    public void forgetPass(View view) {
        finish();
    }

    public void back(View view) {
        finish();
    }

}
