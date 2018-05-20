package com.example.administrator.widgetdemo.adpater;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.widgetdemo.R;
import com.example.administrator.widgetdemo.bean.ZhihuNewsEntity;
import com.example.administrator.widgetdemo.utils.GlideUtil;
import com.example.administrator.widgetdemo.utils.LogUtil;
import com.example.administrator.widgetdemo.utils.StringUtil;
import com.example.administrator.widgetdemo.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: huangxiaoming
 * Date: 2018/5/2
 * Desc: 底部
 * Version: 1.0
 */
public class RvLoadMoreAdapter2 extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /**
     * 普通项和底部
     */
    private final int TYPE_ITEM = 1;
    private final int TYPE_FOOTER = 2;

    /**
     * 当前加载状态
     */
    private int mLoadState = 1;

    /**
     * 是否在加载中，加载中的话，屏蔽上拉加载事件
     */
    private boolean mIsLoading;

    /**
     * 底部上拉加载显示的View
     */
    private View mFootView;

    private OnListener mOnListener;

    private List<ZhihuNewsEntity.StoriesBean> mData;

    private Context mContext;
    private NewsViewHolder newsViewHolder;


    /**
     * 是否加载中
     */
    public boolean ismIsLoading() {
        return mIsLoading;
    }

    public void setmIsLoading(boolean mIsLoading) {
        this.mIsLoading = mIsLoading;
    }

    /**
     * 修改加载View的状态
     */
    public int getLoadState() {
        return mLoadState;
    }

    public void setmLoadState(boolean isError, boolean isAllLoaded, List<ZhihuNewsEntity.StoriesBean> data) {
        if (data == null) {
            notifyItemChanged(mData.size());
        } else {
            addData(data);
            notifyDataSetChanged();
        }
    }

    /**
     * 控制显示 上拉加载View
     */
    public void showFooter() {
        mFootView.setVisibility(View.VISIBLE);
    }

    public void initState() {
        if (mFootView != null) {
            mFootView.setVisibility(View.GONE);
        }
        notifyDataSetChanged();
    }

    /**
     * 事件
     */
    public void setListener(OnListener onListener) {
        mOnListener = onListener;
    }

    public RvLoadMoreAdapter2(Context context) {
        mContext = context;
        mData = new ArrayList<>();
    }


    public void setData(List<ZhihuNewsEntity.StoriesBean> data) {
        mData.clear();
        addData(data);
        // 初始化底部view的状态
        initState();
    }

    public void addData(List<ZhihuNewsEntity.StoriesBean> data) {
        mData.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) { // 到了底部
            LogUtil.defLog("我到了底部");
            return TYPE_FOOTER;
        } else {
            LogUtil.defLog("我一直在下潜");
            return TYPE_ITEM;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            // 普通item
            View view = LayoutInflater.from(mContext).inflate(R.layout.tab_list_item, parent, false);
            return new NewsViewHolder(view);
        } else {
            // 底部
            mFootView = LayoutInflater.from(mContext).inflate(R.layout.item_first_footer, parent, false);
            return new LoadMoreViewHolder(mFootView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_ITEM) {
            newsViewHolder = (NewsViewHolder) holder;

            ZhihuNewsEntity.StoriesBean storiesBean = mData.get(position);
            if (storiesBean != null) {
                if (storiesBean.getImages() != null
                        && storiesBean.getImages().size() > 0
                        && StringUtil.isNotEmpty(storiesBean.getImages().get(0)))
                    GlideUtil.loadImage(storiesBean.getImages().get(0), newsViewHolder.iv);
                if (StringUtil.isNotEmpty(storiesBean.getTitle())) {
                    newsViewHolder.tv.setText(storiesBean.getTitle());
                }
                newsViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastUtil.show(mContext, storiesBean.getTitle());
                    }
                });
            }
        } else {
            // 底部
        }
    }


    @Override
    public int getItemCount() {
        return mData.size() != 0 ? mData.size() + 1 : 0;
    }

    class NewsViewHolder extends RecyclerView.ViewHolder {

        ImageView iv;
        TextView tv;

        public NewsViewHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.tv_txt);
            iv = (ImageView) itemView.findViewById(R.id.iv_img);
        }
    }

    class LoadMoreViewHolder extends RecyclerView.ViewHolder {
        LinearLayout mLoadMore;

        public LoadMoreViewHolder(View itemView) {
            super(itemView);
            mLoadMore = (LinearLayout) itemView.findViewById(R.id.ll_load);
        }
    }

    // item点击事件和底部重新加载点击事件
    public interface OnListener {
        void onItemClick();

        void onReLoad();
    }
}
