package com.example.administrator.widgetdemo.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.administrator.widgetdemo.BuildConfig;
import com.example.administrator.widgetdemo.Constant;
import com.example.administrator.widgetdemo.R;
import com.example.administrator.widgetdemo.bean.VersionEntity;
import com.example.administrator.widgetdemo.provider.GenericFileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @创建世间 2012-5-11下午5:28:56
 * @类说明 检查软件升级
 */
public class ApkVersionUtil {
    private Context context;
    private int version;// 版本号
    private VersionEntity versionEntity;
    private static final int UPDATEING = 1;// 下载的标记
    private static final int UPDATE_FINISH = 2;// 下载完成的标记
    private static final int UPDATE_FIAL = 3;// 下载完成的标记
    // 下载文件的存放目录
    private static final String savePath = android.os.Environment
            .getExternalStorageDirectory().getPath() + Constant.DOWNLOAD_PATH;
    /* 进度条与通知ui刷新的handler和msg常量 */
    private ProgressBar progressBar;
    private TextView titleView, progressView;
    private Dialog downloadDialog;// 下载对话框
    private int progress;// 下载的数量
    private Thread downLoadThread;// 下载的Thread
    private boolean interceptFlag = false;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case UPDATEING:
                    progressView.setText(progress + "%");
                    progressBar.setProgress(progress);
                    break;
                case UPDATE_FINISH:
                    downloadDialog.dismiss();
                    ToastUtil.show(context, "下载完成");
                    installAPK();
                    break;
                case UPDATE_FIAL:
                    downloadDialog.dismiss();
                    ToastUtil.show(context, "下载失败");
                    break;

                default:
                    break;
            }
        }

        ;
    };

    public ApkVersionUtil(Activity context, VersionEntity entity) {
        this.versionEntity = entity;
        this.context = context;
    }

    public void checkUpdate(Activity context, VersionEntity entity, boolean isShowToast) {
//        if (null == entity) return;
//        Logger.i(entity.toString());
//        versionEntity = entity;
//        version = NetUtils.getVersion();
//        try {
//            if (Integer.parseInt(entity.getVersion()) > version) {
////                Toast.makeText(context, "发现新版本！", Toast.LENGTH_SHORT).show();
//                DialogUtil.query(context, UIUtils.getString(R.string.setting_version_upgrade), UIUtils.getString(R.string.find_new_version_updated), new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        downloadApk();
//                        DialogUtil.cancel();
//                    }
//                });
//            } else {
//                if (isShowToast) {
//                    Toast.makeText(context, UIUtils.getString(R.string.version_upgrade_is_best), Toast.LENGTH_SHORT).show();
//                }
//            }
//        } catch (Exception e) {
//        }

    }

    private void showDowloadProgress() {
        downloadDialog = new Dialog(context, R.style.MyDialog);
        View view = LayoutInflater.from(context).inflate(
                R.layout.download_progress, null);
        progressBar = (ProgressBar) view.findViewById(R.id.loading_progress);
        titleView = (TextView) view.findViewById(R.id.dialog_title);
        progressView = (TextView) view.findViewById(R.id.progress);

        titleView.setText("更新应用版本：" + versionEntity.getVersionName());

        Window dialogWindow = downloadDialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        downloadDialog.setCanceledOnTouchOutside(false);
        downloadDialog.setContentView(view);
        downloadDialog.setCancelable(false);
        downloadDialog.show();
    }

    // 下载apk的Runnable
    private Runnable downRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                URL url = new URL(versionEntity.getUrl());
                Request request = new Request.Builder().url(url).build();
                OkHttpClient okHttpClient = new OkHttpClient();
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        handler.sendEmptyMessage(UPDATE_FIAL);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        InputStream is = response.body().byteStream();
                        long length = response.body().contentLength();
                        File file = new File(savePath);
                        //android创建目录有先创建父目录，再创建子目录（默认只能创建一层目录）
                        if (!file.exists()) {
                            file.mkdir();
                        }
                        String apkFile = savePath + "/" + getUrlApk(versionEntity.getUrl());
                        File ApkFile = new File(apkFile);
                        // 判断文件是否存在，不存在则创建
                        if (!ApkFile.exists()) {
                            ApkFile.createNewFile();
                        }
                        FileOutputStream fos = new FileOutputStream(ApkFile);
                        int count = 0;
                        byte buf[] = new byte[64];

                        do {
                            int numread = is.read(buf);
                            count += numread;
                            progress = (int) (((float) count / length) * 100);
                            // 更新进度
                            handler.sendEmptyMessage(UPDATEING);
                            if (numread <= 0) {
                                // 下载完成通知安装
                                handler.sendEmptyMessage(UPDATE_FINISH);
                                break;
                            }
                            fos.write(buf, 0, numread);
                        } while (!interceptFlag);// 点击取消就停止下载.
                        fos.close();
                        is.close();
                    }
                });

            } catch (MalformedURLException e) {
                LogUtil.defLog(e.toString());
                handler.sendEmptyMessage(UPDATE_FIAL);
            }
        }
    };


    public void downloadApk() {
        showDowloadProgress();
        downLoadThread = new Thread(downRunnable);
        downLoadThread.start();
    }

    private void installAPK() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        File apkFile = new File(savePath + "/" + getUrlApk(versionEntity.getUrl()));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = GenericFileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", apkFile);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    public static String getUrlApk(String path) {
        String name = path.substring(path.lastIndexOf("/") + 1);
        return name;
    }

    public static int getAppVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }
}
