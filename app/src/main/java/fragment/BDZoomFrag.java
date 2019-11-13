package fragment;

import com.as_160213_bd_map.R;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.lib_common_ui.base.BaseLazyFragment;


import butterknife.BindView;

import utils.BDMapZoom;


// Created by Administrator on 2016/7/20.

public class BDZoomFrag extends BaseLazyFragment {

    // 右下角部分
    @BindView(R.id.bdMapZoom)
    BDMapZoom mBdMapZoom; // 缩放控件

    private BaiduMap mBdMap;

    @Override
    protected int getContentViewId() {
        return R.layout.bd_zoom_frag;
    }

    @Override
    protected void startLoadData(String from) {
        initData();

        mBdMapZoom.setBDMap(mBdMap); // 设置百度地图控件
    }

    private void initData() {
        mBdMap = ((MapView) getActivity().findViewById(R.id.mv_map_activity)).getMap();
    }

    public BDMapZoom getBdMapZoom() {
        return mBdMapZoom;
    }


}
