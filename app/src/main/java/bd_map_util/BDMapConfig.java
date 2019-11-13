package bd_map_util;

import android.view.View;

import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.lib_sdk.utils.Logcat;


// Created by Administrator on 2016/11/12.

@SuppressWarnings("unused")
public class BDMapConfig {


    public static BaiduMap getBDMap(MapView mapView) {
        BaiduMap baiduMap = mapView.getMap();
        baiduMap.setBuildingsEnabled(true); // 设置显示楼体
        return baiduMap;
    }

    public static void hideChildAt(MapView mapView) {
        mapView.getChildAt(1).setVisibility(View.INVISIBLE); // 1 - logo
        mapView.getChildAt(2).setVisibility(View.INVISIBLE); // 2 - 缩放图标
        mapView.getChildAt(3).setVisibility(View.INVISIBLE); // 3 - 比例尺
    }

    public static LocationClientOption getLocationClientOption() {
        LocationClientOption option = new LocationClientOption();

        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd09ll"); // 5:bd09ll(百度经纬度), 6:bd09mc(百度米制经纬度);
        option.setIsNeedAddress(true);

        option.setOpenGps(true);
        option.setScanSpan(1000); // 设置定位时间间隔
        return option;
    }

    public static void showBDMapChild(MapView mapView) {
        // 0:主视图,  1:logo,  2:缩放图标,  3:比例尺 (需等待视图加载后查询)
        for (int i = 0; i < mapView.getChildCount(); i++) {
            Logcat.Companion.d("Child " + i + ":" + mapView.getChildAt(i).toString());

        }
    }


}
