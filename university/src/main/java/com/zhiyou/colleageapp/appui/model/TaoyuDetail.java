package com.zhiyou.colleageapp.appui.model;

import com.google.gson.annotations.SerializedName;
import com.zhiyou.colleageapp.constants.LocalKey;

import java.util.List;

/**
 * Created by chuyh on 2016/5/20 0020.
 */
public class TaoyuDetail {

    @SerializedName(LocalKey.TAOYU_GOODS_INFO)
    private Taoyu mTaoyu;
    @SerializedName(LocalKey.COMMENT)
    private List<TaoyuComment> mTaoyuComments;

    public Taoyu getTaoyu() {
        return mTaoyu;
    }

    public void setTaoyu(Taoyu taoyu) {
        mTaoyu = taoyu;
    }

    public List<TaoyuComment> getTaoyuComments() {
        return mTaoyuComments;
    }

    public void setTaoyuComments(List<TaoyuComment> taoyuComments) {
        mTaoyuComments = taoyuComments;
    }
}
