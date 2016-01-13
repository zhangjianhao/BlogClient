import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

/**
 * Created by 张建浩 on 2015/4/19.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
//        initImageLoader(this);
    }

    /** 初始化ImageLoader */
    public static void initImageLoader(Context context) {
        File cacheDir = StorageUtils.getOwnCacheDirectory(context, "blogclient/Cache");//获取到缓存的目录地址
        Log.d("cacheDir", cacheDir.getPath());
        ImageLoaderConfiguration config = new ImageLoaderConfiguration
    .Builder(context)
    .memoryCacheExtraOptions(480, 800) // max width, max height，即保存的每个缓存文件的最大长宽
    .threadPoolSize(3)//线程池内加载的数量
    .threadPriority(Thread.NORM_PRIORITY - 2)
    .denyCacheImageMultipleSizesInMemory()
    .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // You can pass your own memory cache implementation/你可以通过自己的内存缓存实现
    .memoryCacheSize(2 * 1024 * 1024)
    .discCacheSize(50 * 1024 * 1024)
    .discCacheFileNameGenerator(new Md5FileNameGenerator())//将保存的时候的URI名称用MD5 加密
    .tasksProcessingOrder(QueueProcessingType.LIFO)
    .discCacheFileCount(100) //缓存的文件数量
    .discCache(new UnlimitedDiscCache(cacheDir))//自定义缓存路径
    .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
    .imageDownloader(new BaseImageDownloader(context, 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间
    .build();//开始构建
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);//全局初始化此配置
    }
}
