package com.as_160213_bd_map

import com.baidu.mapapi.SDKInitializer
import com.lib_sdk.base.BaseApplication
import com.lib_sdk.utils.XLog
import lib.toast.XToast

@Suppress("unused")
class MapApp : BaseApplication() {

    override fun onCreate() {
        super.onCreate()

        XLog.d()

        XToast.init(this)

        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        SDKInitializer.initialize(applicationContext)

    }

}
