package fragment;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.as_160213_bd_map.MapActivity;
import com.as_160213_bd_map.R;
import com.as_160213_bd_map.databinding.BdTypeTrafficFragBinding;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.lib_common_ui.base.BaseLazyFragment;
import com.lib_sdk.utils.XLog;

import dlg_frag.BDTypeDlgFrag;

// Created by Administrator on 2016/7/19.

public class BDTypeTrafficFrag extends BaseLazyFragment {

    private BdTypeTrafficFragBinding bdTypeTrafficFragBinding;

    private MapActivity mMapActivity;
    private BaiduMap mBdMap;

    private BDTypeDlgFrag mapTypeDlgFrag;


    @Override
    protected int getContentViewId() {
        return R.layout.bd_type_traffic_frag;
    }

    @Override
    public void onCreateViewBind(@Nullable ViewGroup viewGroup, @NonNull View view) {
        super.onCreateViewBind(viewGroup, view);
        bdTypeTrafficFragBinding = (BdTypeTrafficFragBinding) getBinding();
    }

    @Override
    protected void startLoadData(String from) {
        initData();

        initEvent();
    }

    private void initEvent() {
        if (null != bdTypeTrafficFragBinding) {
            bdTypeTrafficFragBinding.ivMapType.setOnClickListener(v ->
                    mapTypeDlgFrag.show(getActivity().getSupportFragmentManager(), ""));

            bdTypeTrafficFragBinding.ivMapTraffic.setOnClickListener(v -> {
                if (mBdMap.isTrafficEnabled()) {
                    XLog.d("关闭实时路况");
                } else {
                    XLog.d("开启实时路况");
                }
                mBdMap.setTrafficEnabled(!mBdMap.isTrafficEnabled());
                bdTypeTrafficFragBinding.ivMapTraffic.setSelected(mBdMap.isTrafficEnabled());
            });

            bdTypeTrafficFragBinding.ivClearRoute.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                ((MapActivity) getActivity()).getBdNavFrag().getRouteList().clear();
//                mBdMap.clear(); //
//                mMapActivity.getBdDrawFrag().clearItems();
//                XLog.d("清空图层数据");
                }
            });
        }
    }

    private void initData() {
        mMapActivity = (MapActivity) getActivity();
        mBdMap = ((MapView) mMapActivity.findViewById(R.id.mv_map_activity)).getMap();

        mapTypeDlgFrag = new BDTypeDlgFrag();
    }

}
