package com.example.administrator.widgetdemo.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.example.administrator.widgetdemo.R;
import com.example.administrator.widgetdemo.utils.LogUtil;
import com.example.administrator.widgetdemo.utils.PermissionUtil;
import com.example.administrator.widgetdemo.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:huangxiaoming
 * Date:2018/4/19
 * Desc:程序入口
 * Version:1.0
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_1).setOnClickListener(v -> startActivity(new Intent(MainActivity.this,BannerActivity.class)));

        findViewById(R.id.btn_2).setOnClickListener(v -> {
            ArrayList<String> imgList = new ArrayList<>();
            imgList.add("https://resources.matcha-jp.com/archive_files/jp/2016/09/what_is_sakura.jpg");
            imgList.add("https://fthmb.tqn.com/Sq6Kp6lndB0KWrXJWpJroTQECN0=/960x0/filters:no_upscale()/Cherry-Blossoms-Dennis-Govoni-56cb3b173df78cfb379b7b91.jpg");
            imgList.add("https://d1p2fuior9l0tb.cloudfront.net/wp-content/uploads/2018/03/11194616/Jefferson-Memorial-shutterstock_1872661851.jpg");
            imgList.add("https://cdn-images-1.medium.com/max/1200/1*qsdj8-E2ZmBSiZFQTDTzHw.jpeg");
            Intent intent =  new Intent(MainActivity.this,PhotoViewActivity.class);
            intent.putStringArrayListExtra("images",imgList);
            startActivity(intent);
        });

        findViewById(R.id.btn_3).setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this,WebViewActivity.class);
            intent.putExtra(WebViewActivity.LOAD_URL,"https://www.jianshu.com/");
            startActivity(intent);
        });

        findViewById(R.id.btn_4).setOnClickListener(v -> startActivity(new Intent(MainActivity.this,CrashHandlerActivity.class)));

        if (Build.VERSION.SDK_INT >= 23) {
            PermissionUtil.request(this, new PermissionUtil.IPermisssionListener() {
                @Override
                public void granted() {
                    ToastUtil.show(MainActivity.this,"granted");
                }

                @Override
                public void refused() {
                    ToastUtil.show(MainActivity.this,"您怎么能拒绝这个权限呢？");

                }
            }, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        findViewById(R.id.btn_5).setOnClickListener(view -> {
            throw new RuntimeException("I'm a cool exception and I crashed the main thread!");
        });
        findViewById(R.id.btn_6).setOnClickListener(v -> startActivity(new Intent(MainActivity.this,ListActivity.class)));
        findViewById(R.id.btn_7).setOnClickListener(v -> startActivity(new Intent(MainActivity.this,QiniuImageUploadActivity.class)));
        findViewById(R.id.btn_8).setOnClickListener(v -> startActivity(new Intent(MainActivity.this,ImageSelectorActivity.class)));
        findViewById(R.id.btn_9).setOnClickListener(v -> startActivity(new Intent(MainActivity.this,BaiduMapActivity.class)));
        findViewById(R.id.btn_10).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,QRCodeActivity.class));
            }
        });
        getLocationInfo();
    }

    /**
     * 获取手机的经纬度
     */
    private void getLocationInfo() {
        //获取当前的位置
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        List<String> providerList = locationManager.getProviders(true);
        String provider = "";
        if (providerList.contains(LocationManager.NETWORK_PROVIDER)) { //网络提供器
            provider = LocationManager.NETWORK_PROVIDER;
        } else if (provider.contains(LocationManager.GPS_PROVIDER)) { //GPS提供器
            provider = LocationManager.GPS_PROVIDER;
        } else {
            Toast.makeText(MainActivity.this, "No location provider to use",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        @SuppressLint("MissingPermission") Location location = locationManager.getLastKnownLocation(provider);
        LogUtil.defLog("GPS_TAG_LONGITUDE"+location.getLongitude());
        LogUtil.defLog("GPS_TAG_LATITUDE"+location.getLatitude());

        // 将GPS设备采集的原始GPS坐标转换成百度坐标
        CoordinateConverter converter  = new CoordinateConverter();
        converter.from(CoordinateConverter.CoordType.GPS);
        // sourceLatLng待转换坐标
        LatLng sourceLatLng = new LatLng(location.getLatitude(),location.getLongitude());
        converter.coord(sourceLatLng);
        LatLng desLatLng = converter.convert();
        LogUtil.defLog("BAIDU_TAG_LONGITUDE"+desLatLng.longitude);
        LogUtil.defLog("BAIDU_TAG_LATITUDE"+desLatLng.latitude);
    }
}
