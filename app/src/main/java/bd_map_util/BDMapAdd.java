package bd_map_util;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.List;

// Created by Administrator on 2016/11/30.

@SuppressWarnings("unused")
public class BDMapAdd {

    // -------------------------------------------------- overlay
    public static Marker overlay(BaiduMap bdMap, LatLng latLng, BitmapDescriptor bmDes,
                                 Bundle bundle, boolean draggable, boolean isSetMapStatus,
                                 float anchorX, float anchorY) {
        MarkerOptions options = new MarkerOptions();
        options.position(latLng).icon(bmDes).draggable(draggable);
        Marker marker = (Marker) bdMap.addOverlay(options);

        if (bundle != null) {
            marker.setExtraInfo(bundle);
        }
        if (isSetMapStatus) {
            bdMap.setMapStatus(MapStatusUpdateFactory.newLatLng(latLng));
        }
        marker.setAnchor(anchorX, anchorY);
        return marker;
    }

    public static Marker overlay(BaiduMap bdMap, LatLng latLng, BitmapDescriptor bmDes,
                                 boolean isSetMapStatus, float anchorX, float anchorY) {
        return overlay(bdMap, latLng, bmDes, null, false, isSetMapStatus, 0.5f, 0.5f);
    }

    public static Marker overlay(BaiduMap bdMap, LatLng latLng, BitmapDescriptor bmDes,
                                 float anchorX, float anchorY) {
        return overlay(bdMap, latLng, bmDes, null, false, false, 0.5f, 0.5f);
    }

    public static Marker overlay(BaiduMap bdMap, LatLng latLng, BitmapDescriptor bmDes,
                                 boolean isSetMapStatus) {
        return overlay(bdMap, latLng, bmDes, null, false, isSetMapStatus, 0.5f, 0.5f);
    }

    public static void overlays(BaiduMap bdMap, List<LatLng> list, BitmapDescriptor bmDes) {
        BDMapUtil.throwException(list, 1);
        for (int i = 0; i < list.size(); i++) {
            overlay(bdMap, list.get(i), bmDes, null, false, false, 0.5f, 0.5f);
        }
    }

    // -------------------------------------------------- infoWindow
    public static InfoWindow infoWindow(Context ctx, BaiduMap bdMap, LatLng latLng, View v,
                                        InfoWindow.OnInfoWindowClickListener listener) {
        BitmapDescriptor bmDes = BitmapDescriptorFactory.fromView(v);
        InfoWindow infoWindow = new InfoWindow(bmDes, latLng, 0, listener);
        bdMap.showInfoWindow(infoWindow);
        return infoWindow;
    }

    // --------------------------------------------------  circle  polygon  text
    public static void circle(BaiduMap bdMap, LatLng latLng, int radius, int strokeWidth,
                              int strokeColor, int fillColor) {
        OverlayOptions options = new CircleOptions().center(latLng).radius(radius)
                .stroke(new Stroke(strokeWidth, strokeColor)).fillColor(fillColor);
        bdMap.addOverlay(options);
    }

    // --- 绘制折线 (闭合)
    public static void polygon(BaiduMap bdMap, List<LatLng> list, int strokeWidth, int strokeColor,
                               int fillColor) {
        BDMapUtil.throwException(list, 2);
        OverlayOptions options = new PolygonOptions().points(list)
                .stroke(new Stroke(strokeWidth, strokeColor)).fillColor(fillColor);
        bdMap.addOverlay(options);
    }

    // --- 绘制多纹理分段的折线 (不闭合)
    public static void polygon(BaiduMap bdMap, List<LatLng> list, int width,
                               List<BitmapDescriptor> customTextureList) {
        BDMapUtil.throwException(list, 2);
        List<Integer> textureIndexList = new ArrayList<>();
        for (int i = 0; i < customTextureList.size(); i++) {
            textureIndexList.add(i);
        }

        OverlayOptions options = new PolylineOptions().points(list).dottedLine(true)
                .width(width).customTextureList(customTextureList).textureIndex(textureIndexList);
        bdMap.addOverlay(options);
    }

    public static void text(BaiduMap bdMap, LatLng latLng, String text, int fontSize,
                            int fontColor, int bgColor, int rotate) {
        OverlayOptions options = new TextOptions().position(latLng).text(text)
                .fontSize(fontSize).fontColor(fontColor).bgColor(bgColor).rotate(-rotate);
        bdMap.addOverlay(options);
    }

}
