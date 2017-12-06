package com.zhiyou.colleageapp.httpservice.service;

import com.zhiyou.colleageapp.appui.model.SchoolInfo;
import com.zhiyou.colleageapp.appui.model.SchoolNews;
import com.zhiyou.colleageapp.appui.model.SchoolSlider;
import com.zhiyou.colleageapp.appui.model.SchoolSmallNews;
import com.zhiyou.colleageapp.appui.model.SocietyNews;
import com.zhiyou.colleageapp.constants.HttpKey;
import com.zhiyou.colleageapp.httpservice.UrlConstant;
import com.zhiyou.colleageapp.httpservice.ApiResult;

import java.util.List;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by chuyh on 2016/5/10.
 */
public interface SchoolService {
    /**
     * 获取校园信息
     *
     * @return
     */
    @GET(UrlConstant.GetSchoolInfo)
    Observable<ApiResult<SchoolInfo>> getSchoolInfo(@Query(HttpKey.SIGNITURE) String signature);

    /**
     * 获取schoolfragment轮播图片信息
     *
     * @return
     */
    @GET(UrlConstant.GetSchoolSlider)
    Observable<ApiResult<List<SchoolSlider>>> getSchoolSlider(@Query(HttpKey.SIGNITURE) String signature);

    /**
     * 获取schoolnews 新闻详情信息
     * news_id
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.GetSchoolNewsDetail)
    Observable<ApiResult> getSchoolNewsDetail(
            @Query(HttpKey.SIGNITURE) String signature,
            @Field(HttpKey.NEWS_ID) String id
    );

    /**
     * 获取社会 新闻列表
     *
     * @return
     */
    @GET(UrlConstant.GetSocietyNewsList)
    Observable<ApiResult<List<SocietyNews>>> getSocietyNewsList(@Query(HttpKey.SIGNITURE) String signature);

    /**
     * 获取学校 新闻列表
     *
     * @return
     */
    @GET(UrlConstant.GetSchoolNewsList)
    Observable<ApiResult<List<SchoolNews>>> getSchoolNewsList(@Query(HttpKey.SIGNITURE) String signature);

    /**
     * 获取学校专业
     *
     * @return
     */
    @GET(UrlConstant.GetSchoolMajorList)
    Observable<ApiResult<List<SocietyNews>>> getSchoolMajorList(@Query(HttpKey.SIGNITURE) String signature);

    /**
     * 获取学校首页小图新闻
     *
     * @return
     */
    @GET(UrlConstant.GetSchoolSimgnewsList)
    Observable<ApiResult<List<SchoolSmallNews>>> getSchoolSimgnewsList(@Query(HttpKey.SIGNITURE) String signature);
}
