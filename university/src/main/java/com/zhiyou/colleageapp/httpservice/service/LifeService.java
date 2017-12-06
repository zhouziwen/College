package com.zhiyou.colleageapp.httpservice.service;

import com.zhiyou.colleageapp.appui.model.LifeInfo;
import com.zhiyou.colleageapp.appui.model.PartTime;
import com.zhiyou.colleageapp.appui.model.SchoolActDetail;
import com.zhiyou.colleageapp.appui.model.SchoolActivity;
import com.zhiyou.colleageapp.appui.model.SchoolInfo;
import com.zhiyou.colleageapp.appui.model.Taoyu;
import com.zhiyou.colleageapp.appui.model.TaoyuCategory;
import com.zhiyou.colleageapp.appui.model.TaoyuDetail;
import com.zhiyou.colleageapp.appui.model.TreeHole;
import com.zhiyou.colleageapp.appui.model.TreeHoleDetailInfo;
import com.zhiyou.colleageapp.constants.HttpKey;
import com.zhiyou.colleageapp.httpservice.ApiResult;
import com.zhiyou.colleageapp.httpservice.UrlConstant;

import java.util.List;
import java.util.Map;

import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by chuyh on 2016/5/10.
 */
public interface LifeService {

    /**
     * 获取生活信息
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.GetLifeInfoUrl)
    Observable<ApiResult<LifeInfo>> getLifeInfo(@FieldMap Map<String, String> map);

    /**
     * 获取树洞列表信息
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.GetTreeHoleList)
    Observable<ApiResult<List<TreeHole>>> getTreeHoleList(@FieldMap Map<String, String> map);

    /**
     * 获取我发表的树洞列表信息
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.TreeMylist)
    Observable<ApiResult<List<TreeHole>>> getMyTreeHoleList(@FieldMap Map<String, String> map);

    /**
     * 获取我评论的树洞列表信息
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.TreeComList)
    Observable<ApiResult<List<TreeHole>>> getComTreeHoleList(@FieldMap Map<String, String> map);

    /**
     * 发布树洞信息
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.PubTreeHole)
    Observable<ApiResult> pubTreeHole(@FieldMap Map<String, String> map, @Field(HttpKey.CONTENT) String content);

    /**
     * 树洞评论列表信息
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.TreeDetail)
    Observable<ApiResult<TreeHoleDetailInfo>> getTreeHoleComment(@FieldMap Map<String, String> map, @Field(HttpKey.TREEHOLE_ID) String id);

    /**
     * 获取兼职列表信息
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.GetPartTime)
    Observable<ApiResult<List<PartTime>>> getOfferList(@FieldMap Map<String, String> map);

    /**
     * 获取兼职详情信息
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.GetPartTimeDetail)
    Observable<ApiResult<PartTime>> getOfferDetail(
            @FieldMap Map<String, String> map,
            @Field(HttpKey.PARTTIME_ID) String id
    );

    /**
     * 获取校园活动列表信息
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.GetSchoolActList)
    Observable<ApiResult<List<SchoolActivity>>> getSchoolActList(@FieldMap Map<String, String> map);

    /**
     * 获取校园活动详情信息
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.GetSchoolActDetail)
    Observable<ApiResult<SchoolActDetail>> getSchoolActDetail(
            @FieldMap Map<String, String> map,
            @Field(HttpKey.SCHOOLACT_ID) String id
    );

    /**
     * 获取淘渔列表信息
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.GetTaoyuList)
    Observable<ApiResult<List<Taoyu>>> getTaoyuList(@FieldMap Map<String, String> map);

    /**
     * 获取淘渔分类信息
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.TaoyuCategory)
    Observable<ApiResult<List<TaoyuCategory>>> getTaoyuCategory(@FieldMap Map<String, String> map);


    /**
     * 发布淘渔信息
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.PubTaoyu)
    Observable<ApiResult<TaoyuDetail>> pubTaoyu(
            @FieldMap Map<String, String> map,
            @Field(HttpKey.TAOYU_NAME) String name,
            @Field(HttpKey.TAOYU_IMGS) String[] imgs,
            @Field(HttpKey.TAOYU_CATEID) String cateid,
            @Field(HttpKey.TAOYU_PRICE) String price,
            @Field(HttpKey.TAOYU_INTRO) String intro
    );


    /**
     * 获取兼职详情信息
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.GetTaoyuDetail)
    Observable<ApiResult<TaoyuDetail>> getTaoyuDetail(
            @FieldMap Map<String, String> map,
            @Field(HttpKey.TAOYU_ID) String id
    );

    /**
     * 获取话题列表信息
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.GetTalkList)
    Observable<ApiResult<List<SchoolActivity>>> getTalkList(@FieldMap Map<String, String> map);

    /**
     * 获取话题详情信息
     *
     * @return
     */
    @FormUrlEncoded
    @POST(UrlConstant.GetTaoyuDetail)
    Observable<ApiResult> getTalkDetail(
            @FieldMap Map<String, String> map,
            @Field(HttpKey.TALK_ID) String id
    );

}
