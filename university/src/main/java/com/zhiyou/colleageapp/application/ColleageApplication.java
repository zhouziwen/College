package com.zhiyou.colleageapp.application;
import android.app.Application;
import android.content.Context;
import com.zhiyou.colleageapp.Helper;
import com.zhiyou.colleageapp.manager.PreferenceManager;
/**
 * Created by wujiaolong on 16/5/13.
 *
 */
public class ColleageApplication extends Application {
    private static ColleageApplication mInstance;
    private boolean mIsTokenExpired = false;
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        PreferenceManager.init();
        Helper.getInstance().initEase(mInstance);
    }
    public static ColleageApplication getInstance(){
        return mInstance;
    }
    @Override
    protected void attachBaseContext(Context base){
        super.attachBaseContext(base);
    }
    public boolean isTokenExpired(){
        return mIsTokenExpired;
    }
    public void setTokenExpired(boolean tokenExpired){
        mIsTokenExpired = tokenExpired;
    }
}
