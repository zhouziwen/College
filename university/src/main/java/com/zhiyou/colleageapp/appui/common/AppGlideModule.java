package com.zhiyou.colleageapp.appui.common;
import android.content.Context;
import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.module.GlideModule;
import com.zhiyou.colleageapp.utils.CommonPath;
/**
 * Create by LongWeiHu on 2016/6/21.
 * set the custom Memory cache
 */
public class AppGlideModule implements GlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        MemorySizeCalculator calculator = new MemorySizeCalculator(context);
        int defaultMemoryCacheSize = calculator.getMemoryCacheSize();
        int defaultBitmapPoolSize = calculator.getBitmapPoolSize();

        int customMemoryCacheSize = (int) (1.8 * defaultMemoryCacheSize);
        int customBitmapPoolSize = (int) (1.8 * defaultBitmapPoolSize);

        builder.setMemoryCache(new LruResourceCache(customMemoryCacheSize));
        builder.setBitmapPool(new LruBitmapPool(customBitmapPoolSize));

        int diskCacheSize =100*1024*1024;
        DiskLruCacheFactory factory = new DiskLruCacheFactory(CommonPath.getDirImg(), "cache", diskCacheSize);
        builder.setDiskCache(factory);
    }

    @Override
    public void registerComponents(Context context, Glide glide) {

    }
}
