package com.hwz.zhbj.view.menudetail;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hwz.zhbj.R;
import com.hwz.zhbj.domain.PhotosBean;
import com.hwz.zhbj.global.Constants;
import com.hwz.zhbj.util.CacheUtils;
import com.hwz.zhbj.util.MyBitmapUtils;
import com.hwz.zhbj.util.URLUtils;
import com.hwz.zhbj.view.BaseMenuDetailPager;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * Created by huwang on 2017/6/7.
 * 组图详情页
 */

public class PhotosMenuDetailPager extends BaseMenuDetailPager {
    @ViewInject(R.id.lv_photo)
    private ListView mListView;
    @ViewInject(R.id.gv_photo)
    private GridView mGridView;
    private ImageButton mBtnShow;
    private ArrayList<PhotosBean.PhotosNews> mNewsList;

    private boolean isListView = true;  // 是否为LIstView展示

    public PhotosMenuDetailPager(Activity activity, ImageButton btnShow) {
        super(activity);
        mBtnShow = btnShow;
        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // 组图切换
                // 切换
                if (isListView) {
                    // GridView
                    mGridView.setVisibility(View.VISIBLE);
                    mListView.setVisibility(View.GONE);
                    mBtnShow.setImageResource(R.mipmap.icon_pic_list_type);
                    isListView = false;
                } else {
                    mGridView.setVisibility(View.GONE);
                    mListView.setVisibility(View.VISIBLE);
                    mBtnShow.setImageResource(R.mipmap.icon_pic_grid_type);
                    isListView = true;
                }
            }
        });
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.pager_photos_menu_detail, null);
        ViewUtils.inject(this, view);
        return view;
    }

    @Override
    public void initData() {
        String cache = CacheUtils.getCache(mActivity, Constants.PHOTOS_URL);
        if (!TextUtils.isEmpty(cache)) {
            processData(cache);
        }
        getDataFromServer();
    }

    private void getDataFromServer() {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, Constants.PHOTOS_URL, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String json = responseInfo.result;
                System.out.println("服务器返回结果" + json);
                processData(json);

                CacheUtils.setCache(mActivity, Constants.PHOTOS_URL, json);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                // 请求失败
                error.printStackTrace();
                Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void processData(String json) {
        Gson gson = new Gson();
        PhotosBean photosBean = gson.fromJson(json, PhotosBean.class);
        mNewsList = photosBean.data.news;

        mListView.setAdapter(new PhotosAdapter());
        mGridView.setAdapter(new PhotosAdapter());
    }

    class PhotosAdapter extends BaseAdapter {
        //        private BitmapUtils mBitmapUtils;
        private MyBitmapUtils mBitmapUtils;

        public PhotosAdapter() {
//            mBitmapUtils = new BitmapUtils(mActivity);
//            mBitmapUtils.configDefaultLoadingImage(R.mipmap.pic_item_list_default);
            mBitmapUtils = new MyBitmapUtils();
        }

        @Override
        public int getCount() {
            return mNewsList.size();
        }

        @Override
        public PhotosBean.PhotosNews getItem(int position) {
            return mNewsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = View.inflate(mActivity, R.layout.list_item_photo, null);
                holder = new ViewHolder();
                holder.ivPic = (ImageView) convertView.findViewById(R.id.iv_pic);
                holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            PhotosBean.PhotosNews photosNews = getItem(position);
            holder.tvTitle.setText(photosNews.title);

            mBitmapUtils.display(holder.ivPic, URLUtils.replaceUrl(photosNews.listimage));

            return convertView;
        }
    }

    static class ViewHolder {
        public ImageView ivPic;
        public TextView tvTitle;
    }
}
