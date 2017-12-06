package com.zhiyou.colleageapp.httpservice;

import com.zhiyou.colleageapp.constants.HttpKey;

import java.util.HashMap;
import java.util.Map;

public interface UrlConstant {

    String BASE = "https://studentapp-kblog.rhcloud.com/";
    String BaseUrl = BASE + "api/";

    //=================================================
    //                  当前用户相关
    //=================================================
    String REGISTER_URL = "sign/signup";
    String LOGIN_URL = "sign/signin";
    String STUDENT = "student/";
    String PERSONAL_PROFILE = STUDENT + "getinfo?";
    String MODIFY_PERSONAL_INFO = STUDENT +"modifyinfo?";
    //===========================================================
    //                 朋友相关
    //===========================================================
    String FRIEND_BASE = "friendgroup/";
    /**
     * mine friend list
     */
    String FRIEND_LIST = FRIEND_BASE + "getfriends?";
    /**
     * delete one of mine friend
     */
    String FRIEND_DELETE = FRIEND_BASE + "delfriend?";
    /**
     * add one friend
     */
    String FRIEND_ADD = FRIEND_BASE + "addfriend?";
    /**
     * 查询用户是否存在
     */
    String FRIEND_IS_EXISTS = FRIEND_BASE + "getusername?";
    /**
     * move friends or group members to black list, include operate group and contact
     */
    String FRIEND_MOVE_TO_BLACK_LIST = FRIEND_BASE+"addbalck?";
    /**
     * 朋友模块我的所有的群的列表(我是群主的和我加入的群)
     */
    String FRIEND_GROUP_LIST = FRIEND_BASE + "grouplist?";

    /**
     * 一个群组中的成员列表
     */
    String FRIEND_GROUP_MEMBERS = FRIEND_BASE + "allmembers?";

    /**
     * create a group
     */
    String FRIEND_GROUP_CREATE = FRIEND_BASE + "addgroup?";
    /**
     * add member to a group
     */
    String FRIEND_GROUP_ADD_MEMBERS = FRIEND_BASE + "addmembers?";
    /**
     * quit a group
     */
    String FRIEND_GROUP_QUIT = FRIEND_BASE + "quitgroup?";
    /**
     * delete one group
     */
    String FRIEND_GROUP_DELETE = FRIEND_BASE + "delgroup?";

    //=============================================================

    String GetLifeInfoUrl = "life/index?";
    String GetTreeHoleList = "treehole/index?";
    String PubTreeHole = "treehole/addtreehole?";
    //兼职列表和兼职详情
    String GetPartTime = "parttime/index?";
    String GetPartTimeDetail = "parttime/partdetail?";
    //校园活动列表
    String GetSchoolActList = "life/list/index?";
    String GetSchoolActDetail = "life/actdetail/index?";
    //淘渔列表和淘渔详情
    String GetTaoyuList = "schooltao/index?";
    String GetTaoyuDetail = "schooltao/goodsdetail?";
    String PubTaoyu = "schooltao/addtaoyu?";
    String TaoyuCategory = "schooltao/taoyucategory?";
    //话题界面列表详情  topic_id
    String GetTalkList = "topic/topiclist?";
    String GetTalkDetail = "topic/topicdetail?";
    //上传图片
    String Upload = "topic/upload?";

    String TreeMylist = "treehole/mylist?";
    String TreeComList = "treehole/mycomlist?";
    String TreeDetail = "treehole/treeholedetail?";

    //获取校园信息
    String GetSchoolInfo = "school/info?";
    String GetSchoolSlider = "school/slider?";
    String GetSchoolNewsList = "schoolnews/schoolnews?";
    String GetSchoolNewsDetail = "schoolnews/newsdetail?";
    String GetSocietyNewsList = "schoolnews/socialnews?";
    String GetSchoolMajorList = "school/schoolmajor?";
    //首页小图新闻
    String GetSchoolSimgnewsList = "school/simgnewslist?";

    //校园概况
    String SCHOOLINFO1 = BASE + "api/school/info?signature=ad569e516e5d6d1b10e607956fd7059f";
    //历史前沿
    String SCHOOLINFO2 = BASE + "api/school/schoolhistory?signature=ad569e516e5d6d1b10e607956fd7059f";
    //现任领导
    String SCHOOLINFO3 = BASE + "api/school/schoolleader?signature=ad569e516e5d6d1b10e607956fd7059f";



    class UserInfo {
        public static String mToken;
        public static String signature;
        public static Map<String, String> mFiledMap = new HashMap<>();

        public static void setFiledMap() {
            mFiledMap.put(HttpKey.SIGNITURE, signature);
            mFiledMap.put(HttpKey.TOKEN, mToken);
        }
    }

}
