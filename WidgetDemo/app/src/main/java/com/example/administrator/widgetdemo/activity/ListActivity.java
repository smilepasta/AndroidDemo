package com.example.administrator.widgetdemo.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Toast;

import com.example.administrator.widgetdemo.R;
import com.example.administrator.widgetdemo.adpater.RvLoadMoreAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:huangxiaoming
 * Date:2018/4/19
 * Desc:上拉刷新，下拉加载
 * Version:1.0
 */
public class ListActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private Context mContext;
    private SwipeRefreshLayout mRefresh;
    private RecyclerView mList;
    private LinearLayoutManager mLayoutManager;
    private RvLoadMoreAdapter mAdapter;
    private boolean isUp;
    private int state=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        mContext = this;

        initView();


    }

    private void initView() {
        mList = findViewById(R.id.rv_list);
        mAdapter = new RvLoadMoreAdapter(mContext);
        mLayoutManager = new LinearLayoutManager(mContext);
        mList.setLayoutManager(mLayoutManager);
        mList.setAdapter(mAdapter);

        mRefresh = findViewById(R.id.srl_refresh);
        mRefresh.setOnRefreshListener(this);

        mAdapter.setListener(new RvLoadMoreAdapter.OnListener() {
            @Override
            public void onItemClick() {
                //item点击事件
            }

            @Override
            public void onReLoad() {
                //加载更多点击事件
                mAdapter.setmLoadState(false, false, null);
                mAdapter.setmIsLoading(true);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        List<String> data = initData();
                        if (state == RvLoadMoreAdapter.STATE_ERROR_NET) {
                            mAdapter.setmLoadState(true, false, null);
                        } else if (state == RvLoadMoreAdapter.STATE_ALL_LOADED) {
                            mAdapter.setmLoadState(false, true, null);
                        } else
                            mAdapter.setmLoadState(false, false, data);
                        mAdapter.setmIsLoading(false);
                    }
                }, 2000);
            }
        });

        // 监听滑动事件，判断是否是手指向上滑动，避免item不满一页时，下拉刷新触发上拉加载事件。
        mList.setOnTouchListener(new View.OnTouchListener() {
            float oldy;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        oldy=event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        //
                        if(oldy-event.getY()> ViewConfiguration.get(mContext).getScaledTouchSlop()){
                            isUp=true;
                        } else {
                            isUp=false;
                        }
                }
                return false; // 不拦截事件
            }
        });

        mList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private int lastVisibleItemPosition;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                //正在滚动
                if(isUp && !mAdapter.ismIsLoading() && newState==RecyclerView.SCROLL_STATE_IDLE  &&
                        lastVisibleItemPosition+1==mAdapter.getItemCount()){  // 判断条件：手指上滑、当前不是正在加载的状态、停止滑动、末尾的item为最后一项
                    // 初始状态时，footer为隐藏，这里将其设为可见
                    mAdapter.showFooter();
                    if(mAdapter.getLoadState()==RvLoadMoreAdapter.STATE_FIRST){ //初始状态
                        mAdapter.setmLoadState(false,false,null);
                    }
                    // 只有显示状态时，才能响应滑动事件（不包括错误状态和加载完成状态）
                    if(mAdapter.getLoadState()==RvLoadMoreAdapter.STATE_SHOW){ // 显示状态
                        mAdapter.setmIsLoading(true);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                List<String> data=initData();
                                if(state==RvLoadMoreAdapter.STATE_ERROR_NET){
                                    mAdapter.setmLoadState(true,false,null);
                                } else  if(state==RvLoadMoreAdapter.STATE_ALL_LOADED){
                                    mAdapter.setmLoadState(false,true,null);
                                } else
                                    mAdapter.setmLoadState(false,false,data);
                                mAdapter.setmIsLoading(false);
                            }
                        },2000);
                    }

                }

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItemPosition=mLayoutManager.findLastVisibleItemPosition();
            }
        });
        List<String> data = initData();
        mAdapter.setData(data);
    }

    public List<String> initData() {
        List<String> data = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            String str = "测试数据" + i;
            data.add(str);
        }
        return data;
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                List<String> data=initData();
                if(state==RvLoadMoreAdapter.STATE_ERROR_NET){
                    Toast.makeText(mContext,"网络错误",Toast.LENGTH_SHORT).show();
                } else  if(state==RvLoadMoreAdapter.STATE_ALL_LOADED){
                    Toast.makeText(mContext,"服务器上没有数据了",Toast.LENGTH_SHORT).show();
                } else{
                    mAdapter.setData(data);
                }

                mAdapter.setmIsLoading(false);
                mRefresh.setRefreshing(false);

            }
        },2000);
    }
}
