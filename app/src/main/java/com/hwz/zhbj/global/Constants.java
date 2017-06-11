package com.hwz.zhbj.global;

import android.os.Environment;

/**
 * Created by huwang on 2017/6/6.
 */

public class Constants {
    public static final String SERVER_URL = "http://10.0.3.2:8080/zhbj";
    public static final String CATEGORY_URL = SERVER_URL + "/categories.json";  // 分类信息
    public static final String PHOTOS_URL = SERVER_URL + "/photos/photos_1.json"; // 组图信息


    public static final String LOCAL_CACHE_PATH =
            Environment.getExternalStorageDirectory().getAbsolutePath() + "/zhbj_cache";
}
