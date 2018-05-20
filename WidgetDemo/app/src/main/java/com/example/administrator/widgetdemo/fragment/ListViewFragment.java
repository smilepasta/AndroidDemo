package com.example.administrator.widgetdemo.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.widgetdemo.R;
import com.example.administrator.widgetdemo.adpater.ZhihuAdapter;
import com.example.administrator.widgetdemo.adpater.viewholder.LoadMoreFooter;
import com.example.administrator.widgetdemo.bean.ZhihuNewsEntity;
import com.example.administrator.widgetdemo.utils.LogUtil;
import com.example.administrator.widgetdemo.widget.recyclerview.CustomRecyclerView;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Author: huangxiaoming
 * Date: 2018/5/2
 * Desc:
 * Version: 1.0
 */
public class ListViewFragment extends BasicFragment implements SwipeRefreshLayout.OnRefreshListener, LoadMoreFooter.OnLoadMoreListener {


    private ArrayList<ZhihuNewsEntity.StoriesBean> newsList = new ArrayList<>();

    private View view;

    private CustomRecyclerView mListView;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private LinearLayoutManager mLayoutManager;

    Context mContext;
    private ZhihuAdapter mZhihuAdapter;

    private LoadMoreFooter loadMoreFooter;

    String date = "20180502";
    private String url;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Override
    public void fetchData() {
        getFirstData();
    }

    public void getFirstData() {
        url = "http://news-at.zhihu.com/api/4/news/before/" + date;
        new Thread(new Runnable() {
            @Override
            public void run() {
                //初始化，在一个APP中建议只需要一个OkHttpClient实例，类似Applcation
                OkHttpClient client = new OkHttpClient();
                Request request = new Request
                        .Builder() //利用建造者模式创建Request对象
                        .url(url) //设置请求的URL
                        .build(); //生成Request对象

                Response response = null;
                try {
                    //将请求添加到请求队列等待执行，并返回执行后的Response对象
                    response = client.newCall(request).execute();
                    //获取Http Status Code.其中200表示成功
                    if (response.code() == 200) {
                        //这里需要注意，response.body().string()是获取返回的结果，此句话只能调用一次，再次调用获得不到结果。
                        //所以先将结果使用result变量接收
                        String result = response.body().string();
                        ZhihuNewsEntity zhihuNewsEntity = new Gson().fromJson(result, ZhihuNewsEntity.class);
                        date = zhihuNewsEntity.getDate();
                        newsList = zhihuNewsEntity.getStories();
                        LogUtil.defLog(zhihuNewsEntity.toString());
                        ((Activity) mContext).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mZhihuAdapter.addItems(newsList);

                                isDataInitiated = true;
                                if (mSwipeRefreshLayout.isRefreshing()) {
                                    mSwipeRefreshLayout.setRefreshing(false);
                                }

                                loadMoreFooter.setState(mZhihuAdapter.getItemCount() >= 200 ? LoadMoreFooter.STATE_FINISHED : LoadMoreFooter.STATE_ENDLESS);
                            }
                        });

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    loadMoreFooter.setState(LoadMoreFooter.STATE_FAILED); // 加载失败了给错误状态
                } finally {
                    if (response != null) {
                        response.body().close();
                    }
                }
            }
        }).start();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_list, container, false);
        initView();
        return view;
    }

    private void initView() {

        mListView = (CustomRecyclerView) view.findViewById(R.id.recycler_view);
        mListView.setLayoutManager(new LinearLayoutManager(mContext));
        mListView.setItemAnimator(new DefaultItemAnimator());
        mZhihuAdapter = new ZhihuAdapter(mContext);
        mListView.setAdapter(mZhihuAdapter);

        loadMoreFooter = new LoadMoreFooter(mContext, mListView, this);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeLayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                getSecondData();
            }
        }, 1000);

    }

    @Override
    public void onLoadMore() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getSecondData();
            }
        }, 1000);
    }

    public  void getSecondData() {
        url = "http://news-at.zhihu.com/api/4/news/before/" + date;
        new Thread(new Runnable() {
            @Override
            public void run() {
                //初始化，在一个APP中建议只需要一个OkHttpClient实例，类似Applcation
                OkHttpClient client = new OkHttpClient();
                Request request = new Request
                        .Builder() //利用建造者模式创建Request对象
                        .url(url) //设置请求的URL
                        .build(); //生成Request对象

                Response response = null;
                try {
                    //将请求添加到请求队列等待执行，并返回执行后的Response对象
                    response = client.newCall(request).execute();
                    //获取Http Status Code.其中200表示成功
                    if (response.code() == 200) {
                        //这里需要注意，response.body().string()是获取返回的结果，此句话只能调用一次，再次调用获得不到结果。
                        //所以先将结果使用result变量接收
                        String result = response.body().string();
                        ZhihuNewsEntity zhihuNewsEntity = new Gson().fromJson(result, ZhihuNewsEntity.class);
                        date = zhihuNewsEntity.getDate();
                        newsList = zhihuNewsEntity.getStories();
                        LogUtil.defLog(zhihuNewsEntity.toString());
                        ((Activity) mContext).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(mSwipeRefreshLayout.isRefreshing()){
                                    mSwipeRefreshLayout.setRefreshing(false);
                                    mZhihuAdapter.clearData();
                                }
                                mZhihuAdapter.addItems(newsList);
                                loadMoreFooter.setState(mZhihuAdapter.getItemCount() >= 200 ? LoadMoreFooter.STATE_FINISHED : LoadMoreFooter.STATE_ENDLESS);
                            }
                        });

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    loadMoreFooter.setState(LoadMoreFooter.STATE_FAILED); // 加载失败了给错误状态
                } finally {
                    if (response != null) {
                        response.body().close();
                    }
                }
            }
        }).start();
    }
}