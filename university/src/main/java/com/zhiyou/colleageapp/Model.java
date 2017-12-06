package com.zhiyou.colleageapp;

import com.zhiyou.colleageapp.db.InviteMessageDao;
import com.zhiyou.colleageapp.domain.InviteMessage;
import com.zhiyou.colleageapp.domain.User;
import com.zhiyou.colleageapp.eenum.Key;
import com.zhiyou.colleageapp.manager.DBManager;
import com.zhiyou.colleageapp.manager.PreferenceManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;

public class Model {
    protected Map<Key,Object> valueCache = new HashMap<>();

    //==============================User相关====================================
    public boolean saveContactList(List<User> contactList) {
        // TODO:By LongWeiHu 2016/5/27
        if (contactList == null || contactList.isEmpty()) {
            return false;
        }
        DBManager.getInstance().getUserDao().insertOrReplaceInTx(contactList,true);
        return true;
    }

    public Map<String, User> getContactList() {
        List<User> users = DBManager.getInstance().getUserDao().loadAll();
        Map<String, User> userMap = new HashMap<>();
        for (User user: users) {
            userMap.put(user.getUsername(), user);
        }

        return userMap;
    }

    public void saveContact(User user){
        DBManager.getInstance().getUserDao().insertOrReplace(user);
    }


    //==================================InviteMessage相关=======================================

    /**
     * @return 降序获取消息
     */
    public List<InviteMessage> getInviteMsgList() {
        QueryBuilder<InviteMessage> queryBuilder = DBManager.getInstance().getInviteMessageDao().queryBuilder();
        Query<InviteMessage> query = queryBuilder.orderDesc(InviteMessageDao.Properties.TimeStamp).build();
        return query.list();
    }

    //==================================SharedPreference相关=====================================

    /**
     * 获取当前用户的环信id
     */
    public String getCurrentUsernName(){
        return PreferenceManager.getInstance().getCurrentUsername();
    }

    public void setSettingMsgNotification(boolean paramBoolean) {
        PreferenceManager.getInstance().setSettingMsgNotification(paramBoolean);
        valueCache.put(Key.VibrateAndPlayToneOn, paramBoolean);
    }

    public boolean getSettingMsgNotification() {
        Object val = valueCache.get(Key.VibrateAndPlayToneOn);

        if(val == null){
            val = PreferenceManager.getInstance().getSettingMsgNotification();
            valueCache.put(Key.VibrateAndPlayToneOn, val);
        }

        return (Boolean) (val != null?val:true);
    }

    public void setSettingMsgSound(boolean paramBoolean) {
        PreferenceManager.getInstance().setSettingMsgSound(paramBoolean);
        valueCache.put(Key.PlayToneOn, paramBoolean);
    }

    public boolean getSettingMsgSound() {
        Object val = valueCache.get(Key.PlayToneOn);

        if(val == null){
            val = PreferenceManager.getInstance().getSettingMsgSound();
            valueCache.put(Key.PlayToneOn, val);
        }

        return (Boolean) (val != null?val:true);
    }

    public void setSettingMsgVibrate(boolean paramBoolean) {
        PreferenceManager.getInstance().setSettingMsgVibrate(paramBoolean);
        valueCache.put(Key.VibrateOn, paramBoolean);
    }

    public boolean getSettingMsgVibrate() {
        Object val = valueCache.get(Key.VibrateOn);

        if(val == null){
            val = PreferenceManager.getInstance().getSettingMsgVibrate();
            valueCache.put(Key.VibrateOn, val);
        }

        return (Boolean) (val != null?val:true);
    }

    public void setSettingMsgSpeaker(boolean paramBoolean) {
        PreferenceManager.getInstance().setSettingMsgSpeaker(paramBoolean);
        valueCache.put(Key.SpakerOn, paramBoolean);
    }

    public boolean getSettingMsgSpeaker() {
        Object val = valueCache.get(Key.SpakerOn);

        if(val == null){
            val = PreferenceManager.getInstance().getSettingMsgSpeaker();
            valueCache.put(Key.SpakerOn, val);
        }

        return (Boolean) (val != null?val:true);
    }



    public void setGroupsSynced(boolean synced){
        PreferenceManager.getInstance().setGroupsSynced(synced);
    }

    public boolean isGroupsSynced(){
        return PreferenceManager.getInstance().isGroupsSynced();
    }

    public void setContactSynced(boolean synced){
        PreferenceManager.getInstance().setContactSynced(synced);
    }

    public boolean isContactSynced(){
        return PreferenceManager.getInstance().isContactSynced();
    }

    public void setBlacklistSynced(boolean synced){
        PreferenceManager.getInstance().setBlacklistSynced(synced);
    }

    public boolean isBacklistSynced(){
        return PreferenceManager.getInstance().isBacklistSynced();
    }

    public void allowChatroomOwnerLeave(boolean value){
        PreferenceManager.getInstance().setSettingAllowChatroomOwnerLeave(value);
    }

    public boolean isChatroomOwnerLeaveAllowed(){
        return PreferenceManager.getInstance().getSettingAllowChatroomOwnerLeave();
    }

    public void setDeleteMessagesAsExitGroup(boolean value) {
        PreferenceManager.getInstance().setDeleteMessagesAsExitGroup(value);
    }

    public boolean isDeleteMessagesAsExitGroup() {
        return PreferenceManager.getInstance().isDeleteMessagesAsExitGroup();
    }

    public void setAutoAcceptGroupInvitation(boolean value) {
        PreferenceManager.getInstance().setAutoAcceptGroupInvitation(value);
    }

    public boolean isAutoAcceptGroupInvitation() {
        return PreferenceManager.getInstance().isAutoAcceptGroupInvitation();
    }


    public void setAdaptiveVideoEncode(boolean value) {
        PreferenceManager.getInstance().setAdaptiveVideoEncode(value);
    }

    public boolean isAdaptiveVideoEncode() {
        return PreferenceManager.getInstance().isAdaptiveVideoEncode();
    }


    //=================================================================



    public List<String> getDisabledGroups(){
        return DBManager.getInstance().getPrefDao().queryDisableGroupNames(DBManager.getInstance().getDb());
    }

    public List<String> getDisabledIds(){
        Object value = valueCache.get(Key.DisabledIds);
        if(value == null){
            value = DBManager.getInstance().getPrefDao().queryDisableGroupIds(DBManager.getInstance().getDb());
            valueCache.put(Key.DisabledIds, value);
        }

        return (List<String>) value;
    }


}
