package com.zhiyou.colleageapp.appui.model;

import com.google.gson.annotations.SerializedName;
import com.zhiyou.colleageapp.constants.LocalKey;

/**
 * Created by chuyh on 2016/5/20 0020.
 */
public class SchoolInfo {

    @SerializedName(LocalKey.SCHOOL_ID)
    private int mSchoolId;
    @SerializedName(LocalKey.SCHOOL_NAME)
    private String mSchoolName;
    @SerializedName(LocalKey.SCHOOL_PROVINCE)
    private String mSchoolProvince;
    @SerializedName(LocalKey.SCHOOL_CITY)
    private String mSchoolCity;
    @SerializedName(LocalKey.SCHOOL_STATUS)
    private boolean mSchoolStatus;
    @SerializedName(LocalKey.SCHOOL_DESC)
    private String mSchoolDesc;
    @SerializedName(LocalKey.SCHOOL_SIGNATURE)
    private String mSchoolSignature;
    @SerializedName(LocalKey.SCHOOL_INPUTTIME)
    private String mSchoolInputtime;
    @SerializedName(LocalKey.SCHOOL_ACTIVETIME)
    private String mSchoolActivetime;

    public int getSchoolId() {
        return mSchoolId;
    }

    public void setSchoolId(int schoolId) {
        mSchoolId = schoolId;
    }

    public String getSchoolName() {
        return mSchoolName;
    }

    public void setSchoolName(String schoolName) {
        mSchoolName = schoolName;
    }

    public String getSchoolProvince() {
        return mSchoolProvince;
    }

    public void setSchoolProvince(String schoolProvince) {
        mSchoolProvince = schoolProvince;
    }

    public String getSchoolCity() {
        return mSchoolCity;
    }

    public void setSchoolCity(String schoolCity) {
        mSchoolCity = schoolCity;
    }

    public boolean isSchoolStatus() {
        return mSchoolStatus;
    }

    public void setSchoolStatus(boolean schoolStatus) {
        mSchoolStatus = schoolStatus;
    }

    public String getSchoolDesc() {
        return mSchoolDesc;
    }

    public void setSchoolDesc(String schoolDesc) {
        mSchoolDesc = schoolDesc;
    }

    public String getSchoolSignature() {
        return mSchoolSignature;
    }

    public void setSchoolSignature(String schoolSignature) {
        mSchoolSignature = schoolSignature;
    }

    public String getSchoolInputtime() {
        return mSchoolInputtime;
    }

    public void setSchoolInputtime(String schoolInputtime) {
        mSchoolInputtime = schoolInputtime;
    }

    public String getSchoolActivetime() {
        return mSchoolActivetime;
    }

    public void setSchoolActivetime(String schoolActivetime) {
        mSchoolActivetime = schoolActivetime;
    }

}
