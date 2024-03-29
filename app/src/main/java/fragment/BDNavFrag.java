package fragment;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.as_160213_bd_map.MapActivity;
import com.as_160213_bd_map.R;
import com.as_160213_bd_map.databinding.BdNavFragBinding;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BNRoutePlanNode.CoordinateType;
import com.baidu.navisdk.adapter.BaiduNaviManagerFactory;
import com.baidu.navisdk.adapter.IBNRoutePlanManager;
import com.baidu.navisdk.adapter.IBNTTSManager;
import com.baidu.navisdk.adapter.IBaiduNaviManager;
import com.lib_common_ui.base.BaseLazyFragment;
import com.lib_sdk.utils.ExtStg;
import com.lib_sdk.utils.XLog;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import bd_map_util.BDMapAdd;
import lib.toast.XToast;
import navi.DemoGuideActivity;
import navi.NormalUtils;


// Created by y on 2016/7/20.

@SuppressWarnings("unused")
public class BDNavFrag extends BaseLazyFragment<BdNavFragBinding> {


    private BaiduMap mBdMap;

    // initNavigation
    private String mSDCardPath = null;
    private final String APP_FOLDER_NAME = "TestMap";

    public static final String ROUTE_PLAN_NODE = "routePlanNode";

    public static List<Activity> activityList = new LinkedList<>(); // 添加导航 Activity

    private final List<BNRoutePlanNode> mRouteList = new ArrayList<>();

    private final BitmapDescriptor[] mBmDescriptors = new BitmapDescriptor[4];

    private BitmapDescriptor mBmDesGeo;

    private MapActivity mMapActivity;

    private GeoCoder mGeoCoder; // 搜索模块，也可去掉地图模块独立使用

    private ProgressDialog mProgressDialog;

    //
    private static final String[] authBaseArr = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    private static final int authBaseRequestCode = 1;

    private boolean hasInitSuccess = false;

    private BNRoutePlanNode mStartNode = null;

    @Override
    protected int getContentViewId() {
        return R.layout.bd_nav_frag;
    }


    @Override
    protected void startLoadData(String from) {
        initView();

        initData();

        initEvent();

        if (initDirs()) { // 导航部分
//            initNavi();
        } else {
            XLog.d("External Storage Not Exist");
        }
    }

