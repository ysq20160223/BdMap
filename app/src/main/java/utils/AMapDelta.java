package utils;



import com.baidu.mapapi.model.LatLng;

import java.util.List;

// Created by Administrator on 2016/11/1.

public class AMapDelta {

    // 计算每次移动的距离
    public static double getMoveDistance(double slope, double distance) {
        if (slope == Double.MAX_VALUE || slope == 0) {
            return distance;
        }
        return Math.abs((distance * slope) / Math.sqrt(1 + slope * slope));
    }

    // 获取循环结束大小
    public static double getTo(LatLng to, double slope) {
        if (slope == 0) { // 在同一纬度
            return to.longitude;
        }
        return to.latitude;
    }

    // 获取循环初始值大小
    public static double getFrom(LatLng from, double slope) {
        if (slope == 0) { // 在同一纬度
            return from.longitude;
        }
        return from.latitude;
    }

    // 根据 点 和 斜率 计算截距
    public static double getIntercept(double slope, LatLng latLng) {
        return latLng.latitude - slope * latLng.longitude;
    }

    // 判断是否为反序 ( 低纬度 到 高纬度 为正序; 同纬度 - 小经度 到 大经度 为正序 )
    public static boolean isReverse(LatLng from, LatLng to, double slope) {
        if (slope == 0) { // y(lat) 坐标相等, x(lon) 坐标不相等 - 在同一纬度
            return from.longitude > to.longitude;
        }
        return (from.latitude > to.latitude);
    }

    // 根据 两点的经纬度 算斜率
    public static double getSlope(LatLng from, LatLng to) {
        if (to.longitude == from.longitude) { // x(lon) 坐标相等 , y(lat) 坐标不相等 - 在同一经度
            return Double.MAX_VALUE;
        }
        if (to.latitude == from.latitude) { // y(lat) 坐标相等, x(lon) 坐标不相等 - 在同一纬度
            return 0;
        }
        double y = to.latitude - from.latitude;
        double x = to.longitude - from.longitude;
        return y / x;
    }

    // 根据 两点的经纬度 算取图标转的角度
    public static double getAngle(LatLng from, LatLng to) {
        double slope = AMapDelta.getSlope(from, to);
        double y = to.latitude - from.latitude;
        if (slope == Double.MAX_VALUE) { // x(lon) 坐标相等 , y(lat) 坐标不相等 - 在同一经度
            if (y > 0) {
                return 90;
            } else {
                return 270;
            }
        }
        if (slope == 0) { // y(lat) 坐标相等, x(lon) 坐标不相等 - 在同一纬度
            double x = to.longitude - from.longitude;
            if (x > 0) {
                return 0;
            } else {
                return 180;
            }
        }

        float deltaAngle = 0;
        if (y > 0) {
            deltaAngle = (slope < 0) ? 180 : 0; //
        }
        if (y < 0) {
            deltaAngle = (slope < 0) ? 0 : -180;
        }

        double radio = Math.atan(slope);
        return 180 * (radio / Math.PI) + deltaAngle;
    }

    // 根据点获取图标转的角度
    public static double getAngle(int startIndex, List<LatLng> list) {
        if ((startIndex + 1) >= list.size()) {
            throw new RuntimeException("index out of bound");
        }
        return getAngle(list.get(startIndex), list.get(startIndex + 1));
    }

}
