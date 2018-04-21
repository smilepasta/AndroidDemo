package com.example.administrator.widgetdemo.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.administrator.widgetdemo.R;
import com.example.administrator.widgetdemo.utils.GlideUtil;
import com.example.administrator.widgetdemo.utils.QiniuAuth;
import com.example.administrator.widgetdemo.utils.FileUtil;
import com.example.administrator.widgetdemo.utils.StringUtil;
import com.example.administrator.widgetdemo.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:huangxiaoming
 * Date:2018/4/19
 * Desc:七牛图片上传
 * Version:1.0
 */
public class QiniuImageUploadActivity extends AppCompatActivity {


    public final static int REQUEST_CAMERA_IMAGE = 1;

    private String mUploadFilePath;
    private Context mContext;

    private ProgressBar mLoadBar;
    private ImageView mImg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qiniu_image_upload);
        mContext = this;
        findViewById(R.id.select_file).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectFile();
            }
        });

        findViewById(R.id.upload_file).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFile();
            }
        });
        mLoadBar = findViewById(R.id.pb_load_bar);
        mImg = findViewById(R.id.iv_show);
    }

    private void uploadFile() {
        if (StringUtil.isEmpty(mUploadFilePath)) {
            return;
        }
        mLoadBar.setVisibility(View.VISIBLE);
        final List<String> filePathList = new ArrayList<>();
        filePathList.add(mUploadFilePath);
        QiniuAuth.getInstence().init(new QiniuAuth.IQiniuListener() {

            @Override
            public void uploadImageSuccess(String url, int uploadSuccessCount) {
                if(filePathList.size() == uploadSuccessCount){
                    ToastUtil.show(mContext,"上传成功");
                    GlideUtil.loadImage(url,mImg);
                    mLoadBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void uploadImageFailed(String error) {
                if(StringUtil.isNotEmpty(error)){
                    ToastUtil.show(mContext,error);
                }
                mLoadBar.setVisibility(View.GONE);
            }
        },filePathList, (Activity) mContext);
    }

    private void selectFile() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CAMERA_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CAMERA_IMAGE:
                if (resultCode == RESULT_OK) {
                    if (data != null) {
                        Uri uri = data.getData();
                        mUploadFilePath = FileUtil.getRealFilePath(mContext, uri);
                    }
                }
                break;
        }
    }
}
