package com.zhiyou.colleageapp.appui.model;

import android.app.Activity;
import android.content.Context;

import com.hyphenate.chat.EMMessage;
import com.zhiyou.colleageapp.appui.listener.EaseNotifier;
import com.zhiyou.colleageapp.appui.listener.EaseSettingsProvider;
import com.zhiyou.colleageapp.domain.EaseEmoIcon;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class EaseUI {
    private static final String TAG = EaseUI.class.getSimpleName();

    /**
     * the global EaseUI instance
     */
    private static EaseUI instance = null;

    private EaseSettingsProvider settingsProvider;
    
    /**
     * application context
     */
    private Context appContext;
    
    /**
     * initEase flag: test if the sdk has been inited before, we don't need to initEase again
     */
    private boolean mSdkInit = false;
    
    /**
     * the notifier
     */
    private EaseNotifier notifier;

    /**
     * 用来记录注册了eventlistener的foreground Activity
     */
    private List<Activity> activityList = new ArrayList<>();

    public void pushActivity(Activity activity){
        if(!activityList.contains(activity)){
            activityList.add(0,activity);
        }
    }

    public void popActivity(Activity activity){
        activityList.remove(activity);
    }

    
    private EaseUI(){}
    
    /**
     * 获取EaseUI单实例对象
     * @return
     */
    public synchronized static EaseUI getInstance(){
        if(instance == null){
            instance = new EaseUI();
        }
        return instance;
    }
    
    /**
     *this function will initialize the HuanXin SDK
     * 
     * @return boolean true if caller can continue to call HuanXin related APIs after calling onInit, otherwise false.
     * 
     * 初始化环信sdk及easeui库
     * 返回true如果正确初始化，否则false，如果返回为false，请在后续的调用中不要调用任何和环信相关的代码
     * @param context
     * @return
     */
    public synchronized boolean init(Context context){
        if(mSdkInit){
            return true;
        }
        appContext = context;
        initNotifier();
        if(settingsProvider == null){
            settingsProvider = new DefaultSettingsProvider();
        }
        mSdkInit = true;
        return true;
    }

    
   private void initNotifier(){
        notifier = new EaseNotifier();
        notifier.init(appContext);
    }
    

    public EaseNotifier getNotifier(){
        return notifier;
    }
    
    public boolean hasForegroundActivity(){
        return activityList.size() != 0;
    }
    


    public void setSettingsProvider(EaseSettingsProvider settingsProvider){
        this.settingsProvider = settingsProvider;
    }
    
    public EaseSettingsProvider getSettingsProvider(){
        return settingsProvider;
    }
    
    

    /**
     * 表情信息提供者
     *
     */
    public interface EaseEmoIconInfoProvider {
        /**
         * 根据唯一识别号返回此表情内容
         * @param emoIconIdentityCode
         * @return
         */
        EaseEmoIcon getEmoIconInfo(String emoIconIdentityCode);
        
        /**
         * 获取文字表情的映射Map,map的key为表情的emoji文本内容，value为对应的图片资源id或者本地路径(不能为网络地址)
         * @return
         */
        Map<String, Object> getTextEmoIconMapping();
    }
    
    private EaseEmoIconInfoProvider emoIconInfoProvider;
    
    /**
     * 获取表情提供者
     * @return
     */
    public EaseEmoIconInfoProvider getEmoIconInfoProvider(){
        return emoIconInfoProvider;
    }
    
    /**
     * 设置表情信息提供者
     * @param emoIconInfoProvider
     */
    public void setEmoIconInfoProvider(EaseEmoIconInfoProvider emoIconInfoProvider){
        this.emoIconInfoProvider = emoIconInfoProvider;
    }
    

    /**
     * default settings provider
     *
     */
    protected class DefaultSettingsProvider implements EaseSettingsProvider{

        @Override
        public boolean isMsgNotifyAllowed(EMMessage message) {
            return true;
        }

        @Override
        public boolean isMsgSoundAllowed(EMMessage message) {
            return true;
        }

        @Override
        public boolean isMsgVibrateAllowed(EMMessage message) {
            return true;
        }

        @Override
        public boolean isSpeakerOpened() {
            return true;
        }

        
    }
}
