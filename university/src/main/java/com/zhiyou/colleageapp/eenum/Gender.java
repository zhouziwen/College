package com.zhiyou.colleageapp.eenum;

/**
 * Author ： LongWeiHu on 2016/5/18.
 */
public enum Gender {
    Male(0) ,
    Female(1);


    public int mValue;
    Gender(int value) {
        mValue = value;
    }

    public int getValue() {
        return mValue;
    }

    public static Gender valueFromInt(int value) {
        Gender gender = Male;
        switch (value) {
            default:
            case 1:
                gender = Male;
                break;
            case 0:
                gender = Female;
                break;
        }
        return gender;
    }
}
