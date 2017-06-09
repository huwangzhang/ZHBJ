package com.hwz.zhbj.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by huwang on 2017/6/6.
 */

public class NoScrollViewPager extends ViewPager {
    private boolean isInter;
    public NoScrollViewPager(Context context) {
        super(context);
    }

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

    /**
     * @param isInter true表示拦截，反之
     * 设置ViewPager是否拦截事件
     */
    public void setInter(boolean isInter) {
        this.isInter = isInter;
    }
}
