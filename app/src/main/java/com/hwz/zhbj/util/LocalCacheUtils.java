package com.hwz.zhbj.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.hwz.zhbj.global.Constants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by huwang on 2017/6/10.
 */

public class LocalCacheUtils {
    public void setLocalCache(String url, Bitmap bitmap) {
        File dir = new File(Constants.LOCAL_CACHE_PATH);
        if (!dir.exists() || !dir.isDirectory()) {
            dir.mkdirs();
        }

        try {
            String filename = MD5Encoder.encode(url);

            File cacheFile = new File(dir, filename);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(cacheFile)); // 直接压缩之本地
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Bitmap getLocalCache(String url) {
        try {
            File cacheFile = new File(Constants.LOCAL_CACHE_PATH, MD5Encoder.encode(url));
            if (cacheFile.exists()) {
                Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(cacheFile));
                return bitmap;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
