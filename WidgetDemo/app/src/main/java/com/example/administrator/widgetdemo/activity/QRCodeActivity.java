package com.example.administrator.widgetdemo.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.RadioButton;

import com.example.administrator.widgetdemo.R;
import com.example.administrator.widgetdemo.utils.PermissionUtil;
import com.example.administrator.widgetdemo.utils.StringUtil;
import com.example.administrator.widgetdemo.utils.ToastUtil;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

/**
 * Author: huangxiaoming
 * Date: 2018/4/23
 * Desc: 二维码扫描
 * Version: 1.0
 */
public class QRCodeActivity extends AppCompatActivity{

    private final static int QRCODE_REQUEST_CODE = 1;

    private AppCompatEditText mQRCodeVal;
    private AppCompatImageView mQRCodeImg;
    private RadioButton mLogoRadio;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        mQRCodeVal = findViewById(R.id.et_qrcode_val);
        mQRCodeImg = findViewById(R.id.iv_qrcode);

        mLogoRadio = findViewById(R.id.rb_logo);

        findViewById(R.id.btn_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askPermission(v);
            }
        });
        findViewById(R.id.btn_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawableQRCodeImage();
            }
        });
    }

    private void drawableQRCodeImage() {
        String textContent = mQRCodeVal.getText().toString();
        if (StringUtil.isEmpty(textContent)) {
            ToastUtil.show(this,"你输入的数据为空，请重新输入");
            return;
        }
        mQRCodeVal.setText("");
        //判断是否设置logo
        Bitmap mBitmap = CodeUtils.createImage(textContent
                , 560
                , 560
                , mLogoRadio.isChecked()
                        ?BitmapFactory.decodeResource(getResources(), R.mipmap.ic_location_red)
                        :null
        );
        mQRCodeImg.setImageBitmap(mBitmap);
    }

    private void askPermission(View v) {
        PermissionUtil.request(this, new PermissionUtil.IPermisssionListener() {
            @Override
            public void granted() {
                Intent intent = new Intent(QRCodeActivity.this, CaptureActivity.class);
                startActivityForResult(intent, QRCODE_REQUEST_CODE);
            }

            @Override
            public void refused() {
                ToastUtil.show(QRCodeActivity.this,"refused");
            }
        }, Manifest.permission.CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == QRCODE_REQUEST_CODE) {
            //处理扫描结果（在界面上显示）
            if (data != null) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    ToastUtil.show(this, "解析结果:" + result);
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    ToastUtil.show(this, "解析二维码失败");
                }
            }
        }
    }
}
