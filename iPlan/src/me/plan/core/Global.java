package me.plan.core;

import android.content.Context;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.othermedia.asyncimage.AsyncImageCache;
import com.othermedia.asyncimage.AsyncImageRegistry;

import java.util.Random;

/**
 * Created by tangb4c on 2015/1/25.
 */
public class Global {
    static Context ctx;
    public final static Random random = new Random();
    public final static ObjectMapper objectMapper = new ObjectMapper();
    static AsyncImageCache defaultCache;
    static void init(Context c) {
        ctx = c;
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        AsyncImageRegistry.initialize(ctx);
        defaultCache = AsyncImageRegistry.getInstance().getDefaultCache();
    }

    static public Context getContext() {
        return ctx;
    }
    static public AsyncImageCache imageCache(){
        return defaultCache;
    }
    public static float getDensity() {
        return ctx.getResources().getDisplayMetrics().density;
    }
}
