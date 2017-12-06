package com.zhiyou.colleageapp.utils;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zhiyou.colleageapp.Helper;
import com.zhiyou.colleageapp.R;
import com.zhiyou.colleageapp.appui.BaseFragment;
import com.zhiyou.colleageapp.domain.User;

/**
 * Author ： LongWeiHu on 2016/5/18.
 */
public class EaseUserUtils {
    /**
     * 设置用户头像
     *
     * @param username
     */
    public static void setUserAvatar(Context context, String username, ImageView imageView) {
        User user = getUserByName(username);
        if (user != null && user.getAvatar() != null) {
            setAvatar(context,user.getAvatar(),imageView,R.drawable.user_default_avatar);
        } else {
            Glide.with(context).load(R.drawable.user_default_avatar).into(imageView);
        }
    }

    /**
     * 设置用户头像
     *
     * @param context    :
     * @param avatarUrl  : avatar
     * @param imageView  :
     * @param defaultImg :
     */
    public static void setAvatar(Context context, String avatarUrl, ImageView imageView, int defaultImg) {
        Glide.with(context).load(avatarUrl).diskCacheStrategy(DiskCacheStrategy.RESULT).placeholder(defaultImg)
                .centerCrop().into(imageView);
    }

    public static void setAvatar(BaseFragment context, String avatarUrl, ImageView imageView, int defaultImg) {
        Glide.with(context).load(avatarUrl).placeholder(defaultImg).centerCrop().into(imageView);

    }

    /**
     * 设置用户昵称
     */
    public static void setUserNick(String username, TextView textView) {
        if (textView != null) {
            User user = getUserByName(username);
            if (user != null && user.getNick() != null) {
                textView.setText(user.getNick());
            } else {
                textView.setText(username);
            }
        }
    }

    public static User getUserByName(String username) {
        return Helper.getInstance().getUser(username);
    }

}
