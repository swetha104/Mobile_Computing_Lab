package com.example.myapplication;

import android.Manifest;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.ActivityCompat;


import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MyGPSService extends Service implements LocationListener {
    private static String TAG = MyGPSService.class.getCanonicalName();
    private MyGPSServiceImpl impl;

    public LocationManager locationManager;
    private double longitude, latitude, startPointLat, startPointLong;
    private float distance, speed;
    private boolean firstTime = true;
    //    static private final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 1;
    static private final float minDistance = 10;
    static private final long minTime = 1000;
    private List<Location> locationList = new ArrayList<Location>();
    private Location loc;

    @Override
    public void onLocationChanged(Location location) {
        loc = location;
        longitude = location.getLongitude();
        latitude = location.getLatitude();
        if (firstTime) {
            startPointLat = latitude;
            startPointLong = longitude;
            firstTime = false;
        }

        Location startPoint = new Location("start point");
        startPoint.setLatitude(startPointLat);
        startPoint.setLongitude(startPointLong);
        Location currentPoint = new Location("current point");
        currentPoint.setLatitude(latitude);
        currentPoint.setLongitude(longitude);
        distance = startPoint.distanceTo(currentPoint);
        speed = location.getSpeed();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private class MyGPSServiceImpl extends IMyGPSService.Stub {
        @Override
        public String ping(String message) {
            if (message.equals("start")) {
                firstTime = true;
                if (!locationList.isEmpty()){
                    locationList.removeAll(locationList);
                }
                registerForUpdates();
                return "Started Logging...";
            } else if (message.equals("longitude")) {
                return Double.toString(longitude);
            } else if (message.equals("latitude")) {
                locationList.add(loc);
                return Double.toString(latitude);

            } else if (message.equals("speed")) {
                return Float.toString(speed);
            } else if (message.equals("distance")) {
                return Float.toString(distance);
            } else if (message.equals("stop")) {
                return "stopped";
            } else if (message.equals("update")) {
                updateValuesToGPXFile();
                return "GPX file saved";
            } else {
                return "what??";
            }
        }
    }

    public void updateValuesToGPXFile() {
        String header = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?><gpx xmlns=\"http://www.topografix.com/GPX/1/1\" " +
                "creator=\"MapSource 6.15.5\" version=\"1.1\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"  " +
                "xsi:schemaLocation=\"http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd\"><trk>\n";
        String name = "<name>" + "gpx example" + "</name><trkseg>\n";
        String segments = "";
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        for (Location point : locationList) {
            segments += "<trkpt lat=\"" + point.getLatitude() + "\" lon=\"" + point.getLongitude() +
                    "\"><time>" + df.format(new Date(point.getTime())) + "</time></trkpt>\n";
        }

        String footer = "</trkseg></trk></gpx>";
        File newFile = new File(getExternalFilesDir(null), "gpxfile.gpx");

        try {
            FileWriter writer = new FileWriter(newFile, false);
            writer.append(header);
            writer.append(name);
            writer.append(segments);
            writer.append(footer);
            writer.flush();
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        locationList.removeAll(locationList);
    }

    public void registerForUpdates(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return ;
        }
        else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, this);
        }

    }

    @Override
    public void onCreate() {
        Log.i(TAG, "Creating Service");
        super.onCreate();
        impl = new MyGPSServiceImpl();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return ;
        }
        else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, this);
        }
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, this);
//        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
//
//        }


    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "Binding Service");
        return impl;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Log.i(TAG, "Starting Service");

        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onDestroy(){
        Log.i(TAG, "Destroying Service");
        super.onDestroy();
        locationManager.removeUpdates(this);
//        locationList.removeAll(locationList);

    }


}
