package adp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.mapapi.map.offline.MKOLUpdateElement;
import com.baidu.mapapi.map.offline.MKOfflineMap;

import java.util.List;

import base.BaseAdp;
import holder.LocalMapHolder;

// Created by Administrator on 2016/7/26.

public class LocalMapAdp extends BaseAdp<MKOLUpdateElement> {

    private Context ctx;

    private MKOfflineMap mkOfflineMap;

    public LocalMapAdp(Context ctx, List<MKOLUpdateElement> list, MKOfflineMap mkOfflineMap) {
        super(list);

        this.ctx = ctx;
        this.mkOfflineMap = mkOfflineMap;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LocalMapHolder localMapHolder;
        if (convertView == null) {
            localMapHolder = new LocalMapHolder(ctx);
        } else {
            localMapHolder = (LocalMapHolder) convertView.getTag();
        }

        localMapHolder.setData((MKOLUpdateElement) getItem(position), mkOfflineMap, callBack);

        return localMapHolder.getConvertView();
    }

    private CallBack callBack;

    public void setOnUpdateCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public interface CallBack {
        void update();
    }

}
