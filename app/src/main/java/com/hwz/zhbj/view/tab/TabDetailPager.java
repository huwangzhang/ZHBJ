package com.hwz.zhbj.view.tab;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hwz.zhbj.NewsDetailActivity;
import com.hwz.zhbj.R;
import com.hwz.zhbj.domain.NewsBean;
import com.hwz.zhbj.domain.TabDetailBean;
import com.hwz.zhbj.global.Constants;
import com.hwz.zhbj.util.CacheUtils;
import com.hwz.zhbj.util.PrefUtils;
import com.hwz.zhbj.util.URLUtils;
import com.hwz.zhbj.view.BaseMenuDetailPager;
import com.hwz.zhbj.view.PullRefreshListView;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;

import static com.hwz.zhbj.R.id.indicator;

/**
 * Created by huwang on 2017/6/7.
 */

public class TabDetailPager extends BaseMenuDetailPager {
    private TabDetailBean mTabDetailBean;
    private NewsBean.NewsMenuTab mNewsMenuTab;
    private String mUrl;
    private ArrayList<TabDetailBean.TopNews> mTopnews;

    @ViewInject(R.id.vp_top_news)
    private ViewPager mViewPager;
    @ViewInject(R.id.tv_title)
    private TextView mTvTitle;
    @ViewInject(indicator)
    private CirclePageIndicator mCirclePageIndicator;
    @ViewInject(R.id.lv_list)
    private PullRefreshListView mListView;
    private ArrayList<TabDetailBean.News> mNewsList;
    private NewsAdapter mNewsAdapter;
    private String mMoreUrl;

    private Handler mHandler;

    public TabDetailPager(Activity activity, NewsBean.NewsMenuTab newsMenuTab) {
        super(activity);
        mNewsMenuTab = newsMenuTab;


        mUrl = Constants.SERVER_URL + mNewsMenuTab.url;
    }

    @Override
    public View initView() {

        View view = View.inflate(mActivity, R.layout.pager_tab_detail, null);
        ViewUtils.inject(this, view);

        View mHeaderView = View.inflate(mActivity, R.layout.list_item_header, null);
        ViewUtils.inject(this, mHeaderView); // 注入头布局

        // listview添加头布局
        mListView.addHeaderView(mHeaderView);


        mListView.setOnRefreshListener(new PullRefreshListView.OnRefreshListener() {
            @Override
            public void OnRefresh() {
                // 刷新逻辑
                getDataFromServer();
            }

            @Override
            public void OnLoadMore() {
                // listView 加载更多数据
                if (mMoreUrl != null) {
                    // 有下一页数据
                    getMoreDataFromServer();
                } else {
                    // 没有
                    mListView.onRefreshComplete(true); // 隐藏控件

                    Toast.makeText(mActivity, "没有更多数据了！", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 头布局有占位
                int headerViewsCount = mListView.getHeaderViewsCount(); // 头布局数量
                position -= headerViewsCount;
                System.out.println(position + "  被点击了！");

                // 新闻对象
                TabDetailBean.News news = mNewsList.get(position);

                // readed_ids:记录已读的新闻ID
                String readed_ids = PrefUtils.getString(mActivity, "readed_ids");

                if (!readed_ids.contains(news.id)) {  // 不包含，存储id
                    readed_ids = readed_ids + news.id + ",";
                    PrefUtils.putString(mActivity, "readed_ids", readed_ids);
                }

//                mNewsAdapter.notifyDataSetChanged();

                // 被点击的Item文字变色
                TextView title = (TextView) view.findViewById(R.id.tv_title);
                title.setTextColor(Color.GRAY);

                Intent intent = new Intent(mActivity, NewsDetailActivity.class);
                intent.putExtra("news_url", URLUtils.replaceUrl(news.url));
                mActivity.startActivity(intent);
            }
        });
        return view;
    }

    private void getMoreDataFromServer() {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, mMoreUrl, new RequestCallBack<String>() {
            /**
             * @param responseInfo
             * 运行在主线程，XUtils特性
             */
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                // 请求成功
                String result = responseInfo.result;
                System.out.println("服务器返回结果" + result);
                processData(result, true);
//                System.out.println("murl: " + mUrl);
                // 后面不需要缓存
//                CacheUtils.setCache(mActivity, mUrl, result);

                // 隐藏下拉刷新布局, 刷新结束
                mListView.onRefreshComplete(true);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                // 请求失败
                error.printStackTrace();
                Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();

                // 隐藏下拉刷新布局, 失败重置控件
                mListView.onRefreshComplete(false);
            }
        });
    }

    @Override
    public void initData() {

        // TODO 注意，由于在没有数据时候，刚开始设置的默认值为""， 所以检查通过，导致后面Gson解析出错

        String cache = CacheUtils.getCache(mActivity, mUrl);
//        if (cache != null)  // 采用下面的格式更加健壮
        if (!TextUtils.isEmpty(cache)) {
            System.out.println("本地数据：" + cache);
            processData(cache, false);
        }
        // 获取网络数据
        getDataFromServer();
    }

