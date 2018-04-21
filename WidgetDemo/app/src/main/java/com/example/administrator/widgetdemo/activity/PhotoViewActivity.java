package com.example.administrator.widgetdemo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.administrator.widgetdemo.R;
import com.example.administrator.widgetdemo.widget.photoview.PhotoView;

import java.util.ArrayList;

/**
 * Author:huangxiaoming
 * Date:2018/4/19
 * Desc:图片预览
 * Version:1.0
 */
public class PhotoViewActivity extends Activity {

    private ViewPager mPager;

    private ArrayList<String> imgList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);

        Intent intent = getIntent();
        if(intent != null){
            imgList = intent.getStringArrayListExtra("images");
        }
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setPageMargin((int) (getResources().getDisplayMetrics().density * 15));
        mPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return imgList.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                PhotoView view = new PhotoView(PhotoViewActivity.this);
                view.enable();
                Glide.with(PhotoViewActivity.this)
                        .load(imgList.get(position))
                        .crossFade()
                        .fitCenter()
                        .into(view);
                container.addView(view);
                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }
        });
    }
}
