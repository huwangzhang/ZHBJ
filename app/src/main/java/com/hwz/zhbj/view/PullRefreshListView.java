package com.hwz.zhbj.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hwz.zhbj.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by huwang on 2017/6/8.
 * 下拉刷新
 */

public class PullRefreshListView extends ListView implements AbsListView.OnScrollListener {

    private static final int STATE_PULL_REFRESH = 1;
    private static final int STATE_RELEASE_REFRESH = 2;
    private static final int STATE_REFRESH = 3;

    private int mCurrentState = STATE_PULL_REFRESH;

    private View mHeaderView;
    private int mHeaderHeight;
    private int mStartY = -1;
    private TextView mTvTitle;
    private TextView mTvTime;
    private ImageView mArrowRefresh;
    private RotateAnimation mAnimUp;
    private RotateAnimation mAnimDown;
    private ProgressBar mProgressBar;
    private OnRefreshListener mRefreshListener;
    private View mFooterView;
    private int mFooterHeight;

    private boolean isLoadMore; // 标记是否正在加载更多

    public PullRefreshListView(Context context) {
        this(context, null);
    }

    public PullRefreshListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullRefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initHeaderView();
        initFooterView();
    }

    public void initFooterView() {
        mFooterView = View.inflate(getContext(), R.layout.refresh_news_footer, null);
        addFooterView(mFooterView);

        mFooterView.measure(0, 0);
        mFooterHeight = mFooterView.getMeasuredHeight();
        mFooterView.setPadding(0, -mFooterHeight, 0, 0);

        setOnScrollListener(this);
    }

    /**
     * 初始化头布局
     */
    public void initHeaderView() {
        mHeaderView = View.inflate(getContext(), R.layout.refresh_news_header, null);
        addHeaderView(mHeaderView);

        mTvTitle = (TextView) mHeaderView.findViewById(R.id.tv_title);
        mTvTime = (TextView) mHeaderView.findViewById(R.id.tv_time);
        mArrowRefresh = (ImageView) mHeaderView.findViewById(R.id.iv_arrow);
        mProgressBar = (ProgressBar) mHeaderView.findViewById(R.id.pb_loading);

        // 隐藏下拉刷新布局
        mHeaderView.measure(0, 0);
        mHeaderHeight = mHeaderView.getMeasuredHeight();
        mHeaderView.setPadding(0, -mHeaderHeight, 0, 0);

        initAnim();

        setCurrentTime();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mStartY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_UP:
                mStartY = -1;
                if (mCurrentState == STATE_RELEASE_REFRESH) {
                    mCurrentState = STATE_REFRESH;
                    refreshState();// 刷新状态
                    mHeaderView.setPadding(0, 0, 0, 0);// 完整展示刷新布局
                    // 回掉 , 刷新
                    if (mRefreshListener != null) {
                        mRefreshListener.OnRefresh();
                    }
                } else if (mCurrentState == STATE_PULL_REFRESH) {
                    // 隐藏布局
                    mHeaderView.setPadding(0, -mHeaderHeight, 0, 0);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mStartY == -1) {
                    mStartY = (int) ev.getY();
                }

                if (mCurrentState == STATE_REFRESH) {
                    break; // 正在刷新
                }

                int endY = (int) ev.getY();
                int dy = endY - mStartY;

                int position = getFirstVisiblePosition();// 当前第一个显示的位置

                if (dy > 0 && position == 0) { // 下拉，当前显示第一个item
                    int padding = dy - mHeaderHeight;  // 下拉距离
                    mHeaderView.setPadding(0, padding, 0, 0);

                    if (padding > 0 && mCurrentState != STATE_RELEASE_REFRESH) {
                        // 松开刷新
                        mCurrentState = STATE_RELEASE_REFRESH;
                        refreshState();
                    } else if (padding < 0 && mCurrentState != STATE_PULL_REFRESH) {
                        // 下拉刷新
                        mCurrentState = STATE_PULL_REFRESH;
                        refreshState();
                    }
                    return true; // 消费掉
                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(ev);
    }

    private void refreshState() {
        switch (mCurrentState) {
            case STATE_PULL_REFRESH:
                mTvTitle.setText("下拉刷新");
                mArrowRefresh.startAnimation(mAnimDown);
                mProgressBar.setVisibility(INVISIBLE);
                mArrowRefresh.setVisibility(VISIBLE);
                break;
            case STATE_RELEASE_REFRESH:
                mTvTitle.setText("松开立即刷新");
                mArrowRefresh.startAnimation(mAnimUp);
                mProgressBar.setVisibility(INVISIBLE);
                mArrowRefresh.setVisibility(VISIBLE);
                break;
            case STATE_REFRESH:
                mTvTitle.setText("正在刷新");
                mArrowRefresh.clearAnimation(); // 清除动画，否则无法隐藏
                mProgressBar.setVisibility(VISIBLE);
                mArrowRefresh.setVisibility(INVISIBLE);

                break;
        }
    }

    /**
     * 箭头动画
     */
    private void initAnim() {
        mAnimUp = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mAnimUp.setDuration(200);
        mAnimUp.setFillAfter(true);

        mAnimDown = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mAnimDown.setDuration(200);
        mAnimDown.setFillAfter(true);
    }

    /**
     * 刷新结束,隐藏控件
     */
    public void onRefreshComplete(boolean success) {
        if (!isLoadMore) {
            mHeaderView.setPadding(0, -mHeaderHeight, 0, 0);
            mCurrentState = STATE_PULL_REFRESH;
            mTvTitle.setText("下拉刷新");
            mProgressBar.setVisibility(INVISIBLE);
            mArrowRefresh.setVisibility(VISIBLE);
            if (success) {
                // 设置下拉时间
                setCurrentTime();
            }
        } else {
            mFooterView.setPadding(0, -mFooterHeight, 0, 0);
            isLoadMore = false;
        }

    }

    /**
     * @param listener 设置接口
     */
    public void setOnRefreshListener(OnRefreshListener listener) {
        mRefreshListener = listener;
    }

    /**
     * @param view
     * @param scrollState 滑动状态发生变化
     */
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == SCROLL_STATE_IDLE) { // 空闲状态
            int lastVisiblePosition = getLastVisiblePosition();

            if (lastVisiblePosition == getCount() - 1 && !isLoadMore) { // 显示最后一个item，没有加载更多，请求数据
                isLoadMore = true;
                System.out.println("加载更多...");
                mFooterView.setPadding(0, 0, 0, 0); // 显示加载更多的布局
                setSelection(getCount() - 1);

                // 主界面加载下一页
                if (mRefreshListener != null) {
                    mRefreshListener.OnLoadMore();
                }
            }
        }
    }

    /**
     * @param view
     * @param firstVisibleItem
     * @param visibleItemCount
     * @param totalItemCount   滑动过程
     */
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }


    /**
     * 下拉刷新回掉接口
     */
    public interface OnRefreshListener {
        public void OnRefresh();

        public void OnLoadMore();
    }

    private void setCurrentTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
        String time = format.format(new Date());

        mTvTime.setText(time);
    }
}
