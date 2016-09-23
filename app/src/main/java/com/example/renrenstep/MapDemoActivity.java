package com.example.renrenstep;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import adapter.LocationsAdapter;
import tools.FileUtils;

/**
 * Created by admin on 2016/3/7.
 */
public class MapDemoActivity extends Activity implements View.OnClickListener,OnGetGeoCoderResultListener,AdapterView.OnItemClickListener{

    private MapView mapView;
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();
    private BaiduMap mBaiduMap;
    private BitmapDescriptor mCurrentMarker;
    private Boolean isFirstLoc = true;
    private LinearLayout ll_cancel,ll_send;
    private ListView mListView;
    private List mInfoList;
    private GeoCoder mGeoCoder;
    private LocationsAdapter mAdapter;
    private PoiInfo mPoiInfo;
    private View loadingLayout;
    private FileUtils fileUtils;
    private Point mCenterPoint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_map_demo);
        initView();
        initLocationInfo();
        mLocationClient.start();
    }

    private void initView() {
        mapView = (MapView) findViewById(R.id.mapView);
        ll_cancel = (LinearLayout) findViewById(R.id.ll_cancel);
        ll_cancel.setOnClickListener(this);
        ll_send = (LinearLayout) findViewById(R.id.ll_send);
        ll_send.setOnClickListener(this);
        mListView = (ListView) findViewById(R.id.locationList);
        loadingLayout = findViewById(R.id.loadingLayout);
        mInfoList = new ArrayList<PoiInfo>();

        // 地理编码
        mGeoCoder = GeoCoder.newInstance();
        mGeoCoder.setOnGetGeoCodeResultListener(this);

        mAdapter = new LocationsAdapter(getApplicationContext(),mInfoList);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);

        fileUtils = new FileUtils("renrenstep");
    }


    private void initLocationInfo() {

        mBaiduMap = mapView.getMap();
        mCenterPoint = mBaiduMap.getMapStatus().targetScreen;
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        mapView.showZoomControls(false);
        mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);    //注册监听函数

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span=0;
        option.setScanSpan(span);
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        option.setLocationNotify(true);
        option.setIsNeedLocationDescribe(true);
        option.setIsNeedLocationPoiList(true);
        option.setIgnoreKillProcess(false);
        option.SetIgnoreCacheException(false);
        option.setEnableSimulateGps(false);
        mLocationClient.setLocOption(option);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mapView.onPause();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_cancel:
                onBackPressed();
                break;
            case R.id.ll_send:
                Log.e("MapDemoActivity","send location button clicked");
               //截图
                mBaiduMap.snapshot(callback);
                break;
        }
    }


        BaiduMap.SnapshotReadyCallback callback = new BaiduMap.SnapshotReadyCallback() {
            /**
             * 地图截屏回调接口
             * @param snapshot 截屏返回的 bitmap 数据
             */
            public void onSnapshotReady(Bitmap snapshot){


                Log.e("snapshotcallback", "onSnapShotReady--" + String.valueOf(snapshot.getByteCount()));

                String filename = String.valueOf(System.currentTimeMillis())+".jpeg";

                try {
                    saveFile(snapshot,filename);
                } catch (IOException e) {
                    e.printStackTrace();
                }


                Intent intent = new Intent();
                    intent.putExtra("map_snapshot_path",  fileUtils.getFilePath()+filename);
                    MapDemoActivity.this.setResult(RESULT_OK, intent);
                    MapDemoActivity.this.finish();
                }


            };

    public void saveFile(Bitmap bm, String fileName) throws IOException {
        String path = fileUtils.getFilePath();
        File dirFile = new File(path);
        if(!dirFile.exists()){
            dirFile.mkdir();
        }
        File myCaptureFile = new File(path + fileName);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
        bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        bos.flush();
        bos.close();
    }


    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
        if (geoCodeResult == null || geoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
          // 没有找到检索结果
        }
    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
        if (reverseGeoCodeResult == null || reverseGeoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
            // 没有找到检索结果
        }else{
            mPoiInfo = new PoiInfo();
            mPoiInfo.name = "[位置]";
            mPoiInfo.location = reverseGeoCodeResult.getLocation();
            mPoiInfo.address = reverseGeoCodeResult.getAddress();
            mInfoList.clear();
            mInfoList.add(mPoiInfo);

           // 将周边信息加入表
            if (reverseGeoCodeResult.getPoiList() != null) {
                mInfoList.addAll(reverseGeoCodeResult.getPoiList());
            }

            mAdapter.notifyDataSetChanged();
        }

        loadingLayout.setVisibility(View.GONE);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        BitmapDescriptor mSelectIco = BitmapDescriptorFactory
                .fromResource(android.R.drawable.ic_menu_mylocation);
        mBaiduMap.clear();
        PoiInfo info = (PoiInfo) mAdapter.getItem(i);
        LatLng la = info.location;

// 动画跳转
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(la);
        mBaiduMap.animateMapStatus(u);

// 添加覆盖物
        OverlayOptions ooA = new MarkerOptions().position(la)
                .icon(mSelectIco).anchor(0.5f, 0.5f);
        mBaiduMap.addOverlay(ooA);


       /* LatLng currentLatLng;
        currentLatLng = mBaiduMap.getProjection().fromScreenLocation(
                mCenterPoint);*/

// 发起反地理编码检索
       /* mGeoCoder.reverseGeoCode((new ReverseGeoCodeOption())
                .location(la));*/
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {

            // 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）
            mCurrentMarker = BitmapDescriptorFactory
                    .fromResource(android.R.drawable.ic_menu_mylocation);
            MyLocationConfiguration config = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, mCurrentMarker);
            mBaiduMap.setMyLocationConfigeration(config);

            if (location == null || mapView == null) {
                return;
            }
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                            // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);

            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng currentLatLng = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(currentLatLng).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

                //调用反地理编码
                mGeoCoder.reverseGeoCode((new ReverseGeoCodeOption())
                        .location(currentLatLng));
            }
        }



    }
}
