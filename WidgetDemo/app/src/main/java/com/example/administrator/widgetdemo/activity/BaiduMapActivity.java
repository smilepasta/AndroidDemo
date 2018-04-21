package com.example.administrator.widgetdemo.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.cloud.CloudListener;
import com.baidu.mapapi.cloud.CloudManager;
import com.baidu.mapapi.cloud.CloudPoiInfo;
import com.baidu.mapapi.cloud.CloudRgcResult;
import com.baidu.mapapi.cloud.CloudSearchResult;
import com.baidu.mapapi.cloud.DetailSearchResult;
import com.baidu.mapapi.cloud.NearbySearchInfo;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.example.administrator.widgetdemo.R;
import com.example.administrator.widgetdemo.utils.LogUtil;
import com.example.administrator.widgetdemo.utils.ToastUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Author:huangxiaoming
 * Date: 2018/4/19
 * Desc:
 * Version:1.0
 */
public class BaiduMapActivity extends AppCompatActivity{
    private MapView mMapView;
    private BaiduMap mBaiduMap;

    private Context mContext;

    BaiduMap.OnMarkerClickListener mMarkerClickListener = marker -> {
        Bundle bundle =  marker.getExtraInfo();
        ToastUtil.show(mContext,bundle.getString("address"));
        return true;
    };

    private RxPermissions mRxPermissions;
    private LocationClient mLocationClient;
    private MbdLocationListener mbdLocationListener = new MbdLocationListener();

    private void initView() {
        mContext = this;
        setTitle("百度地图");
        mMapView = (MapView) findViewById(R.id.mv_bitmap);
        mBaiduMap = mMapView.getMap();
    }

    @SuppressLint("CheckResult")
    private void initData() {
        mRxPermissions = new RxPermissions(this);
        mRxPermissions.request(Manifest.permission.ACCESS_FINE_LOCATION).subscribe(granted->{
           if(granted){
               doLocation();
           }else{
               ToastUtil.show(mContext,"您怎么能拒绝这个权限呢？");
           }
        });
    }

    /**
     * 获取当前坐标
     */
    private void doLocation() {
        mLocationClient = new LocationClient(this);

        mLocationClient.registerLocationListener(mbdLocationListener);
        //初始化定位
        initLocation();
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
        option.setIsNeedAddress(true);// 位置，一定要设置，否则后面得不到地址
        option.setOpenGps(true);// 打开GPS
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 高精度
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }

    private void getSingleSupplyLocation(double la,double lo) {
        // 开启定位功能
        mBaiduMap.setMyLocationEnabled(true);
        // 构造定位数据
        MyLocationData locationData = new MyLocationData.Builder()
                .latitude(la)
                .longitude(lo)
                .build();
        // 设置定位数据
        mBaiduMap.setMyLocationData(locationData);
        // 自定以图表
        BitmapDescriptor marker = BitmapDescriptorFactory
                .fromResource(R.mipmap.ic_location_red);
        // 设置定位图层的配置，设置图标跟随状态（图标一直在地图中心）
        MyLocationConfiguration config = new MyLocationConfiguration(
                MyLocationConfiguration.LocationMode.FOLLOWING, true, marker);
        mBaiduMap.setMyLocationConfiguration(config);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.unRegisterLocationListener(mbdLocationListener);
        mLocationClient = null;
        mMapView.onDestroy();
        CloudManager.getInstance().destroy();

    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    private class MbdLocationListener extends BDAbstractLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            LogUtil.defLog("onReceiveLocation");
            if(bdLocation != null){
                getSingleSupplyLocation(bdLocation.getLatitude(),bdLocation.getLongitude());
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baidu_map);
        initView();
        initData();
    }

}
