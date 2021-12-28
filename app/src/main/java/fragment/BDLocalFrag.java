package fragment;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.as_160213_bd_map.R;
import com.as_160213_bd_map.databinding.BdLocalFragBinding;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.model.LatLng;
import com.lib_common_ui.base.BaseLazyFragment;

import bd_map_util.BDMapConfig;
import utils.BDLocalListener;
import utils.FormatUtil;
import utils.OrientListener;

// Created by Administrator on 2016/7/20.

public class BDLocalFrag extends BaseLazyFragment {

    private BdLocalFragBinding bdLocalFragBinding = null;

    private BaiduMap mBdMap;

    private MyLocationConfiguration.LocationMode mLocalMode;

    private LocationClient mLocalClient;
    private OrientListener mOrientListener; // 定位监听

    private BDLocalListener mBdLocalListener;
    private float mLocalOrient;

    private BitmapDescriptor mBmDesLocal; // 定位


    @Override
    protected int getContentViewId() {
        return R.layout.bd_local_frag;
    }

    @Override
    public void onCreateViewBind(@Nullable ViewGroup viewGroup, @NonNull View view) {
        super.onCreateViewBind(viewGroup, view);
        bdLocalFragBinding = (BdLocalFragBinding) getBinding();
    }

    @Override
    protected void startLoadData(String from) {
        initData();

        initEvent();

        initScalePosition();
    }

    private void setMyLocationConfiguration() {
        mBdMap.setMyLocationConfiguration(new MyLocationConfiguration(mLocalMode, true, mBmDesLocal));
    }

    private void setLocation(boolean isFirstLocal) {
        bdLocalFragBinding.ivBdLocalMapMode.setImageResource(R.mipmap.map_mode_normal);
        LatLng latLng = new LatLng(mBdLocalListener.getLocalLat(), mBdLocalListener.getLocalLng());

        if (isFirstLocal) { // 15.0f 约 500m - 17.0f 约 100m
            mBdMap.setMapStatus(MapStatusUpdateFactory.newLatLngZoom(latLng, 17.9f));
        } else {
            mBdMap.setMapStatus(MapStatusUpdateFactory.newLatLng(latLng));
        }
    }

    // 当前定位点是否偏移了屏幕中心点
    public void setMapStatusChange(MapStatus mapStatus) {

        String tempLat = FormatUtil.format(mapStatus.target.latitude, "#.0000");
        String tempLon = FormatUtil.format(mapStatus.target.longitude, "#.0000");

        String localLat = FormatUtil.format(mBdLocalListener.getLocalLat(), "#.0000");
        String localLon = FormatUtil.format(mBdLocalListener.getLocalLng(), "#.0000");

        bdLocalFragBinding.ivBdLocalMapLocal.setSelected(localLat.equals(tempLat) && localLon.equals(tempLon));
    }

    private void initEvent() {
        mOrientListener.setOnOrientListener(orient -> {
            mLocalOrient = orient;
            mBdLocalListener.setLocalOrient(mLocalOrient);
        });

        if (null != bdLocalFragBinding) {
            bdLocalFragBinding.ivBdLocalMapLocal.setOnClickListener(v -> setLocation(false));

            bdLocalFragBinding.ivBdLocalMapMode.setOnClickListener(v -> {
                switch (mLocalMode) {
                    case NORMAL:
                        bdLocalFragBinding.ivBdLocalMapMode.setImageResource(R.mipmap.map_mode_compass);
                        mLocalMode = MyLocationConfiguration.LocationMode.COMPASS;
                        setMyLocationConfiguration();
                        break;

                    case COMPASS:
                        bdLocalFragBinding.ivBdLocalMapMode.setImageResource(R.mipmap.map_mode_follow);
                        mLocalMode = MyLocationConfiguration.LocationMode.FOLLOWING;
                        setMyLocationConfiguration();
                        break;

                    case FOLLOWING:
                        bdLocalFragBinding.ivBdLocalMapMode.setImageResource(R.mipmap.map_mode_normal);
                        mLocalMode = MyLocationConfiguration.LocationMode.NORMAL;
                        setMyLocationConfiguration();

                        MapStatus ms = new MapStatus.Builder(mBdMap.getMapStatus()).rotate(0)
                                .overlook(0).build();
                        mBdMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(ms), 750);
                        break;
                }
            });
        }
    }

    private void initData() {

        MapView mMapView = (MapView) getActivity().findViewById(R.id.mv_map_activity);
        mBdMap = mMapView.getMap();

        mBmDesLocal = BitmapDescriptorFactory.fromResource(R.mipmap.map_bm_des_local);
        mLocalMode = MyLocationConfiguration.LocationMode.NORMAL;
        MyLocationConfiguration localConfig = new MyLocationConfiguration(mLocalMode, true, mBmDesLocal);
        mBdMap.setMyLocationConfiguration(localConfig);

        mOrientListener = new OrientListener(getActivity()); // 方向传感器

        mBdLocalListener = new BDLocalListener(getActivity(), mBdMap, mLocalOrient); // 定位

        // 定位服务的客户端。目前只支持在主线程中启动
        mLocalClient = new LocationClient(getActivity(), BDMapConfig.getLocationClientOption());
        mLocalClient.registerLocationListener(mBdLocalListener); // 注册定位监听

        bdLocalFragBinding.ivBdLocalMapLocal.setSelected(true);
    }


    private void initScalePosition() {
//        Measure.setOnGlobalCallBack(mVMapLocal, new Measure.OnGlobalCallBack() {
//            @Override
//            public void onLoaded(int w, int h, int l, int t, int r, int b,
//                                 int mL, int mT, int mR, int mB, int pL, int pT, int pR, int pB) {
//                int x = r + mL;
//                int y = b - mB - mMapView.getChildAt(3).getHeight();
//
//                mMapView.getChildAt(3).setX(x);
//                mMapView.getChildAt(3).setY(y);
//                mMapView.getChildAt(3).setVisibility(View.VISIBLE);
//                mMapView.getChildAt(3).setAlpha(1f);
//            }
//        });

    }

    // --------------------------------------------------------------------------------------
    @Override
    public void onStart() {
        super.onStart();
        if (!mLocalClient.isStarted()) {
            mLocalClient.start();
            mOrientListener.start();
        }
        mBdMap.setMyLocationEnabled(true);
    }

    @Override
    public void onStop() {
        super.onStop();
        mLocalClient.stop();
        mOrientListener.stop();
        mBdMap.setMyLocationEnabled(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBmDesLocal.recycle();
    }


    // --------------------------------------------------------------------------------------

    public BDLocalListener getBdLocalListener() {
        return mBdLocalListener;
    }

}
