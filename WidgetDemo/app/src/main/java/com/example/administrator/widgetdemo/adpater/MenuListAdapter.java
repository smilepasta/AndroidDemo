package com.example.administrator.widgetdemo.adpater;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.widgetdemo.R;
import com.example.administrator.widgetdemo.bean.MenuBean;
import com.example.administrator.widgetdemo.utils.StringUtil;
import com.example.administrator.widgetdemo.utils.UIUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:huangxiaoming
 * Date:2018/4/10
 * Desc: 菜单列表
 * Version:
 */
public class MenuListAdapter extends RecyclerView.Adapter<MenuListAdapter.MenuViewHolder> {

    private List<MenuBean> menuList = new ArrayList<>();
    private Context mContext;
    private OnItemClickListener onItemClickListener;

    public MenuListAdapter(Context context, List<MenuBean> menuList) {
        mContext = context;
        this.menuList = menuList;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public MenuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MenuViewHolder(View.inflate(mContext, R.layout.item_listview_popwin, null));
    }

    @Override
    public void onBindViewHolder(MenuViewHolder holder, int position) {
        MenuBean menu = menuList.get(position);
        if (menu != null) {
            if (menu.isChecked()) {
                holder.nameTextView.setTextColor(UIUtil.getColor(R.color.colorPrimary));
            } else {
                holder.nameTextView.setTextColor(UIUtil.getColor(R.color.blackSub));
            }
            if (StringUtil.isNotEmpty(menu.getName())) {
                holder.nameTextView.setText(menu.getName());
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(position);
                    }

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    public void setData(List<MenuBean> menuList) {
        this.menuList = menuList;
        notifyDataSetChanged();
    }

    class MenuViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;

        MenuViewHolder(View view) {
            super(view);
            nameTextView = view.findViewById(R.id.listview_popwind_tv);
        }
    }
}
