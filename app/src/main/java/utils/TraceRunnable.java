package utils;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.model.LatLng;
import com.lib_sdk.utils.XLog;

import java.util.List;

import bd_map_util.BDMapUtil;
import handler.TraceHandler;

// Created by Administrator on 2016/11/2.

public class TraceRunnable implements Runnable {

    private boolean isSuspended;
    private boolean isRun;
    private boolean isStop;

    private List<LatLng> list;
    private Marker marker;
    private double distance;
    private long timeInterval;
    private BaiduMap bdMap;
    private TraceHandler traceHandler;

    public TraceRunnable(BaiduMap bdMap, TraceHandler traceHandler, List<LatLng> list,
                         Marker marker, double distance, long timeInterval) {
        this.bdMap = bdMap;
        this.traceHandler = traceHandler;
        this.list = list;

        this.marker = marker;
        this.distance = distance;
        this.timeInterval = timeInterval;
    }

    @Override
    public void run() {
        synchronized (this) {
            isRun = true;
        }
        for (int i = 0; i < list.size() - 1; i++) {
            LatLng from = list.get(i);
            LatLng to = list.get(i + 1);
            marker.setPosition(from);
            marker.setRotate((float) AMapDelta.getAngle(from, to));

            double slope = AMapDelta.getSlope(from, to);
            boolean isReverse = AMapDelta.isReverse(from, to, slope);
            double moveDistance = AMapDelta.getMoveDistance(slope, distance);
            moveDistance = isReverse ? moveDistance : -1 * moveDistance;
            double intercept = AMapDelta.getIntercept(slope, from);

            for (double j = AMapDelta.getFrom(from, slope);
                 (j > AMapDelta.getTo(to, slope)) == isReverse; j -= moveDistance) {
                if (isStop) {
                    return;
                }
                LatLng latLng;
                if (slope == 0) {
                    latLng = new LatLng(from.latitude, j);
                } else if (slope == Double.MAX_VALUE) {
                    latLng = new LatLng(j, from.longitude);
                } else {
                    latLng = new LatLng(j, (j - intercept) / slope);
                }

                BDMapUtil.moveToCenter(bdMap, latLng);
                marker.setPosition(latLng);

                try {
                    Thread.sleep(timeInterval);
                    synchronized (this) {
                        while (isSuspended) {
                            wait();
                        }
                    }
                } catch (Exception e) {
                    XLog.INSTANCE.d("play Trace Exception: " + e.toString());
                }
            }
        }
        synchronized (this) {
            isRun = false;
        }

        if (traceHandler != null) {
            traceHandler.sendMessage(traceHandler.obtainMessage(0));
        }
    }

    public synchronized void suspend() {
        isSuspended = true;
    }

    public synchronized void resume() {
        isSuspended = false;
        notify();
    }

    public boolean isRun() {
        return isRun;
    }

    public boolean isSuspended() {
        return isSuspended;
    }

    public void setStop(boolean stop) {
        isStop = stop;
    }

    public void setTimeInterval(long timeInterval) {
        this.timeInterval = timeInterval;
    }
}