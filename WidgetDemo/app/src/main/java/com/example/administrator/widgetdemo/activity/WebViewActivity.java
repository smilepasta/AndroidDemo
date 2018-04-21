package com.example.administrator.widgetdemo.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.example.administrator.widgetdemo.R;
import com.example.administrator.widgetdemo.utils.LogUtil;
import com.example.administrator.widgetdemo.utils.StringUtil;
import com.example.administrator.widgetdemo.utils.UIUtil;

/**
 * Author:huangxiaoming
 * Date:2018/4/19
 * Desc:web view
 * Version:1.0
 */
public class WebViewActivity extends AppCompatActivity {

    public static final String LOAD_URL = "loadUrl";

    private WebView mWebView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ProgressBar mLoadBar;
    private WebSettings mWs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        mWebView = findViewById(R.id.wv_web);
        mLoadBar = findViewById(R.id.pb_load_bar);

        mSwipeRefreshLayout = findViewById(R.id.srl_refresh);
        mSwipeRefreshLayout.setColorSchemeColors(UIUtil.getColor(R.color.colorPrimaryDark)
                , UIUtil.getColor(R.color.colorPrimary)
                , UIUtil.getColor(R.color.colorAccent));
        mSwipeRefreshLayout.setOnRefreshListener(() -> mWebView.reload());

        mWs = mWebView.getSettings();
        // 设置可以支持缩放
        mWs.setSupportZoom(true);
        // 设置出现缩放工具
        mWs.setBuiltInZoomControls(true);
        //设置可在大视野范围内上下左右拖动，并且可以任意比例缩放
        mWs.setUseWideViewPort(true);
        //设置默认加载的可视范围是大视野范围
        mWs.setLoadWithOverviewMode(true);
        //自适应屏幕
        mWs.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWs.setJavaScriptEnabled(true);

        String url = getIntent().getStringExtra(LOAD_URL);
        if (StringUtil.isNotEmpty(url)) {
            mWebView.loadUrl(url);
        }
        mWebView.setWebChromeClient(new WebChromeClient() {
                                        public void onProgressChanged(WebView view, int progress) {
                                            mLoadBar.setProgress(progress);
                                        }
                                    }
        );
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mLoadBar.setVisibility(View.GONE);
                LogUtil.defLog("onPageFinished");
                if (mSwipeRefreshLayout.isRefreshing()) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (StringUtil.isNotEmpty(title)) {
                    setTitle(title);
                }
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mWebView.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                if (mWebView.getScrollY() == 0) {
                    mSwipeRefreshLayout.setEnabled(true);
                } else {
                    mSwipeRefreshLayout.setEnabled(false);
                }
            });
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
