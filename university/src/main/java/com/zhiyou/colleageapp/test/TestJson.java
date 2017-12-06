package com.zhiyou.colleageapp.test;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Created by LongWH on 2016/10/31.
 * All Rights Reserved by ZhiYou @2016 - 2017
 */
public class TestJson {
    public static void main(String[] args) {
        JsonObject jsonObject = new JsonObject();
        JsonArray activity = jsonObject.getAsJsonArray("activity");
        JsonObject element = (JsonObject) activity.get(0);
        JsonElement appointCountEle = element.get("appoint_count");
        boolean result = appointCountEle.getAsBoolean();
    }
}
