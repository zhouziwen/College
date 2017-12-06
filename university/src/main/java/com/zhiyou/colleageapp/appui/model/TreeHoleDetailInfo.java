package com.zhiyou.colleageapp.appui.model;

import com.google.gson.annotations.SerializedName;
import com.zhiyou.colleageapp.constants.LocalKey;

import java.util.List;

/**
 * Created by chuyh on 2016/5/20 0020.
 */
public class TreeHoleDetailInfo {

    @SerializedName(LocalKey.TREEHOLE_DETAIL_HOLE)
    private TreeHole mTreeHole;
    @SerializedName(LocalKey.TREEHOLE_DETAIL_COMMENT)
    private List<TreeHoleComment> mComments;

    public TreeHole getTreeHole() {
        return mTreeHole;
    }

    public void setTreeHole(TreeHole treeHole) {
        mTreeHole = treeHole;
    }

    public List<TreeHoleComment> getComments() {
        return mComments;
    }

    public void setComments(List<TreeHoleComment> comments) {
        mComments = comments;
    }
}
