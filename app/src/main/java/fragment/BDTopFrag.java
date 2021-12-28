package fragment;

// Created by Administrator on 2017/4/20.

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.as_160213_bd_map.R;
import com.as_160213_bd_map.databinding.BdTopFragBinding;
import com.lib_common_ui.base.BaseLazyFragment;


public class BDTopFrag extends BaseLazyFragment {

    private BdTopFragBinding bdTopFragBinding;


    @Override
    protected int getContentViewId() {
        return R.layout.bd_top_frag;
    }

    @Override
    public void onCreateViewBind(@Nullable ViewGroup viewGroup, @NonNull View view) {
        super.onCreateViewBind(viewGroup, view);
        bdTopFragBinding = (BdTopFragBinding) getBinding();

    }

    @Override
    protected void startLoadData(String from) {
        initData();
    }



    private void initData() {

    }

}