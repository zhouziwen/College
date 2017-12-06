package com.zhiyou.colleageapp.eenum;

/**
 * Author ： LongWeiHu on 2016/5/18.
 */
public enum InviteMsgStatus {
    //==好友
    /**被单个好友邀请*/
    BEINVITEED,
    /**被单个好友拒绝*/
    BEREFUSED,
    /**对方(单个好友)同意*/
    BEAGREED,

    //==群组
    /**对方申请进入群*/
    BEAPPLYED,
    /**我同意了对方的请求*/
    AGREED,
    /**我拒绝了对方的请求*/
    REFUSED,

    //==群邀请
    /**收到对方的群邀请**/
    GROUPINVITATION,
    /**收到对方同意群邀请的通知**/
    GROUPINVITATION_ACCEPTED,
    /**收到对方拒绝群邀请的通知**/
    GROUPINVITATION_DECLINED
}
