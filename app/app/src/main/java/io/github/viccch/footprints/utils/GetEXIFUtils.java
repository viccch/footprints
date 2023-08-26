package io.github.viccch.footprints.utils;

import com.drew.imaging.ImageMetadataReader;
import com.drew.lang.GeoLocation;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.GpsDirectory;

import java.io.File;
import java.util.Date;

public class GetEXIFUtils {

    public static class Result {
        public long gps_date;
        public double gps_longitude;
        public double gps_latitude;

        public Result() {
        }

        public Result(long date, double lon, double lat) {
            gps_date = date;
            gps_longitude = lon;
            gps_latitude = lat;
        }
    }

    public static Result getTimeAndLocation(String filepath) {
        Result res = new Result();
        try {
            Metadata metadata = ImageMetadataReader.readMetadata(new File(filepath));

            if (metadata.containsDirectoryOfType(GpsDirectory.class)) {
                GpsDirectory gpsDirectory = metadata.getFirstDirectoryOfType(GpsDirectory.class);
                Date date = gpsDirectory.getGpsDate();
                GeoLocation geoLocation = gpsDirectory.getGeoLocation();
                res = new Result(date.getTime(), geoLocation.getLongitude(), geoLocation.getLatitude());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public static void main(String[] args) {
        String str = "E:\\classes\\自然地理与地质地貌实习\\note\\7.12\\pic\\IMG_20230712_074316.jpg";
        Result result = GetEXIFUtils.getTimeAndLocation(str);
        System.out.println(new Date(result.gps_date));
        System.out.println(result.gps_latitude);
        System.out.println(result.gps_longitude);
    }
}
