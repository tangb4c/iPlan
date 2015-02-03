package me.plan.core;

import android.content.Context;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Random;

/**
 * Created by tangb4c on 2015/1/25.
 */
public class Global {
    static Context ctx;
    public final static Random random = new Random();
    public final static ObjectMapper objectMapper = new ObjectMapper();
    static void init(Context c) {
        ctx = c;

    }

    static public Context getContext() {
        return ctx;
    }

    public static float getDensity() {
        return ctx.getResources().getDisplayMetrics().density;
    }
}
