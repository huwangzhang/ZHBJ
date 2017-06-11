package com.hwz.zhbj.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by huwang on 2017/6/10.
 * 网络缓存
 */

public class NetCacheUtils {
    private LocalCacheUtils mLocalCacheUtils;
    private RamCacheUtils mRamCacheUtils;
    public NetCacheUtils(LocalCacheUtils localCacheUtils, RamCacheUtils ramCacheUtils) {
        mLocalCacheUtils = localCacheUtils;
        mRamCacheUtils = ramCacheUtils;
    }

    public void getBitmapFromNet(ImageView imageView, String imageUrl) {
        // AsyncTask异步工具，可以实现异步请求，主界面更新
        // 线程池和handler的封装
        new BitmapTask().execute(imageView, imageUrl);
    }


    /**
     * 泛型1:doInBackground的参数类型，输入参数
     * 泛型2:onProgressUpdate的参数类型, 更新进度
     * 泛型3:onPostExecute的参数类型,doInBackground的返回类型
     */
    class BitmapTask extends AsyncTask<Object, Integer, Bitmap> {

        private ImageView mImageView;
        private String mUrl;

        // 预加载， 运行在主线程中
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            System.out.println("onPreExecute");
        }

        // 运行在子线程
        @Override
        protected Bitmap doInBackground(Object... params) {
            System.out.println("doInBackground");
            mImageView = (ImageView) params[0];
            mUrl = (String) params[1];

            //noinspection WrongThread
            mImageView.setTag(mUrl);// 当前imageview和url绑定在一起

            // 开始下载
            Bitmap bitmap = download(mUrl);

//            publishProgress(values);   // 实现进度更新

            return bitmap;
        }

        // 加载完成，运行在主线程, 可以更新UI
        @Override
        protected void onPostExecute(Bitmap result) {  // 参数为doInBackground返回的结果
            if (result != null) {
                // listView重用机制，可能将错误的图片设置给imageview对象
                // 判断是否正确
                if (mImageView.getTag().equals(mUrl)) {
                    mImageView.setImageBitmap(result);   // 设置图片

                    // 本地缓存
                    mLocalCacheUtils.setLocalCache(mUrl, result);
                    // 内存缓存
                    mRamCacheUtils.setRamCache(mUrl, result);
                }
            }
            System.out.println("onPostExecute");
            super.onPostExecute(result);
        }

        // 进度更新，运行在主线程
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            System.out.println("onProgressUpdate");
        }
    }

    private Bitmap download(String url) {
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);  // 读取超时
            conn.connect();

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                InputStream inputStream = conn.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return null;
    }

}
