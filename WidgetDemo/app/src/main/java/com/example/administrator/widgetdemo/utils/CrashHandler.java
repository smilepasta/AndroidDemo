package com.example.administrator.widgetdemo.utils;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Looper;
import android.widget.Toast;

import com.example.administrator.widgetdemo.activity.MainActivity;
import com.example.administrator.widgetdemo.R;
import com.example.administrator.widgetdemo.XMApp;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * Author:huangxiaoming
 * Date:2018/4/19
 * Desc:
 * Version:
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private Context mContext;
    private static CrashHandler mInstance = new CrashHandler();

    private CrashHandler() {
    }

    /**
     * 单例模式，保证只有一个CrashHandler实例存在
     *
     * @return
     */
    public static CrashHandler getInstance() {
        return mInstance;
    }

    /**
     * 异常发生时，系统回调的函数，我们在这里处理一些操作
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
//                saveCrashReport2SD(mContext, ex);
        //如果栈顶是入口activity（如MainActivity）,就重新启动app。如果不是，就不启动
        if (getTopActivity() == MainActivity.class) {
            showCrashToast(UIUtil.getString(R.string.tips_1));
            restartApp();
        } else {
            showCrashToast(UIUtil.getString(R.string.tips_2));
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }

    /**
     * 为我们的应用程序设置自定义Crash处理
     */
    public void initCrashHandler(Context context) {
        mContext = context;
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    private void restartApp() {
        Intent intent = new Intent(XMApp.getInstance(),
                MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent restartIntent = PendingIntent.getActivity(XMApp.getInstance()
                .getApplicationContext(), 0, intent, PendingIntent.FLAG_ONE_SHOT);
        //重启应用
        AlarmManager mgr = (AlarmManager) XMApp.getInstance().getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis(), restartIntent);

        //清空Activity栈,防止系统自动重启至崩溃页面,导致崩溃再次出现.
//            ActivityLifeManager.getInstance().finishAllActivity();
        //退出程序
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
        System.gc();
    }

    private void showCrashToast(final String val) {
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(XMApp.getInstance(), val,
                        Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }.start();
        try {
            Thread.sleep(1000);//Toast展示的时间
        } catch (InterruptedException e) {
        }
    }

    /**
     * 获取一些简单的信息,软件版本，手机版本，型号等信息存放在LinkedHashMap中
     *
     * @param context
     * @return
     */
    private HashMap<String, String> obtainSimpleInfo(Context context) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        PackageManager mPackageManager = context.getPackageManager();
        PackageInfo mPackageInfo = null;
        try {
            mPackageInfo = mPackageManager.getPackageInfo(context.getPackageName(),
                    PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        map.put("APP", "AppName");
        map.put("用户名", "xiaoming");
        map.put("品牌", "" + Build.BRAND);
        map.put("型号", "" + Build.MODEL);
        map.put("SDK版本", "" + Build.VERSION.SDK_INT);
        map.put("versionName", mPackageInfo.versionName);
        map.put("versionCode", "" + mPackageInfo.versionCode);
        map.put("crash时间", parserTime(System.currentTimeMillis()));

        return map;
    }


    /**
     * 获取系统未捕捉的错误信息
     *
     * @param throwable
     * @return
     */
    private String obtainExceptionInfo(Throwable throwable) {
        StringWriter mStringWriter = new StringWriter();
        PrintWriter mPrintWriter = new PrintWriter(mStringWriter);
        throwable.printStackTrace(mPrintWriter);
        mPrintWriter.close();

        LogUtil.defLog(mStringWriter.toString());
        return mStringWriter.toString();
    }

    /**
     * 保存获取的 软件信息，设备信息和出错信息保存在SDcard中
     *
     * @param context
     * @param ex
     * @return
     */
    private void saveCrashReport2SD(Context context, Throwable ex) {
//            String fileName = null;
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : obtainSimpleInfo(context).entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key).append(" = ").append(value).append("\n");
        }
        sb.append(obtainExceptionInfo(ex));
//            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//                File dir = new File(CrashConstant.CRASH_DIR);
//                if (!dir.exists()) {
//                    dir.mkdirs();
//                }
//                try {
//                    fileName = dir.toString() + File.separator + parserTime(System.currentTimeMillis()) +".txt";
//                    FileOutputStream fos = new FileOutputStream(fileName);
//                    fos.write(sb.toString().getBytes());
//                    fos.flush();
//                    fos.close();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//            return fileName;
        CacheUtil.get(XMApp.getInstance()).put("carsh", sb.toString());
//            LogUtil.defLog(CacheUtil.get(XMApp.getInstance()).getAsString("carsh"));
    }

    /**
     * 将毫秒数转换成yyyy-MM-dd-HH-mm-ss的格式，并在后缀加入随机数
     *
     * @param milliseconds
     * @return
     */
    private String parserTime(long milliseconds) {
        System.setProperty("user.timezone", "Asia/Shanghai");
        TimeZone tz = TimeZone.getTimeZone("Asia/Shanghai");
        TimeZone.setDefault(tz);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        return format.format(new Date(milliseconds));
    }

    /**
     * 获取栈顶的activity
     *
     * @return
     */
    public Class<?> getTopActivity() {
        ActivityManager manager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        String className = manager.getRunningTasks(1).get(0).topActivity.getClassName();
        Class<?> cls = null;
        try {
            cls = Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return cls;
    }
}
