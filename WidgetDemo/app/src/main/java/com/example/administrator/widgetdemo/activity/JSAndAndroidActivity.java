package com.example.administrator.widgetdemo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.administrator.widgetdemo.R;
import com.example.administrator.widgetdemo.utils.ToastUtil;

public class JSAndAndroidActivity extends AppCompatActivity {


    WebView webView;

    private void initView() {
        webView = findViewById(R.id.wv_web);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);// 表示支持js
        settings.setDomStorageEnabled(true);

        webView.addJavascriptInterface(new AndroidtoJs(), "test");//AndroidtoJS类对象映射到js的test对象
    }

    private void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            int uid = intent.getIntExtra("id", 0);
            webView.loadUrl("file:///android_asset/index.html");

            webView.setWebViewClient(new WebViewClient() {
                                         @Override
                                         public void onPageFinished(WebView view, String url) {
                                             String key = "uid";
                                             String val = "1111111";
                                             view.loadUrl("javascript:localStorage.setItem('" + key + "','" + val + "');");
                                         }
                                     }
            );

            findViewById(R.id.btn_calljs).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    webView.loadUrl("javascript:callJS()");
                }
            });
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_js_android);
        initView();
        initData();
    }

    // 继承自Object类
    public class AndroidtoJs extends Object {

        // 定义JS需要调用的方法
        // 被JS调用的方法必须加入@JavascriptInterface注解
        @JavascriptInterface
        public void hello(String msg) {
            ToastUtil.show(JSAndAndroidActivity.this, msg);

        }
    }

}
