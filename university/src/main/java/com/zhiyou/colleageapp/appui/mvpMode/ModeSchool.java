package com.zhiyou.colleageapp.appui.mvpMode;

import com.zhiyou.colleageapp.appui.model.SchoolInfo;
import com.zhiyou.colleageapp.appui.model.SchoolNews;
import com.zhiyou.colleageapp.appui.model.SchoolSlider;
import com.zhiyou.colleageapp.appui.model.SchoolSmallNews;
import com.zhiyou.colleageapp.appui.model.SocietyNews;
import com.zhiyou.colleageapp.appui.mvpMode.action.IActionBase;
import com.zhiyou.colleageapp.constants.HttpKey;
import com.zhiyou.colleageapp.httpservice.ApiFactory;
import com.zhiyou.colleageapp.httpservice.ApiResult;
import com.zhiyou.colleageapp.httpservice.service.SchoolService;

import java.util.List;

import retrofit2.http.Field;
import retrofit2.http.Query;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Create by chuyh on 2016/5/30.
 */
public class ModeSchool extends ModeBase implements SchoolService {

    public ModeSchool() {
        super(null);
    }
    @Override
    public Observable<ApiResult<SchoolInfo>> getSchoolInfo(@Query(HttpKey.SIGNITURE) String signature) {
        return ApiFactory.getFartory().getSchoolService().getSchoolInfo(signature)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<ApiResult<List<SchoolSlider>>> getSchoolSlider(@Query(HttpKey.SIGNITURE) String signature) {
        return ApiFactory.getFartory().getSchoolService().getSchoolSlider(signature)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<ApiResult> getSchoolNewsDetail(@Query(HttpKey.SIGNITURE) String signature, @Field(HttpKey.NEWS_ID) String id) {
        return ApiFactory.getFartory().getSchoolService().getSchoolNewsDetail(signature, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<ApiResult<List<SocietyNews>>> getSocietyNewsList(@Query(HttpKey.SIGNITURE) String signature) {
        return ApiFactory.getFartory().getSchoolService().getSocietyNewsList(signature)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<ApiResult<List<SchoolNews>>> getSchoolNewsList(@Query(HttpKey.SIGNITURE) String signature) {
        return ApiFactory.getFartory().getSchoolService().getSchoolNewsList(signature)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<ApiResult<List<SocietyNews>>> getSchoolMajorList(@Query(HttpKey.SIGNITURE) String signature) {
        return ApiFactory.getFartory().getSchoolService().getSchoolMajorList(signature)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<ApiResult<List<SchoolSmallNews>>> getSchoolSimgnewsList(@Query(HttpKey.SIGNITURE) String signature) {
        return ApiFactory.getFartory().getSchoolService().getSchoolSimgnewsList(signature)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
