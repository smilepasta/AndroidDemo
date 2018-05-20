package com.example.administrator.widgetdemo.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Author: huangxiaoming
 * Date: 2018/5/2
 * Desc:
 * Version: 1.0
 */
abstract class BasicFragment extends Fragment {
    /**
     * Fragment是否已经绑定
     */
    protected boolean isViewInitiated;
    /**
     * 用户是否可见
     */
    protected boolean isVisibleToUser;
    /**
     * 是否绑定数据
     */
    protected boolean isDataInitiated;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isViewInitiated = true;
        prepareFetchData();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        prepareFetchData();
    }

    /**
     * 是时候绑定数据了
     */
    public abstract void fetchData();

    /**
     * 是时候准备数据了
     *
     * @return
     */
    public boolean prepareFetchData() {
        return prepareFetchData(false);
    }

    public boolean prepareFetchData(boolean forceUpdate) {
        if (isVisibleToUser && isViewInitiated && (!isDataInitiated || forceUpdate)) {
            fetchData();
            isDataInitiated = true;
            return true;
        }
        return false;
    }
}