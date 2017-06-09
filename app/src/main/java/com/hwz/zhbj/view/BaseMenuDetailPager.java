package com.hwz.zhbj.view;

import android.app.Activity;
import android.view.View;

/**
 * Created by huwang on 2017/6/7.
 */

public abstract class BaseMenuDetailPager {
    public Activity mActivity;
    public View mRootView;   //菜单详情页跟布局
    public BaseMenuDetailPager(Activity activity) {
        mActivity = activity;
        mRootView = initView();
    }


    /**
     * 初始化布局
     */
    public abstract View initView();

    public void initData() {
    }
}
