package com.zhiyou.colleageapp.appui.model;

import com.google.gson.annotations.SerializedName;
import com.zhiyou.colleageapp.constants.LocalKey;

import java.util.List;

/**
 * Created by chuyh on 2016/5/20 0020.
 */
public class SchoolActDetail {

    @SerializedName(LocalKey.SCHOOL_ACT)
    private SchoolActivity mSchoolActivity;
    @SerializedName(LocalKey.COMMENT)
    private List<SchoolActComment> mCommentList;

    public SchoolActivity getSchoolActivity() {
        return mSchoolActivity;
    }

    public void setSchoolActivity(SchoolActivity schoolActivity) {
        mSchoolActivity = schoolActivity;
    }

    public List<SchoolActComment> getCommentList() {
        return mCommentList;
    }

    public void setCommentList(List<SchoolActComment> commentList) {
        mCommentList = commentList;
    }
}
