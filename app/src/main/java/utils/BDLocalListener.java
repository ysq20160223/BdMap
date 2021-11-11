package utils;


import android.content.Context;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.lib_sdk.utils.XLog;

// Created by Administrator on 2016/11/11.

@SuppressWarnings("unused")
public class BDLocalListener implements BDLocationListener {

    private Context ctx;

    private BaiduMap bdMap;

    private float localOrient;

    private LatLng localLatLng;
    private double localLat;
    private double localLng;

    private boolean isFirstLocal = true;

    public BDLocalListener(Context ctx, BaiduMap baiDuMap, float localOrient) {
        this.ctx = ctx;
        this.bdMap = baiDuMap;
        this.localOrient = localOrient;
    }

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        localLat = bdLocation.getLatitude();
        localLng = bdLocation.getLongitude();
        localLatLng = new LatLng(localLat, localLng);

        XLog.d("city: " + bdLocation.getCity() + ", street: " + bdLocation.getStreet());

        MyLocationData data = new MyLocationData.Builder().accuracy(bdLocation.getRadius())
                .direction(localOrient).longitude(localLng).latitude(localLat).build();
        bdMap.setMyLocationData(data);

        if (isFirstLocal) {
            isFirstLocal = false;
            bdMap.setMapStatus(MapStatusUpdateFactory.newLatLngZoom(localLatLng, 17f));

            Toast.makeText(ctx, bdLocation.getAddress().address, Toast.LENGTH_SHORT).show();
        }

    }
    // -------------------------------------------------------------------------------------

    public void setLocalOrient(float localOrient) {
        this.localOrient = localOrient;
    }

    public double getLocalLat() {
        return localLat;
    }

    public double getLocalLng() {
        return localLng;
    }

    public LatLng getLocalLatLng() {
        return localLatLng;
    }

}
