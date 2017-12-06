package com.zhiyou.colleageapp.httpservice;

import com.zhiyou.colleageapp.BuildConfig;
import com.zhiyou.colleageapp.httpservice.service.FileService;
import com.zhiyou.colleageapp.httpservice.service.FriendService;
import com.zhiyou.colleageapp.httpservice.service.LifeService;
import com.zhiyou.colleageapp.httpservice.service.SchoolService;
import com.zhiyou.colleageapp.httpservice.service.UserService;
import com.zhiyou.colleageapp.utils.AppLog;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by chuyh on 2016/5/23 0023.
 */
public class ApiFactory implements ApiService {

    /**
     * 获取Api实例
     * <p/>
     * 延迟初始化，这里是利用了 Java 的语言特性，
     * 内部类只有在使用的时候，才回去加载，从而初始化内部静态变量。
     * 关于线程安全，这是 Java 运行环境自动给你保证的，在加载的时候，会自动隐形的同步。
     * 在访问对象的时候，不需要同步 Java 虚拟机又会自动给你取消同步，所以效率非常高。
     * <p/>
     * 关于final的一些说法
     * 只要对象是正确构造的（被构造对象的引用在构造函数中没有“逸出”），
     * 那么不需要使用同步（指lock和volatile的使用），就可以保证任意线程都能看到这个final域在构造函数中被初始化之后的值。
     *
     * @return
     */

    private Retrofit retrofit;

    public static ApiFactory getFartory() {
        return ApiHolder.factory;
    }

    private static class ApiHolder {
        private static final ApiFactory factory = new ApiFactory();
    }

    protected ApiFactory() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                if (BuildConfig.DEBUG) {
                    AppLog.instance().d("  HttpLogging  :  " + message);
                }
            }
        });
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder().addInterceptor(loggingInterceptor);
        OkHttpClient okHttpClient = builder.build();
        retrofit = new Retrofit.Builder()
                .baseUrl(UrlConstant.BaseUrl)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
    }

    private SchoolService mSchoolService;

    @Override
    public SchoolService getSchoolService() {
        if (mSchoolService == null) {
            mSchoolService = getFartory().retrofit.create(SchoolService.class);
        }
        return mSchoolService;
    }

    private UserService mUserService;

    @Override
    public UserService getUserService() {
        if (mUserService == null) {
            mUserService = getFartory().retrofit.create(UserService.class);
        }
        return mUserService;
    }

    private LifeService mLifeService;

    @Override
    public LifeService getLifeService() {
        if (mLifeService == null) {
            mLifeService = getFartory().retrofit.create(LifeService.class);
        }
        return mLifeService;
    }


    private FriendService mFriendService;

    @Override
    public FriendService getFriendService() {
        if (mFriendService == null) {
            mFriendService = getFartory().retrofit.create(FriendService.class);
        }
        return mFriendService;
    }

    private FileService mFileService;

    @Override
    public FileService getFileService() {
        if (mFileService == null) {
            mFileService = getFartory().retrofit.create(FileService.class);
        }
        return mFileService;
    }
}
