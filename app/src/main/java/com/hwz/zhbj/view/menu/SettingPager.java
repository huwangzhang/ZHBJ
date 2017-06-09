package com.hwz.zhbj.view.menu;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.hwz.zhbj.view.BasePager;

/**
 * Created by huwang on 2017/6/6.
 */

public class SettingPager extends BasePager {
    public SettingPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        Log.i("zhang", "设置加载中...");
        super.initData();
        TextView view = new TextView(mActivity);
        view.setText("设置");
        view.setGravity(Gravity.CENTER);
        view.setTextColor(Color.RED);
        mFlContent.addView(view);

        mTvTitle.setText("设置");
        mBtnMenu.setVisibility(View.GONE);
    }
}
