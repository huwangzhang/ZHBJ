package com.hwz.zhbj;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;

import com.hwz.zhbj.global.ZHBJApplication;
import com.hwz.zhbj.util.PrefUtils;

import static android.view.animation.Animation.RELATIVE_TO_SELF;

public class SplashAvtivity extends AppCompatActivity {

    private FrameLayout mFlSplash;
    private boolean mIsFirstEnter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);// 继承AppCompatActivity,这句话失效。可使用下面操作去掉
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_splash);
        initView();
        initData();
    }

    private void initView() {
        mFlSplash = (FrameLayout) findViewById(R.id.fl_splash);
    }

    private void initData() {
        startAnimForSplash();
    }

    private void startAnimForSplash() {
        mIsFirstEnter = PrefUtils.getBoolean(ZHBJApplication.getContext(), "is_first_enter");

        RotateAnimation rotateAnimation = new RotateAnimation(
                0, 360, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(1000);
        rotateAnimation.setFillAfter(true);
        ScaleAnimation scaleAnimation = new ScaleAnimation(
                0, 1, 0, 1, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(1000);
        scaleAnimation.setFillAfter(true);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(2000);
        alphaAnimation.setFillAfter(true);

        AnimationSet set = new AnimationSet(true);
        set.addAnimation(rotateAnimation);
        set.addAnimation(scaleAnimation);
        set.addAnimation(alphaAnimation);
        mFlSplash.startAnimation(set);

        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent intent = null;
                if (mIsFirstEnter) {
                    intent = new Intent(SplashAvtivity.this, GuideActivity.class);
                } else {
                    intent = new Intent(SplashAvtivity.this, MainActivity.class);
                }
                startActivity(intent);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

}
