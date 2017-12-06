package com.zhiyou.colleageapp.appui.person;

/**
 * Created by zhang on 16/5/13.
 */
public class MineItem {
    private int id;
    private int imgResId;
    private String name;

    public MineItem() {
    }

    public MineItem(int id, int imgResId, String name) {
        this.id = id;
        this.imgResId = imgResId;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public int getImgResId() {
        return imgResId;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setImgResId(int imgResId) {
        this.imgResId = imgResId;
    }

    public void setName(String name) {
        this.name = name;
    }
}
