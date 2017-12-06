package com.zhiyou.colleageapp.appui.mvpMode;

import com.zhiyou.colleageapp.appui.model.LifeInfo;
import com.zhiyou.colleageapp.appui.model.PartTime;
import com.zhiyou.colleageapp.appui.model.SchoolActDetail;
import com.zhiyou.colleageapp.appui.model.SchoolActivity;
import com.zhiyou.colleageapp.appui.model.Taoyu;
import com.zhiyou.colleageapp.appui.model.TaoyuCategory;
import com.zhiyou.colleageapp.appui.model.TaoyuDetail;
import com.zhiyou.colleageapp.appui.model.TreeHole;
import com.zhiyou.colleageapp.appui.model.TreeHoleDetailInfo;
import com.zhiyou.colleageapp.appui.mvpMode.action.IActionBase;
import com.zhiyou.colleageapp.constants.HttpKey;
import com.zhiyou.colleageapp.httpservice.ApiFactory;
import com.zhiyou.colleageapp.httpservice.ApiResult;
import com.zhiyou.colleageapp.httpservice.service.LifeService;

import java.util.List;
import java.util.Map;

import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.Query;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Create by chuyh on 2016/5/30.
 */
public class ModeLife extends ModeBase implements LifeService {

    public ModeLife() {
        super(null);
    }

    @Override
    public Observable<ApiResult<LifeInfo>> getLifeInfo(Map<String, String> map) {
        return ApiFactory.getFartory().getLifeService().getLifeInfo(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<ApiResult<List<TreeHole>>> getTreeHoleList(Map<String, String> map) {
        return ApiFactory.getFartory().getLifeService().getTreeHoleList(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<ApiResult<List<TreeHole>>> getMyTreeHoleList(@FieldMap Map<String, String> map) {
        return ApiFactory.getFartory().getLifeService().getMyTreeHoleList(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<ApiResult<List<TreeHole>>> getComTreeHoleList(@FieldMap Map<String, String> map) {
        return ApiFactory.getFartory().getLifeService().getComTreeHoleList(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<ApiResult> pubTreeHole(Map<String, String> map, @Field(HttpKey.CONTENT) String content) {
        return null;
    }

    @Override
    public Observable<ApiResult<TreeHoleDetailInfo>> getTreeHoleComment(@FieldMap Map<String, String> map, @Field(HttpKey.TREEHOLE_ID) String id) {
        return ApiFactory.getFartory().getLifeService().getTreeHoleComment(map, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<ApiResult<List<PartTime>>> getOfferList(Map<String, String> map) {
        return ApiFactory.getFartory().getLifeService().getOfferList(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<ApiResult<PartTime>> getOfferDetail(Map<String, String> map, @Field(HttpKey.PARTTIME_ID) String id) {
        return ApiFactory.getFartory().getLifeService().getOfferDetail(map, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<ApiResult<List<SchoolActivity>>> getSchoolActList(Map<String, String> map) {
        return ApiFactory.getFartory().getLifeService().getSchoolActList(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<ApiResult<SchoolActDetail>> getSchoolActDetail(Map<String, String> map, @Field(HttpKey.SCHOOLACT_ID) String id) {
        return ApiFactory.getFartory().getLifeService().getSchoolActDetail(map, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<ApiResult<List<Taoyu>>> getTaoyuList(Map<String, String> map) {
        return ApiFactory.getFartory().getLifeService().getTaoyuList(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<ApiResult<List<TaoyuCategory>>> getTaoyuCategory(@FieldMap Map<String, String> map) {
        return ApiFactory.getFartory().getLifeService().getTaoyuCategory(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<ApiResult<TaoyuDetail>> pubTaoyu(@FieldMap Map<String, String> map, @Field(HttpKey.TAOYU_NAME) String name, @Field(HttpKey.TAOYU_IMGS) String[] imgs, @Field(HttpKey.TAOYU_CATEID) String cateid, @Field(HttpKey.TAOYU_PRICE) String price, @Field(HttpKey.TAOYU_INTRO) String intro) {
        return ApiFactory.getFartory().getLifeService().pubTaoyu(map, name, imgs, cateid, price, intro)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    @Override
    public Observable<ApiResult<TaoyuDetail>> getTaoyuDetail(Map<String, String> map, @Field(HttpKey.TAOYU_ID) String id) {
        return ApiFactory.getFartory().getLifeService().getTaoyuDetail(map, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<ApiResult<List<SchoolActivity>>> getTalkList(Map<String, String> map) {
        return null;
    }

    @Override
    public Observable<ApiResult> getTalkDetail(Map<String, String> map, @Field(HttpKey.TALK_ID) String id) {
        return null;
    }
}
