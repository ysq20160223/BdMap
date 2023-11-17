package fragment;

// Created by Administrator on 2017/4/20.

import android.view.View;
import android.view.ViewGroup;

import com.as_160213_bd_map.R;
import com.as_160213_bd_map.databinding.BdTopFragBinding;
import com.lib_common_ui.base.BaseLazyFragment;


public class BDTopFrag extends BaseLazyFragment<BdTopFragBinding> {


    @Override
    protected int getContentViewId() {
        return R.layout.bd_top_frag;
    }

    @Override
    public void onCreateViewBind(ViewGroup viewGroup, View view) {
        super.onCreateViewBind(viewGroup, view);

    }

    @Override
    protected void startLoadData(String from) {
        initData();
    }


    private void initData() {

    }

}