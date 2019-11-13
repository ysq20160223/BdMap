package fragment;

// Created by Administrator on 2017/4/20.

import com.as_160213_bd_map.R;
import com.lib_common_ui.base.BaseLazyFragment;


public class BDTopFrag extends BaseLazyFragment {


    @Override
    protected int getContentViewId() {
        return R.layout.bd_top_frag;
    }

    @Override
    protected void startLoadData(String from) {
        initData();
    }


//    @SuppressWarnings("unused")
//    @OnClick({R.id.iv_top_search})
//    void setOnClick(View view) {
//        switch (view.getId()) {
//
//        }
//    }

    private void initData() {

    }

}