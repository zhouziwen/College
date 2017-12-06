package com.zhiyou.colleageapp.test;

import com.zhiyou.colleageapp.eenum.Gender;

/**
 * Created by LongWH on 2016/8/28.
 * All Rights Reserved by ZhiYou @2016 - 2017
 */
public class Test {
    public static void main(String[] args) {
        Food food = Food.Appetizer.SALAD;
        food = Food.MainCourse.BURRITO;
        Food.Appetizer.SALAD.compareTo(Food.Appetizer.SOUP);
    }
}
