package com.zhiyou.colleageapp.test;

/**
 * Created by LongWH on 2016/8/28.
 * All Rights Reserved by ZhiYou @2016 - 2017
 */
public interface Food {
    enum Appetizer implements Food{
        SALAD,SOUP,SPRING_ROLLS;
    }
    enum MainCourse implements Food{
        LASAGNE,BURRITO,PAD_THAI,
        LENTILS,HUMMOUS,VINDALOO;
    }
}
