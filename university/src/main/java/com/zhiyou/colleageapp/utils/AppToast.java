package com.zhiyou.colleageapp.utils;

import com.zhiyou.colleageapp.application.ColleageApplication;

/**
 * Create By LongWeiHu On 2016年5月22日
 */
public class AppToast {

    private static ToastCreate mToastCreate = new ToastCreate();


    public static void showCenterText(String text) {
        mToastCreate.getTextCenterToast(text).show();
    }

    public static void showCenterText(int textId) {
        if (textId < 0) {
            return;
        }
        mToastCreate.getTextCenterToast(textId).show();
    }

    public static void showCenterImg(String text, boolean isSuccess) {
        mToastCreate.getImgCenterToast(text, isSuccess).show();
    }


    public static void showCenterImg(int textId, boolean isSuccess) {
        mToastCreate.getImgCenterToast(textId, isSuccess).show();
    }



    public static void showCenterText(int textIdFirst, Throwable e) {
        String errorMsg = "";
        if (e != null) {
            errorMsg = e.getMessage();
        }
        showCenterText(textIdFirst,errorMsg);
    }

    public static void showCenterText(int textIdFirst, String second) {
        String text = ColleageApplication.getInstance().getResources().getString(textIdFirst);
        mToastCreate.getTextBottomToast(String.format("%s%s", text, second)).show();
    }


    public static void showBottom(int textId) {
        if (textId <0) {
            return;
        }
        mToastCreate.getTextBottomToast(textId).show();
    }

    public static void showBottom(String str) {
        mToastCreate.getTextBottomToast(str).show();
    }

    public static void showBottom(int textIdFirst, int textIdSecond) {
        String text = ColleageApplication.getInstance().getResources().getString(textIdFirst);
        String textSecond = "";
        if (textIdSecond >0) {
            textSecond = ResUtils.getString(textIdSecond);
        }
        showBottom(text,textSecond);
    }

    public static void showBottom(String textFirst, String textSecond) {
        mToastCreate.getTextBottomToast(String.format("%s%s", textFirst, textSecond)).show();
    }

    public static void showBottomText(int textIdFirst, Throwable e) {
        String errorMsg = "";
        if (e != null) {
            errorMsg = e.getMessage();
        }
        showBottomText(textIdFirst,errorMsg);
    }

    public static void showBottomText(int textIdFirst, String msg) {
        String text = "";
        if (textIdFirst > 0) {
            text = ColleageApplication.getInstance().getResources().getString(textIdFirst);
        }
        mToastCreate.getTextBottomToast(String.format("%s%s", text, msg)).show();
    }



}
