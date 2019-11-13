package fragment;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.as_160213_bd_map.MapActivity;
import com.as_160213_bd_map.OfflineActivity;
import com.as_160213_bd_map.R;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.model.LatLng;
import com.lib_common_ui.base.BaseLazyFragment;
import com.lib_sdk.utils.ToastUtil;


import java.util.ArrayList;
import java.util.List;

import baidu.mapapi.clusterutil.clustering.Cluster;
import baidu.mapapi.clusterutil.clustering.ClusterItem;
import baidu.mapapi.clusterutil.clustering.ClusterManager;
import bd_map_util.BDMapAdd;
import bd_map_util.BDMapUtil;
import bean.Info;
import butterknife.BindView;
import butterknife.OnClick;
import handler.TraceHandler;
import utils.TraceRunnable;

// Created by Administrator on 2016/10/24.

public class BDDrawFrag extends BaseLazyFragment {

    @BindView(R.id.iv_draw_trace_line)
    ImageView mIvTraceLine;

    private MapActivity mMapActivity;
    private MapView mMapView;
    private BaiduMap mBdMap;

    private BitmapDescriptor mBmDes;

    private BitmapDescriptor mRedTexture;
    private BitmapDescriptor mBlueTexture;
    private BitmapDescriptor mGreenTexture;

    private TraceHandler mTraceHandler;

    private BitmapDescriptor mBmDesMove;

    private ClusterManager<MyClusterItem> mClusterManager;

    private MapStatusUpdate mMapStatusUpdateBefore;
    private MapStatusUpdate mMapStatusUpdateAfter;
    private List<MyClusterItem> mMyClusterItemList = new ArrayList<>();

    private LatLng mBoundCenter;
    private TraceRunnable mTraceRunnable;
    private List<BitmapDescriptor> mTraceList;

    @Override
    protected int getContentViewId() {
        return R.layout.bd_draw_frag;
    }

    @Override
    protected void startLoadData(String from) {
        initData();

        initEvent();
    }

    @SuppressWarnings("unused")
    @OnClick({R.id.iv_draw_trace_polygon, R.id.iv_draw_trace_line, R.id.iv_draw_trace_cluster,
            R.id.iv_draw_trace_offlineMap})
    void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.iv_draw_trace_polygon:
                clearItems();
                drawGraph();
                break;

            case R.id.iv_draw_trace_line:
                mBdMap.setMapStatus(mMapStatusUpdateBefore);
                addLine();
                traceRun();
                break;

            case R.id.iv_draw_trace_cluster:
                mBdMap.animateMapStatus(mMapStatusUpdateBefore);

                clearItems();
                addItems();

                mBdMap.animateMapStatus(mMapStatusUpdateAfter);
                break;

