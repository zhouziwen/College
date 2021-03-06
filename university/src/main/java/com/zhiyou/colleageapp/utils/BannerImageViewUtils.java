package com.zhiyou.colleageapp.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;

import com.zhiyou.colleageapp.R;


/**
 * ImageView创建工厂
 */
public class BannerImageViewUtils {

	/**
	 * 获取ImageView视图的同时加载显示url
	 * 
	 * @return
	 */
	public static ImageView getImageView(Context context, String url) {
		ImageView imageView = (ImageView)LayoutInflater.from(context).inflate(R.layout.view_banner, null);
//		ImageLoader.getInstance().displayImage(url, imageView);
		return imageView;
	}

	public static ImageView getImageView(Context context) {
		return (ImageView)LayoutInflater.from(context).inflate(R.layout.view_banner, null);
	}
}
