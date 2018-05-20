package com.example.administrator.widgetdemo.adpater;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.widgetdemo.R;
import com.example.administrator.widgetdemo.bean.ZhihuNewsEntity;
import com.example.administrator.widgetdemo.utils.GlideUtil;
import com.example.administrator.widgetdemo.utils.StringUtil;
import com.example.administrator.widgetdemo.utils.ToastUtil;

import java.util.ArrayList;


public class ZhihuAdapter extends RecyclerView.Adapter<ZhihuAdapter.NewsViewHolder> {

    private ArrayList<ZhihuNewsEntity.StoriesBean> zhihuDailyItems = new ArrayList<>();
    private Context mContext;

    public ZhihuAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NewsViewHolder(LayoutInflater.from(mContext).inflate(R.layout.tab_list_item, parent, false));
    }


    @Override
    public void onBindViewHolder(final NewsViewHolder holder, int position) {
        ZhihuNewsEntity.StoriesBean zhihuDailyItem = zhihuDailyItems.get(holder.getAdapterPosition());
        if (zhihuDailyItem != null) {
            if (zhihuDailyItem.getImages() != null
                    && zhihuDailyItem.getImages().size() > 0
                    && StringUtil.isNotEmpty(zhihuDailyItem.getImages().get(0)))
                GlideUtil.loadImage(zhihuDailyItem.getImages().get(0), holder.iv);
            if (StringUtil.isNotEmpty(zhihuDailyItem.getTitle())) {
                holder.tv.setText(zhihuDailyItem.getTitle());
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtil.show(mContext, zhihuDailyItem.getTitle());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return zhihuDailyItems.size();
    }


    public void addItems(ArrayList<ZhihuNewsEntity.StoriesBean> list) {
        zhihuDailyItems.addAll(list);
        int startPosition = getItemCount();
        notifyItemRangeInserted(startPosition,zhihuDailyItems.size());
    }

    public void clearData(){
        zhihuDailyItems.clear();
        notifyDataSetChanged();
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
}
