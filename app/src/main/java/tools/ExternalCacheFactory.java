package tools;

import android.content.Context;
import android.util.Log;

import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;

import java.io.File;

/**
 * Created by admin on 2016/3/29.
 */
public class ExternalCacheFactory extends DiskLruCacheFactory {



    public ExternalCacheFactory(Context context) {
        this(context, DiskCache.Factory.DEFAULT_DISK_CACHE_DIR, DiskCache.Factory.DEFAULT_DISK_CACHE_SIZE);
    }

    public ExternalCacheFactory(Context context, int diskCacheSize) {
        this(context, DiskCache.Factory.DEFAULT_DISK_CACHE_DIR, diskCacheSize);
    }

    public ExternalCacheFactory(final Context context, final String diskCacheName, int diskCacheSize) {
        super(new CacheDirectoryGetter() {
            @Override
            public File getCacheDirectory() {
                File cacheDirectory;
                FileUtils fileUtils = new FileUtils("stepic");
                String filePath = fileUtils.getFilePath();
                cacheDirectory = new File(filePath);

               /* if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    // 如果SD卡可以用的话把图片缓存到SD卡上
                    cacheDirectory = context.getExternalCacheDir();
                } else {
                    // 把图片缓存到应用data/data/包/...下
                    cacheDirectory = context.getCacheDir();
                }*/
                if (cacheDirectory == null) {
                    Log.e("cacheDir","is null");
                    return null;
                }
                if (diskCacheName != null) {
                    return new File(cacheDirectory, diskCacheName);
                }
                return cacheDirectory;
            }

        }, diskCacheSize);
    }
}
