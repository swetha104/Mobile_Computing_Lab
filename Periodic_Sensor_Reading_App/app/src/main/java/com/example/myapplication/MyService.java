package com.example.myapplication;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;



public class MyService extends Service implements SensorEventListener {
    private static String TAG = MyService.class.getCanonicalName();
    private static final int NOTIF_ID = 1;
    private static final String NOTIF_CHANNEL_ID = "Channel_Id";
    private MyServiceImpl impl;
    String magnoSensorData, acceleroSensorData;
    private SensorManager sensorManager;
    public Handler handler = null;
    public static Runnable runnable = null;
    public Context context = this;
//    MainActivity activity = new MainActivity();
//    TextView gyroTextView = activity.findViewById(textView);
    Sensor accelerometer, mMagno;




    private class MyServiceImpl extends IMyService.Stub{

        @Override
        public String ping(String message) throws RemoteException {
            String returnMessage;


            if(message.equals("magno")){
                return magnoSensorData;
            }

            else if(message.equals("accelero")){
                return acceleroSensorData;
            }
            return null;
        }
    }
    @Override
    public void onCreate(){
        handler = new Handler();
        runnable = new Runnable() {
            public void run() {
                Toast.makeText(context, "Service is still running", Toast.LENGTH_LONG).show();
                handler.postDelayed(runnable, 10000);
            }
        };

        handler.postDelayed(runnable, 15000);
        Log.i(TAG, "Creating service");
        super.onCreate();
        impl = new MyServiceImpl();
        Log.i(TAG, "onCreate: Intializing Sensor Services");


        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if(accelerometer != null) {
            sensorManager.registerListener(MyService.this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            Log.d(TAG, "onCreate: Registered Accelerometer Listener");
        }
        else{
            acceleroSensorData="Acc not supported";

        }

//        mGyro = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mMagno = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if(mMagno != null) {
            sensorManager.registerListener(MyService.this, mMagno, SensorManager.SENSOR_DELAY_NORMAL);
            Log.d(TAG, "onCreate: Registered Gyroscope Listener");
        }
        else{
            magnoSensorData="Gyro not supported";

        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "Binding service");
        return impl;
//        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Log.i(TAG, "Starting service");

        return super.onStartCommand(intent, flags, startId);
    }
//    foreground code is here!!


    @Override
    public void onDestroy(){
        Log.i(TAG, "Destroying service");
        super.onDestroy();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            Log.d(TAG, "onSensorChanged: X:" + event.values[0] + "Y:" + event.values[1] + "Z:" + event.values[2]);
            acceleroSensorData = "X: " + event.values[0] + ", Y: " + event.values[1] + ", Z: " + event.values[2];
        } else if (sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {

            magnoSensorData = "X: " + event.values[0] + ", Y: " + event.values[1] + ", Z: " + event.values[2];
        }


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
