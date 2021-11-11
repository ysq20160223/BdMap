package bd_map_util;



import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviParaOption;
import com.baidu.mapapi.utils.OpenClientUtil;
import com.lib_sdk.utils.XLog;

// Created by Administrator on 2016/12/8.

public class BDInvoke {

    private final static String NATIVE = "native";
    private final static String WALKING = "walking";
    private final static String BIKING = "biking";

    private static void nav(Context ctx, LatLng from, LatLng to, String type) {
        NaviParaOption option = new NaviParaOption().startPoint(from).endPoint(to);
        try {
            BaiduMapNavigation.setSupportWebNavi(false);
            switch (type) {
                case NATIVE:
                    BaiduMapNavigation.openBaiduMapNavi(option, ctx);
                    break;

                case WALKING:
                    BaiduMapNavigation.openBaiduMapWalkNavi(option, ctx);
                    break;

                case BIKING:
                    BaiduMapNavigation.openBaiduMapBikeNavi(option, ctx);
                    break;
            }

        } catch (Exception e) {
            showDialog(ctx);
            XLog.d(e.toString());
        }
    }

    public static void nativeNav(Context ctx, LatLng from, LatLng to) {
        nav(ctx, from, to, NATIVE);
    }

    public static void walkingNav(Context ctx, LatLng from, LatLng to) {
        nav(ctx, from, to, WALKING);
    }

    public static void bikingNav(Context ctx, LatLng from, LatLng to) {
        nav(ctx, from, to, BIKING);
    }

    private static void showDialog(final Context ctx) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setTitle("提示");
        builder.setMessage("当前尚未安装百度地图app或app版本过低，点击确认安装？");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                OpenClientUtil.getLatestBaiduMapApp(ctx);
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

}
