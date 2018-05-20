package com.example.administrator.widgetdemo.adpater.viewholder;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.administrator.widgetdemo.R;
import com.example.administrator.widgetdemo.utils.LogUtil;
import com.example.administrator.widgetdemo.widget.recyclerview.CustomRecyclerView;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class LoadMoreFooter {

    /**
     * 不可用状态（STATE_DISABLED）：第一次下拉刷新还没成功的情况下，这时候是不能够加载更多的。
     */
    public static final int STATE_DISABLED = 0;
    /**
     * 加载中（STATE_LOADING）：正在请求数据中。
     */
    public static final int STATE_LOADING = 1;
    /**
     * 加载完了，没有更多数据啦（STATE_FINISHED）：已经加载到最后一页了，之后没有数据了，也就不在需要触发加载更多了。
     */
    public static final int STATE_FINISHED = 2;
    /**
     * 可以触发加载的预备状态（STATE_ENDLESS）：这时如果监听滚动到底部，则触发加载
     */
    public static final int STATE_ENDLESS = 3;
    /**
     * 加载失败了（STATE_FAILED）：网络错误或者啥错误，数据请求没成功。这里其实应该给一个类似“加载失败，点击重试”的文案，用户可以通过点击或者重新滚动到底部再次触发加载。
     */
    public static final int STATE_FAILED = 4;

    @IntDef({STATE_DISABLED, STATE_LOADING, STATE_FINISHED, STATE_ENDLESS, STATE_FAILED})
    @Retention(RetentionPolicy.SOURCE)
    public @interface State {}

    public interface OnLoadMoreListener {

        void onLoadMore();

    }

    ProgressBar progressBar;

    TextView tvText;

    @State
    private int state = STATE_DISABLED;

    private final OnLoadMoreListener loadMoreListener;

    public LoadMoreFooter(@NonNull Context context, @NonNull CustomRecyclerView recyclerView, @NonNull OnLoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
        View footerView = LayoutInflater.from(context).inflate(R.layout.item_first_footer, recyclerView.getFooterContainer(), false);
        recyclerView.addFooterView(footerView);
        progressBar = footerView.findViewById(R.id.progressBar);
        tvText = footerView.findViewById(R.id.tv_text);

        tvText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLoadMore();
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (!recyclerView.canScrollVertically(1)) {
                    checkLoadMore();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (!recyclerView.canScrollVertically(1)) {
                    checkLoadMore();
                }
            }

        });
    }

    @State
    public int getState() {
        return state;
    }

    public void setState(@State int state) {
        if (this.state != state) {
            this.state = state;
            switch (state) {
                case STATE_DISABLED:
                    progressBar.setVisibility(View.INVISIBLE);
                    tvText.setVisibility(View.INVISIBLE);
                    tvText.setText(null);
                    tvText.setClickable(false);
                    break;
                case STATE_LOADING:
                    progressBar.setVisibility(View.VISIBLE);
                    tvText.setVisibility(View.INVISIBLE);
                    tvText.setText(null);
                    tvText.setClickable(false);
                    break;
                case STATE_FINISHED:
                    progressBar.setVisibility(View.INVISIBLE);
                    tvText.setVisibility(View.VISIBLE);
                    tvText.setText("--- 我是有底线的 ---");
                    tvText.setClickable(false);
                    break;
                case STATE_ENDLESS:
                    progressBar.setVisibility(View.INVISIBLE);
                    tvText.setVisibility(View.VISIBLE);
                    tvText.setText(null);
                    tvText.setClickable(true);
                    break;
                case STATE_FAILED:
                    progressBar.setVisibility(View.INVISIBLE);
                    tvText.setVisibility(View.VISIBLE);
                    tvText.setText("load failed");
                    tvText.setClickable(true);
                    break;
                default:
                    throw new AssertionError("Unknown load more state.");
            }
        }
    }

    private void checkLoadMore() {
        LogUtil.defLog("当前的加载条状态："+getState());
        if (getState() == STATE_ENDLESS || getState() == STATE_FAILED) {
            setState(STATE_LOADING);
            loadMoreListener.onLoadMore();
        }
    }

}