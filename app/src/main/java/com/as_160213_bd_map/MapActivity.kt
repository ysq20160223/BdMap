package com.as_160213_bd_map

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import bd_map_util.BDMapConfig
import butterknife.BindView
import com.baidu.mapapi.map.BaiduMap
import com.baidu.mapapi.map.MapView
import com.lib_common_ui.base.BaseActivity
import com.lib_sdk.utils.Logcat
import fragment.*
import utils.FragmentUtil
import utils.JniUtil

/*
    // 5C 113.94734,22.529459
    // 深圳北 114.037553,22.616874
    sNode = new BNRoutePlanNode(113.94734, 22.529459, "", "", coType);
    eNode = new BNRoutePlanNode(114.037553, 22.616874, "", "", coType);
 */

class MapActivity : BaseActivity() {

    @JvmField
    @BindView(R.id.mv_map_activity)
    var mMapView: MapView? = null // xml 地图

    @JvmField
    @BindView(R.id.layout_map_activity_top)
    var mLayoutTop: LinearLayout? = null

    @JvmField
    @BindView(R.id.layout_map_activity_bd_nav)
    var mLayoutLeftTop: LinearLayout? = null

    @JvmField
    @BindView(R.id.layout_map_activity_right_top)
    var mLayoutRightTop: LinearLayout? = null

    var mBdMap: BaiduMap? = null

    // ------------------------------------------------------------------------------------
    var topFrag: BDTopFrag? = null

    /**
     * Left Top
     */
//    var bdNavFrag: BDNavFrag? = null

    var rightTopFrag: BDTypeTrafficFrag? = null


    /**
     * Right Bottom
     */
    var bdZoomFrag: BDZoomFrag? = null


    /**
     * Left Bottom
     */
    var bdLocalFrag: BDLocalFrag? = null

    /**
     * Right Center
     */
    var bdDrawFrag: BDDrawFrag? = null


    override fun getContentViewId(): Int {
        return R.layout.activity_main
    }

    override fun onViewCreated(savedInstanceState: Bundle?) {
        initView()
        initData()
        initEvent()

        Logcat.d("JniUtil: " + JniUtil.getStringFromNDK())
    }

    private fun initEvent() {
        mBdMap?.setOnMapLoadedCallback { initCompassPosition() }

        mLayoutTop?.post {
            mLayoutRightTop?.setPadding(0, mLayoutTop?.bottom ?: 0, 0, 0)
            mLayoutLeftTop?.setPadding(0, mLayoutTop?.bottom ?: 0, 0, 0)

            mLayoutRightTop?.visibility = View.VISIBLE
            mLayoutLeftTop?.visibility = View.VISIBLE
        }

    }

    // 指南针以中心为原点
    private fun initCompassPosition() {

        //        Measure.setOnLocalCallBack(mLayoutLeftTop, new Measure.OnLocalCallBack() {
        //            @Override
        //            public void onLoaded(int w, int h, int l, int t, int r, int b,
        //                                 int mL, int mT, int mR, int mB, int pL, int pT, int pR, int pB) {
        //                int x = mBdMap.getCompassPosition().x / 2 + Measure.getScreenW(mActivity) / 30;
        //                int y = b + x;
        //
        ////                Logcat.i("Compass x: " + x + ", y: " + y);
        //
        //                mBdMap.setCompassPosition(new Point(x, y));
        //            }
        //        });
    }

    private fun initData() {
        topFrag = BDTopFrag()
        FragmentUtil.add(supportFragmentManager, R.id.layout_map_activity_top, topFrag)

//        bdNavFrag = BDNavFrag()
//        FragmentUtil.add(supportFragmentManager, R.id.layout_map_activity_bd_nav, bdNavFrag)

        rightTopFrag = BDTypeTrafficFrag()
        FragmentUtil.add(supportFragmentManager, R.id.layout_map_activity_right_top, rightTopFrag)

        bdZoomFrag = BDZoomFrag()
        FragmentUtil.add(supportFragmentManager, R.id.layout_map_activity_bd_zoom, bdZoomFrag)

        bdLocalFrag = BDLocalFrag()
        FragmentUtil.add(supportFragmentManager, R.id.layout_map_activity_bd_local, bdLocalFrag)

        bdDrawFrag = BDDrawFrag()
        FragmentUtil.add(supportFragmentManager, R.id.layout_map_activity_bd_draw, bdDrawFrag)
    }

    private fun initView() {
        mBdMap = BDMapConfig.getBDMap(mMapView)
        BDMapConfig.hideChildAt(mMapView)
    }

    // ------------------------------------------------------------------------------------
    override fun onDestroy() {
        super.onDestroy()

        mMapView?.onDestroy()
        android.os.Process.killProcess(android.os.Process.myPid())
    }

    override fun onResume() {
        super.onResume()
        mMapView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        mMapView?.onPause()
    }

}