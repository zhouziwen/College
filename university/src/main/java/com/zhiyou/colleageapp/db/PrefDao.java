package com.zhiyou.colleageapp.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.zhiyou.colleageapp.constants.DBKey;
import com.zhiyou.colleageapp.domain.Pref;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * DAO for table "PREF".
*/
public class PrefDao extends AbstractDao<Pref, String> {

    public static final String TABLENAME = "PREF";

    /**
     * Properties of entity Pref.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property DisableGroupId = new Property(0, String.class, "DisableGroupId", true, "DISABLE_GROUP_ID");
        public final static Property UserId = new Property(1, String.class, "UserId", false, "USER_ID");
        public final static Property DisableGroupName = new Property(2, String.class, "DisableGroupName", false, "DISABLE_GROUP_NAME");
    };


    public PrefDao(DaoConfig config) {
        super(config);
    }
    
    public PrefDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"PREF\" (" + //
                "\"DISABLE_GROUP_ID\" TEXT PRIMARY KEY NOT NULL UNIQUE ," + // 0: DisableGroupId
                "\"USER_ID\" TEXT," + // 1: UserId
                "\"DISABLE_GROUP_NAME\" TEXT);"); // 2: DisableGroupName
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"PREF\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Pref entity) {
        stmt.clearBindings();
 
        String DisableGroupId = entity.getDisableGroupId();
        if (DisableGroupId != null) {
            stmt.bindString(1, DisableGroupId);
        }
 
        String UserId = entity.getUserId();
        if (UserId != null) {
            stmt.bindString(2, UserId);
        }
 
        String DisableGroupName = entity.getDisableGroupName();
        if (DisableGroupName != null) {
            stmt.bindString(3, DisableGroupName);
        }
    }

    /** @inheritdoc */
    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Pref readEntity(Cursor cursor, int offset) {
        Pref entity = new Pref( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // DisableGroupId
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // UserId
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2) // DisableGroupName
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Pref entity, int offset) {
        entity.setDisableGroupId(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setUserId(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setDisableGroupName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
     }
    
    /** @inheritdoc */
    @Override
    protected String updateKeyAfterInsert(Pref entity, long rowId) {
        return entity.getDisableGroupId();
    }
    
    /** @inheritdoc */
    @Override
    public String getKey(Pref entity) {
        if(entity != null) {
            return entity.getDisableGroupId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }


    public List<String> queryDisableGroupIds(SQLiteDatabase db) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT ");
        builder.append(DBKey.PREF_DISABLE_GROUP_ID);
        builder.append(" FROM ");
        builder.append(PrefDao.TABLENAME);
        Cursor cursor = db.rawQuery(builder.toString(), null);
        if (cursor == null) {
            return null;
        }
        List<String> idList = new ArrayList<>();
        while (cursor.moveToNext()) {
            idList.add(cursor.getString(0));
        }

        cursor.close();

        return idList;
    }


    public List<String> queryDisableGroupNames(SQLiteDatabase db) {
        if (db == null) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT ");
        builder.append(DBKey.PREF_DISABLE_GROUP_NAME);
        builder.append(" FROM ");
        builder.append(PrefDao.TABLENAME);
        Cursor cursor = db.rawQuery(builder.toString(), null);
        if (cursor == null) {
            return null;
        }
        List<String> nameList = new ArrayList<>();
        while (cursor.moveToNext()) {
            nameList.add(cursor.getString(2));
        }

        cursor.close();

        return nameList;
    }
}
