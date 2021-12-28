package holder;

import android.content.Context;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.as_160213_bd_map.R;
import com.as_160213_bd_map.databinding.OfflineLocalmapItemBinding;
import com.baidu.mapapi.map.offline.MKOLUpdateElement;
import com.baidu.mapapi.map.offline.MKOfflineMap;

import adp.LocalMapAdp;
import base.BaseHolder;
import lib.toast.XToast;


public class LocalMapHolder extends BaseHolder {

    private OfflineLocalmapItemBinding offlineLocalmapItemBinding;

    //
    private MKOLUpdateElement e;

    private MKOfflineMap mkOfflineMap;

    private LocalMapAdp.CallBack callBack;

    public LocalMapHolder(Context ctx) {
        super(ctx);

    }

    @Override
    public View initConvertView(Context ctx) {
        View v = View.inflate(ctx, R.layout.offline_localmap_item, null);
        offlineLocalmapItemBinding = DataBindingUtil.bind(v);
        if (null != offlineLocalmapItemBinding) {
            offlineLocalmapItemBinding.offlineLocalMapRemove.setOnClickListener(v1 -> {
                boolean result = mkOfflineMap.remove(e.cityID);
                if (result) {
                    if (callBack != null) {
                        callBack.update();
                    }
                    XToast.INSTANCE.show("Delete Success");
                }
            });
        }
        return v;
    }


    public void setData(MKOLUpdateElement e, MKOfflineMap mkOfflineMap, LocalMapAdp.CallBack callBack) {
        this.e = e;
        this.mkOfflineMap = mkOfflineMap;
        this.callBack = callBack;

        offlineLocalmapItemBinding.offlineLocalMapTitle.setText(e.cityName);
        String radio = e.ratio + "%";
        offlineLocalmapItemBinding.offlineLocalMapRatio.setText(radio);
        if (e.update) {
            offlineLocalmapItemBinding.offlineLocalMapUpdate.setText("可更新");
        } else {
            offlineLocalmapItemBinding.offlineLocalMapUpdate.setText("最新");
        }
    }

}
