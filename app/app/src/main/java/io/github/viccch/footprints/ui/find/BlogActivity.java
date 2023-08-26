package io.github.viccch.footprints.ui.find;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.MapsInitializer;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolylineOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import io.github.viccch.footprints.APP;
import io.github.viccch.footprints.AppCommunicationManager;
import io.github.viccch.footprints.R;
import io.github.viccch.footprints.ui.me.PeopleActivity;

public class BlogActivity extends AppCompatActivity {

    Bundle instance;
    Blog blog;
    MapView mapView;
    List<Marker> markerList = new ArrayList<>();
    ScrollView scrollView;
    ViewGroup linearLayout;
    TextView textView_id;
    TextView textView_datetime;

    ImageView imageView_head;

    ImageButton btn_delete;
    String my_id;

    RecyclerView recyclerView;
    ToggleButton toggleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog);
        instance = savedInstanceState;
        //设置回退键
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        scrollView = findViewById(R.id.scrollView);

        //获取传递的blog信息
        Intent intent = getIntent();
        String row_json = intent.getStringExtra("data");
        blog = new Gson().fromJson(row_json, Blog.class);

        setTitle(blog.blog_title);

        my_id = APP.getInstance().getUser().getUser_id();


        btn_delete = findViewById(R.id.btn_delete);
        btn_delete.setOnClickListener(v -> {
            if (my_id.equals(blog.blog_user_id)) {
                new AlertDialog.Builder(this)
                        .setTitle("确认对话框")
                        .setMessage("你确定要删除”" + blog.blog_title + "“吗?")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确定", (dialog, which) -> {
                            AppCommunicationManager.RemoveBlog(String.valueOf(blog.id));
                            Toast.makeText(this, "已删除", Toast.LENGTH_SHORT);
                            finish();
                        })
                        .show();
            }
        });
        if (!my_id.equals(blog.blog_user_id)) {
            btn_delete.setVisibility(View.GONE);
        }

        textView_id = findViewById(R.id.textView_id);
        textView_id.setText(blog.blog_user_id);
        textView_id.setOnClickListener(v -> {
            Intent peopleIntent = new Intent(this, PeopleActivity.class);
            peopleIntent.putExtra("id", textView_id.getText());
            startActivity(peopleIntent);
        });

        textView_datetime = findViewById(R.id.textView_datetime_blog_pub);
        textView_datetime.setText(blog.blog_time);

        imageView_head = findViewById(R.id.imageView_head);
        Glide.with(this)
                .load(R.drawable.user_head)
                .placeholder(R.drawable.baseline_broken_image_24)
                .transform(new CircleCrop())
                .into(imageView_head);
        imageView_head.setOnClickListener(v -> textView_id.callOnClick());

        linearLayout = findViewById(R.id.linearLayout);

        view_parser();

