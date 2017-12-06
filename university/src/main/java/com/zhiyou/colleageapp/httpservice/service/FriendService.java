package com.zhiyou.colleageapp.httpservice.service;

import com.zhiyou.colleageapp.constants.HttpKey;
import com.zhiyou.colleageapp.domain.FriendGroup;
import com.zhiyou.colleageapp.domain.Person;
import com.zhiyou.colleageapp.domain.PersonalProfile;
import com.zhiyou.colleageapp.domain.User;
import com.zhiyou.colleageapp.httpservice.ApiResult;
import com.zhiyou.colleageapp.httpservice.UrlConstant;

import java.util.List;
import java.util.Map;

import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Create by LongWeiHu on 2016/6/1.
 */
public interface FriendService {

    /**get the friend list of mine
     * @return Observable&lt;ApiResult<List<User>>&gt;
     */
    @FormUrlEncoded
    @POST(UrlConstant.FRIEND_LIST)
    Observable<ApiResult<List<User>>> getFriendList(@FieldMap Map<String,String> map);

    /**delete one friend
     * @param friendName : the name of the friend be deleted
     * @return Observable&lt;ApiResult&gt;
     */
    @FormUrlEncoded
    @POST(UrlConstant.FRIEND_DELETE)
    Observable<ApiResult> deleteFriend(@FieldMap Map<String,String> fieldMap,
                                       @Field(HttpKey.FRIEND_NAME) String friendName);

    /**
     * @param friendName :
     * @return :
     */
    @FormUrlEncoded
    @POST(UrlConstant.FRIEND_ADD)
    Observable<ApiResult> addFriend(@FieldMap Map<String,String> fieldMap,@Field(HttpKey.FRIEND_NAME) String friendName);

    /**判断用户是否存在
     * @param friendName :
     * @return :
     */
    @FormUrlEncoded
    @POST(UrlConstant.FRIEND_IS_EXISTS)
    Observable<ApiResult<User>> isUserExist(@Field(HttpKey.FRIEND_NAME) String friendName);
    /**
     * @param fieldMap :
     * @return :
     */
    @FormUrlEncoded
    @POST(UrlConstant.FRIEND_GROUP_LIST)
    Observable<ApiResult<Map<String,List<FriendGroup>>>> getGroupList(@FieldMap Map<String,String> fieldMap);


    /**
     * @return :
     */
    @FormUrlEncoded
    @POST(UrlConstant.FRIEND_GROUP_MEMBERS)
    Observable<ApiResult<List<Person>>> getGroupMembers(@FieldMap Map<String,String> fieldMap,@Field(HttpKey.GROUP_ID) String groupId);

    /**
     * @param Name :
     * @param summary :
     * @param isPublic :
     * @param isAddNeedApproval :
     * @param members :
     * @return :
     */
    @FormUrlEncoded
    @POST(UrlConstant.FRIEND_GROUP_CREATE)
        // TODO:By LongWeiHu 2016/6/1 这里还有问题：未标识群是否是公开的
    Observable<ApiResult<FriendGroup>> createGroup(
            @FieldMap Map<String,String> fieldMap,
            @Field(HttpKey.GROUP_NAME) String Name,
            @Field(HttpKey.GROUP_DESC) String summary,
            @Field(HttpKey.GROUP_IS_PUBLIC) boolean isPublic,
            @Field(HttpKey.GROUP_IS_NEED_APPROVAL) boolean isAddNeedApproval,
            @Field(HttpKey.GROUP_MEMBERS) List<String> members);


//    @FormUrlEncoded
//    @POST(UrlConstant.FRIEND_GROUP_ADD_MEMBERS)
//    Observable<ApiResult> add


    /**quit the group
     * @param fieldMap :
     * @param groupId :
     * @param who :
     * @return :
     */
    @FormUrlEncoded
    @POST(UrlConstant.FRIEND_GROUP_QUIT)
    Observable<ApiResult> quitGroup(@FieldMap Map<String,String> fieldMap,@Field(HttpKey.GROUP_ID) String groupId,String who);

    /** delete the group
     * @param fileMap :
     * @param groupId :
     * @return :
     */
    @FormUrlEncoded
    @POST(UrlConstant.FRIEND_GROUP_DELETE)
    Observable<ApiResult> deleteGroup(@FieldMap Map<String,String> fileMap,@Field(HttpKey.GROUP_ID) String groupId);

    /**
     * move friends or group members to black list, include operate group and contact
     *
     * @param mFiledMap :
     * @param userNames :
     * @return :
     */
    @FormUrlEncoded
    @POST(UrlConstant.FRIEND_MOVE_TO_BLACK_LIST)
    Observable<ApiResult> moveContactsToBlackList(@FieldMap Map<String, String> mFiledMap, @Field(HttpKey.FRIEND_NAME) List<String> userNames);

    /**获取朋友详情,包括自己,当获取自己时, friendName不传
     * @param map :
     * @param friendName :
     * @return :
     */
    @FormUrlEncoded
    @POST(UrlConstant.PERSONAL_PROFILE)
    Observable<ApiResult<PersonalProfile>> getPersonalProfile(@FieldMap Map<String,String> map, @Field(HttpKey.FRIEND_NAME) String friendName);
}
