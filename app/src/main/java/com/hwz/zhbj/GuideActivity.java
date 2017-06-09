package com.hwz.zhbj;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hwz.zhbj.global.ZHBJApplication;
import com.hwz.zhbj.util.DensityUtils;
import com.hwz.zhbj.util.PrefUtils;

import java.util.ArrayList;

/**
 * Created by huwang on 2017/6/6.
 */

public class GuideActivity extends Activity {

    private ViewPager mViewPagerGuide;
    private LinearLayout mLLPoint;
    private Button mBtnStart;
    private int mPointWidth;
    private View mRedPoint;

    private ArrayList<ImageView> mIvGuides;
    private int[] resId = new int[]{
            R.mipmap.guide_1,
            R.mipmap.guide_2,
            R.mipmap.guide_3
    };
    private static final String TAG = "GuideActivity";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        initView();
        initData();
    }

    private void initView() {
        mViewPagerGuide = (ViewPager) findViewById(R.id.vp_guide);
        mLLPoint = (LinearLayout) findViewById(R.id.ll_pointset);
        mBtnStart = (Button) findViewById(R.id.btn_start);
        mRedPoint = findViewById(R.id.view_point_red);
    }

    private void initData() {
        mBtnStart.setVisibility(View.INVISIBLE);
        mBtnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrefUtils.putBoolean(ZHBJApplication.getContext(), "is_first_enter", false);
                startActivity(new Intent(GuideActivity.this, MainActivity.class));
                finish();
            }
        });
        mIvGuides = new ArrayList<ImageView>();
        for (int i = 0; i < resId.length; i++) {
            ImageView imageView = new ImageView(ZHBJApplication.getContext());
            imageView.setBackgroundResource(resId[i]);
            mIvGuides.add(imageView);
            ImageView point = new ImageView(ZHBJApplication.getContext());
            point.setImageResource(R.mipmap.dot_normal);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    DensityUtils.dp2px(ZHBJApplication.getContext(), 10),
                    DensityUtils.dp2px(ZHBJApplication.getContext(), 10));
            if (i != 0) {
                params.leftMargin = DensityUtils.dp2px(ZHBJApplication.getContext(), 10);
            }
            point.setLayoutParams(params);
            mLLPoint.addView(point);
        }

        mViewPagerGuide.setAdapter(new GuideAdapter());
        mViewPagerGuide.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                int leftDist = (int) (mPointWidth * (position + positionOffset));
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mRedPoint.getLayoutParams();
                params.leftMargin = leftDist;
                mRedPoint.setLayoutParams(params);
            }

            @Override
            public void onPageSelected(int position) {
                Log.d(TAG, "current position: " + position);
                if (position == resId.length - 1) {
                    mBtnStart.setVisibility(View.VISIBLE);
                } else {
                    mBtnStart.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mLLPoint.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                GuideActivity.this.mLLPoint.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                mPointWidth = mLLPoint.getChildAt(1).getLeft() - mLLPoint.getChildAt(0).getLeft();
                Log.i(TAG, "间距: " + DensityUtils.px2dp(ZHBJApplication.getContext(), mPointWidth));
            }
        });
    }

    class GuideAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return resId.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = mIvGuides.get(position);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
