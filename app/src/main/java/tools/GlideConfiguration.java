package tools;

import android.content.Context;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.module.GlideModule;

/**
 * Created by admin on 2016/3/29.
 */
public class GlideConfiguration implements GlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        ExternalCacheFactory externalCacheDiskCacheFactory = new ExternalCacheFactory(context);
        builder.setDiskCache(externalCacheDiskCacheFactory);

        Log.w("GlideCongfig", Glide.getPhotoCacheDir(context).getPath());
    }

    @Override
    public void registerComponents(Context context, Glide glide) {

    }
}
