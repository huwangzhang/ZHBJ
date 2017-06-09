package com.hwz.zhbj.view.menudetail;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.hwz.zhbj.view.BaseMenuDetailPager;

/**
 * Created by huwang on 2017/6/7.
 * 专题详情页
 */

public class TopicMenuDetailPager extends BaseMenuDetailPager {
    public TopicMenuDetailPager(Activity activity) {
        super(activity);
    }

    @Override
    public View initView() {
        TextView view = new TextView(mActivity);
        view.setText("专题详情页");
        view.setGravity(Gravity.CENTER);
        view.setTextColor(Color.RED);
        return view;
    }
}