    private boolean hasBasePhoneAuth() {
        PackageManager pm = requireActivity().getPackageManager();
        for (String auth : authBaseArr) {
            if (pm.checkPermission(auth, requireActivity().getPackageName()) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private void initNavi() {
        // 申请权限
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            if (!hasBasePhoneAuth()) {
                this.requestPermissions(authBaseArr, authBaseRequestCode);
                return;
            }
        }

        BaiduNaviManagerFactory.getBaiduNaviManager().init(getActivity(),
                mSDCardPath, APP_FOLDER_NAME, new IBaiduNaviManager.INaviInitListener() {

                    @Override
                    public void onAuthResult(int status, String msg) {
                        String result;
                        if (0 == status) {
                            result = "key校验成功!";
                        } else {
                            result = "key校验失败, " + msg;
                        }
                        Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
                        XLog.d(result);
                    }

                    @Override
                    public void initStart() {
                        Toast.makeText(getActivity(), "百度导航引擎初始化开始", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void initSuccess() {
                        Toast.makeText(getActivity(), "百度导航引擎初始化成功", Toast.LENGTH_SHORT).show();
                        hasInitSuccess = true;
                        // 初始化tts
                        initTTS();
                    }

                    @Override
                    public void initFailed(int i) {
                        Toast.makeText(getActivity(), "百度导航引擎初始化失败", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private String getSdcardDir() {
        if (Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().toString();
        }
        return null;
    }

    private void initTTS() {
        // 使用内置TTS
        BaiduNaviManagerFactory.getTTSManager().initTTS(requireActivity().getApplicationContext(),
                getSdcardDir(), APP_FOLDER_NAME, NormalUtils.getTTSAppID());

        // 不使用内置TTS
//         BaiduNaviManagerFactory.getTTSManager().initTTS(mTTSCallback);

        // 注册同步内置tts状态回调
        BaiduNaviManagerFactory.getTTSManager().setOnTTSStateChangedListener(
                new IBNTTSManager.IOnTTSPlayStateChangedListener() {
                    @Override
                    public void onPlayStart() {
                        XLog.d("onPlayStart");
                    }

                    @Override
                    public void onPlayEnd(String speechId) {
                        XLog.d("speechId: " + speechId);
                    }

                    @Override
                    public void onPlayError(int code, String message) {
                        XLog.d("code: " + code + ", message: " + message);
                    }
                }
        );

        // 注册内置tts 异步状态消息
        BaiduNaviManagerFactory.getTTSManager().setOnTTSStateChangedHandler(
                new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message msg) {
                        XLog.d("msg: " + msg);
                    }
                }
        );
    }

    private List<BNRoutePlanNode> geneList(int coType) {
        if (!hasInitSuccess) {
            Toast.makeText(getActivity(), "还未初始化!", Toast.LENGTH_SHORT).show();
        }

        BNRoutePlanNode sNode = new BNRoutePlanNode(116.30142, 40.05087, "百度大厦", "百度大厦", coType);
        BNRoutePlanNode eNode = new BNRoutePlanNode(116.39750, 39.90882, "北京天安门", "北京天安门", coType);
        switch (coType) {
            case CoordinateType.GCJ02: {
                sNode = new BNRoutePlanNode(116.30142, 40.05087, "百度大厦", "百度大厦", coType);
                eNode = new BNRoutePlanNode(116.39750, 39.90882, "北京天安门", "北京天安门", coType);
                break;
            }
            case CoordinateType.WGS84: {
                sNode = new BNRoutePlanNode(116.300821, 40.050969, "百度大厦", "百度大厦", coType);
                eNode = new BNRoutePlanNode(116.397491, 39.908749, "北京天安门", "北京天安门", coType);
                break;
            }
            case CoordinateType.BD09_MC: {
                sNode = new BNRoutePlanNode(12947471, 4846474, "百度大厦", "百度大厦", coType);
                eNode = new BNRoutePlanNode(12958160, 4825947, "北京天安门", "北京天安门", coType);
                break;
            }
            case CoordinateType.BD09LL: {
//                sNode = new BNRoutePlanNode(116.30784537597782, 40.057009624099436, "百度大厦", "百度大厦", coType);
//                eNode = new BNRoutePlanNode(116.40386525193937, 39.915160800132085, "北京天安门", "北京天安门", coType);

                // 5C 113.94734,22.529459
                // 深圳北 114.037553,22.616874
                sNode = new BNRoutePlanNode(113.94734, 22.529459, "百度大厦", "百度大厦", coType);
                eNode = new BNRoutePlanNode(114.037553, 22.616874, "北京天安门", "北京天安门", coType);
                break;
            }
            default:
                break;
        }

        mStartNode = sNode;

        List<BNRoutePlanNode> list = new ArrayList<>();
        list.add(sNode);
        list.add(eNode);

        return list;
    }

    private void routePlanToNavi(List<BNRoutePlanNode> list) {
        BaiduNaviManagerFactory.getRoutePlanManager().routeplanToNavi(
                list,
                IBNRoutePlanManager.RoutePlanPreference.ROUTE_PLAN_PREFERENCE_DEFAULT,
                null,
                new Handler(Looper.getMainLooper()) {
                    @Override
                    public void handleMessage(Message msg) {
                        switch (msg.what) {
                            case IBNRoutePlanManager.MSG_NAVI_ROUTE_PLAN_START:
                                Toast.makeText(getActivity(), "算路开始", Toast.LENGTH_SHORT).show();
                                XLog.d("算路开始");
                                break;

                            case IBNRoutePlanManager.MSG_NAVI_ROUTE_PLAN_SUCCESS:
                                Toast.makeText(getActivity(), "算路成功", Toast.LENGTH_SHORT).show();
                                XLog.d("算路成功");
                                break;

                            case IBNRoutePlanManager.MSG_NAVI_ROUTE_PLAN_FAILED:
                                Toast.makeText(getActivity(), "算路失败", Toast.LENGTH_SHORT)
                                        .show();
                                XLog.d("算路失败");
                                break;

                            case IBNRoutePlanManager.MSG_NAVI_ROUTE_PLAN_TO_NAVI:
                                Toast.makeText(getActivity(), "算路成功准备进入导航", Toast.LENGTH_SHORT)
                                        .show();
                                XLog.d("算路成功准备进入导航");

                                Intent intent = new Intent(getActivity(),
                                        DemoGuideActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable(ROUTE_PLAN_NODE, mStartNode);
                                intent.putExtras(bundle);
                                startActivity(intent);
                                break;

                            default:
                                break;
                        }
                    }
                });
    }

    private class MyOnGetGeoCoderResultListener implements OnGetGeoCoderResultListener {
        @Override
        public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

            dismissProgressDialog();

            mBdMap.clear();

            if (geoCodeResult == null || geoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
                XLog.d("No Find");
                return;
            }

            LatLng latLng = geoCodeResult.getLocation();

            BDMapAdd.overlay(mBdMap, latLng, mBmDesGeo, null, true, true, 0.5f, 0.1f);

            View v = View.inflate(getActivity(), R.layout.info_window, null);
            TextView tv = v.findViewById(R.id.tv_infoWindow_title);
            String text = geoCodeResult.getAddress();
            tv.setText(text);

            BDMapAdd.infoWindow(getActivity(), mBdMap, latLng, v, null);
        }

        @Override
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
            dismissProgressDialog();

            View v = View.inflate(getActivity(), R.layout.info_window, null);
            TextView tv = v.findViewById(R.id.tv_infoWindow_title);
            tv.setText(reverseGeoCodeResult.getAddress());

            BDMapAdd.infoWindow(getActivity(), mBdMap, reverseGeoCodeResult.getLocation(), v, null);
        }
    }

    private void dismissProgressDialog() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mProgressDialog.dismiss();
            }
        }, 350);
    }

    private Bitmap getViewBitmap(View v) {
        v.setDrawingCacheEnabled(true);

        v.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());

        v.buildDrawingCache();

        return Bitmap.createBitmap(v.getDrawingCache());
    }

    private void initEvent() {
        mBdMap.setOnMapLongClickListener(latLng -> {
            if (mRouteList.size() < 4) {
                BitmapDescriptor bmDes = mBmDescriptors[mRouteList.size()];
                Bundle bundle = new Bundle();
                bundle.putInt("position", mRouteList.size());
                BDMapAdd.overlay(mBdMap, latLng, bmDes, bundle, true, false, 0.5f, 0.1f);
                XLog.d("设置 " + (mRouteList.size() + 1) + " 成功");
                XToast.INSTANCE.show("" + latLng);


                mRouteList.add(new BNRoutePlanNode(latLng.longitude, latLng.latitude, "", "",
                        CoordinateType.BD09LL));
            } else {
                XLog.d();
                XToast.INSTANCE.show("最多只能设置四个");
            }
        });

        mBdMap.setOnMarkerClickListener(marker -> {
            Bundle bundle = marker.getExtraInfo();
            if (bundle != null) {
                int position = bundle.getInt("position");
                BNRoutePlanNode node = mRouteList.get(position);
                final LatLng latLng = new LatLng(node.getLongitude(), node.getLatitude());
                mBdMap.animateMapStatus(MapStatusUpdateFactory.newLatLng(latLng), 300);

                //
                mProgressDialog.show();
                mGeoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(latLng));
            }
            return true;
        });

        if (null != getBinding()) {
            getBinding().ivMyNav.setOnClickListener(v -> {
                if (mRouteList.size() > 0) {
                    // 1, 从5C到深圳北
//                    routePlanToNavi(geneList(BNRoutePlanNode.CoordinateType.BD09LL));

                    // 2
                    LatLng latLng = Objects.requireNonNull(mMapActivity.getBdLocalFrag()).getBdLocalListener().getLocalLatLng();
                    double lon = latLng.longitude;
                    double lat = latLng.latitude;

                    mStartNode = new BNRoutePlanNode(lon, lat, "s", "ss", CoordinateType.BD09LL);

                    mRouteList.add(0, mStartNode);
                    routePlanToNavi(mRouteList);

//                        invokeNav(lon, lat); // 调用外部导航
                } else {
                    XLog.d("长按地图确定目的地");
                }
            });

            getBinding().ivMyGeo.setOnClickListener(v -> {
                mProgressDialog.show();
                mGeoCoder.geocode(new GeoCodeOption().city("深圳市").address("福田区红荔西路7127号"));
            });
        }

        mGeoCoder.setOnGetGeoCodeResultListener(new MyOnGetGeoCoderResultListener());

    }

