package com.hwz.zhbj;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.WindowManager;

import com.hwz.zhbj.fragment.ContentFragment;
import com.hwz.zhbj.fragment.LeftMenuFragment;
import com.hwz.zhbj.global.ZHBJApplication;

import me.tangke.slidemenu.SlideMenu;

/**
 * Created by huwang on 2017/6/6.
 */

public class MainActivity extends FragmentActivity {
    private static final String FRAG_CONTENT = "frag_content";
    private static final String FRAG_MENU_LEFT = "frag_menu_left";
    private SlideMenu mSlideMenu;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSlideMenu = new SlideMenu(this);
        setContentView(mSlideMenu);
        initView(mSlideMenu);
        initFragment();
    }

    private void initFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //  FragmentTransaction replace(int containerViewId, Fragment fragment,String tag);
        transaction.replace(R.id.fl_main, new ContentFragment(), FRAG_CONTENT);
        transaction.replace(R.id.fl_left_menu, new LeftMenuFragment(), FRAG_MENU_LEFT);
        transaction.commit();
    }

    private void initView(SlideMenu slideMenu) {
        // Setup the content
        View contentView = View.inflate(ZHBJApplication.getContext(), R.layout.activity_main, null);
        slideMenu.addView(contentView, new SlideMenu.LayoutParams(
                SlideMenu.LayoutParams.MATCH_PARENT, SlideMenu.LayoutParams.MATCH_PARENT,
                SlideMenu.LayoutParams.ROLE_CONTENT));

        // Setup the primary menu
//        View primaryMenu = new View(this);
        View primaryMenu = View.inflate(ZHBJApplication.getContext(), R.layout.left_menu, null);
        WindowManager manager = getWindowManager();
        int width = manager.getDefaultDisplay().getWidth();
        slideMenu.addView(primaryMenu, new SlideMenu.LayoutParams(width * 300 / 768,
                SlideMenu.LayoutParams.MATCH_PARENT, SlideMenu.LayoutParams.ROLE_PRIMARY_MENU));

        slideMenu.setPrimaryShadowWidth(30); // 滑动阴影
//        slideMenu.setInterpolator(new LinearInterpolator());

    }

    public ContentFragment getContentFragment() {
        return (ContentFragment) getSupportFragmentManager().findFragmentByTag(FRAG_CONTENT);
    }

    public LeftMenuFragment getLeftMenuFragment() {
        return (LeftMenuFragment) getSupportFragmentManager().findFragmentByTag(FRAG_MENU_LEFT);
    }

    public SlideMenu getSlideMenu() {
        return mSlideMenu;
    }
}
