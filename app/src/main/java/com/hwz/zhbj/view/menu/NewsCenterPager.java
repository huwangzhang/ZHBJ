package com.hwz.zhbj.view.menu;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hwz.zhbj.MainActivity;
import com.hwz.zhbj.domain.NewsBean;
import com.hwz.zhbj.fragment.LeftMenuFragment;
import com.hwz.zhbj.global.Constants;
import com.hwz.zhbj.util.CacheUtils;
import com.hwz.zhbj.view.BaseMenuDetailPager;
import com.hwz.zhbj.view.BasePager;
import com.hwz.zhbj.view.menudetail.InteractMenuDetailPager;
import com.hwz.zhbj.view.menudetail.NewsMenuDetailPager;
import com.hwz.zhbj.view.menudetail.PhotosMenuDetailPager;
import com.hwz.zhbj.view.menudetail.TopicMenuDetailPager;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.ArrayList;

/**
 * Created by huwang on 2017/6/6.
 */

public class NewsCenterPager extends BasePager {
    private ArrayList<BaseMenuDetailPager> mBaseMenuDetailPagers;
    private NewsBean mNewsBean; // 包含分类的所有信息

    public NewsCenterPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        Log.i("zhang", "新闻中心加载中...");
        super.initData();

        mTvTitle.setText("新闻中心");
        mBtnMenu.setVisibility(View.VISIBLE);


        // 判断有没有缓存
        String cache = CacheUtils.getCache(mActivity, Constants.CATEGORY_URL);
        if (!TextUtils.isEmpty(cache)) {
            // 有缓存，直接去本地获取数据
            System.out.println("本地返回结果: " + cache);
            processData(cache);
        }
        // 请求服务器获取数据
        getDataFromServer();
    }

    /**
     * 从服务器获取数据
     */
    private void getDataFromServer() {
        HttpUtils utils = new HttpUtils();
        utils.send(HttpRequest.HttpMethod.GET, Constants.CATEGORY_URL, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                // 请求成功
                String result = responseInfo.result;
                System.out.println("服务器返回结果" + result);
                processData(result);
                CacheUtils.setCache(mActivity, Constants.CATEGORY_URL, result);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                // 请求失败
                error.printStackTrace();
                Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 利用Gson解析数据
     */
    protected void processData(String result) {
        Gson gson = new Gson();
        mNewsBean = gson.fromJson(result, NewsBean.class);
        System.out.println("解析结果: " + mNewsBean);

        MainActivity mainUi = (MainActivity) mActivity;
        LeftMenuFragment fragment = mainUi.getLeftMenuFragment();
        fragment.setMenuData(mNewsBean.data);

        // 初始化菜单详情页
        mBaseMenuDetailPagers = new ArrayList<BaseMenuDetailPager>();
        mBaseMenuDetailPagers.add(new NewsMenuDetailPager(mActivity, mNewsBean.data.get(0).children));
        mBaseMenuDetailPagers.add(new TopicMenuDetailPager(mActivity));
        mBaseMenuDetailPagers.add(new PhotosMenuDetailPager(mActivity, mBtnShow));
        mBaseMenuDetailPagers.add(new InteractMenuDetailPager(mActivity));
        setCurrentDetailPager(0); // 默认页面
    }

    /**
     * @param position
     * 设置详情页
     */
    public void setCurrentDetailPager(int position) {
        // 更新frameLayout添加布局
        BaseMenuDetailPager pager = mBaseMenuDetailPagers.get(position);
        mFlContent.removeAllViews();
        mFlContent.addView(pager.mRootView);
        // 初始化数据
        pager.initData();
        // 更新标题
        mTvTitle.setText(mNewsBean.data.get(position).title);

        // 组图页面显示切换
        if (pager instanceof PhotosMenuDetailPager) {
            mBtnShow.setVisibility(View.VISIBLE);
        } else {
            mBtnShow.setVisibility(View.GONE);
        }
    }
}
