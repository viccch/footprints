package io.github.viccch.footprints.ui.record;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMap.OnMapClickListener;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.MapsInitializer;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.github.viccch.footprints.APP;
import io.github.viccch.footprints.AppCommunicationManager;
import io.github.viccch.footprints.R;
import io.github.viccch.footprints.ui.find.Blog;

public class RecordFragment
        extends
        Fragment
        implements
        OnMapClickListener,
        LocationSource,
        AMapLocationListener {

    public static class Point_Content {
        public Location location = null;
        public String title = null;
        public String time = null;
        public String description = null;
        public List<Uri> uriList = new ArrayList<>();
        public List<String> typeList = new ArrayList<>();

        public List<String> fileURL = new ArrayList<>();
    }

    //MY_DATA
    List<List<Double>> line_string = new ArrayList<>();
    Polyline polyline;
    List<Marker> markerList = new ArrayList<>();
    List<Point_Content> pointContents = new ArrayList<>();

    double distance = 0.0;
    int seconds = 0;
    String start_time;
    AMapLocation start_point_location;
    //

    TextView textView_latitude;
    TextView textView_longitude;
    TextView textView_altitude;
    TextView textView_seconds;
    TextView textView_distance;


    private AMapLocationClient mlocationClient;
    private OnLocationChangedListener mListener;
    private AMapLocationClientOption mLocationOption;

    private MapView mMapView;
    private AMap mAMap;

    private RadioGroup radioGroup_mapType;

    private RadioButton btn_load_satellite;
    private RadioButton btn_load_normal;

    private RadioButton btn_load_night;

    private RadioButton btn_load_navi;

    private Switch btn_show_buildings;
    private Switch btn_show_traffic;
    private Switch btn_show_maptext;

    ToggleButton btn_log_start;

    DrawerLayout mDrawerLayout;
    ImageButton btn_settings;
    ImageButton btn_add;

    ImageButton btn_camera;

    boolean on = false;

    Handler handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {

        @Override
        public boolean handleMessage(Message message) {

            switch (message.what) {
                case 0: {
                    handler.removeCallbacks(thread);
                }
                break;
                case 1: {
                    Location location = mAMap.getMyLocation();
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    double altitude = location.getAltitude();
                    textView_longitude.setText((int) (longitude) + "°" + (int) ((longitude % 1.0) / (1.0 / 60)) + "'" + String.format("%.2f", (longitude % (1.0 / 60)) / (1.0 / 3600)) + "''");
                    textView_latitude.setText((int) (latitude) + "°" + (int) ((latitude % 1.0) / (1.0 / 60)) + "'" + String.format("%.2f", (latitude % (1.0 / 60)) / (1.0 / 3600)) + "''");
                    textView_altitude.setText(altitude + "米");

                    distance = 0;
                    if (line_string.size() > 1) {
                        for (int i = 1; i < line_string.size(); i++) {
                            LatLng p1 = new LatLng(line_string.get(i).get(1), line_string.get(i).get(0));
                            LatLng p2 = new LatLng(line_string.get(i - 1).get(1), line_string.get(i - 1).get(0));
                            distance += AMapUtils.calculateLineDistance(p2, p1);
                        }
                    }
                    textView_distance.setText(String.format("%.2f", distance / 1000));
                    textView_seconds.setText(String.format("%02d", (int) (seconds / (60 * 60))) + ":" + String.format("%02d", (int) (seconds / 60)) + ":" + String.format("%02d", (int) (seconds % 60)) + "");

                    if (on) {
                        seconds += 1;
                        line_string.add(new ArrayList<Double>() {{
                            add(longitude);
                            add(latitude);
                            add(altitude);
                        }});

                        List<LatLng> latLngs = new ArrayList<>();

                        for (List<Double> p : line_string) {
                            latLngs.add(new LatLng(p.get(1), p.get(0)));
                        }

                        if (polyline == null) {
                            polyline = mAMap.addPolyline(new PolylineOptions()
                                    .width(10)
                                    .color(Color.rgb(255, 0, 0))
                                    .addAll(latLngs));
                        }
                        polyline.setPoints(latLngs);
                    }
                }
                break;
                default: {
                }
                break;
            }
            return false;
        }
    });

    Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
            Message message = Message.obtain();
            message.what = 1;
            handler.sendMessage(message);

            //3秒后又将线程加入到线程队列中
            handler.postDelayed(thread, 1000);
        }
    });

    View view;

    @Override
    public void onStart() {
        super.onStart();

        //默认打开卫星图
        radioGroup_mapType.check(btn_load_satellite.getId());

        //默认打开注记图层，关闭交通图层、建筑图层
        btn_show_maptext.setChecked(true);
        btn_show_traffic.setChecked(false);
        btn_show_buildings.setChecked(true);
    }

    void add_marker(Point_Content point_content) {

        pointContents.add(point_content);

        markerList.add(mAMap.addMarker(new MarkerOptions().position(new LatLng(point_content.location.getLatitude(), point_content.location.getLongitude()))));

        mAMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                TestFragment testFragment = new TestFragment();

                testFragment.set_myListener(
                        new TestFragment.MyListener() {
                            @Override
                            public void send_info(Point_Content point_content1) {
                                testFragment.send_info();
                            }

                            @Override
                            public void set_info() {
//                                testFragment.set_info(point_content);
                                int index = markerList.indexOf(marker);
                                if (index != -1)
                                    testFragment.set_info(pointContents.get(index));
                                else
                                    testFragment.set_info(point_content);
                            }
                        }
                );

                testFragment.show(RecordFragment.this.getActivity().getSupportFragmentManager(), null);
                return false;
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup
            container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_record, container, false);

        //确认隐私政策
        MapsInitializer.updatePrivacyShow(this.getContext(), true, true);
        MapsInitializer.updatePrivacyAgree(this.getContext(), true);

        mMapView = view.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);

        if (mAMap == null) {
            mAMap = mMapView.getMap();
            setUpMap();
        }

        mDrawerLayout = view.findViewById(R.id.drawer_layout);

        btn_camera = view.findViewById(R.id.btn_camera);
        btn_camera.setOnClickListener(v -> {
            startActivity(new Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA_SECURE));
        });

        btn_add = view.findViewById(R.id.btn_add);
        btn_add.setOnClickListener(v -> {

            Point_Content point_content = new Point_Content();
            point_content.location = mAMap.getMyLocation();

            TestFragment testFragment = new TestFragment();
            testFragment.set_myListener(
                    new TestFragment.MyListener() {
                        //获取TestFragment中的数据
                        @Override
                        public void send_info(Point_Content point_content) {
//                            if (!pointContents.contains(point_content)) {
                            testFragment.send_info();
                            add_marker(point_content);
//                            }
                        }

                        @Override
                        public void set_info() {
                            testFragment.set_info(point_content);
                        }
                    }
            );

            testFragment.show(getActivity().getSupportFragmentManager(), null);
        });
        btn_add.setVisibility(View.GONE);


        btn_settings = view.findViewById(R.id.btn_settings);
        btn_settings.setOnClickListener(v -> {
            mDrawerLayout.open();
        });

        btn_log_start = view.findViewById(R.id.btn_log_start);
        btn_log_start.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                on = true;
                btn_add.setVisibility(View.VISIBLE);
                btn_log_start.setBackgroundResource(R.drawable.baseline_stop_circle_24);

