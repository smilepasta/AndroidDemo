package com.example.administrator.widgetdemo;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;
import com.example.administrator.widgetdemo.utils.CrashHandler;

/**
 * Author:huangxiaoming
 * Date:2018/3/29
 * Desc:app
 * Version:1.0
 */
public class XMApp extends Application {

    private static XMApp instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
//        CrashHandler.getInstance().initCrashHandler(this);

        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(this);
    }

    public static XMApp getInstance(){
        return instance;
    }

}