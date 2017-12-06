package com.zhiyou.colleageapp.httpservice;

import com.zhiyou.colleageapp.httpservice.service.FileService;
import com.zhiyou.colleageapp.httpservice.service.FriendService;
import com.zhiyou.colleageapp.httpservice.service.LifeService;
import com.zhiyou.colleageapp.httpservice.service.SchoolService;
import com.zhiyou.colleageapp.httpservice.service.UserService;

/**
 * Created by chuyh on 2016/5/10.
 */
public interface ApiService {
    /**
     * 学校相关接口Api
     *
     * @return
     */
    SchoolService getSchoolService();

    /**
     * 用户相关接口Api
     *
     * @return
     */
    UserService getUserService();

    /**
     * 生活相关接口Api
     *
     * @return
     */
    LifeService getLifeService();

    /**
     * 朋友相关
     *
     * @return FriendService
     */
    FriendService getFriendService();

    /**
     * 文件相关
     *
     * @return FriendService
     */
    FileService getFileService();
}
