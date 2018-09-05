package com.example.administrator.widgetdemo.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.example.administrator.widgetdemo.IMyAidlInterface;

/**
 * Author: huangxiaoming
 * Date: 2018/7/18
 * Desc:
 * Version: 1.0
 */
public class MyService extends Service {

    public MyService() {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    class MyBinder extends IMyAidlInterface.Stub {

        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public String getName() throws RemoteException {
            return "手拿菜刀砍电线，一路火花带闪电";
        }
    }

}
