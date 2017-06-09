package com.hwz.zhbj.view.menudetail;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.hwz.zhbj.MainActivity;
import com.hwz.zhbj.R;
import com.hwz.zhbj.domain.NewsBean;
import com.hwz.zhbj.view.BaseMenuDetailPager;
import com.hwz.zhbj.view.tab.TabDetailPager;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;

import me.tangke.slidemenu.SlideMenu;

/**
 * Created by huwang on 2017/6/7.
 * 新闻详情页
 */

public class NewsMenuDetailPager extends BaseMenuDetailPager {
    @ViewInject(R.id.vp_news_menu_detail)
    private ViewPager mViewPager;
    @ViewInject(R.id.indicator)
    private TabPageIndicator mIndicator;

    private ArrayList<NewsBean.NewsMenuTab> mTabData;
    private ArrayList<TabDetailPager> mTabDetailPagers;

    public NewsMenuDetailPager(Activity activity, ArrayList<NewsBean.NewsMenuTab> children) {
        super(activity);
        this.mTabData = children;
    }

    @Override
    public View initView() {
//        TextView view = new TextView(mActivity);
//        view.setText("新闻详情页");
//        view.setGravity(Gravity.CENTER);
//        view.setTextColor(Color.RED);
        View view = View.inflate(mActivity, R.layout.pager_news_menu_detail, null);
        ViewUtils.inject(this, view);
        return view;
    }

    @Override
    public void initData() {
        mTabDetailPagers = new ArrayList<TabDetailPager>();
        // 初始化页签
        for (int i = 0; i < mTabData.size(); i++) {
            TabDetailPager tabDetailPager = new TabDetailPager(mActivity, mTabData.get(i));
            mTabDetailPagers.add(tabDetailPager);
        }
        mViewPager.setAdapter(new NewsDetailAdater());

        // 参考demo示例完成绑定
        mIndicator.setViewPager(mViewPager);
        mIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                System.out.println("当前位置: " + position +
                        ", positionOffset" + positionOffset + ", positionOffsetPixels: " + positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                // TODO 当页标签为最左边时候，左滑侧滑栏应该显示（BUG) 待解决
//                if (position == 0) {
//                    setViewPagerInter(true);
//                } else {
//                    setViewPagerInter(false);
//                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                System.out.println("状态改变!");
            }
        });

        mIndicator.setCurrentItem(0);

    }

    /**
     * @param isInter 设置主布局中viewPager是否拦截事件
     *                调用ContentFragment中
     */
    protected void setViewPagerInter(boolean isInter) {
        MainActivity mainUi = (MainActivity) mActivity;
        mainUi.getContentFragment().setViewPagerInter(isInter);
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

    class NewsDetailAdater extends PagerAdapter {

        /**
         * @param position
         * @return 返回指示器的标题
         */
        @Override
        public CharSequence getPageTitle(int position) {
            NewsBean.NewsMenuTab menuTab = mTabData.get(position);
            return menuTab.title;
        }

        @Override
        public int getCount() {
            return mTabDetailPagers.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TabDetailPager pager = mTabDetailPagers.get(position);
            View view = pager.mRootView;
            container.addView(view);
            pager.initData();
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    @OnClick(R.id.btn_next)
    public void nextPage(View view) {
        // 下一个页面
        int currentItem = mViewPager.getCurrentItem();
        currentItem++;
        mViewPager.setCurrentItem(currentItem);
    }
}
