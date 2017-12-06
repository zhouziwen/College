package com.zhiyou.colleageapp.db;

import android.database.sqlite.SQLiteDatabase;

import com.zhiyou.colleageapp.domain.FriendGroup;
import com.zhiyou.colleageapp.domain.InviteMessage;
import com.zhiyou.colleageapp.domain.Message;
import com.zhiyou.colleageapp.domain.Pref;
import com.zhiyou.colleageapp.domain.User;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {
    private final DaoConfig userDaoConfig;
    private final DaoConfig messageDaoConfig;
    private final DaoConfig inviteMessageDaoConfig;
    private final DaoConfig prefDaoConfig;
    private final DaoConfig groupDaoConfig;
    private final UserDao userDao;
    private final MessageDao messageDao;
    private final InviteMessageDao inviteMessageDao;
    private final PrefDao prefDao;
    private final GroupDao groupDao;
    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);
        userDaoConfig = daoConfigMap.get(UserDao.class).clone();
        userDaoConfig.initIdentityScope(type);
        messageDaoConfig = daoConfigMap.get(MessageDao.class).clone();
        messageDaoConfig.initIdentityScope(type);
        inviteMessageDaoConfig = daoConfigMap.get(InviteMessageDao.class).clone();
        inviteMessageDaoConfig.initIdentityScope(type);
        prefDaoConfig = daoConfigMap.get(PrefDao.class).clone();
        prefDaoConfig.initIdentityScope(type);
        groupDaoConfig = daoConfigMap.get(GroupDao.class).clone();
        groupDaoConfig.initIdentityScope(type);
        userDao = new UserDao(userDaoConfig, this);
        messageDao = new MessageDao(messageDaoConfig, this);
        inviteMessageDao = new InviteMessageDao(inviteMessageDaoConfig, this);
        prefDao = new PrefDao(prefDaoConfig, this);
        groupDao = new GroupDao(groupDaoConfig, this);
        registerDao(User.class, userDao);
        registerDao(Message.class, messageDao);
        registerDao(InviteMessage.class, inviteMessageDao);
        registerDao(Pref.class, prefDao);
        registerDao(FriendGroup.class, groupDao);
    }
    
    public void clear() {
        userDaoConfig.getIdentityScope().clear();
        messageDaoConfig.getIdentityScope().clear();
        inviteMessageDaoConfig.getIdentityScope().clear();
        prefDaoConfig.getIdentityScope().clear();
        groupDaoConfig.getIdentityScope().clear();
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public MessageDao getMessageDao() {
        return messageDao;
    }

    public InviteMessageDao getInviteMessageDao() {
        return inviteMessageDao;
    }

    public PrefDao getPrefDao() {
        return prefDao;
    }

    public GroupDao getGroupDao() {
        return groupDao;
    }

}
