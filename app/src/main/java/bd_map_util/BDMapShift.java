package bd_map_util;

import com.baidu.mapapi.model.LatLng;

// Created by Administrator on 2016/9/27.
/*

功能 01 : 将 百度 经纬度 转换为 百度地址 (深圳北)
示例 : http://api.map.baidu.com/geocoder/v2/?output=json&ak=6dS03rVNfEZcuHBjnljfW2mv&location=22.616606,114.036931

//------------------------

功能 02 : 将 源坐标 转换为 百度坐标

GPS坐标 转 百度 坐标 (深圳北)
示例 : http://api.map.baidu.com/geoconv/v1/?coords=114.030229,22.610331&from=1&to=5&ak=6dS03rVNfEZcuHBjnljfW2mv

参数参考百度 : http://lbsyun.baidu.com/index.php?title=webapi/guide/changeposition

*/

@SuppressWarnings("unused")
public class BDMapShift {


    private static String ak = "6dS03rVNfEZcuHBjnljfW2mv";

//    /**
//     * 百度经纬度 转 百度地址
//     */
//    public static void address(final Context ctx, final AddressCallBack callBack,
//                               HttpUtils httpUtils, LatLng bdLatLng) {
//        if (callBack == null) {
//            throw new RuntimeException("callBack not be null");
//        }
//
//        String uri = "http://api.map.baidu.com/geocoder/v2/";
//        RequestParams params = new RequestParams();
//        params.addBodyParameter("output", "json");
//        params.addBodyParameter("ak", ak);
//        params.addBodyParameter("location", bdLatLng.latitude + "," + bdLatLng.longitude);
//
//        httpUtils.send(HttpRequest.HttpMethod.POST, uri, params, new RequestCallBack<String>() {
//            @Override
//            public void onSuccess(ResponseInfo<String> responseInfo) {
////                Log.i(TAG, "result : " + responseInfo.result);
//                try {
//                    JSONObject jsonObject = new JSONObject(responseInfo.result);
//                    if (jsonObject.optInt("status") == 0) {
//                        JSONObject result = new JSONObject(jsonObject.optString("result"));
//                        String address = result.optString("formatted_address");
//
//                        callBack.onSuccess(address, responseInfo.result); // 接口回调
//                    }
//                } catch (JSONException e) {
//                    ToastUtil.text(ctx, "经纬度 转 百度地址 - 解析出现异常");
//                    Log.i(TAG, "经纬度 转 百度地址 - 解析出现异常 e : " + e.toString());
//                }
//            }
//
//            @Override
//            public void onFailure(HttpException e, String s) {
//                ToastUtil.text(ctx, "经纬度 转 百度地址 失败");
//                Log.i(TAG, "经纬度 转换 百度地址 失败 : " + s);
//            }
//        });
//    }
//
//    /**
//     * 源经纬度 转 百度经纬度
//     */
//    public static void latLng(final Context ctx, final LatLngCallBack callBack,
//                              HttpUtils httpUtils, LatLng srcLatLng, int from, int to) {
//        if (callBack == null) {
//            throw new RuntimeException("callBack not be null");
//        }
//
//        String uri = "http://api.map.baidu.com/geoconv/v1/";
//        RequestParams params = new RequestParams();
//        params.addBodyParameter("coords", srcLatLng.longitude + "," + srcLatLng.latitude);
//        params.addBodyParameter("from", from + "");
//        params.addBodyParameter("to", to + "");
//        params.addBodyParameter("ak", ak);
//
//        httpUtils.send(HttpRequest.HttpMethod.POST, uri, params, new RequestCallBack<String>() {
//            @Override
//            public void onSuccess(ResponseInfo<String> responseInfo) {
//                try {
//                    JSONObject jsonObject = new JSONObject(responseInfo.result);
//                    int status = jsonObject.optInt("status");
//                    if (status == 0) {
//                        JSONArray jsonArray = jsonObject.optJSONArray("result");
//                        JSONObject jsonObjectResult = jsonArray.getJSONObject(0);
//                        Double lon = jsonObjectResult.optDouble("x");
//                        Double lat = jsonObjectResult.optDouble("y");
//
//                        callBack.onSuccess(new LatLng(lat, lon)); // 接口回调
//                    } else {
//                        ToastUtil.text(ctx, "源经纬度 转 百度经纬度 - 出现错误");
//                    }
//                } catch (JSONException e) {
//                    ToastUtil.text(ctx, "源经纬度 转 百度经纬度 解析出现异常");
//                    Log.i(TAG, "源经纬度 转 百度经纬度 解析出现异常 e : " + e.toString());
//                }
//            }
//
//            @Override
//            public void onFailure(HttpException e, String s) {
//                ToastUtil.text(ctx, "源经纬度 转 百度经纬度 失败");
//                Log.i(TAG, "源经纬度 转 百度经纬度 失败 : " + s);
//            }
//        });
//    }

    // ---------------------------------------------------- CallBack
    public interface AddressCallBack {
        void onSuccess(String address, String detail);
    }

    public interface LatLngCallBack {
        void onSuccess(LatLng bdLatLng);
    }
}
