package me.plan.core;

import android.app.Application;

/**
 * Created by tangb4c on 2015/1/25.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {
        Global.ctx = getApplicationContext();
    }
}
