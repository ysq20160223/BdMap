package com.as_160213_bd_map

import adp.LocalMapAdp
import adp.ProvinceCityAdp
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.as_160213_bd_map.databinding.ActivityOfflineBinding
import com.baidu.mapapi.SDKInitializer
import com.baidu.mapapi.map.offline.MKOLUpdateElement
import com.baidu.mapapi.map.offline.MKOfflineMap
import com.lib_common_ui.base.BaseActivity
import com.lib_sdk.utils.XLog
import lib.toast.XToast
import java.util.*


class OfflineActivity : BaseActivity() {

    private var binding: ActivityOfflineBinding? = null


    // ----------------------------------------------------------------------------
    private var mkOfflineMap: MKOfflineMap? = null
    private var mInputMethodManager: InputMethodManager? = null

    // 已下载
    private var mLocalMapList: ArrayList<MKOLUpdateElement> = ArrayList()
    private var mLocalMapAdp: LocalMapAdp? = null

    private//
    //
    val data: List<ProvinceCityAdp.ProvinceItem>
        get() {

            val recordProvinces = mkOfflineMap?.offlineCityList

            val provinceItemList = ArrayList<ProvinceCityAdp.ProvinceItem>()
            if (null != recordProvinces) {
                for (i in recordProvinces.indices) {

                    val provinceItem = ProvinceCityAdp.ProvinceItem()
                    provinceItem.p_name = recordProvinces[i].cityName
                    provinceItem.p_id = recordProvinces[i].cityID
                    provinceItem.p_size = formatDataSize(recordProvinces[i].size)
                    val cityList = recordProvinces[i].childCities

                    if (cityList != null) {
                        for (j in cityList.indices) {
                            val cityItem = ProvinceCityAdp.CityItem()
                            cityItem.c_name = cityList[j].cityName
                            cityItem.c_id = cityList[j].cityID
                            cityItem.c_size = formatDataSize(cityList[j].size)
                            provinceItem.cityItemList.add(cityItem)
                        }
                    }
                    provinceItemList.add(provinceItem)
                }
            }

            return provinceItemList
        }

    override fun setContentViewBefore() {
        super.setContentViewBefore()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_offline)
    }

    override fun onViewCreated(savedInstanceState: Bundle?) {


        SDKInitializer.initialize(applicationContext) //


        initObj()

        initMKOfflineMapListener() // 先注册监听后取值

        initView()

        setOnCallBackListener()

    }

    private fun setOnCallBackListener() {
        mLocalMapAdp?.setOnUpdateCallBack { this.updateView() }
    }


    fun onClick(view: View) {
        when (view.id) {
            R.id.btn_city_list -> {
                mInputMethodManager?.hideSoftInputFromWindow(binding?.btnCityList?.windowToken, 0)
                clickCityList()
            }

            R.id.btn_download_manage -> {
                mInputMethodManager?.hideSoftInputFromWindow(
                    binding?.btnDownloadManage?.windowToken,
                    0
                )
                clickDownloadManager()
            }
        }
    }

    private fun startDownload(cityId: Int) {
        clickDownloadManager()
        for ((index, obj) in mLocalMapList.withIndex()) {
            if (cityId == mLocalMapList[index].cityID) {
                XToast.show("已下载或正在下载 - $obj")
                return
            }
        }

        mkOfflineMap?.start(cityId)
        Toast.makeText(this, "开始下载离线地图 cityId: $cityId", Toast.LENGTH_SHORT).show()
        updateView()
    }

    private fun clickCityList() {
        binding?.animateExpandableListView?.visibility = View.VISIBLE
        binding?.layoutDownloadManage?.visibility = View.GONE
    }

    private fun clickDownloadManager() {
        binding?.animateExpandableListView?.visibility = View.GONE
        binding?.layoutDownloadManage?.visibility = View.VISIBLE
    }

    private fun updateView() {
        mLocalMapList.clear()

        val tempList = mkOfflineMap?.allUpdateInfo
        if (tempList != null && tempList.size > 0) {
            mLocalMapList.addAll(tempList)

        }

        mLocalMapAdp?.notifyDataSetChanged()
    }

    private fun initMKOfflineMapListener() {
        mkOfflineMap?.init { type, state ->
            when (type) {
                MKOfflineMap.TYPE_DOWNLOAD_UPDATE -> {
                    val update = mkOfflineMap?.getUpdateInfo(state)
                    // 处理下载进度更新提示
                    if (update != null) {
                        updateView()
                    }
                }

                MKOfflineMap.TYPE_NEW_OFFLINE ->
                    // 有新离线地图安装
                    XLog.d(String.format(Locale.getDefault(), "add offlineMap num:%d", state))

                MKOfflineMap.TYPE_VER_UPDATE -> {
                }
            }// 版本更新提示
            // MKOLUpdateElement e = mOffline.getUpdateInfo(state);
        }
    }


    private fun initView() {

        // 1
        val items = data

        // 2
        val provinceCityAdp = ProvinceCityAdp(this)
        provinceCityAdp.setList(items)
        provinceCityAdp.setOnCallBackListener { this.startDownload(it) }

        // 3
        binding?.animateExpandableListView?.setAdapter(provinceCityAdp)
        binding?.animateExpandableListView?.setGroupIndicator(null)

        binding?.layoutDownloadManage?.visibility = View.GONE

        //

        mLocalMapAdp = LocalMapAdp(getActivity(), mLocalMapList, mkOfflineMap)
        binding?.lvLocalCities?.adapter = mLocalMapAdp

        updateView()
    }

    private fun formatDataSize(size: Int): String {
        return if (size < 1024 * 1024) {
            String.format(Locale.getDefault(), "%dK", size / 1024)
        } else {
            String.format(Locale.getDefault(), "%.2fM", size / (1024 * 1024.0))
        }
    }

    private fun initObj() {
        mInputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        mkOfflineMap = MKOfflineMap()
    }

    //---------------------------------------------------------------------------------------------
    override fun onDestroy() {
        super.onDestroy()
        mkOfflineMap?.destroy()
    }

}
