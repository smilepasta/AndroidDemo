package com.example.administrator.widgetdemo;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import java.util.HashSet;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;

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
        //全局的异常处理
//        CrashHandler.getInstance().initCrashHandler(this);

        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(this);

        //ZXing初始化
        ZXingLibrary.initDisplayOpinion(this);

        //jpush
        initJpush();
    }

    private void initJpush() {
        //初始化sdk
        JPushInterface.setDebugMode(true);//正式版的时候设置false，关闭调试
        JPushInterface.init(this);
        //建议添加tag标签，发送消息的之后就可以指定tag标签来发送了
        Set<String> set = new HashSet<>();
        set.add("tag1");//名字任意，可多添加几个
        JPushInterface.setTags(this, set, null);
        JPushInterface.setAlias(this, 0, "tag1");
    }

    public static XMApp getInstance() {
        return instance;
    }

}