    private void initData() {

        activityList.add(getActivity());

        int[] resId = {R.mipmap.route_descriptor_01, R.mipmap.route_descriptor_02,
                R.mipmap.route_descriptor_03, R.mipmap.route_descriptor_04};
        for (int i = 0; i < mBmDescriptors.length; i++) {
            mBmDescriptors[i] = BitmapDescriptorFactory.fromResource(resId[i]);
        }

        mGeoCoder = GeoCoder.newInstance();
        mBmDesGeo = BitmapDescriptorFactory.fromResource(R.mipmap.check_descriptor);

        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage("Loading");
    }

    private void initView() {
        mMapActivity = (MapActivity) getActivity();
        mBdMap = ((MapView) requireActivity().findViewById(R.id.mv_map_activity)).getMap();
    }

//    private void invokeNav(double lon, double lat) {
//        LatLng to = mRouteList.get(0);
//        LatLng from = new LatLng(lat, lon);
//
//        BDInvoke.nativeNav(mActivity, from, to);
//        BDInvoke.walkingNav(mActivity, from, to);
//        BDInvoke.bikingNav(mActivity, from, to);
//    }


    private boolean initDirs() {
        File file = ExtStg.Companion.getCacheDir(mMapActivity);
        if (file != null) {
            mSDCardPath = file.getPath();
        }
        if (mSDCardPath == null) {
            return false;
        }
        File f = new File(mSDCardPath, APP_FOLDER_NAME);
        if (!f.exists()) {
            try {
                f.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }


    public List<BNRoutePlanNode> getRouteList() {
        return mRouteList;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        for (BitmapDescriptor bitmapDescriptor : mBmDescriptors) {
            bitmapDescriptor.recycle();
        }

    }


}
