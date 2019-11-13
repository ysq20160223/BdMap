package utils;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

// Created by Administrator on 2016/2/14

@SuppressWarnings("unused")
public class OrientListener implements SensorEventListener {

    private Context context;

    private SensorManager sensorManager;
    private Sensor sensor;

    private float lastOrient;

    public OrientListener(Context context) {
        this.context = context;
    }

    public void start() {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION); // 方向传感器
        }

        if (sensor != null && sensorManager != null) {
//            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);
        }

    }

    public void stop() {
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
            float dataX = event.values[SensorManager.DATA_X];

            if (Math.abs(dataX - lastOrient) > 0.5) {
                if (onOrientListener != null) {
                    onOrientListener.onOrientChanged(dataX);
                }
            }
            lastOrient = dataX;
        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    // -------------------------------------------------------------------------- interface
    private OnOrientListener onOrientListener;

    public void setOnOrientListener(OnOrientListener onOrientListener) {
        this.onOrientListener = onOrientListener;
    }

    public interface OnOrientListener {
        void onOrientChanged(float orient);
    }
}
