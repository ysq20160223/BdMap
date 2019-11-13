package dlg_frag;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.DialogFragment;

import com.as_160213_bd_map.R;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

// Created by Administrator on 2016/7/19.

public class BDTypeDlgFrag extends DialogFragment {

    private BaiduMap bdMap;

    @BindView(R.id.iv_BDType_normal)
    ImageView iv_BDType_normal;

    @BindView(R.id.iv_BDType_satellite)
    ImageView iv_BDType_satellite;

    public BDTypeDlgFrag() {
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.DlgFrag);
    }


    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,
                             Bundle savedInstanceState) {

        bdMap = ((MapView) getActivity().findViewById(R.id.mv_map_activity)).getMap();

        View view = inflater.inflate(R.layout.bd_type_dlg_frag, container, false);

        ButterKnife.bind(this, view);

        setNormalSatellite();

        return view;
    }


    @OnClick({R.id.iv_mapTypeClose, R.id.iv_BDType_normal, R.id.iv_BDType_satellite})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_mapTypeClose:
                dismiss();
                break;

            case R.id.iv_BDType_normal:
                if (bdMap.getMapType() != BaiduMap.MAP_TYPE_NORMAL) {
                    bdMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                    setNormalSatellite();
                }
                break;

            case R.id.iv_BDType_satellite:
                if (bdMap.getMapType() != BaiduMap.MAP_TYPE_SATELLITE) {
                    bdMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                    setNormalSatellite();
                }
                break;
        }
    }

    private void setNormalSatellite() {
        iv_BDType_normal.setSelected(bdMap.getMapType() == BaiduMap.MAP_TYPE_NORMAL);
        iv_BDType_satellite.setSelected(bdMap.getMapType() == BaiduMap.MAP_TYPE_SATELLITE);
    }

}
