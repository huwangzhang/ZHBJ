package com.hwz.zhbj.view.menudetail;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.hwz.zhbj.view.BaseMenuDetailPager;

/**
 * Created by huwang on 2017/6/7.
 * 互动详情页
 */

public class InteractMenuDetailPager extends BaseMenuDetailPager {
    public InteractMenuDetailPager(Activity activity) {
        super(activity);
    }

    @Override
    public View initView() {
        TextView view = new TextView(mActivity);
        view.setText("互动详情页");
        view.setGravity(Gravity.CENTER);
        view.setTextColor(Color.RED);
        return view;
    }
}
