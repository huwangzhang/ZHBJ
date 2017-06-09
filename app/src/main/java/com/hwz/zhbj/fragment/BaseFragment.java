package com.hwz.zhbj.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hwz.zhbj.MainActivity;

import me.tangke.slidemenu.SlideMenu;

/**
 * Created by huwang on 2017/6/6.
 */

public abstract class BaseFragment extends Fragment {
    protected Activity mActivity;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    /**
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return 初始化fragment布局
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return initView();
    }

    /**
     * 初始化相关数据
     */
    public abstract void initData();

    /**
     * 初始化布局
     * @return
     */
    public abstract View initView();

    protected void setSlideMenuEnable(boolean enable) {
        SlideMenu slideMenu = ((MainActivity) mActivity).getSlideMenu();
        int slideDirectionFlag = slideMenu.getSlideDirection();
        if (enable) {
            slideDirectionFlag |= SlideMenu.FLAG_DIRECTION_RIGHT;
        } else {
            slideDirectionFlag &= ~SlideMenu.FLAG_DIRECTION_RIGHT;
        }
        slideMenu.setSlideDirection(slideDirectionFlag);
    }

    /**
     * 打开或者关闭侧边栏
     */
    protected void toggle() {
        SlideMenu slideMenu = ((MainActivity) mActivity).getSlideMenu();
        if (slideMenu.isOpen()) {
            Log.i("zhang", "toggle close");
            slideMenu.close(false);
        } else {
            Log.i("zhang", "toggle open");
            slideMenu.open(false, false);
        }
    }
}
