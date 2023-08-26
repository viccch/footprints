package io.github.viccch.footprints.ui.record;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.OnMapClickListener;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.MapsInitializer;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;

import io.github.viccch.footprints.R;

public class MapActivity
        extends
//        CheckPermissionsActivity
        AppCompatActivity
        implements
        View.OnClickListener,
        OnMapClickListener,
        LocationSource,
        AMapLocationListener,
        OnCheckedChangeListener {

    /*字段*/
    private TextView tvResult;

    private AMapLocationClient mlocationClient;
    private OnLocationChangedListener mListener;
    private AMapLocationClientOption mLocationOption;

    private MapView mMapView;
    private AMap mAMap;

    private Button btn_load_satellite;
    private Button btn_load_normal;
    private Button btn_load_navi;
    private Button btn_load_night;

    private Button btn_show_buildings;
    private Button btn_show_traffic;
    private Button btn_show_maptext;


    // 中心点坐标
    private final LatLng centerLatLng = null;

    //函数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        //更新确认隐私政策
        MapsInitializer.updatePrivacyShow(this, true, true);
        MapsInitializer.updatePrivacyAgree(this, true);


        btn_load_satellite = (Button) findViewById(R.id.btn_load_satellite);
        btn_load_satellite.setOnClickListener(this);

        btn_load_normal = (Button) findViewById(R.id.btn_load_normal);
        btn_load_normal.setOnClickListener(this);

        btn_load_night = (Button) findViewById(R.id.btn_load_night);
        btn_load_night.setOnClickListener(this);

        btn_load_navi = (Button) findViewById(R.id.btn_load_navi);
        btn_load_navi.setOnClickListener(this);

        btn_show_buildings = (Button) findViewById(R.id.btn_show_buildings);
        btn_show_buildings.setOnClickListener(this);

        btn_show_traffic = (Button) findViewById(R.id.btn_show_traffic);
        btn_show_traffic.setOnClickListener(this);

        btn_show_maptext = (Button) findViewById(R.id.btn_show_maptext);
        btn_show_maptext.setOnClickListener(this);

        tvResult = (TextView) findViewById(R.id.tv_result);
        mMapView = (MapView) findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);

        if (mAMap == null) {
            mAMap = mMapView.getMap();
            mAMap.getUiSettings().setRotateGesturesEnabled(false);
            mAMap.moveCamera(CameraUpdateFactory.zoomBy(6));
            setUpMap();
        }
    }

    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        mAMap.setOnMapClickListener(this);
        mAMap.setLocationSource(this);// 设置定位监听
        mAMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        // 自定义系统定位蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        // 自定义定位蓝点图标
        myLocationStyle.myLocationIcon(
                BitmapDescriptorFactory.fromResource(R.drawable.gps_point2));
        // 自定义精度范围的圆形边框颜色
        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));
        // 自定义精度范围的圆形边框宽度
        myLocationStyle.strokeWidth(0);
        // 设置圆形的填充颜色
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);
        // 将自定义的 myLocationStyle 对象添加到地图上
        mAMap.setMyLocationStyle(myLocationStyle);
        mAMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        // 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
        //mAMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
    }

    private boolean is_show_buildings = false;
    private boolean is_show_traffic = false;
    private boolean is_show_maptext = false;

    @Override
    public void onClick(View v) {
        mMapView.showContextMenu();
        if (v.getId() == btn_load_satellite.getId()) {
            mAMap.setMapType(AMap.MAP_TYPE_SATELLITE);
        } else if (v.getId() == btn_load_normal.getId()) {
            mAMap.setMapType(AMap.MAP_TYPE_NORMAL);
        } else if (v.getId() == btn_load_navi.getId()) {
            mAMap.setMapType(AMap.MAP_TYPE_NAVI);
        } else if (v.getId() == btn_load_night.getId()) {
            mAMap.setMapType(AMap.MAP_TYPE_NIGHT);
        }

        if (v.getId() == btn_show_maptext.getId()) {
            is_show_maptext = !is_show_maptext;
            mAMap.showMapText(is_show_maptext);
        } else if (v.getId() == btn_show_buildings.getId()) {
            is_show_buildings = !is_show_buildings;
            mAMap.showBuildings(is_show_buildings);
        } else if (v.getId() == btn_show_traffic.getId()) {
            is_show_traffic = !is_show_traffic;
            mAMap.setTrafficEnabled(is_show_traffic);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

    }


    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null && amapLocation.getErrorCode() == 0) {
//                tvResult.setVisibility(View.GONE);
                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点

                tvResult.setText(amapLocation.getLatitude() + "," + amapLocation.getLongitude());
            } else {
                String errText = "定位失败," + amapLocation.getErrorCode() + ": "
                        + amapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
                tvResult.setVisibility(View.VISIBLE);
                tvResult.setText(errText);
            }
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mlocationClient == null) {
            try {
                mlocationClient = new AMapLocationClient(this);
                mLocationOption = new AMapLocationClientOption();
                // 设置定位监听
                mlocationClient.setLocationListener(this);
                // 设置为高精度定位模式
                mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
                // 只是为了获取当前位置，所以设置为单次定位
                mLocationOption.setOnceLocation(true);
                // 设置定位参数
                mlocationClient.setLocationOption(mLocationOption);
                mlocationClient.startLocation();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
            mlocationClient = null;
        }
        if (mLocationOption != null) {
            mLocationOption = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
        deactivate();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}