//                seconds = 0;
                start_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                start_point_location = mlocationClient.getLastKnownLocation();
            } else {
                new AlertDialog.Builder(this.getContext())
                        .setTitle("确认结束吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {


                                final Blog blog = new Blog();
                                blog.blog_user_id = APP.getInstance().getUser().getUser_id();

                                blog.blog_content = new Blog.Content();
                                blog.blog_content.seconds = seconds;
                                blog.blog_content.start_time = start_time;
                                blog.blog_content.head_image_url = "http://192.168.31.223:3000/res/admin/test/eg_tulip.jpg";
                                blog.blog_content.location = start_point_location.getProvince() + " " + start_point_location.getCity() + " " + start_point_location.getDistrict();
                                blog.blog_content.distance = distance;

                                blog.blog_content.line_string = line_string;


                                ////////////////////////////////
                                {
                                    on = false;

                                    btn_add.setVisibility(View.GONE);
                                    btn_log_start.setBackgroundResource(R.drawable.baseline_play_circle_24);

                                    for (Marker marker : markerList) {
                                        marker.destroy();
                                    }
                                    line_string = new ArrayList<>();
                                    List<LatLng> latLngs = new ArrayList<>();
                                    polyline.setPoints(latLngs);
                                    seconds = 0;

                                    textView_distance.setText("0.00");
                                    textView_seconds.setText("00" + ":" + "00" + ":" + "00");
                                }
                                //////////////////////////////////


                                final EditText textView = new EditText(getContext());

                                new AlertDialog
                                        .Builder(getContext())
                                        .setCancelable(false)
                                        .setTitle("请输入标题")
                                        .setView(textView)
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                blog.blog_title = textView.getText().toString();
                                                {
                                                    Dialog dialog = new AlertDialog
                                                            .Builder(getContext())
                                                            .setCancelable(false)
                                                            .setTitle("正在上传")
                                                            .setView(getLayoutInflater().inflate(R.layout.progress_dialog, null, false))
                                                            .create();

                                                    dialog.show();

                                                    //上传
                                                    new Thread(() -> {
                                                        for (Point_Content point_content : pointContents) {
                                                            for (Uri uri : point_content.uriList) {
                                                                String URL = AppCommunicationManager.Upload(uri);
                                                                point_content.fileURL.add(URL);
                                                            }
                                                        }

                                                        List<Blog.Content.Marker> list = new ArrayList<>();

                                                        for (Point_Content point_content : pointContents) {
                                                            Blog.Content.Marker m = new Blog.Content.Marker();
                                                            m.position = new ArrayList<Double>() {{
                                                                add(point_content.location.getLongitude());
                                                                add(point_content.location.getLatitude());
                                                                add(point_content.location.getAltitude());
                                                            }};

                                                            m.time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(point_content.location.getTime());
                                                            m.title = point_content.title;
                                                            m.description = point_content.description;
                                                            m.mime_items = new ArrayList<Blog.Content.Marker.MIME_Item>() {{
                                                                for (int i = 0; i < point_content.fileURL.size(); i++) {
                                                                    add(new Blog.Content.Marker.MIME_Item(point_content.fileURL.get(i), point_content.typeList.get(i)));
                                                                }
                                                            }};
                                                            list.add(m);
                                                        }

                                                        blog.blog_content.markers = list;

                                                        for (Blog.Content.Marker marker : blog.blog_content.markers) {
                                                            for (Blog.Content.Marker.MIME_Item mime_item : marker.mime_items) {
                                                                if (mime_item.type.equals(Blog.Content.Marker.MIME_Item.ContentType.TYPE_IMAGE.value)) {
                                                                    blog.blog_content.head_image_url = mime_item.url;
                                                                    break;
                                                                }
                                                            }
                                                        }

                                                        AppCommunicationManager.PublishBlog(blog.blog_user_id, blog.blog_title, blog.parse_content());

                                                        dialog.dismiss();
                                                        dialogInterface.dismiss();
                                                    }).start();
                                                }
                                            }
                                        })
                                        .create()
                                        .show();
                            }
                        })
                        .setNegativeButton("取消", (dialogInterface, i) -> dialogInterface.cancel())
                        .create()
                        .show();
            }
        });

        btn_load_satellite = view.findViewById(R.id.btn_load_satellite);
        btn_load_normal = view.findViewById(R.id.btn_load_normal);
        btn_load_night = view.findViewById(R.id.btn_load_night);
        btn_load_navi = view.findViewById(R.id.btn_load_navi);

        radioGroup_mapType = view.findViewById(R.id.radioGroup_mapType);
        radioGroup_mapType.setOnCheckedChangeListener((radioGroup, checkedId) ->

        {
            if (checkedId == btn_load_normal.getId()) {
                mAMap.setMapType(AMap.MAP_TYPE_NORMAL);
            } else if (checkedId == btn_load_satellite.getId()) {
                mAMap.setMapType(AMap.MAP_TYPE_SATELLITE);
            } else if (checkedId == btn_load_navi.getId()) {
                mAMap.setMapType(AMap.MAP_TYPE_NAVI);
            } else if (checkedId == btn_load_night.getId()) {
                mAMap.setMapType(AMap.MAP_TYPE_NIGHT);
            }
        });

        btn_show_buildings = view.findViewById(R.id.btn_show_buildings);
        btn_show_buildings.setOnCheckedChangeListener((compoundButton, b) ->
        {
            mAMap.showBuildings(b);
        });

        btn_show_traffic = view.findViewById(R.id.btn_show_traffic);
        btn_show_traffic.setOnCheckedChangeListener((compoundButton, b) ->
        {
            mAMap.setTrafficEnabled(b);
        });

        btn_show_maptext = view.findViewById(R.id.btn_show_maptext);
        btn_show_maptext.setOnCheckedChangeListener((compoundButton, b) ->
        {
            mAMap.showMapText(b);
        });

        textView_latitude = view.findViewById(R.id.textView_latitude);
        textView_longitude = view.findViewById(R.id.textView_longitude);
        textView_altitude = view.findViewById(R.id.textView_altitude);
        textView_distance = view.findViewById(R.id.textView_distance);
        textView_seconds = view.findViewById(R.id.textView_seconds);

        //2秒后启动定位监测线程
        handler.postDelayed(thread, 2000);

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        mAMap.getUiSettings().setMyLocationButtonEnabled(true);//启用设置默认定位按钮
        mAMap.getUiSettings().setZoomPosition(AMapOptions.ZOOM_POSITION_RIGHT_CENTER);
