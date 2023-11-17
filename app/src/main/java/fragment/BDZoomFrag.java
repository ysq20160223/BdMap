package fragment;

import com.as_160213_bd_map.R;
import com.as_160213_bd_map.databinding.BdZoomFragBinding;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.lib_common_ui.base.BaseLazyFragment;

import java.util.Objects;

import utils.BDMapZoom;


// Created by Administrator on 2016/7/20.

public class BDZoomFrag extends BaseLazyFragment<BdZoomFragBinding> {

    private BaiduMap mBdMap;

    @Override
    protected int getContentViewId() {
        return R.layout.bd_zoom_frag;
    }

    @Override
    protected void startLoadData(String from) {
        initData();

        Objects.requireNonNull(getBinding()).bdMapZoom.setBDMap(mBdMap); // 设置百度地图控件
    }

    private void initData() {
        mBdMap = ((MapView) requireActivity().findViewById(R.id.mv_map_activity)).getMap();
    }

    public BDMapZoom getBdMapZoom() {
        return Objects.requireNonNull(getBinding()).bdMapZoom;
    }


}
