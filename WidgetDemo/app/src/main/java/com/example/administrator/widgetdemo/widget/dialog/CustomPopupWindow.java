package com.example.administrator.widgetdemo.widget.dialog;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.widget.PopupWindow;

/**
 * Author: huangxiaoming
 * Date: 2018/5/12
 * Desc: PopupWindow在Android7.0和7.1系统上显示位置不正确的问题解决
 * Version: 1.0
 */
public class CustomPopupWindow extends PopupWindow {

    public CustomPopupWindow(Context context) {
        super(context, null);
    }

    public CustomPopupWindow(View contentView, int width, int height) {
        super(contentView, width, height);
    }

    @Override
    public void showAsDropDown(View anchor) {
//        if (Build.VERSION.SDK_INT >= 24) {
//            Rect rect = new Rect();
//            anchor.getGlobalVisibleRect(rect);
//            int h = anchor.getResources().getDisplayMetrics().heightPixels - rect.bottom;
//            setHeight(h);
//        }
        super.showAsDropDown(anchor);
    }
}