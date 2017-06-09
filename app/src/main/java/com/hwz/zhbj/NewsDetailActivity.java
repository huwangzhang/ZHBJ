package com.hwz.zhbj;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.Timer;
import java.util.TimerTask;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by huwang on 2017/6/8.
 * 新闻详情
 */

public class NewsDetailActivity extends Activity implements View.OnClickListener {
//    private ProgressDialog progressDialog = null;

    @ViewInject(R.id.ll_control)
    private LinearLayout mLLControl;
    @ViewInject(R.id.btn_back)
    private ImageButton mBtnBack;
    @ViewInject(R.id.btn_share)
    private ImageButton mBtnShare;
    @ViewInject(R.id.btn_text_size)
    private ImageButton mBtnTextSize;
    @ViewInject(R.id.btn_menu)
    private ImageButton mBtnMenu;
    @ViewInject(R.id.wv_news_detail)
    private WebView mWebView;
    @ViewInject(R.id.pb_new)
    private ProgressBar mPbNewDetail;

    private String mUrl;
    private Timer mTimer;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_detail);
        ShareSDK.initSDK(this,null);
        ViewUtils.inject(this);
        initView();
        initData();
    }

    private void initData() {
    }

    private void initView() {
//        if (progressDialog == null) {
//            progressDialog = new ProgressDialog(this);
//        }
//        progressDialog.setTitle("正在加载中...");
//        progressDialog.show();
        Intent intent = getIntent();
        mUrl = intent.getStringExtra("news_url");
        mLLControl.setVisibility(View.VISIBLE);
        mBtnBack.setVisibility(View.VISIBLE);
        mBtnMenu.setVisibility(View.GONE);
        mBtnBack.setOnClickListener(this);
        mBtnShare.setOnClickListener(this);
        mBtnTextSize.setOnClickListener(this);

        System.out.println("url: " + mUrl);
        WebSettings settings = mWebView.getSettings();
        settings.setBuiltInZoomControls(true); //显示缩放按钮
        settings.setUseWideViewPort(true);   // 支持缩放
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        mWebView.setWebViewClient(new WebViewClient() {
            // 开始加载网页
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                System.out.println("开始加载网页了");
                mPbNewDetail.setVisibility(View.VISIBLE);
                mTimer = new Timer();
                mTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mPbNewDetail.setVisibility(View.INVISIBLE);
                            }
                        });
                    }
                }, 1000);
            }

            // 网页加载结束
            @Override
            public void onPageFinished(WebView view, String url) {
                System.out.println("网页加载结束");
                // TODO: 2017/6/8  有点bug， 不知道为什么加载完成之后，不执行这个方法，等待一会时间
                // 才会 执行
                mTimer.cancel();
                mPbNewDetail.setVisibility(View.INVISIBLE);
            }

            // 所有链接跳转会走此方法
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                System.out.println("跳转链接:" + url);
                view.loadUrl(url);// 在跳转链接时强制在当前webview中加载
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                return true;
            }

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                Toast.makeText(NewsDetailActivity.this, "同步失败，请稍候再试", Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//              super.onReceivedSslError(view, handler, error);
                System.out.println("onReceivedSslError执行了!");
                handler.proceed();
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                // 进度发生变化
                System.out.println("进度:" + newProgress);
                if (newProgress == 100) {
                    mPbNewDetail.setVisibility(View.INVISIBLE);
                }
            }
        });

        mWebView.loadUrl(mUrl);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_share:
                System.out.println("sd卡路径:" + Environment.getExternalStorageDirectory().getAbsolutePath());
                showShare();
                break;
            case R.id.btn_text_size:
                showChooseDialog();
                break;
            default:
                break;
        }
    }

    private void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();

//        oks.setTheme(OnekeyShareTheme.CLASSIC);
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用
        oks.setTitle("标题");
        // titleUrl是标题的网络链接，QQ和QQ空间等使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImagePath("/storage/emulated/0/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

        // 启动分享GUI
        oks.show(this);
    }

    private int mTempIndex;
    private int mCurrentIndex;

    /**
     * 字体大小
     */
    private void showChooseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("字体设置");
        final String[] resItem = new String[]{
                "超大号字体",
                "大号字体",
                "正常字体",
                "小号字体",
                "超小号字体"
        };
        builder.setSingleChoiceItems(resItem, mCurrentIndex, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mTempIndex = which;
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (mTempIndex) {
                    case 0:
                        mWebView.getSettings().setTextZoom(200);
                        break;
                    case 1:
                        mWebView.getSettings().setTextZoom(150);
                        break;
                    case 2:
                        mWebView.getSettings().setTextZoom(100);
                        break;
                    case 3:
                        mWebView.getSettings().setTextZoom(75);
                        break;
                    case 4:
                        mWebView.getSettings().setTextZoom(50);
                        break;
                    default:
                        break;
                }
                mCurrentIndex = mTempIndex;
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }
}
