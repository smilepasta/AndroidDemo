package com.example.administrator.widgetdemo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.administrator.widgetdemo.R;
import com.example.administrator.widgetdemo.widget.BannerLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:huangxiaoming
 * Date:2018/4/19
 * Desc:Banner图
 * Version:1.0
 */
public class BannerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner);

        BannerLayout bannerLayout1 = (BannerLayout) findViewById(R.id.banner1);
        BannerLayout bannerLayout2 = (BannerLayout) findViewById(R.id.banner2);
        BannerLayout bannerLayout3 = (BannerLayout) findViewById(R.id.banner3);

        final List<String> urls1 = new ArrayList<>();
        urls1.add("https://resources.matcha-jp.com/archive_files/jp/2016/09/what_is_sakura.jpg");
        urls1.add("https://fthmb.tqn.com/Sq6Kp6lndB0KWrXJWpJroTQECN0=/960x0/filters:no_upscale()/Cherry-Blossoms-Dennis-Govoni-56cb3b173df78cfb379b7b91.jpg");
        urls1.add("https://d1p2fuior9l0tb.cloudfront.net/wp-content/uploads/2018/03/11194616/Jefferson-Memorial-shutterstock_1872661851.jpg");
        urls1.add("https://cdn-images-1.medium.com/max/1200/1*qsdj8-E2ZmBSiZFQTDTzHw.jpeg");
        bannerLayout1.setViewUrls(urls1);

        //添加监听事件
        bannerLayout1.setOnBannerItemClickListener(position -> Toast.makeText(BannerActivity.this, String.valueOf(position), Toast.LENGTH_SHORT).show());

        final List<String> urls2 = new ArrayList<>();
        urls2.add("http://images.xuejuzi.cn/1505/1_150511223801_1.jpg");
        urls2.add("http://images.xuejuzi.cn/1505/1_150511223813_1.jpg");
        urls2.add("http://img.sc115.com/tx/ns/pic/1503l4nncimpv4v.jpg");
        urls2.add("https://cdn-images-1.medium.com/max/1200/1*qsdj8-E2ZmBSiZFQTDTzHw.jpeg");
        bannerLayout2.setViewUrls(urls2);
        //添加监听事件
        bannerLayout2.setOnBannerItemClickListener(position -> Toast.makeText(BannerActivity.this, String.valueOf(position), Toast.LENGTH_SHORT).show());

        bannerLayout3.setViewUrls(urls1);
        //添加监听事件
        bannerLayout3.setOnBannerItemClickListener(position -> Toast.makeText(BannerActivity.this, String.valueOf(position), Toast.LENGTH_SHORT).show());

    }
}