package com.zhiyou.colleageapp.utils;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;

import com.zhiyou.colleageapp.appui.BaseActivity;

import java.io.File;
import java.util.UUID;

/**
 *Create By LongWeiHu on 2016.6.20
 */
public final class CameraHandler {

    //拍照对应的request code
    public static final int REQUEST_CODE_CAMERA = 0x21; //拍照
    public static final int REQUEST_CODE_CAMERA_VIDEO = 0x22; //视频录制
    /**
     * 选择
     */
    public static final int REQUEST_CODE_PICK = 0x23;
    public static final int REQUEST_CODE_CUTTING = 0x24;
    public static final int CATEGORY = 0x25;
    public static String mCameraPicPath = "";//默认生成路径，可以直接修改
    public static String mPickPicPath = "";//默认生成路径，可以直接修改
    private final int SCALE = 5;//照片缩小比例


    private static Intent defaultIntent(){
        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mCameraPicPath = CommonPath.getDirUserTemp() + UUID.randomUUID().toString() + ".jpg";
        captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(mCameraPicPath)));
        captureIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);

        return captureIntent;
    }

    private static Intent defaultVideoIntent(){
        Intent captureIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        mCameraPicPath = CommonPath.getDirUserTemp() + UUID.randomUUID().toString() + ".mp4";
        captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(mCameraPicPath)));
        captureIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        captureIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 15);

        return captureIntent;
    }

    public static void startCameraActivityForResult(Fragment fragment){
        startCameraActivityForResult(fragment, defaultIntent());

    }

    public static void startCameraActivityForResult(BaseActivity activity, String fragmentTag){
        Fragment fragment = activity.getFragmentByTag(fragmentTag);
        startCameraActivityForResult(fragment,defaultIntent());
    }

    /**
     * @param fragment :
     * @param intent 如果不是默认intent，需要对应修改mCameraPicPath
     */
    public static void startCameraActivityForResult(Fragment fragment, Intent intent){
        if (fragment != null && intent != null){
            switch (intent.getAction()) {
                case MediaStore.ACTION_IMAGE_CAPTURE:
                    fragment.startActivityForResult(intent,REQUEST_CODE_CAMERA);
                break;
                case MediaStore.ACTION_VIDEO_CAPTURE:
                    fragment.startActivityForResult(intent,REQUEST_CODE_CAMERA_VIDEO);

                break;
            }
        }
    }

    public static void startCameraActivityForResult(BaseActivity activity, String fragmentTag, Intent intent) {
        Fragment fragment = activity.getFragmentByTag(fragmentTag);
        startCameraActivityForResult(fragment,intent);
    }

    public static void startVideoActivityForResult(Fragment fragment){
        startCameraActivityForResult(fragment, defaultVideoIntent());
    }


    public static void startPickPicActivityForResult(Fragment fragment) {

        Intent pickIntent = new Intent(Intent.ACTION_PICK, null);
        pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        fragment.startActivityForResult(pickIntent,REQUEST_CODE_PICK);
    }

    public static void startPhotoZoom(Uri uri,Fragment fragment) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", true);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        intent.putExtra("noFaceDetection", true);
        fragment.startActivityForResult(intent, REQUEST_CODE_CUTTING);
    }
}