//        mapView.bringToFront();

        toggleButton = findViewById(R.id.toggleButton);
        toggleButton.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                mapView.setVisibility(View.VISIBLE);
            } else {
                mapView.setVisibility(View.GONE);
            }
        });
        toggleButton.setChecked(true);
    }

    private void initRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        MarkerApapter markerApapter = new MarkerApapter(blog.blog_content.markers, this);
        recyclerView.setAdapter(markerApapter);
    }

    void view_parser() {

        TextView textView_distance = findViewById(R.id.textView_distance);
        TextView textView_seconds = findViewById(R.id.textView_seconds);
        TextView textView_location = findViewById(R.id.textView_location);
        TextView textView_datetime_start = findViewById(R.id.textView_datetime_start);

        textView_datetime_start.setText(blog.blog_content.start_time);
        textView_location.setText(blog.blog_content.location);
        textView_seconds.setText(String.format("%02d", (int) (blog.blog_content.seconds / (60 * 60))) + ":" + String.format("%02d", (int) (blog.blog_content.seconds / 60)) + ":" + String.format("%02d", (int) (blog.blog_content.seconds % 60)) + "");
        textView_distance.setText(String.format("%.2f", blog.blog_content.calculate_distance()));

        initRecyclerView();

        mapView_parser();

//        if (marker.type.equals(BlogContent.ContentType.TYPE_TEXT.value)) {
//            linearLayout.addView(textView_parser(blogContent.data));
//        } else if (blogContent.type.equals(BlogContent.ContentType.TYPE_IMAGE.value)) {
//            linearLayout.addView(imageView_parser(blogContent.data));
//        } else if (blogContent.type.equals(BlogContent.ContentType.TYPE_VIDEO.value)) {
//            linearLayout.addView(videoView_parser(blogContent.data));
//        } else if (blogContent.type.equals(BlogContent.ContentType.TYPE_MAP.value)) {
//            linearLayout.addView(mapView_parser(blogContent.data));
//        } else {
//            //throw new IllegalStateException("Unexpected value: " + blogContent.type);
//        }
    }


    void mapView_parser() {

        //更新确认隐私政策
        MapsInitializer.updatePrivacyShow(this, true, true);
        MapsInitializer.updatePrivacyAgree(this, true);
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(instance);
        AMap aMap = mapView.getMap();

        //加载地图的点
        List<LatLng> points = new ArrayList<>();

        for (int i = 0; i < blog.blog_content.line_string.size(); i++) {
            points.add(new LatLng(blog.blog_content.line_string.get(i).get(1), blog.blog_content.line_string.get(i).get(0)));
        }

        //计算矩形边框
        LatLngBounds.Builder builder = LatLngBounds.builder();

        for (LatLng p : points) {
            builder.include(p);
        }

        LatLngBounds bounds = builder.build();

//        LatLng center_point = new LatLng(
//                (bounds.northeast.latitude + bounds.southwest.latitude) / 2,
//                (bounds.northeast.longitude + bounds.southwest.longitude) / 2);

        //将路线绘制到地图上
        aMap.addPolyline(new PolylineOptions()
                .addAll(points)
                .width(10)
                .useGradient(true)//平滑线条颜色
                .colorValues(ColorGradientGenerator.get_red2green(10)
                )
        );

        //移动并缩放地图到指定的边框中
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 100);
        aMap.moveCamera(cameraUpdate);
        aMap.setMapType(AMap.MAP_TYPE_SATELLITE);

        for (Blog.Content.Marker marker : blog.blog_content.markers) {
            markerList.add(aMap.addMarker(new MarkerOptions().position(new LatLng(marker.position.get(1), marker.position.get(0)))));
        }

        Context context = this;
        aMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker m) {
                int position = markerList.indexOf(m);
                View v = getLayoutInflater().inflate(R.layout.list_marker, linearLayout, false);
                {
                    Blog.Content.Marker marker = blog.blog_content.markers.get(position);
//                    markerList.get(position);
                    ((TextView) v.findViewById(R.id.itemTextView_title)).setText(marker.title);
                    ((TextView) v.findViewById(R.id.itemTextView_description)).setText(marker.description);
                    ((TextView) v.findViewById(R.id.itemTextView_datetime)).setText(marker.time);

                    double longitude = marker.position.get(0);
                    double latitude = marker.position.get(1);
                    double altitude = marker.position.get(2);
                    String lon = ((int) (longitude) + "°" + (int) ((longitude % 1.0) / (1.0 / 60)) + "'" + String.format("%.2f", (longitude % (1.0 / 60)) / (1.0 / 3600)) + "''");
                    String lat = ((int) (latitude) + "°" + (int) ((latitude % 1.0) / (1.0 / 60)) + "'" + String.format("%.2f", (latitude % (1.0 / 60)) / (1.0 / 3600)) + "''");
                    ((TextView) v.findViewById(R.id.itemTextView_position)).setText("北纬：" + lat + "，东经：" + lon);
                    ((TextView) v.findViewById(R.id.itemTextView_altitude)).setText("海拔：" + (int) altitude + "m");

                    for (Blog.Content.Marker.MIME_Item mime_item : marker.mime_items) {
                        if (mime_item.type.equals(Blog.Content.Marker.MIME_Item.ContentType.TYPE_IMAGE.value)) {
                            ((ViewGroup) v.findViewById(R.id.mimes)).addView(imageView_parser(mime_item.url));
                        } else if (mime_item.type.equals(Blog.Content.Marker.MIME_Item.ContentType.TYPE_VIDEO.value)) {
                            ((ViewGroup) v.findViewById(R.id.mimes)).addView(videoView_parser(mime_item.url));
                        }
                    }
                }
                new AlertDialog
                        .Builder(context)
                        .setView(v)
                        .create()
                        .show();
                return false;
            }
        });
    }

    TextView textView_parser(String text) {
        TextView textView = (TextView) LayoutInflater.from(this).inflate(R.layout.list_blog_textview_layout, linearLayout, false);
        textView.setText(text);
        return textView;
    }

    ImageView imageView_parser(String url) {

        ImageView imageView = (ImageView) LayoutInflater.from(this).inflate(R.layout.list_blog_imageview_layout, linearLayout, false);

        imageView.setOnClickListener(v -> {
            startActivity(new Intent(this, ScaleImageActivity.class).putExtra(ScaleImageActivity.M_ARG_PARAM_URL, url));
        });

        Glide.with(this)
                .load(url)
                .placeholder(R.drawable.baseline_broken_image_24)
                .into(imageView);

        return imageView;
    }

    PlayerView videoView_parser(String url) {

//        String videoPath = "http://vfx.mtime.cn/Video/2019/07/12/mp4/190712140656051701.mp4";
        PlayerView playerView = (PlayerView) LayoutInflater.from(this).inflate(R.layout.list_blog_playerview_layout, linearLayout, false);
        playerView.setPlayer(new ExoPlayer.Builder(this).build());
        playerView.getPlayer().addMediaItem(MediaItem.fromUri(url));
        playerView.getPlayer().prepare();

        playerView.getPlayer().addListener(new Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int playbackState) {
                Player.Listener.super.onPlaybackStateChanged(playbackState);
                playerView.hideController();
            }
        });
