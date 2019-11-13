package holder;

/*
 * Created by Administrator on 2017/4/24.
 */

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.as_160213_bd_map.R;
import com.baidu.mapapi.map.offline.MKOLUpdateElement;
import com.baidu.mapapi.map.offline.MKOfflineMap;
import com.lib_sdk.utils.ToastUtil;


import adp.LocalMapAdp;
import base.BaseHolder;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class LocalMapHolder extends BaseHolder {

    private Context ctx;

    @BindView(R.id.offline_localMap_title)
    TextView offline_localMap_title;

    @BindView(R.id.offline_localMap_update)
    TextView offline_localMap_update;

    @BindView(R.id.offline_localMap_ratio)
    TextView offline_localMap_ratio;

    //
    private MKOLUpdateElement e;

    private MKOfflineMap mkOfflineMap;

    private LocalMapAdp.CallBack callBack;

    public LocalMapHolder(Context ctx) {
        super(ctx);

        this.ctx = ctx;
    }

    @Override
    public View initConvertView(Context ctx) {
        View v = View.inflate(ctx, R.layout.offline_localmap_item, null);

        ButterKnife.bind(this, v);
        return v;
    }


    @OnClick({R.id.offline_localMap_remove})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.offline_localMap_remove:
                boolean result = mkOfflineMap.remove(e.cityID);
                if (result) {
                    if (callBack != null) {
                        callBack.update();
                    }
                    ToastUtil.Companion.text(ctx, "Delete Success");
                }
                break;
        }
    }

    public void setData(MKOLUpdateElement e, MKOfflineMap mkOfflineMap, LocalMapAdp.CallBack callBack) {
        this.e = e;
        this.mkOfflineMap = mkOfflineMap;
        this.callBack = callBack;

        offline_localMap_title.setText(e.cityName);
        String radio = e.ratio + "%";
        offline_localMap_ratio.setText(radio);
        if (e.update) {
            offline_localMap_update.setText("可更新");
        } else {
            offline_localMap_update.setText("最新");
        }
    }

}
