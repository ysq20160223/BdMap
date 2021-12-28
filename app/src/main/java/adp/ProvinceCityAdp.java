package adp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;

import com.as_160213_bd_map.R;
import com.as_160213_bd_map.databinding.OfflineCityItemBinding;
import com.as_160213_bd_map.databinding.OfflineProvinceItemBinding;

import java.util.ArrayList;
import java.util.List;

import utils.AnimatedExpandableListView;

// Created by Administrator on 2016/7/28.

public class ProvinceCityAdp extends AnimatedExpandableListView.AnimatedExpandableListAdapter {
    private final LayoutInflater inflater;

    private List<ProvinceItem> groupItemList;

    public ProvinceCityAdp(Context context) {
        inflater = LayoutInflater.from(context);
    }

    public void setList(List<ProvinceItem> items) {
        this.groupItemList = items;
    }

    @Override
    public CityItem getChild(int groupPosition, int childPosition) {
        return groupItemList.get(groupPosition).cityItemList.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getRealChildView(int groupPosition, int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {
        CityHolder childHolder;
        final CityItem cityItem = getChild(groupPosition, childPosition);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.offline_city_item, parent, false);
            childHolder = new CityHolder(convertView);
            convertView.setTag(childHolder);
        } else {
            childHolder = (CityHolder) convertView.getTag();
        }

        childHolder.tv_offline_city_city.setText(cityItem.c_name);
        String mapSize = "地图 " + cityItem.c_size;
        childHolder.tv_offline_city_mapSize.setText(mapSize);

        childHolder.iv_offline_city_start.setOnClickListener(v -> {
            if (callBack != null) {
                callBack.download(cityItem.c_id);
            }
        });

        return convertView;
    }

    @Override
    public int getRealChildrenCount(int groupPosition) {
        return groupItemList.get(groupPosition).cityItemList.size();
    }

    @Override
    public ProvinceItem getGroup(int groupPosition) {
        return groupItemList.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return groupItemList.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        ProvinceHolder groupHolder;
        final ProvinceItem provinceItem = getGroup(groupPosition);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.offline_province_item, parent, false);
            groupHolder = new ProvinceHolder(convertView);
//            ButterKnife.bind(groupHolder, convertView);
            convertView.setTag(groupHolder);
        } else {
            groupHolder = (ProvinceHolder) convertView.getTag();
        }

        groupHolder.tv_offline_province_province.setText(provinceItem.p_name);
        String mapSize = "地图 " + provinceItem.p_size;
        groupHolder.tv_offline_province_mapSize.setText(mapSize);

        if (isExpanded) {
            groupHolder.tv_offline_province_arrow.setBackgroundResource(R.mipmap.arrow_q_list_down);
        } else {
            groupHolder.tv_offline_province_arrow.setBackgroundResource(R.mipmap.arrow_q_list_right);
        }

        groupHolder.iv_offline_province_start.setOnClickListener(v -> {
            if (callBack != null) {
                callBack.download(provinceItem.p_id);
            }
        });

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int arg0, int arg1) {
        return true;
    }

    public static class ProvinceItem {
        public String p_name;
        public int p_id;
        public String p_size;
        public List<CityItem> cityItemList = new ArrayList<>();
    }

    public static class CityItem {
        public String c_name;
        public int c_id;
        public String c_size;
    }

    static class CityHolder {

        TextView tv_offline_city_city;

        TextView tv_offline_city_mapSize;

        ImageView iv_offline_city_start;

        CityHolder(View view) {
            OfflineCityItemBinding offlineCityItemBinding = DataBindingUtil.bind(view);
            if (null != offlineCityItemBinding) {
                tv_offline_city_city = offlineCityItemBinding.tvOfflineCityCity;
                tv_offline_city_mapSize = offlineCityItemBinding.tvOfflineCityMapSize;
                iv_offline_city_start = offlineCityItemBinding.ivOfflineCityStart;
            }
        }
    }

    static class ProvinceHolder {

        ImageView tv_offline_province_arrow;
        TextView tv_offline_province_province;
        TextView tv_offline_province_mapSize;
        ImageView iv_offline_province_start;

        ProvinceHolder(View view) {
            OfflineProvinceItemBinding offlineProvinceItemBinding = DataBindingUtil.bind(view);
            if (null != offlineProvinceItemBinding) {
                tv_offline_province_arrow = offlineProvinceItemBinding.tvOfflineProvinceArrow;
                tv_offline_province_province = offlineProvinceItemBinding.tvOfflineProvinceProvince;
                tv_offline_province_mapSize = offlineProvinceItemBinding.tvOfflineProvinceMapSize;
                iv_offline_province_start = offlineProvinceItemBinding.ivOfflineProvinceStart;
            }
        }

    }

    private CallBack callBack;

    public void setOnCallBackListener(CallBack callBack) {
        this.callBack = callBack;
    }

    public interface CallBack {
        void download(int cityId);
    }

}
