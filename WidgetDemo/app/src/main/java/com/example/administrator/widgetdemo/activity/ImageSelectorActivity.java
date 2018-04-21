package com.example.administrator.widgetdemo.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.example.administrator.widgetdemo.R;
import com.example.administrator.widgetdemo.utils.FileUtil;
import com.example.administrator.widgetdemo.utils.LogUtil;
import com.example.administrator.widgetdemo.utils.ToastUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.util.List;

/**
 * Author:huangxiaoming
 * Date:2018/4/19
 * Desc:图片选择器
 * Version:1.0
 */
public class ImageSelectorActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_CHOOSE = 1;

    private Context mContext;
    private RxPermissions mRxPermissions;
    private ImageView mImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_selector);

        mContext = this;
        // where this is an Activity instance
        mRxPermissions = new RxPermissions(this);

        findViewById(R.id.btn_1).setOnClickListener(v -> askPermission(v));
        findViewById(R.id.btn_2).setOnClickListener(v -> askPermission(v));
        mImg = findViewById(R.id.iv_img);
    }


    @SuppressLint("CheckResult")
    private void askPermission(View v) {
        mRxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(granted -> {
            if (granted) {
                // All requested permissions are granted
                switch (v.getId()){
                    case R.id.btn_1:
                        Matisse.from(ImageSelectorActivity.this)
                                .choose(MimeType.of(MimeType.JPEG, MimeType.PNG, MimeType.GIF))
                                .theme(R.style.Matisse_Zhihu)
                                .countable(false)
                                .maxSelectable(9)
                                .imageEngine(new GlideEngine())
                                .forResult(REQUEST_CODE_CHOOSE);
                        break;
                    case R.id.btn_2:
                        Matisse.from(ImageSelectorActivity.this)
                                .choose(MimeType.ofVideo())
                                .theme(R.style.Matisse_Dracula)
                                .countable(true)
                                .capture(true)
                                .captureStrategy(new CaptureStrategy(true,"com.example.administrator.widgetdemo.fileProvider"))
                                .maxSelectable(9)
                                .imageEngine(new GlideEngine())
                                .forResult(REQUEST_CODE_CHOOSE);
                        break;}


            } else {
                // At least one permission is denied
                ToastUtil.show(mContext,"您怎么能拒绝这个权限呢？");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            List<Uri> uriList = Matisse.obtainResult(data);
            List<String> pathList = Matisse.obtainPathResult(data);

            mImg.setImageURI(uriList.get(0));

            LogUtil.defLog(uriList.toString());

            for (Uri uri : uriList) {
                 String filePath = FileUtil.getRealFilePath(mContext,uri);
                 LogUtil.defLog("uriList+"+filePath);
            }

            for (String path : pathList) {
                LogUtil.defLog("pathList+"+path);
            }

        }
    }
}
