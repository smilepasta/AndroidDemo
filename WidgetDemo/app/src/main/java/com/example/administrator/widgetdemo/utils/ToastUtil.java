package com.example.administrator.widgetdemo.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Author:huangxiaoming
 * Date:2018/4/10 0010
 * Desc:
 * Version:
 */
public class ToastUtil {
    private ToastUtil() {
        throw new AssertionError();
    }

    private static Toast toast;

    public static void show(Context context, int resId) {
        show(context, context.getResources().getText(resId), Toast.LENGTH_SHORT);
    }

    public static void show(Context context, int resId, int duration) {
        show(context, context.getResources().getText(resId), duration);
    }

    public static void show(Context context, CharSequence text) {
        show(context, text, Toast.LENGTH_SHORT);
    }

    public static void show(Context context, CharSequence text, int duration) {
        if(toast == null ) {
            toast = Toast.makeText(context,text, duration);
            toast.setGravity(Gravity.CENTER, 0, 0);
        }else{
            toast.setText(text);
            toast.setDuration(duration);
        }
        toast.show();
    }


    public static void show(Context context, int resId, Object... args) {
        show(context, String.format(context.getResources().getString(resId), args), Toast.LENGTH_SHORT);
    }

    public static void show(Context context, String format, Object... args) {
        show(context, String.format(format, args), Toast.LENGTH_SHORT);
    }

    public static void show(Context context, int resId, int duration, Object... args) {
        show(context, String.format(context.getResources().getString(resId), args), duration);
    }

    public static void show(Context context, String format, int duration, Object... args) {
        show(context, String.format(format, args), duration);
    }
}