            case R.id.iv_draw_trace_offlineMap:
                intent = new Intent(getActivity(), OfflineActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void addLine() {
        if (mTraceList.size() == 0) {
            mTraceList.add(mGreenTexture);
            BDMapAdd.polygon(mBdMap, Info.Companion.getList(), 8, mTraceList);
        }
    }

    private void traceRun() {
        if (mTraceRunnable == null) {
            Marker markerMove = BDMapAdd.overlay(mBdMap, Info.Companion.getList().get(0), mBmDesMove, true, 0.5f, 0.5f);
            mTraceRunnable = new TraceRunnable(mBdMap, mTraceHandler, Info.Companion.getList(), markerMove, 0.0003, 20);
        }

        if (mTraceRunnable.isRun() && !mTraceRunnable.isSuspended()) {
            mTraceRunnable.suspend();
            mIvTraceLine.setSelected(false);
        } else if (mTraceRunnable.isRun() && mTraceRunnable.isSuspended()) {
            mTraceRunnable.resume();
            mIvTraceLine.setSelected(true);
        } else {
            new Thread(mTraceRunnable).start();
            mIvTraceLine.setSelected(true);
        }

    }

    private void drawGraph() {
        // ---
        BDMapAdd.overlays(mBdMap, Info.Companion.getList(), mBmDes);
        mBdMap.animateMapStatus(mMapStatusUpdateBefore);

        // ---
        for (int i = 0; i < Info.Companion.getList().size(); i++) {
            BDMapAdd.circle(mBdMap, Info.Companion.getList().get(i), 200, 3, 0xFFff0000, 0x6645c01a);
        }

        // ---
        List<BitmapDescriptor> list = new ArrayList<>();
        list.add(mGreenTexture); // blueTexture redTexture
        BDMapAdd.polygon(mBdMap, Info.Companion.getList(), 8, list);
//        BDMapAdd.polygon(bdMap, Info.list, 1, 0xFF0088ff, 0x210088ff); // 围栏

        // ---
        BDMapAdd.text(mBdMap, mBoundCenter, "Du Wei", 30, 0xFFff00ff, 0xAAffff00, -30);
    }

    /**
     * 每个Marker点，包含Marker点坐标以及图标
     */
    public class MyClusterItem implements ClusterItem {

        private LatLng mLatLng;

        MyClusterItem(LatLng latLng) {
            this.mLatLng = latLng;
        }

        @Override
        public LatLng getPosition() {
            return mLatLng;
        }

        @Override
        public BitmapDescriptor getBitmapDescriptor() {
            return BitmapDescriptorFactory.fromResource(R.mipmap.check_descriptor);
        }
    }

    private void addItems() {

        for (int i = 0; i < Info.Companion.getList().size(); i++) {
            LatLng latLng = Info.Companion.getList().get(i);
            mMyClusterItemList.add(new MyClusterItem(latLng));
        }
        mClusterManager.addItems(mMyClusterItemList);
    }

    public void clearItems() {
        mClusterManager.clearItems();
        mMyClusterItemList.clear();
        mBdMap.clear();
    }

    private void initEvent() {
        mBdMap.setOnMapStatusChangeListener(mClusterManager);
        mBdMap.setOnMarkerClickListener(mClusterManager);

        mClusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<MyClusterItem>() {
            @Override
            public boolean onClusterClick(Cluster<MyClusterItem> cluster) {
                ToastUtil.Companion.text(mActivity, "getSize : " + cluster.getSize());
                return false;
            }
        });

        mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<MyClusterItem>() {
            @Override
            public boolean onClusterItemClick(MyClusterItem item) {
                ToastUtil.Companion.text(mActivity, item.getPosition() + "");
                return false;
            }
        });

        //
        mClusterManager.setOnMapStatusListener(new ClusterManager.OnMapStatusListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChange(MapStatus mapStatus) {
                mMapActivity.getBdLocalFrag().setMapStatusChange(mapStatus);
            }

            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {
                mMapActivity.getBdZoomFrag().getBdMapZoom().updateZoom();
            }
        });

    }

    private void initData() {
        mMapActivity = (MapActivity) getActivity();
        mMapView = getActivity().findViewById(R.id.mv_map_activity);

        mBdMap = mMapView.getMap();
        mBmDes = BitmapDescriptorFactory.fromResource(R.mipmap.check_descriptor);

        mRedTexture = BitmapDescriptorFactory.fromAsset("ic_red_arrow.png");
        mBlueTexture = BitmapDescriptorFactory.fromAsset("ic_blue_arrow.png");
        mGreenTexture = BitmapDescriptorFactory.fromAsset("ic_green_arrow.png");

        mTraceList = new ArrayList<>();
        mBmDesMove = BitmapDescriptorFactory.fromResource(R.mipmap.bm_des_move);
        mTraceHandler = new TraceHandler(mIvTraceLine);

        mClusterManager = new ClusterManager<>(mActivity, mBdMap);

        mBoundCenter = BDMapUtil.boundCenter(Info.Companion.getList());

        mMapStatusUpdateBefore = MapStatusUpdateFactory.newLatLngZoom(mBoundCenter, 15.5f);
        mMapStatusUpdateAfter = MapStatusUpdateFactory.newLatLngZoom(mBoundCenter, 15.500001f);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBmDes.recycle();

        mRedTexture.recycle();
        mBlueTexture.recycle();
        mGreenTexture.recycle();

        mTraceHandler.removeCallbacksAndMessages(null);
    }

}
