package com.zhiyou.colleageapp.test;

/**
 * Created by LongWH on 2016/8/28.
 * All Rights Reserved by ZhiYou @2016 - 2017
 */
public enum ETaskType {
    UNKNOWN("unknown"){
        @Override
        String getName() {
            mName = "unknown";
            return mName;
        }
    };
    protected String mName;
    ETaskType(String name){

    }
    abstract String getName();
}
