package com.hwz.zhbj.util;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import java.lang.ref.SoftReference;

/**
 * Created by huwang on 2017/6/10.
 * 防止内存溢出
 * android设备会给每个app分配一定内存大小， 一般为16M
 * 超出16M内存会溢出的
 *
 *
 * 引用类型：
 * 默认为强引用，垃圾回收器不回收
 * 软引用：垃圾回收器考虑回收SoftReference
 * 弱引用：比软引用回收几率更大WeakReference
 * 虚引用：垃圾回收器回收优先级更大PhantomReference
 *
 * android 2.3开始，垃圾回收器更倾向于回收软引用和弱引用。
 * 这使得他们不在可靠
 *
 * android3.0 开始，图片数据会存储在本地内存中，无法用一种可预见的方式释放。
 *
 * 核心类为LruCache非常适合缓存图片. 最近最少使用算法
 * 可以将最近最少使用的对象回收掉，保证内存不会超出预设值。
 * 主要原理是：把最近使用的对象用强引用存储在LinkedHashMap中，
 * 并且把最近最少使用的对象在缓存值达到预设值时候，移除内存。
 */

public class RamCacheUtils {
    private LruCache<String, Bitmap> mRamCache;

    public RamCacheUtils() {
        long maxMemory = Runtime.getRuntime().maxMemory();
        System.out.println("maxMemory: " + maxMemory);
        mRamCache = new LruCache<String, Bitmap>((int) (maxMemory/8)) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };
    }
//    private HashMap<String, SoftReference<Bitmap>> mCache = new HashMap<String, SoftReference<Bitmap>>();
    public void setRamCache(String url, Bitmap bitmap) {
        SoftReference<Bitmap> soft = new SoftReference<Bitmap>(bitmap);
//        mCache.put(url, soft);
        mRamCache.put(url, bitmap);
    }

    public Bitmap getRamCache(String url) {
        return mRamCache.get(url);
//        SoftReference<Bitmap> reference = mCache.get(url);
//        if (reference != null) {
//            return reference.get();
//        }
//        return null;
    }
}
