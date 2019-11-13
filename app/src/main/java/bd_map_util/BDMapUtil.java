package bd_map_util;

import android.app.Activity;
import android.graphics.Point;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Projection;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;

import com.lib_sdk.utils.Logcat;

import java.text.DecimalFormat;
import java.util.List;

// Created by Administrator on 2016/7/7.

/*
 * public abstract class OverlayOptions
 * public final class MarkerOptions extends OverlayOptions
 * --------------------------------------------------------------------
 * public abstract class Overlay
 * public final class Marker extends Overlay
 */
@SuppressWarnings("unused")
public class BDMapUtil {

    // -------------------------------------------------- 计算两点之间距离
    public static String getDistance(LatLng from, LatLng to) {
        double lat1 = (Math.PI / 180) * from.latitude;
        double lon1 = (Math.PI / 180) * from.longitude;
        double lat2 = (Math.PI / 180) * to.latitude;
        double lon2 = (Math.PI / 180) * to.longitude;

        double R = 6371; // 地球半径

        double distance = Math.acos(Math.sin(lat1) * Math.sin(lat2) +
                Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon2 - lon1)) * R;
//        Log.i(TAG, "distance:" + distance);
        DecimalFormat dF = new DecimalFormat("#.00");
        if (distance <= 1) {
            return dF.format(distance * 1000) + " m";
        }
        return dF.format(distance) + " km";
    }

    // -------------------------------------------------- 多边形中心点经纬度
    public static LatLng boundCenter(List<LatLng> list) {
        throwException(list, 3);
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (int i = 0; i < list.size(); i++) {
            builder.include(list.get(i));
        }
        return builder.build().getCenter();
    }

    // -------------------------------------------------- 百度坐标 转 GCJ 坐标
    public static BDLocation bd2gcj(double lon, double lat) {
        BDLocation bdLocation = new BDLocation();
        bdLocation.setLongitude(lon);
        bdLocation.setLatitude(lat);
        return bd2gcj(bdLocation);
    }

    private static BDLocation bd2gcj(BDLocation loc) {
        return LocationClient.getBDLocationInCoorType(loc, BDLocation.BDLOCATION_BD09LL_TO_GCJ02);
    }

    // -------------------------------------------------- Nav


    // --------------------------------------------------
    public static void toastRunOnUiThread(final Activity activity, final String msg) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     *
     */
    public static void moveToCenter(BaiduMap bdMap, LatLng latLng, int w, int h) {
        if (bdMap == null) {
            return;
        }

        MapStatus mapStatus = bdMap.getMapStatus();
        Projection projection = bdMap.getProjection();

        if (mapStatus == null || projection == null) {
            Logcat.Companion.e("mapStatus:" + mapStatus + ", projection:" + projection);
            return;
        }

        Point tp = mapStatus.targetScreen; // 屏幕中心的点
        Point lp = projection.toScreenLocation(latLng); // 把 参数经纬度 转化为屏幕上的点

        w = (w < 0) ? tp.x / 4 : w;
        h = (h < 0) ? tp.y / 4 : h;

        // w < 当前经纬度 x < 屏幕宽度 - w
        // h < 当前经纬度 y < 屏幕宽度 - h
        if (lp.x < w || lp.x > tp.x * 2 - w || lp.y < h || lp.y > tp.y * 2 - h) {
            bdMap.animateMapStatus(MapStatusUpdateFactory.newLatLng(latLng), 500);
        }
    }

    public static void moveToCenter(BaiduMap bdMap, LatLng latLng) {
        moveToCenter(bdMap, latLng, -1, -1);
    }

    public static void throwException(List<?> list, int size) {
        if (list == null || list.size() < size) {
            throw new IllegalArgumentException("list is null or list size less than " + size);
        }
    }

}
