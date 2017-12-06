package com.zhiyou.colleageapp.httpservice.service;

import com.zhiyou.colleageapp.constants.HttpKey;
import com.zhiyou.colleageapp.domain.LoginUser;
import com.zhiyou.colleageapp.domain.PersonalProfile;
import com.zhiyou.colleageapp.httpservice.ApiResult;
import com.zhiyou.colleageapp.httpservice.UrlConstant;

import java.util.Map;

import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by chuyh on 2016/5/10.
 */
public interface UserService {
    /**
     * 用户注册
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.REGISTER_URL)
    Observable<ApiResult> register(
            @Query(HttpKey.SIGNITURE) String signature,
            @Field(HttpKey.USER_NAME) String username,
            @Field(HttpKey.USER_PWD) String password,
            @Field(HttpKey.SMS_CODE) String sms_code

    );

    /**
     * 用户登录
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.LOGIN_URL)
    Observable<ApiResult<LoginUser>> login(
            @Query(HttpKey.SIGNITURE) String signature,
            @Field(HttpKey.USER_NAME) String username,
            @Field(HttpKey.USER_PWD) String password
    );

    /**modify the personal info
     * @param fieldMap :
     * @param newValue : the new content map
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.MODIFY_PERSONAL_INFO)
    Observable<ApiResult<PersonalProfile>> modifyPersonalInfo(@FieldMap Map<String,String> fieldMap, @FieldMap Map<String,String> newValue);

}

