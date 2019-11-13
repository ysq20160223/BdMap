package utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.as_160213_bd_map.R;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;

// Created by Administrator on 2016/3/3.

public class BDMapZoom extends LinearLayout implements View.OnClickListener {

    private Context ctx;

    private ImageView iv_zoom_in; // 放大按钮
    private ImageView iv_zoom_out; // 缩小按钮

    private BaiduMap bdMap;
    private float minZoomLevel; // 地图最小级别
    private float maxZoomLevel; // 地图最大级别

    public BDMapZoom(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BDMapZoom(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.ctx = context;
        init();
    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.bd_map_zoom, null);
        iv_zoom_in = (ImageView) layout.findViewById(R.id.iv_mapZoomIn); // 获取放大按钮
        iv_zoom_out = (ImageView) layout.findViewById(R.id.iv_mapZoomOut); // 获取缩小按钮

        iv_zoom_in.setOnClickListener(this); // 设置点击事件
        iv_zoom_out.setOnClickListener(this);
        addView(layout); // 添加View
    }

    @Override
    public void onClick(View v) {
        MapStatus mapStatus = bdMap.getMapStatus();
        switch (v.getId()) {
            case R.id.iv_mapZoomIn:
                bdMap.setMapStatus(MapStatusUpdateFactory.zoomTo(mapStatus.zoom + 1));
                updateZoom(); // 改变缩放按钮
                break;
            case R.id.iv_mapZoomOut:
                bdMap.setMapStatus(MapStatusUpdateFactory.zoomTo(mapStatus.zoom - 1));
                updateZoom(); // 改变缩放按钮
                break;
        }
    }

    public void setBDMap(BaiduMap baiduMap) {
        this.bdMap = baiduMap;
        maxZoomLevel = baiduMap.getMaxZoomLevel(); // 获取百度地图最大最小级别
        minZoomLevel = baiduMap.getMinZoomLevel();
//        Log.i(TAG, "maxZoomLevel:" + maxZoomLevel + ", minZoomLevel:" + minZoomLevel);
        updateZoom(); // 改变缩放按钮
    }

    public void updateZoom() {
        float zoom = bdMap.getMapStatus().zoom; // 获取当前地图状态
//        Log.i(TAG, "zoom:" + zoom);

        if (zoom >= maxZoomLevel - 1) {
            iv_zoom_in.setEnabled(false);
        } else {
            iv_zoom_in.setEnabled(true);
        }

        if (zoom <= minZoomLevel) {
            iv_zoom_out.setEnabled(false);
        } else {
            iv_zoom_out.setEnabled(true);
        }
    }

}