# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/wujiaolong/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-optimizationpasses 5
-dontskipnonpubliclibraryclasses
-dontoptimize
-verbose
-dontwarn
-dontskipnonpubliclibraryclassmembers
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-useuniqueclassmembernames
-dontpreverify
-flattenpackagehierarchy
-dontusemixedcaseclassnames

#系统的
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.FragmentActivity
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.preference.Preference

#应用的
-keep public class * extends com.zhiyou.colleageapp.appui.BaseFragment
-keep public class * extends com.zhiyou.colleageapp.appui.BaseActivity

#相关反射的代码不混淆
-keep class * implements com.zhiyou.colleageapp.appui.listener.* { *; }

-keepclassmembers class * implements java.io.Serializable {
	static final long serialVersionUID;
	private static final java.io.ObjectStreamField[] serialPersistentFields;
	private void writeObject(java.io.ObjectOutputStream);
	private void readObject(java.io.ObjectInputStream);
	java.lang.Object writeReplace();
	java.lang.Object readResolve();
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

#.so文件
-libraryjars libs/armeabi/libBaiduMapSDK_base_v3_6_1.so
-libraryjars libs/armeabi/libBaiduMapSDK_map_v3_6_1.so
-libraryjars libs/armeabi/libBaiduMapSDK_util_v3_6_1.so
-libraryjars libs/armeabi/libhyphenate.so
-libraryjars libs/armeabi/libhyphenate_av.so
-libraryjars libs/armeabi/liblocSDK6a.so

-libraryjars libs/armeabi-v7a/libBaiduMapSDK_base_v3_6_1.so
-libraryjars libs/armeabi-v7a/libBaiduMapSDK_map_v3_6_1.so
-libraryjars libs/armeabi-v7a/libBaiduMapSDK_util_v3_6_1.so
-libraryjars libs/armeabi-v7a/libhyphenate.so
-libraryjars libs/armeabi-v7a/libhyphenate_av.so
-libraryjars libs/armeabi-v7a/liblocSDK6a.so

-libraryjars libs/arm64-v8a/libBaiduMapSDK_base_v3_6_1.so
-libraryjars libs/arm64-v8a/libBaiduMapSDK_map_v3_6_1.so
-libraryjars libs/arm64-v8a/libBaiduMapSDK_util_v3_6_1.so
-libraryjars libs/arm64-v8a/libhyphenate.so
-libraryjars libs/arm64-v8a/libhyphenate_av.so
-libraryjars libs/arm64-v8a/liblocSDK6a.so

-libraryjars libs/x86/liblocSDK6a.so
-libraryjars libs/x86/libhyphenate_av.so

#eventBus 回调不可混淆
-keepclassmembers,includedescriptorclasses class ** { public void register*(**); }

#R文件
-keep public class com.zhiyou.colleageapp.R$*{
    public static final int *;
}

#系统的json
-keepclassmembers class * {
   public <init>(org.json.JSONObject);
}

#jni
-keepclasseswithmembernames class * {
    native <methods>;
}
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

# 保持自定义控件类不被混淆
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context);
}
-keepattributes *Annotation*
-keepattributes *JavascriptInterface*

#@JavascriptInterface接口不混淆
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

#Retrofit
-dontwarn retrofit2.**
-dontwarn retrofit2.converter.gson.**
-dontwarn okhttp3.**
-dontwarn okhttp3.logging.**
-dontwarn okio.**
-dontwarn retrofit2.adapter.rxjava.**

-keep class retrofit2.** { *; }
-keep class retrofit2.adapter.rxjava.** {*;}
-keep class retrofit2.converter.gson.**{*;}
-keep class okhttp3.logging.**{*;}
-keep class okio.**{*;}
-keepattributes Signature
-keepattributes Exceptions

#RxJava and RxAndroid
-dontwarn sun.misc.**
-dontwarn rx.android.**
-dontwarn rx.**
-keep class rx.android.**{*;}
-keep class rx.**{*;}

-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
   long producerIndex;
   long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}

#Gson
-keepattributes Signature-keepattributes *Annotation*
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.** { *; }

#Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

-keepclassmembers class * {
  public <init> (org.json.JSONObject);
}

#第三方jar包
-dontwarn com.handmark.pulltorefresh.library.**
-dontwarn android.support.**
-dontwarn me.dm7.barcodescanner.core.**
-dontwarn me.dm7.barcodescanner.zxing**
-dontwarn com.google.zxing.**
-dontwarn freemarker.**
-dontwarn com.bumptech.glede.**
-dontwarn de.greenrobot.dao.**
-dontwarn de.greenrobot.daogenerator.**
-dontwarn org.hamcrest.**
-dontwarn junit.**
-dontwarn org.junit.**
-dontwarn com.nineoldandroids.**
-dontwarn cm.smmssdk.**
-dontwarn com.baidu.**
-dontwarn class com.hyphenate.*
-dontwarn class internal.org.apache.http.entity.mime.**
-dontwarn class com.mob.commons.**
-dontwarn class com.mob.tools.**
-dontwarn class com.parse.**
#=============================
-keep class com.handmark.pulltorefresh.library.**{*;}
-keep class android.support.**{*;}
-keep class me.dm7.barcodescanner.**{*;}
-keep class com.google.zxing.**{*;}
-keep class freemarker.**{*;}
-keep class com.bumptech.glde.**{*;}
-keep class de.greenrobot.**{*;}
-keep class org.hamcrest.**{*;}
-keep class junit.**{*;}
-keep class org.junit.**{*;}
-keep class com.nineoldandroids.**{*;}
-keep class cm.smmssdk.**{*;}
-keep class com.baidu.**{*;}
-keep class com.hyphenate.*{*;}
-keep class internal.org.apache.http.entity.mime.**{*;}
-keep class com.mob.**{*;}
-keep class com.parse.**{*;}




