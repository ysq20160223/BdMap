package bean

import com.baidu.mapapi.model.LatLng
import java.io.Serializable
import java.util.ArrayList

/*
{
    "status": 0,
    "result": {
        "location": {
            "lng": 118.56270402881,
            "lat": 25.425179023116
        },
        "formatted_address": "福建省莆田市仙游县乡情路",
        "business": "",
        "addressComponent": {
            "adcode": "350322",
            "city": "莆田市",
            "country": "中国",
            "direction": "",
            "distance": "",
            "district": "仙游县",
            "province": "福建省",
            "street": "乡情路",
            "street_number": "",
            "country_code": 0
        },
        "poiRegions": [],
        "sematic_description": "友万酒家西南497米",
        "cityCode": 195
    }
}
*/

class Info : Serializable {

    //    private final static long serialVersionUID = 1000;
    private val longitude: Double = 0.toDouble()
    private val latitude: Double = 0.toDouble()

    companion object {

        var list: MutableList<LatLng> = ArrayList()

        init {
            list.add(LatLng(25.419819, 118.563485)) // jin xi school
            list.add(LatLng(25.410568, 118.574428)) // xi wei
            list.add(LatLng(25.406309, 118.588738)) // du Fen school
            list.add(LatLng(25.414336, 118.587874)) // ABC
            list.add(LatLng(25.428165, 118.580989)) // school five
            list.add(LatLng(25.427092, 118.562775)) // credit

            // 114.028124, 22.542994 // 车公庙
            // 114.036928, 22.616598 // 深圳北
        }
    }

}