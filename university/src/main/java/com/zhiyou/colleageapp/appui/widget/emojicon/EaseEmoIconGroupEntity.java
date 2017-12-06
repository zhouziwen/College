package com.zhiyou.colleageapp.appui.widget.emojicon;

import com.zhiyou.colleageapp.domain.EaseEmoIcon;

import java.util.List;

/**
 * 一组表情所对应的实体类
 *
 */
public class EaseEmoIconGroupEntity {
    /**
     * 表情数据
     */
    private List<EaseEmoIcon> emojiconList;
    /**
     * 图片
     */
    private int icon;
    /**
     * 组名
     */
    private String name;
    /**
     * 表情类型
     */
    private EaseEmoIcon.Type type;
    
    public EaseEmoIconGroupEntity(){}
    
    public EaseEmoIconGroupEntity(int icon, List<EaseEmoIcon> emojiconList){
        this.icon = icon;
        this.emojiconList = emojiconList;
        type = EaseEmoIcon.Type.NORMAL;
    }
    
    public EaseEmoIconGroupEntity(int icon, List<EaseEmoIcon> emojiconList, EaseEmoIcon.Type type){
        this.icon = icon;
        this.emojiconList = emojiconList;
        this.type = type;
    }
    
    public List<EaseEmoIcon> getEmojiconList() {
        return emojiconList;
    }
    public void setEmojiconList(List<EaseEmoIcon> emojiconList) {
        this.emojiconList = emojiconList;
    }
    public int getIcon() {
        return icon;
    }
    public void setIcon(int icon) {
        this.icon = icon;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public EaseEmoIcon.Type getType() {
        return type;
    }

    public void setType(EaseEmoIcon.Type type) {
        this.type = type;
    }
    
    
}
