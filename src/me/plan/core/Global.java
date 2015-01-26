package me.plan.core;

import android.content.Context;

/**
 * Created by tangb4c on 2015/1/25.
 */
public class Global {
    static Context ctx;

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