    private void getDataFromServer() {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, mUrl, new RequestCallBack<String>() {
            /**
             * @param responseInfo
             * 运行在主线程，XUtils特性
             */
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                // 请求成功
                String result = responseInfo.result;
                System.out.println("服务器返回结果" + result);
                processData(result, false);
//                System.out.println("murl: " + mUrl);
                CacheUtils.setCache(mActivity, mUrl, result);

                // 隐藏下拉刷新布局, 刷新结束
                mListView.onRefreshComplete(true);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                // 请求失败
                error.printStackTrace();
                Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();

                // 隐藏下拉刷新布局, 失败重置控件
                mListView.onRefreshComplete(false);
            }
        });
    }

    private void processData(String json, boolean isMore) {
        Gson gson = new Gson();
        mTabDetailBean = gson.fromJson(json, TabDetailBean.class);

        String moreUrl = mTabDetailBean.data.more;
        if (!TextUtils.isEmpty(moreUrl)) {
            mMoreUrl = Constants.SERVER_URL + moreUrl;
        } else {
            mMoreUrl = null;  // 没有数据
        }
        System.out.println("解析完成！");
        if (!isMore) {
            mTopnews = mTabDetailBean.data.topnews;
            if (mTopnews != null) {
                mViewPager.setAdapter(new TopNewsAdapter());
                // 设置指示器
                mCirclePageIndicator.setViewPager(mViewPager);
                mCirclePageIndicator.setSnap(true);

                // 事件设置给Indicator
                mCirclePageIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        TabDetailBean.TopNews topNews = mTopnews.get(position);
                        mTvTitle.setText(topNews.title);
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
                mTvTitle.setText(mTopnews.get(0).title);
                mCirclePageIndicator.onPageSelected(0);  // 选中第一个
            }
            mNewsList = mTabDetailBean.data.news;
            if (mNewsList != null) {
                mNewsAdapter = new NewsAdapter();
                mListView.setAdapter(mNewsAdapter);
            }

            if (mHandler == null) {
                mHandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        int currentItem = mViewPager.getCurrentItem();
                        currentItem++;
                        if (currentItem > mTopnews.size() - 1) {
                            currentItem = 0;
                        }
                        mViewPager.setCurrentItem(currentItem);
                        mHandler.sendEmptyMessageDelayed(0, 2000);
                    }
                };
                mHandler.sendEmptyMessageDelayed(0, 2000);
                mViewPager.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                /// 去除handler发送消息
                                mHandler.removeCallbacksAndMessages(null);
                                break;
                            case MotionEvent.ACTION_UP:
                                mHandler.sendEmptyMessageDelayed(0, 2000);
                                break;
                            case MotionEvent.ACTION_CANCEL: // 取消事件
                                mHandler.sendEmptyMessageDelayed(0, 2000); // 启动广告轮播
                                break;
                            default:
                                break;
                        }
                        return false;
                    }
                });
            }
        } else {
            // 加载更多数据
            ArrayList<TabDetailBean.News> moreNews = mTabDetailBean.data.news;
            mNewsList.addAll(moreNews);

            // 刷新
            mNewsAdapter.notifyDataSetChanged();
        }

    }


    class TopNewsAdapter extends PagerAdapter {
        private BitmapUtils mBitmapUtils;

        public TopNewsAdapter() {
            mBitmapUtils = new BitmapUtils(mActivity);
            mBitmapUtils.configDefaultLoadingImage(R.mipmap.topnews_item_default);
        }

        @Override
        public int getCount() {
            return mTopnews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView view = new ImageView(mActivity);
//            view.setBackgroundResource(R.mipmap.topnews_item_default);
            view.setScaleType(ImageView.ScaleType.FIT_XY);

            String uri = URLUtils.replaceUrl(mTopnews.get(position).topimage);
            System.out.println("topnewurl: " + uri);
            // 下载图片，设置，避免内存溢出，缓存
            // BitmapUtils
            mBitmapUtils.display(view, uri);

            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    class NewsAdapter extends BaseAdapter {
        private BitmapUtils mBitmapUtils;

        public NewsAdapter() {
            mBitmapUtils = new BitmapUtils(mActivity);
            mBitmapUtils.configDefaultLoadingImage(R.mipmap.news_pic_default);
        }

        @Override
        public int getCount() {
            return mNewsList.size();
        }

        @Override
        public TabDetailBean.News getItem(int position) {
            return mNewsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = View.inflate(mActivity, R.layout.list_item_news, null);
                viewHolder = new ViewHolder();
                viewHolder.icon = (ImageView) convertView.findViewById(R.id.iv_news);
                viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
                viewHolder.tvDate = (TextView) convertView.findViewById(R.id.tv_date);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            TabDetailBean.News news = getItem(position);
            viewHolder.tvTitle.setText(news.title);
            viewHolder.tvDate.setText(news.pubdate);

            // readed_ids:记录已读的新闻ID
            String readed_ids = PrefUtils.getString(mActivity, "readed_ids");

            if (readed_ids.contains(news.id)) {  // 包含
                viewHolder.tvTitle.setTextColor(Color.GRAY);
            } else {
                viewHolder.tvTitle.setTextColor(Color.BLACK);
            }

            mBitmapUtils.display(viewHolder.icon, URLUtils.replaceUrl(news.listimage));
            return convertView;
        }
    }

    static class ViewHolder {
        public ImageView icon;
        public TextView tvTitle;
        public TextView tvDate;
    }
}
