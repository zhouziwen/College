package com.zhiyou.colleageapp.httpservice.service;

import com.zhiyou.colleageapp.constants.HttpKey;
import com.zhiyou.colleageapp.httpservice.ApiResult;
import com.zhiyou.colleageapp.httpservice.UrlConstant;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by chuyh on 2016/5/10.
 */
public interface FileService {

    /**
     * 上传一张图片
     *
     * @param name
     * @param imgs
     * @return
     */
    @Multipart
    @POST(UrlConstant.Upload)
    Observable<ApiResult<List<String>>> uploadImage(
            @Query(HttpKey.SIGNITURE) String signature,
            @Part("fileName") String name,
            @Part("file\"; filename=\"image.png\"") RequestBody imgs);

    @Multipart
    @POST(UrlConstant.Upload)
    Observable<ApiResult<List<String>>> uploadImage(
            @QueryMap Map<String,String> map,
            @Part("fileName") String name,
            @Part("file\"; filename=\"image.png\"") RequestBody requestBody);
}
