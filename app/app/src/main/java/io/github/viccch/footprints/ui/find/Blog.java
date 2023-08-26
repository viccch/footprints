package io.github.viccch.footprints.ui.find;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Blog {
    public int id;
    public String blog_time;
    public String blog_user_id;
    public String blog_title;
    public Content blog_content;

    public static class Content {

        public String head_image_url;
        public String start_time;//开始时间
        public int seconds;//总时间
        public String location;//xx省xx市xx县
        public List<Marker> markers;
        public List<List<Double>> line_string;

        public double distance;

        //new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(point_content.location.getTime())

        public double calculate_distance() {
            if (line_string != null && line_string.size() > 1) {
                for (int i = 1; i < line_string.size(); i++) {
                    LatLng p1 = new LatLng(line_string.get(i).get(1), line_string.get(i).get(0));
                    LatLng p2 = new LatLng(line_string.get(i - 1).get(1), line_string.get(i - 1).get(0));
                    distance += AMapUtils.calculateLineDistance(p2, p1);
                }
            }
            distance /= 1000;
            return distance;
        }

        public Date parse_date(String datetime) {
            Date date = null;
            try {
                date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(datetime);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            return date;
        }

        public static class Marker {
            public String title;
            public String description;
            public String time;
            public List<Double> position;
            public List<MIME_Item> mime_items;

            public static class MIME_Item {
                public String type;
                public String url;

                public MIME_Item(String url, String type) {
                    this.url = url;
                    this.type = type;
                }

                public static enum ContentType {
                    //                    TYPE_TEXT("text"),
                    //                    TYPE_MAP("map"),
                    TYPE_IMAGE("image/*"),
                    TYPE_VIDEO("video/*");
                    public final String value;

                    private ContentType(String value) {
                        this.value = value;
                    }
                }
            }
        }
    }

    public String parse_content() {
        return new Gson().toJson(blog_content);
    }
}
