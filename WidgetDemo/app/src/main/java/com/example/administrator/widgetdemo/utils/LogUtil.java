package com.example.administrator.widgetdemo.utils;

import android.util.Log;

import com.example.administrator.widgetdemo.BuildConfig;

/**
 * Author:huangxiaoming
 * Date:2018/4/3 0003
 * Desc:
 * Version:
 */
public class LogUtil {
    public static String APP_TAG = "===Xiaoming===";
	public static boolean DEBUG  = BuildConfig.DEBUG;

    public static void defLog(String content) {
        if (LogUtil.DEBUG) {
            String log = getTraceInfo() + "  :  " + content;
            Log.i(LogUtil.APP_TAG, log);
        }
    }

    public static void printI(String tag, String content) {
        if (LogUtil.DEBUG ) {
            String log = getTraceInfo() + "  :  " + content;
            Log.i(tag, log);
        }
    }

    public static void printE(String tag, String content) {
        if (LogUtil.DEBUG ) {
            String log = getTraceInfo() + "  :  " + content;
            Log.e(tag, log);
        }
    }

    public static void printD(String tag, String content) {
        if (LogUtil.DEBUG ) {
            String log = getTraceInfo() + "  :  " + content;
            Log.d(tag, log);
        }
    }

    public static void printV(String tag, String content) {
        if (LogUtil.DEBUG ) {
            String log = getTraceInfo() + "  :  " + content;
            Log.v(tag, log);
        }
    }

    public static void syso(String content) {
        if (LogUtil.DEBUG ) {
            String log = getTraceInfo() + "  :  " + content;
            System.out.println(log);
        }
    }

    /**
     * 获取堆栈信息
     */
    private static String getTraceInfo() {
        StringBuffer sb = new StringBuffer();
        StackTraceElement[] stacks = new Throwable().getStackTrace();
        String className = stacks[2].getClassName();
        int index = className.lastIndexOf('.');
        if (index >= 0) {
            className = className.substring(index + 1, className.length());
        }
        String methodName = stacks[2].getMethodName();
        int lineNumber = stacks[2].getLineNumber();
        sb.append(className).append("->").append(methodName).append("()->").append(lineNumber);
        return sb.toString();
    }
}
