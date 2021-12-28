package fragment;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.as_160213_bd_map.R;
import com.as_160213_bd_map.databinding.BdZoomFragBinding;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.lib_common_ui.base.BaseLazyFragment;


import utils.BDMapZoom;


// Created by Administrator on 2016/7/20.

public class BDZoomFrag extends BaseLazyFragment {

    private BdZoomFragBinding bdZoomFragBinding;

    private BaiduMap mBdMap;

    @Override
    protected int getContentViewId() {
        return R.layout.bd_zoom_frag;
    }

    @Override
    public void onCreateViewBind(@Nullable ViewGroup viewGroup, @NonNull View view) {
        super.onCreateViewBind(viewGroup, view);
        bdZoomFragBinding = (BdZoomFragBinding) getBinding();
    }

    @Override
    protected void startLoadData(String from) {
        initData();

        bdZoomFragBinding.bdMapZoom.setBDMap(mBdMap); // 设置百度地图控件
    }

    private void initData() {
        mBdMap = ((MapView) getActivity().findViewById(R.id.mv_map_activity)).getMap();
    }

    public BDMapZoom getBdMapZoom() {
        return bdZoomFragBinding.bdMapZoom;
    }


}
