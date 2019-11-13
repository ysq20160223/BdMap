package base;

import android.widget.BaseAdapter;

import java.util.List;

// Created by Administrator on 2016/10/24.

public abstract class BaseAdp<T> extends BaseAdapter {

    private List<T> list;

    public BaseAdp(List<T> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
