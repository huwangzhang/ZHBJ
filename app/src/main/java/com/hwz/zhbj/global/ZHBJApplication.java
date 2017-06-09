package com.hwz.zhbj.global;

import android.app.Application;
import android.content.Context;

/**
 * Created by huwang on 2017/6/6.
 */

public class ZHBJApplication extends Application {
    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
    }

    public static Context getContext() {
        return sContext;
    }
}
