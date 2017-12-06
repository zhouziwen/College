package com.zhiyou.colleageapp.appui.listener;

/**
 * Author ： LongWeiHu on 2016/5/17.
 */
public interface DataSyncListener {
    /**
     * 同步完毕
     * @param success true：成功同步到数据，false失败
     */
     void onSyncComplete(boolean success);
}
