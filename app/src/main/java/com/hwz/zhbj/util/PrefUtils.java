package com.hwz.zhbj.util;

import android.content.Context;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by huwang on 2017/6/6.
 */

public class PrefUtils {
    public static void putString(Context context, String key, String value) {
        context.getSharedPreferences("config", MODE_PRIVATE).edit().putString(key, value).commit();
    }

    public static String getString(Context context, String key) {
        return context.getSharedPreferences("config", MODE_PRIVATE).getString(key, "");
    }

    public static void putInt(Context context, String key, int value) {
        context.getSharedPreferences("config", MODE_PRIVATE).edit().putInt(key, value).commit();
    }

    public static int getInt(Context context, String key) {
        return context.getSharedPreferences("config", MODE_PRIVATE).getInt(key, 0);
    }

    public static void putBoolean(Context context, String key, boolean value) {
        context.getSharedPreferences("config", MODE_PRIVATE).edit().putBoolean(key, value).commit();
    }

    public static boolean getBoolean(Context context, String key) {
        return context.getSharedPreferences("config", MODE_PRIVATE).getBoolean(key, true);
    }
}
