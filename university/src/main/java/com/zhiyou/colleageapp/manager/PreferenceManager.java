package com.zhiyou.colleageapp.manager;
import android.content.Context;
import android.content.SharedPreferences;

import com.zhiyou.colleageapp.application.ColleageApplication;
/**
 * Author ： LongWeiHu on 2016/5/17.
 */
public class PreferenceManager {
    /**
     * 保存Preference的name
     */
    public static final String TEMP = "tempUser";
    public static final String LOGIN_USER = "loginUser";
    private static SharedPreferences mSharedPreferences;
    private static PreferenceManager mPreferenceManager;
    private static SharedPreferences.Editor editor;

    private String SHARED_KEY_SETTING_NOTIFICATION = "shared_key_setting_notification";
    private String SHARED_KEY_SETTING_SOUND = "shared_key_setting_sound";
    private String SHARED_KEY_SETTING_VIBRATE = "shared_key_setting_vibrate";
    private String SHARED_KEY_SETTING_SPEAKER = "shared_key_setting_speaker";
    private static String SHARED_KEY_SETTING_CHAT_ROOM_OWNER_LEAVE = "shared_key_setting_chatroom_owner_leave";
    private static String SHARED_KEY_SETTING_DELETE_MESSAGES_WHEN_EXIT_GROUP = "shared_key_setting_delete_messages_when_exit_group";
    private static String SHARED_KEY_SETTING_AUTO_ACCEPT_GROUP_INVITATION = "shared_key_setting_auto_accept_group_invitation";
    private static String SHARED_KEY_SETTING_ADAPTIVE_VIDEO_ENCODE = "shared_key_setting_adaptive_video_encode";
    private static String SHARED_KEY_SETTING_GROUPS_SYNCED = "SHARED_KEY_SETTING_GROUPS_SYNCED";
    private static String SHARED_KEY_SETTING_CONTACT_SYNCED = "SHARED_KEY_SETTING_CONTACT_SYNCED";
    private static String SHARED_KEY_SETTING_BALCKLIST_SYNCED = "SHARED_KEY_SETTING_BALCKLIST_SYNCED";
    private static String SHARED_KEY_CURRENTUSER_USERNAME = "SHARED_KEY_CURRENTUSER_USERNAME";
    private static String SHARED_KEY_CURRENTUSER_NICK = "SHARED_KEY_CURRENTUSER_NICK";
    private static String SHARED_KEY_CURRENTUSER_AVATAR = "SHARED_KEY_CURRENTUSER_AVATAR";
    private interface EditorKey{
        String key_pwd = "pwd";
        String key_token = "token";
        String key_signature = "signature";
        String key_belong = "belong";
        String key_avatar = "avatar";
    }
    private PreferenceManager(String userName) {
        mSharedPreferences = ColleageApplication.getInstance().getSharedPreferences(userName, Context.MODE_PRIVATE);
        editor = mSharedPreferences.edit();
        editor.apply();
    }
    public static synchronized void init(String userName) {
        mPreferenceManager = new PreferenceManager(userName);
    }
    public static synchronized void init() {
        mPreferenceManager = new PreferenceManager(LOGIN_USER);
    }
    /**
     * 单例模式，获取instance实例
     *
     * @return
     */
    public synchronized static PreferenceManager getInstance() {
        if (mPreferenceManager == null) {
            throw new NullPointerException("PreferenceManager not init !!!");
        }
        return mPreferenceManager;
    }
    public void setSettingMsgNotification(boolean paramBoolean) {
        editor.putBoolean(SHARED_KEY_SETTING_NOTIFICATION, paramBoolean);
        editor.commit();
    }
    public boolean getSettingMsgNotification() {
        return mSharedPreferences.getBoolean(SHARED_KEY_SETTING_NOTIFICATION, true);
    }
    public void setSettingMsgSound(boolean paramBoolean) {
        editor.putBoolean(SHARED_KEY_SETTING_SOUND, paramBoolean);
        editor.commit();
    }
    public boolean getSettingMsgSound() {
        return mSharedPreferences.getBoolean(SHARED_KEY_SETTING_SOUND, true);
    }
    public void setSettingMsgVibrate(boolean paramBoolean) {
        editor.putBoolean(SHARED_KEY_SETTING_VIBRATE, paramBoolean);
        editor.commit();
    }
    public boolean getSettingMsgVibrate() {
        return mSharedPreferences.getBoolean(SHARED_KEY_SETTING_VIBRATE, true);
    }
    public void setSettingMsgSpeaker(boolean paramBoolean) {
        editor.putBoolean(SHARED_KEY_SETTING_SPEAKER, paramBoolean);
        editor.commit();
    }
    public boolean getSettingMsgSpeaker() {
        return mSharedPreferences.getBoolean(SHARED_KEY_SETTING_SPEAKER, true);
    }
    public void setSettingAllowChatroomOwnerLeave(boolean value) {
        editor.putBoolean(SHARED_KEY_SETTING_CHAT_ROOM_OWNER_LEAVE, value);
        editor.commit();
    }
    public boolean getSettingAllowChatroomOwnerLeave() {
        return mSharedPreferences.getBoolean(SHARED_KEY_SETTING_CHAT_ROOM_OWNER_LEAVE, true);
    }
    public void setDeleteMessagesAsExitGroup(boolean value) {
        editor.putBoolean(SHARED_KEY_SETTING_DELETE_MESSAGES_WHEN_EXIT_GROUP, value);
        editor.commit();
    }
    public boolean isDeleteMessagesAsExitGroup() {
        return mSharedPreferences.getBoolean(SHARED_KEY_SETTING_DELETE_MESSAGES_WHEN_EXIT_GROUP, true);
    }
    public void setAutoAcceptGroupInvitation(boolean value) {
        editor.putBoolean(SHARED_KEY_SETTING_AUTO_ACCEPT_GROUP_INVITATION, value);
        editor.commit();
    }
    public boolean isAutoAcceptGroupInvitation() {
        return mSharedPreferences.getBoolean(SHARED_KEY_SETTING_AUTO_ACCEPT_GROUP_INVITATION, true);
    }
    public void setAdaptiveVideoEncode(boolean value) {
        editor.putBoolean(SHARED_KEY_SETTING_ADAPTIVE_VIDEO_ENCODE, value);
        editor.commit();
    }
    public boolean isAdaptiveVideoEncode() {
        return mSharedPreferences.getBoolean(SHARED_KEY_SETTING_ADAPTIVE_VIDEO_ENCODE, false);
    }
    public void setGroupsSynced(boolean synced) {
        editor.putBoolean(SHARED_KEY_SETTING_GROUPS_SYNCED, synced);
        editor.commit();
    }
    public boolean isGroupsSynced() {
        return mSharedPreferences.getBoolean(SHARED_KEY_SETTING_GROUPS_SYNCED, false);
    }
    public void setContactSynced(boolean synced) {
        editor.putBoolean(SHARED_KEY_SETTING_CONTACT_SYNCED, synced);
        editor.commit();
    }
    public boolean isContactSynced() {
        return mSharedPreferences.getBoolean(SHARED_KEY_SETTING_CONTACT_SYNCED, false);
    }
    public void setBlacklistSynced(boolean synced) {
        editor.putBoolean(SHARED_KEY_SETTING_BALCKLIST_SYNCED, synced);
        editor.commit();
    }
    public boolean isBacklistSynced() {
        return mSharedPreferences.getBoolean(SHARED_KEY_SETTING_BALCKLIST_SYNCED, false);
    }
    public void setCurrentUserNick(String nick) {
        editor.putString(SHARED_KEY_CURRENTUSER_NICK, nick);
        editor.commit();
    }
    public void setCurrentUserAvatar(String avatar) {
        editor.putString(EditorKey.key_avatar, avatar);
        editor.commit();
    }
    public String getCurrentUserAvatar() {
        return mSharedPreferences.getString(EditorKey.key_avatar, null);
    }
    public String getCurrentUserNick() {
        return mSharedPreferences.getString(SHARED_KEY_CURRENTUSER_NICK, null);
    }
    public void saveUserName(String username) {
        editor.putString(SHARED_KEY_CURRENTUSER_USERNAME, username);
        editor.commit();
    }
    public String getCurrentUsername() {
        return mSharedPreferences.getString(SHARED_KEY_CURRENTUSER_USERNAME, null);
    }
    public void saveBelong(String belong) {
        editor.putString(EditorKey.key_belong, belong);
        editor.commit();
    }
    public String getBelong() {
        return mSharedPreferences.getString(EditorKey.key_belong,"");
    }
    public void savePwd(String pwd) {
        editor.putString(EditorKey.key_pwd, pwd);
        editor.commit();
    }
    public String getPwd() {
        return mSharedPreferences.getString(EditorKey.key_pwd, null);
    }
    public void removeCurrentUserInfo() {
        editor.remove(SHARED_KEY_CURRENTUSER_NICK);
        editor.remove(SHARED_KEY_CURRENTUSER_AVATAR);
        editor.commit();
    }
    public void saveToken(String token) {
        editor.putString(EditorKey.key_token, token);
        editor.commit();
    }
    public void saveSignature(String appsecret) {
        editor.putString(EditorKey.key_signature, appsecret);
        editor.commit();
    }
    public String getToken() {
        return mSharedPreferences.getString(EditorKey.key_token,"");
    }
    public String getSignature() {
        return mSharedPreferences.getString(EditorKey.key_signature, "");
    }
    public static void clear() {
        if (editor != null) {
            editor.clear();
        }
    }
}
