package base;

// Created by Administrator on 2017/4/24.

import android.content.Context;
import android.view.View;

public abstract class BaseHolder {

    private View convertView;

    public BaseHolder(Context ctx) {
        convertView = initConvertView(ctx);
        convertView.setTag(this);
    }

    public abstract View initConvertView(Context ctx);

    public View getConvertView() {
        return convertView;
    }

}
