package fragment;

import android.view.View;
import android.widget.ImageView;

import com.as_160213_bd_map.MapActivity;
import com.as_160213_bd_map.R;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.lib_common_ui.base.BaseLazyFragment;
import com.lib_sdk.utils.ToastUtil;


import butterknife.BindView;
import butterknife.OnClick;
import dlg_frag.BDTypeDlgFrag;

// Created by Administrator on 2016/7/19.

public class BDTypeTrafficFrag extends BaseLazyFragment {

    @BindView(R.id.iv_mapTraffic)
    ImageView mIvMapTraffic; // 实时路况

    private MapActivity mMapActivity;
    private BaiduMap mBdMap;

    private BDTypeDlgFrag mapTypeDlgFrag;


    @Override
    protected int getContentViewId() {
        return R.layout.bd_type_traffic_frag;
    }

    @Override
    protected void startLoadData(String from) {
        initData();
    }


    @OnClick({R.id.iv_mapType, R.id.iv_mapTraffic, R.id.iv_clearRoute})
    void setOnClick(View view) {
        switch (view.getId()) {
            case R.id.iv_mapType: // 标准地图 或 卫星地图
                mapTypeDlgFrag.show(getActivity().getSupportFragmentManager(), "");
                break;

            case R.id.iv_mapTraffic:
                if (mBdMap.isTrafficEnabled()) {
                    ToastUtil.Companion.text(mActivity, "关闭实时路况");
                } else {
                    ToastUtil.Companion.text(mActivity, "开启实时路况");
                }
                mBdMap.setTrafficEnabled(!mBdMap.isTrafficEnabled());
                mIvMapTraffic.setSelected(mBdMap.isTrafficEnabled());
                break;

            case R.id.iv_clearRoute: // 清除所有 marker
//                ((MapActivity) getActivity()).getBdNavFrag().getRouteList().clear();
//                mBdMap.clear(); //
//                mMapActivity.getBdDrawFrag().clearItems();
//                ToastUtil.Companion.text(mActivity, "清空图层数据");
                break;
        }
    }

    private void initData() {
        mMapActivity = (MapActivity) getActivity();
        mBdMap = ((MapView) mMapActivity.findViewById(R.id.mv_map_activity)).getMap();

        mapTypeDlgFrag = new BDTypeDlgFrag();
    }

}
