package com.example.administrator.widgetdemo.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.example.administrator.widgetdemo.R;
import com.example.administrator.widgetdemo.fragment.ListViewFragment;

import java.util.ArrayList;
import java.util.List;

public class TabListActivity extends AppCompatActivity {

    AppBarLayout mAppBarLayout;

    Toolbar mToolbar;

    Bitmap bitmap;

    ViewPager mViewPager;

    ListViewFragment mFragment1;

    ListViewFragment mFragment2;

    ListViewFragment mFragment3;

    PagerAdapter mPagerAdapter;

    private LinearLayout head_layout;

    private TabLayout toolbar_tab;

    private List<ListViewFragment> mFragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_list);
        initView();
    }

    private void initView() {

        mAppBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.bg);
        mViewPager.setOffscreenPageLimit(2);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                onBackPressed();
            }
        });
        head_layout = (LinearLayout) findViewById(R.id.login_layout);
        toolbar_tab = (TabLayout) findViewById(R.id.toolbar_tab);
        //单纯的背景图片
        head_layout.setBackgroundDrawable(new BitmapDrawable(bitmap));
        final CollapsingToolbarLayout mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
//        设置CollapsingToolbarLayout折叠后toolbar上显示的背景，不设置就是单色
        mCollapsingToolbarLayout.setContentScrim(new BitmapDrawable(bitmap));
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset <= -head_layout.getHeight() / 2) {
                    mCollapsingToolbarLayout.setTitle("Xiaoming");
                } else {
                    mCollapsingToolbarLayout.setTitle(" ");
                }
            }
        });

        initFragment();
    }


    private void initFragment() {
        if (mFragment1 == null) {
            mFragment1 = new ListViewFragment();
        }
        if (mFragment2 == null) {
            mFragment2 = new ListViewFragment();
        }
        if (mFragment3 == null) {
            mFragment3 = new ListViewFragment();
        }
        mFragments.add(mFragment1);
        mFragments.add(mFragment2);
        mFragments.add(mFragment3);
        mPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(toolbar_tab));
        toolbar_tab.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
    }


    public class PagerAdapter extends FragmentPagerAdapter {
        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return mFragment1;
            } else if (position == 1) {
                return mFragment2;
            } else if (position == 2) {
                return mFragment3;
            }

            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

    }
}
