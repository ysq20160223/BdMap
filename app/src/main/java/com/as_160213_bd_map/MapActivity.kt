package com.as_160213_bd_map

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import bd_map_util.BDMapConfig
import com.as_160213_bd_map.databinding.ActivityMainBinding
import com.baidu.mapapi.map.BaiduMap
import com.lib_common_ui.base.BaseActivity
import com.lib_sdk.utils.XLog
import fragment.*
import utils.FragmentUtil
import utils.JniUtil

/*
    // 5C 113.94734,22.529459
    // 深圳北 114.037553,22.616874
    sNode = new BNRoutePlanNode(113.94734, 22.529459, "", "", coType);
    eNode = new BNRoutePlanNode(114.037553, 22.616874, "", "", coType);
 */

open class MapActivity : BaseActivity() {


    var mBdMap: BaiduMap? = null

    // ------------------------------------------------------------------------------------
    var topFrag: BDTopFrag? = null

    /**
     * Left Top
     */
    var bdNavFrag: BDNavFrag? = null

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

    var binding: ActivityMainBinding? = null

    override fun setContentViewBefore() {
        super.setContentViewBefore()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    }


    override fun onViewCreated(savedInstanceState: Bundle?) {
        initView()
        initData()
        initEvent()

        XLog.d("JniUtil: " + JniUtil.getStringFromNDK())
    }

    private fun initEvent() {
        mBdMap?.setOnMapLoadedCallback { initCompassPosition() }

        binding?.layoutMapActivityTop?.post {
            binding?.layoutMapActivityRightTop?.setPadding(
                0,
                binding?.layoutMapActivityTop?.bottom ?: 0,
                0,
                0
            )
            binding?.layoutMapActivityBdNav?.setPadding(
                0,
                binding?.layoutMapActivityTop?.bottom ?: 0,
                0,
                0
            )

            binding?.layoutMapActivityRightTop?.visibility = View.VISIBLE
            binding?.layoutMapActivityBdNav?.visibility = View.VISIBLE
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

        bdNavFrag = BDNavFrag()
        FragmentUtil.add(supportFragmentManager, R.id.layout_map_activity_bd_nav, bdNavFrag)

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
        mBdMap = BDMapConfig.getBDMap(binding?.mvMapActivity)
        BDMapConfig.hideChildAt(binding?.mvMapActivity)
    }

    // ------------------------------------------------------------------------------------
    override fun onDestroy() {
        super.onDestroy()

        binding?.mvMapActivity?.onDestroy()
        android.os.Process.killProcess(android.os.Process.myPid())
    }

    override fun onResume() {
        super.onResume()
        binding?.mvMapActivity?.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding?.mvMapActivity?.onPause()
    }

}