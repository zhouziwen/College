package com.zhiyou.colleageapp.utils;


import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import com.zhiyou.colleageapp.application.ColleageApplication;


/**
 * by y on 2016/4/28.
 */
public class ResUtils {

    private static Resources getResource() {
        return ColleageApplication.getInstance().getResources();
    }

    public static Drawable getDrawable(int id) {
        return getResource().getDrawable(id);
    }

    public static int getColor(int id) {
        return getResource().getColor(id);
    }

    public static String getString(int id) {
        if (id < 0) {
            return "";
        }
        return getResource().getString(id);
    }


    public static String[] getStringArray(int id) {
        return getResource().getStringArray(id);
    }
}
