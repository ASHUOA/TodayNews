package com.example.fanyishuo.zailai.App;

import android.app.Application;
import android.content.Context;
import android.support.v7.app.AppCompatDelegate;

import com.mob.MobSDK;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import org.xutils.x;

import java.io.File;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by fanyishuo on 2017/8/4.
 */

public class AppApplication extends Application {


    private static AppApplication appApplication;
    {
        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initImageLoader(getApplicationContext());

        appApplication=this;
        //qq登陆
        UMShareAPI.get(this);
        Config.DEBUG = true;

        MobSDK.init(this, "1ff9f9bbc1510", "f06175272b7151b0eca792ecbb0e3be0");

        //极光
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        //xlistview
        x.Ext.init(this);

    }
    //非wife情况下
    public static Context getAppContext() {
        return appApplication;
    }
    public static AppApplication getAppApplication(){
        return appApplication;
    }

    public static void initImageLoader(Context context) {
        //获取到缓存的目录地址
        String s = context.getCacheDir().getPath() + "/imageLoader";
        //创建配置ImageLoader(所有的选项都是可选的,只使用那些你真的想定制)，这个可以设定在APPLACATION里面，设置为全局的配置参数
        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(context)
                .threadPoolSize(3)//线程池内加载的数量
                .denyCacheImageMultipleSizesInMemory()//.memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // You can pass your own memory cache implementation你可以通过自己的内存缓存实现
                .memoryCacheSize(2 * 1024 * 1024)
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())//将保存的时候的URI名称用MD5 加密
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .diskCache(new UnlimitedDiskCache(new File(s)))
                //自定义缓存路径//.imageDownloader(new BaseImageDownloader(context, 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间
                .build();
        ImageLoader.getInstance().init(config);//全局初始化此配置
    }
}
