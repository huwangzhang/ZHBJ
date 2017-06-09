package com.hwz.zhbj.fragment;

import android.support.annotation.IdRes;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.hwz.zhbj.R;
import com.hwz.zhbj.view.BasePager;
import com.hwz.zhbj.view.NoScrollViewPager;
import com.hwz.zhbj.view.menu.GovAffairsPager;
import com.hwz.zhbj.view.menu.HomePager;
import com.hwz.zhbj.view.menu.NewsCenterPager;
import com.hwz.zhbj.view.menu.SettingPager;
import com.hwz.zhbj.view.menu.SmartServicePager;

import java.util.ArrayList;

/**
 * Created by huwang on 2017/6/6.
 */

public class ContentFragment extends BaseFragment {
    private NoScrollViewPager mViewPager;
    private ArrayList<BasePager> mPagers;
    private RadioGroup mRadioGroup;

    @Override
    public void initData() {
        mPagers = new ArrayList<BasePager>();
        mPagers.add(new HomePager(mActivity));
        mPagers.add(new NewsCenterPager(mActivity));
        mPagers.add(new SmartServicePager(mActivity));
        mPagers.add(new GovAffairsPager(mActivity));
        mPagers.add(new SettingPager(mActivity));


        mViewPager.setAdapter(new ContentAdapter());
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.home:
                        mViewPager.setCurrentItem(0);
//                        mViewPager.setCurrentItem(0, false); // 取消滑动动画
                        break;
                    case R.id.news:
                        mViewPager.setCurrentItem(1);
                        break;
                    case R.id.smart:
                        mViewPager.setCurrentItem(2);
                        break;
                    case R.id.govaffairs:
                        mViewPager.setCurrentItem(3);
                        break;
                    case R.id.setting:
                        mViewPager.setCurrentItem(4);
                        break;
                    default:
                        break;
                }
            }
        });

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                // viewpager默认加载下一个界面
                BasePager pager = mPagers.get(position);
                pager.initData();
                if (position == 0 || position == mPagers.size() - 1) {
                    setSlideMenuEnable(false);
                } else {
                    setSlideMenuEnable(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        // 加载第一个页面
        mPagers.get(0).initData();
        setSlideMenuEnable(false);
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_content, null);
        mRadioGroup = (RadioGroup) view.findViewById(R.id.rg_group);
        mViewPager = (NoScrollViewPager) view.findViewById(R.id.vp_content);
        return view;
    }

    class ContentAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mPagers.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BasePager pager = mPagers.get(position);
//            pager.initData();
            View view = pager.rootView;
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    /**
     * 获取新闻中心页面
     */
    public NewsCenterPager getNewsCenterPager() {
        NewsCenterPager pager = (NewsCenterPager) mPagers.get(1);
        return pager;
    }

    public void setViewPagerInter(boolean isInter) {
        mViewPager.setInter(isInter);
    }
}
