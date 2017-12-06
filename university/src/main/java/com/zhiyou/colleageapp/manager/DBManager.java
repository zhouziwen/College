package com.zhiyou.colleageapp.manager;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.zhiyou.colleageapp.application.ColleageApplication;
import com.zhiyou.colleageapp.db.DaoMaster;
import com.zhiyou.colleageapp.db.DaoSession;
import com.zhiyou.colleageapp.db.GroupDao;
import com.zhiyou.colleageapp.db.InviteMessageDao;
import com.zhiyou.colleageapp.db.MessageDao;
import com.zhiyou.colleageapp.db.PrefDao;
import com.zhiyou.colleageapp.db.UserDao;

import java.io.File;

/**
 * Author ： LongWeiHu on 2016/5/18.
 */
public class DBManager {
    private SQLiteDatabase mDb;
    private DaoSession mDaoSession;
    private DBManager() {
    }
    /**
     * DBManager的初始化，只在登录成功后初始化一次，其他地方不可调用
     */
    public void init(String userName) {
        File file = ColleageApplication.getInstance().getDatabasePath(userName);
        if (file.exists()) {
            mDb = SQLiteDatabase.openOrCreateDatabase(file, null);
        } else {
            SQLiteOpenHelper helper = new DaoMaster.DevOpenHelper(ColleageApplication.getInstance(), userName, null);
            mDb = helper.getReadableDatabase();
        }
        DaoMaster daoMaster = new DaoMaster(mDb);
        mDaoSession = daoMaster.newSession();
    }
    public void update(String userName) {
        clear();
        init(userName);
    }
    /* 此处使用一个内部类来维护单例 */
    private static class SingletonFactory {
        private static DBManager instance = new DBManager();
    }
    public static DBManager getInstance() {
        return SingletonFactory.instance;
    }
    public  DaoSession getDaoSession() {
        return mDaoSession;
    }
    public UserDao getUserDao() {
        return mDaoSession.getUserDao();
    }
    public MessageDao getMsgDao() {
        return mDaoSession.getMessageDao();
    }
    public InviteMessageDao getInviteMessageDao() {
        return mDaoSession.getInviteMessageDao();
    }
    public PrefDao getPrefDao() {
        return mDaoSession.getPrefDao();
    }
    public GroupDao getGroupDao() {
        return mDaoSession.getGroupDao();
    }
    public  SQLiteDatabase getDb() {
        return mDb;
    }
    public void clear() {
        if (mDaoSession != null) {
            mDaoSession.clear();
        }
    }



}
