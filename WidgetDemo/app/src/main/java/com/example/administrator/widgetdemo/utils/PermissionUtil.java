package com.example.administrator.widgetdemo.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;

import com.tbruyelle.rxpermissions2.RxPermissions;

/**
 * Author:huangxiaoming
 * Date: 2018/4/23
 * Desc:
 * Version:1.0
 */
public class PermissionUtil {

    private static RxPermissions mRxPermissions;

    @SuppressLint("CheckResult")
    public static void request(Activity activity,IPermisssionListener permisssionListener, String ... permissions){
        if(mRxPermissions == null){
           mRxPermissions = new RxPermissions(activity);
        }
        mRxPermissions.request(permissions).subscribe(granted->{
            if(granted){
                permisssionListener.granted();
            }else{
                permisssionListener.refused();
            }
        });
    }

    public interface IPermisssionListener{
        void granted();
        void refused();
    }
}
