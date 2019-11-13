package handler;

import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

// Created by Administrator on 2016/11/2.

public class TraceHandler extends Handler {

    private ImageView iv;

    public TraceHandler(ImageView iv) {
        this.iv = iv;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case 0:
                iv.setSelected(false);
                break;
        }
    }
}