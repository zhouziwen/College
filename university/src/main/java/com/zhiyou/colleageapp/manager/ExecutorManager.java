package com.zhiyou.colleageapp.manager;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/**
 * Created by Administrator on 2016/5/20.
 * 单例的ExecutorManager 用来构建一个全局的线程池
 */
public class ExecutorManager {
    private ExecutorService mExecutorService;
    private ExecutorManager() {
        mExecutorService = Executors.newCachedThreadPool();
    }
    /* 此处使用一个内部类来维护单例 */
    private static class SingletonFactory {
        private static ExecutorManager instance = new ExecutorManager();
    }
    public static ExecutorManager instance() {
      return SingletonFactory.instance;
    }
    public  ExecutorService getExecutorService() {
        return mExecutorService;
    }
}
