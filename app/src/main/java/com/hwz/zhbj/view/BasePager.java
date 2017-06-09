package com.hwz.zhbj.view;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hwz.zhbj.MainActivity;
import com.hwz.zhbj.R;

import me.tangke.slidemenu.SlideMenu;

/**
 * Created by huwang on 2017/6/6.
 */

public class BasePager {
    public Activity mActivity;
    public TextView mTvTitle;
    public ImageButton mBtnShow;
    public ImageButton mBtnMenu;
    public FrameLayout mFlContent;
    public  View rootView;;

    public BasePager(Activity activity){
        mActivity = activity;
        // 页面文件对象
        rootView = initView();
    }

    /**
     * 初始化布局
     * @return
     */
    public View initView() {
        View view = View.inflate(mActivity, R.layout.base_pager, null);
        mFlContent = (FrameLayout)view.findViewById(R.id.fl_content);
        mTvTitle = (TextView) view.findViewById(R.id.tv_title);
        mBtnMenu = (ImageButton) view.findViewById(R.id.btn_menu);
        mBtnShow= (ImageButton) view.findViewById(R.id.btn_show);

        return view;
    }

    public void initData() {
        mBtnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("zhang", "侧边栏按钮 onclick");
                toggle();
            }
        });
    }

    /**
     * 打开或者关闭侧边栏
     */
    protected void toggle() {
        SlideMenu slideMenu = ((MainActivity) mActivity).getSlideMenu();
        if (slideMenu.isOpen()) {
            slideMenu.close(false);
        } else {
            slideMenu.open(false, false);
        }
    }
}