//        mAMap.getUiSettings().setZoomControlsEnabled(false);//禁用默认zoomIn,zoomOut
        mAMap.getUiSettings().setRotateGesturesEnabled(false);//禁用旋转手势
        mAMap.moveCamera(CameraUpdateFactory.zoomTo(15));//缩放zoom到15
        mAMap.setOnMapClickListener(this);
        mAMap.setLocationSource(this);// 设置定位监听
        // 自定义系统定位蓝点
        MyLocationStyle myLocationStyle = (new MyLocationStyle())
                .myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.gps_point2))// 自定义定位蓝点图标
                .strokeColor(Color.argb(0, 0, 0, 0)) // 自定义精度范围的圆形边框颜色
                .strokeWidth(0)  // 自定义精度范围的圆形边框宽度
//                .interval(1000)
                .radiusFillColor(Color.argb(0, 0, 0, 0))  // 设置圆形的填充颜色
                .myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//设置定位的类型为定位模式:定位、且将视角移动到地图中心点。
        mAMap.setMyLocationStyle(myLocationStyle);   // 将自定义的 myLocationStyle 对象添加到地图上
        mAMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false

    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null && amapLocation.getErrorCode() == 0) {

                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点

//                mAMap.clear();

//                List<LatLng> latLngs = new ArrayList<>();
//
//                for (List<Double> p : line_string) {
//                    latLngs.add(new LatLng(p.get(1), p.get(0)));
//                }
//
//                if (polyline == null) {
//                    polyline = mAMap.addPolyline(new PolylineOptions()
//                            .width(10)
//                            .color(Color.rgb(255, 0, 0))
//                            .addAll(latLngs));
//                }
//                polyline.setPoints(latLngs);

            } else {
                String errText = "定位失败," + amapLocation.getErrorCode() + ": "
                        + amapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
//                tvResult.setText(errText);
            }
        }
    }


    @Override
    public void onMapClick(LatLng latLng) {
//        mAMap.addMarker(new MarkerOptions().position(latLng));
    }

    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mlocationClient == null) {
            try {
                mlocationClient = new AMapLocationClient(this.getContext());
                mLocationOption = new AMapLocationClientOption();
                // 设置定位监听
                mlocationClient.setLocationListener(this);
                // 设置为高精度定位模式
                mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
                mLocationOption.setOnceLocation(false);
                mLocationOption.setInterval(1000);
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

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            Toast.makeText(this, "back", Toast.LENGTH_SHORT).show();
//            new AlertDialog.Builder(this)
//                    .setTitle("确认对话框")
//                    .setMessage("你确定要退出？")
//                    .setNegativeButton("取消", null)
//                    .setPositiveButton("确定", (dialog, which) -> finish())
//                    .show();
//        }
//        return true;
//    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        deactivate();
        mMapView.onPause();

        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }
}