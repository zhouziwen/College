package com.zhiyou.colleageapp.appui.listener;

import android.os.Bundle;

import java.util.ArrayList;

/**
 * Author by LongWei Hu on 2016/5/24.
 */
public interface IPickContact {
    void selectContact(ArrayList<String> selectedUserNames, Boolean isChanged, Bundle extraData);
}
