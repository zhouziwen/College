package com.zhiyou.colleageapp.constants;

/**
 * Author ： LongWeiHu on 2016/5/18.
 */
public interface EaseConstant {


    String ACTION_TOKEN_EXPIRED = ConfigConstants.PACKAGE_NAME + ".token_expire";

    String NEW_FRIENDS_USERNAME = "item_new_friends";
    String GROUP_USERNAME = "item_groups";
    String CHAT_ROOM = "item_chatroom";
    String ACCOUNT_REMOVED = "account_removed";
    String ACCOUNT_CONFLICT = "conflict";
    String CHAT_ROBOT = "item_robots";
    String MESSAGE_ATTR_ROBOT_MSGTYPE = "msgtype";
    String ACTION_GROUP_CHANAGED = "action_group_changed";
    String ACTION_CONTACT_CHANAGED = "action_contact_changed";


    String MESSAGE_ATTR_IS_VOICE_CALL = "is_voice_call";
    String MESSAGE_ATTR_IS_VIDEO_CALL = "is_video_call";

    String MESSAGE_ATTR_IS_BIG_EXPRESSION = "em_is_big_expression";
    String MESSAGE_ATTR_EXPRESSION_ID = "em_expression_id";
    int CHATTYPE_SINGLE = 1;
    int CHATTYPE_GROUP = 2;
    int CHATTYPE_CHATROOM = 3;

    String EXTRA_CHAT_TYPE = "mChatType";
    String CHAT_ENTITY_ID = "chatEntityId";

    String FRAGMENT_TAG = "FRAGMENT_TAG";
    String GROUP_ID = "groupId";
    String GROUP = "group";
    int RESOURCE_EMPTY = -1;
    String TITLE = "TITLE";
    String DATA = "DATA";
    String IS_CONFLICT = "isConflict";

    int REQUEST_CODE_REGISTER = 0;
    int REQUEST_CODE_FORGET_PWD = 1;
    String FORWARD_MSG_ID = "forward_msg_id";
    /**
     * 修改个人信息中,修改的哪一个
     */
    String EDIT_WHICH = "which";
    /**
     * the old content of a string
     */
    String EDIT_OLD_CONTENT = "EDIT_OLD_CONTENT";
}
