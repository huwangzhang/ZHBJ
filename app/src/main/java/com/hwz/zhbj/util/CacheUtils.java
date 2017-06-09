package com.hwz.zhbj.util;

import android.content.Context;

/**
 * Created by huwang on 2017/6/6.
 * 缓存工具
 * 以url为key, json数据为value
 */

public class CacheUtils {
    public static String getCache(Context paramContext, String key)
    {
        return PrefUtils.getString(paramContext, key);
    }

    public static void setCache(Context paramContext, String key, String value)
    {
        PrefUtils.putString(paramContext, key, value);
    }
}
