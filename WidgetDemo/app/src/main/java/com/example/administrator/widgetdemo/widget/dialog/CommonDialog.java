package com.example.administrator.widgetdemo.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.administrator.widgetdemo.R;

/**
 * Author:huangxiaoming
 * Date:2018/4/10
 * Desc:
 * Version:
 */
public class CommonDialog {

    private static CommonDialog commonDialog = null;
    private MaterialDialog materialDialog;
    private Dialog loadingDialog;

    private CommonDialog() {
    }

    public static CommonDialog getInstance() {
        if (commonDialog == null) {
            synchronized (CommonDialog.class) {
                if (commonDialog == null) {
                    commonDialog = new CommonDialog();
                }
            }
        }
        return commonDialog;
    }

    public void buildDialog(Activity activity, String content) {
//        if (null != materialDialog) materialDialog = null;
//        materialDialog = new MaterialDialog.Builder(activity)
//                .content(content)
//                .show();
        buildDialog(activity, null, content, null, null, null);
    }

    public void buildDialog(Activity activity, String title, String content, String negativeText, String positiveText, IOnPositionClickListener onPositionClickListener) {
        if (null != materialDialog) materialDialog = null;
        MaterialDialog.Builder builder = new MaterialDialog.Builder(activity);
        if (!TextUtils.isEmpty(title)) {
            builder.title(title);
        }
        if (!TextUtils.isEmpty(content)) {
            builder.content(content);
        }
        if (!TextUtils.isEmpty(positiveText)) {
            builder.positiveText(positiveText);
            builder.onPositive(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    onPositionClickListener.onPositiveClick();
                }
            });
        }
        if (!TextUtils.isEmpty(negativeText)) {
            builder.negativeText(negativeText);
            builder.onNegative(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    onPositionClickListener.onNegativeClick();
                }
            });
        }
        materialDialog = builder.show();
    }

    public void buildHorizontalDialog(Activity activity) {
        new MaterialDialog.Builder(activity)
                .title("更新应用")
                .content("下载中……")
                .contentGravity(GravityEnum.CENTER)
                .progress(false, 100, true)
                .showListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialogInterface) {
                        final MaterialDialog dialog = (MaterialDialog) dialogInterface;
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                while (dialog.getCurrentProgress() != dialog.getMaxProgress()
                                        && !Thread.currentThread().isInterrupted()) {
                                    if (dialog.isCancelled()) {
                                        break;
                                    }
                                    try {
                                        Thread.sleep(50);
                                    } catch (InterruptedException e) {
                                        break;
                                    }
                                    dialog.incrementProgress(1);
                                }
                                activity.runOnUiThread(
                                        () -> {
                                            dialog.setContent("下载完成");
                                        });
                            }
                        }).start();
                    }
                })
                .show();
    }

    /**
     * 点击事件监听
     */
    public interface IOnPositionClickListener {
        void onPositiveClick();

        void onNegativeClick();
    }


    public void dimissDialog() {
        if (null != materialDialog && materialDialog.isShowing()) {
            materialDialog.dismiss();
            materialDialog = null;
        }
        if (null != loadingDialog && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
            loadingDialog = null;
        }
    }

    public Dialog bulidLoadingDialog(Activity activity) {
        return bulidLoadingDialog(activity, null);
    }

    public Dialog bulidLoadingDialog(Activity activity, String content) {
        if (null != loadingDialog) loadingDialog = null;
        if (!TextUtils.isEmpty(content)) {
            content = activity.getString(R.string.loading);
        }
        loadingDialog = new MaterialDialog.Builder(activity)
                .progress(true, 0)
                .content(content)
                .progressIndeterminateStyle(false)
                .show();
        return loadingDialog;
    }

    public class DialogType {
        public static final int DIALOG_PROGRESS_BLACK = 10010;
        public static final int DIALOG_PROGRESS_WHITE = 10011;
    }

}
