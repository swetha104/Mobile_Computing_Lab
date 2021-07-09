package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements ServiceConnection {
    final private String TAG = MainActivity.class.getCanonicalName();
    public Intent i;
    private IMyGPSService myGPSServiceProxy = null;

    Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            requestLocationPermission(1);
        }

        i = new Intent(this, MyGPSService.class);
        bindService(i, this, BIND_AUTO_CREATE);

    }


    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        Log.i(TAG, "Connected to service");
        myGPSServiceProxy = IMyGPSService.Stub.asInterface(service);

    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        Log.i(TAG, "Disconnected by service");
        myGPSServiceProxy = null;

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if (myGPSServiceProxy != null){
            unbindService(this);
        }
    }

    @Override
    public void onStop(){
        super.onStop();
        if (myGPSServiceProxy != null){
            unbindService(this);
            myGPSServiceProxy = null;
        }
    }

    private Runnable runnableCode = new Runnable() {
        @Override
        public void run() {
            String messageFromService;
            TextView Latitude = findViewById(R.id.textView6);
            TextView Longitude = findViewById(R.id.textView7);
            TextView Distance = findViewById(R.id.textView8);
            TextView Speed = findViewById(R.id.textView9);
            if(myGPSServiceProxy != null){
                try {
                    messageFromService = myGPSServiceProxy.ping("latitude");
                    Latitude.setText(messageFromService);
                    messageFromService = myGPSServiceProxy.ping("longitude");
                    Longitude.setText(messageFromService);
                    messageFromService = myGPSServiceProxy.ping("speed");
                    Speed.setText(messageFromService);
                    messageFromService = myGPSServiceProxy.ping("distance");
                    Distance.setText(messageFromService);

                }
                catch (RemoteException ex){
                    messageFromService = "exception";
                }

            }
            else {
                messageFromService = "no service";
            }
            handler.postDelayed(this, 2000);

        }
    };

    public void doRun(String message) {
        String messageRec;
//        TextView latitude = findViewById(R.id.textView);
//        TextView longitude = findViewById(R.id.textView2);
        if (myGPSServiceProxy != null) {
            try {
                messageRec = myGPSServiceProxy.ping(message);
                System.out.println(messageRec);
            } catch (RemoteException ex) {
                messageRec = "exception";
                System.out.println(messageRec);
            }

        } else {
            messageRec = "no Service";
            System.out.println(messageRec);
        }

    }

    public void onClick(View v){

        Button b = (Button)v;
        String buttonText = b.getText().toString();

        if (buttonText.equals("Start Service")){
            doRun("start");
            Toast.makeText(this, "Service started", Toast.LENGTH_LONG).show();
            handler.post(runnableCode);
        }

        else if (buttonText.equals("Stop Service")){
            handler.removeCallbacks(runnableCode);
            Toast.makeText(this, "Service stopped", Toast.LENGTH_LONG).show();
            doRun("stop");
            TextView Latitude = findViewById(R.id.textView6);
            TextView Longitude = findViewById(R.id.textView7);
            TextView Distance = findViewById(R.id.textView8);
            TextView Speed = findViewById(R.id.textView9);
            Latitude.setText("");
            Longitude.setText("");
            Speed.setText("");
            Distance.setText("");
        //            super.unbindService(this);

        }

        else if (buttonText.equals("Update Values")){
            Toast.makeText(this, "Saving gpx file!!", Toast.LENGTH_LONG).show();
            doRun("update");

        }

        else if (buttonText.equals("Exit")){
            finish();
        }

    }

    public void enableLocationSettings(){
        new AlertDialog.Builder(this)
                .setTitle("Enable GPS")
                .setMessage("GPS currently Disabled. Do you want to enable GPS?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(settingsIntent);
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public void requestLocationPermission(int MY_PERMISSIONS_REQUEST_FINE_LOCATION){
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_FINE_LOCATION);
    }

//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults, LocationManager locationManager, ) {
//        if (Build.VERSION.SDK_INT >= 23 &&
//                ContextCompat.checkSelfPermission(this,
//                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, this);
//        }
//    }


}