//        playerView.hideController();

//        ImageView imageView_thumb = findViewById(R.id.imageView_thumb);
//        imageView_thumb.setOnClickListener(v -> {
//            imageView_thumb.setVisibility(View.GONE);
//            playerView.getPlayer().prepare();
//            playerView.getPlayer().setPlayWhenReady(true);
//            playerView.hideController();
//        });
//        Glide.with(this)
//                .load(url)
//                .apply(RequestOptions.frameOf(0))
//                .placeholder(R.drawable.baseline_broken_image_24)
//                .into(imageView_thumb);

        return playerView;
    }

    @Override
    protected void onPause() {
        super.onPause();

        stop_video();
    }

    void stop_video() {
        int num = linearLayout.getChildCount();
        for (int i = 0; i < num; i++) {
            if (linearLayout.getChildAt(i).getClass().equals(PlayerView.class)) {
                ((PlayerView) linearLayout.getChildAt(i)).getPlayer().pause();
            }
        }
    }

    public class MarkerApapter extends RecyclerView.Adapter<MarkerApapter.ViewHolder> {
        private final List<Blog.Content.Marker> markerList;
        private final Context context;

        public MarkerApapter(List<Blog.Content.Marker> markerList, Context context) {
            this.markerList = markerList;
            this.context = context;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(context).inflate(R.layout.list_marker, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Blog.Content.Marker marker = markerList.get(position);
            holder.itemTextView_title.setText(marker.title);
            holder.itemTextView_description.setText(marker.description);
            holder.itemTextView_datetime.setText(marker.time);

            double longitude = marker.position.get(0);
            double latitude = marker.position.get(1);
            double altitude = marker.position.get(2);
            String lon = ((int) (longitude) + "°" + (int) ((longitude % 1.0) / (1.0 / 60)) + "'" + String.format("%.2f", (longitude % (1.0 / 60)) / (1.0 / 3600)) + "''");
            String lat = ((int) (latitude) + "°" + (int) ((latitude % 1.0) / (1.0 / 60)) + "'" + String.format("%.2f", (latitude % (1.0 / 60)) / (1.0 / 3600)) + "''");
            holder.itemTextView_position.setText("北纬：" + lat + "，东经：" + lon);
            holder.itemTextView_altitude.setText("海拔：" + (int) altitude + "m");

            for (Blog.Content.Marker.MIME_Item mime_item : marker.mime_items) {
                if (mime_item.type.equals(Blog.Content.Marker.MIME_Item.ContentType.TYPE_IMAGE.value)) {
                    holder.mimes.addView(imageView_parser(mime_item.url));
                } else if (mime_item.type.equals(Blog.Content.Marker.MIME_Item.ContentType.TYPE_VIDEO.value)) {
                    holder.mimes.addView(videoView_parser(mime_item.url));
                }
            }
        }

        @Override
        public int getItemCount() {
            return markerList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView itemTextView_title;
            TextView itemTextView_description;
            TextView itemTextView_datetime;
            TextView itemTextView_position;
            TextView itemTextView_altitude;
            ViewGroup mimes;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                itemTextView_title = itemView.findViewById(R.id.itemTextView_title);
                itemTextView_description = itemView.findViewById(R.id.itemTextView_description);
                itemTextView_datetime = itemView.findViewById(R.id.itemTextView_datetime);
                itemTextView_position = itemView.findViewById(R.id.itemTextView_position);
                itemTextView_altitude = itemView.findViewById(R.id.itemTextView_altitude);
                mimes = itemView.findViewById(R.id.mimes);
            }
        }
    }
}