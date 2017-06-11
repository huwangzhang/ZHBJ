package com.hwz.zhbj.util;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.hwz.zhbj.R;

/**
 * Created by huwang on 2017/6/10.
 * BitmapUtils已经实现了缓存机制
 */

public class MyBitmapUtils {
    private NetCacheUtils mNetCacheUtils;
    private LocalCacheUtils mLocalCacheUtils;
    private RamCacheUtils mRamCacheUtils;

    public MyBitmapUtils() {
        mLocalCacheUtils = new LocalCacheUtils();
        mRamCacheUtils = new RamCacheUtils();
        mNetCacheUtils = new NetCacheUtils(mLocalCacheUtils, mRamCacheUtils);
    }

    public void display(ImageView imageView, String imageUrl) {
        Bitmap bitmap = null;
        // 设置默认图片
        imageView.setImageResource(R.mipmap.pic_item_list_default);
        // 内存数据
        bitmap = mRamCacheUtils.getRamCache(imageUrl);
        if (bitmap != null) {
            System.out.println("从内存加载数据");
            imageView.setImageBitmap(bitmap);
            return ;
        }

        // 本地数据
        bitmap= mLocalCacheUtils.getLocalCache(imageUrl);
        if (bitmap != null) {
            System.out.println("从本地加载数据");
            imageView.setImageBitmap(bitmap);

            // 内存缓存
            mRamCacheUtils.setRamCache(imageUrl, bitmap);
            return;
        }
        System.out.println("从网络加载数据");
        // 网络加载数据
        mNetCacheUtils.getBitmapFromNet(imageView, imageUrl);
    }
}
