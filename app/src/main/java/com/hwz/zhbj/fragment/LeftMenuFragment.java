package com.hwz.zhbj.fragment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.hwz.zhbj.MainActivity;
import com.hwz.zhbj.R;
import com.hwz.zhbj.domain.NewsBean;
import com.hwz.zhbj.view.menu.NewsCenterPager;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

/**
 * Created by huwang on 2017/6/6.
 */

public class LeftMenuFragment extends BaseFragment {
    @ViewInject(R.id.lv_left_menu)
    private ListView mLvLeftMenu;

    private ArrayList<NewsBean.NewsMenuBean> data;
    private TextView mTvMenu;
    private int mCurrentPos;// 当前被选中item 的位置
    private LeftMenuAdapter mAdapter;

    @Override
    public void initData() {

    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_left_menu, null);
        ViewUtils.inject(this, view);  // 注入view和事件
        return view;
    }

    public void setMenuData(ArrayList<NewsBean.NewsMenuBean> data) {
        mCurrentPos = 0; // 更新(来回切换之后，保持菜单与详情页同步)
        this.data = data;
        mAdapter = new LeftMenuAdapter();
        mLvLeftMenu.setAdapter(mAdapter);
        mLvLeftMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCurrentPos = position;
                mAdapter.notifyDataSetChanged();
                // 关闭侧边栏
                toggle();
                setCurrentDetailPager(position);
            }
        });
    }

    /**
     * @param position
     * 修改详情页
     */
    private void setCurrentDetailPager(int position) {
        // 获取新闻中心的对象
        MainActivity mainUi = (MainActivity) mActivity;
        ContentFragment fragment = mainUi.getContentFragment();
        // 获取新闻中心
        NewsCenterPager newsCenterPager = fragment.getNewsCenterPager();
        // 修改新闻中心布局
        newsCenterPager.setCurrentDetailPager(position);
    }


    class LeftMenuAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public NewsBean.NewsMenuBean getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(mActivity, R.layout.list_item_left_menu, null);
            mTvMenu = (TextView)view.findViewById(R.id.tv_menu);
            NewsBean.NewsMenuBean newsMenuBean = data.get(position);
            mTvMenu.setText(newsMenuBean.title);
            if (position == mCurrentPos) {
                // 被选中
                mTvMenu.setEnabled(true);
            } else {
                mTvMenu.setEnabled(false);
            }
            return view;
        }
    }
}
