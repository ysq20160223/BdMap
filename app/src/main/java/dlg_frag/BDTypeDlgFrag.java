package dlg_frag;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

import com.as_160213_bd_map.R;
import com.as_160213_bd_map.databinding.BdTypeDlgFragBinding;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;


// Created by Administrator on 2016/7/19.

public class BDTypeDlgFrag extends DialogFragment {

    private BdTypeDlgFragBinding bdTypeDlgFragBinding;

    private BaiduMap bdMap;

    public BDTypeDlgFrag() {
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.DlgFrag);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        bdMap = ((MapView) getActivity().findViewById(R.id.mv_map_activity)).getMap();

        View view = inflater.inflate(R.layout.bd_type_dlg_frag, container, false);
        bdTypeDlgFragBinding = DataBindingUtil.bind(view);

        setNormalSatellite();

        bdTypeDlgFragBinding.ivMapTypeClose.setOnClickListener(v -> dismiss());

        bdTypeDlgFragBinding.ivBDTypeNormal.setOnClickListener(v -> {
            if (bdMap.getMapType() != BaiduMap.MAP_TYPE_NORMAL) {
                bdMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                setNormalSatellite();
            }
        });

        bdTypeDlgFragBinding.ivBDTypeSatellite.setOnClickListener(v -> {
            if (bdMap.getMapType() != BaiduMap.MAP_TYPE_SATELLITE) {
                bdMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                setNormalSatellite();
            }
        });

        return view;
    }

    private void setNormalSatellite() {
        bdTypeDlgFragBinding.ivBDTypeNormal.setSelected(bdMap.getMapType() == BaiduMap.MAP_TYPE_NORMAL);
        bdTypeDlgFragBinding.ivBDTypeSatellite.setSelected(bdMap.getMapType() == BaiduMap.MAP_TYPE_SATELLITE);
    }

}
