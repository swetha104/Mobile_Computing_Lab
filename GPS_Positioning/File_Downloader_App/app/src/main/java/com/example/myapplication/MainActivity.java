package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView downloadStatus;
    private Button downloadButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        downloadButton = findViewById(R.id.downloadButton);
        downloadStatus = findViewById(R.id.downloadStatus);

        downloadButton.setOnClickListener(this);

        final int REQUEST_EXTERNAL_STORAGE = 1;
        /*These permissions are required in order to facilitate the download action in Android versions greater than Marshmallow.
         * For versions below this, we can specify the permissions in AndroidManifest.xml*/
        String[] PERMISSIONS_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        int permission =  ActivityCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE);

        /*Condition to check and grant permissions*/
        if(permission!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, new IntentFilter(com.example.myapplication.MyDownloadService.NOTIFICATION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    /*An intent is created here to start the service*/
    @Override
    public void onClick(View v) {
        Intent downloadIntent = new Intent(this, com.example.myapplication.MyDownloadService.class);
        downloadIntent.putExtra(com.example.myapplication.MyDownloadService.FILENAME, "mmc1.pdf");
        downloadIntent.putExtra(com.example.myapplication.MyDownloadService.URL, "https://ars.els-cdn.com/content/image/1-s2.0-S2352302618302175-mmc1.pdf");
        startService(downloadIntent);
        downloadStatus.setText("Download started");
    }

    /*Broadcast are sent when an event of our interest occurs*/
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();

            if (bundle != null) {
                String string = bundle.getString(com.example.myapplication.MyDownloadService.FILEPATH);
                int resultCode = bundle.getInt(com.example.myapplication.MyDownloadService.RESULT);

                if (resultCode == RESULT_OK) {
                    Toast.makeText(MainActivity.this, "Download complete. Download URI: " + string, Toast.LENGTH_LONG).show();
                    downloadStatus.setText("Download done");
                } else {
                    Toast.makeText(MainActivity.this, "Download failed", Toast.LENGTH_LONG).show();
                    downloadStatus.setText("Download failed");
                }
            }
        }
    };
}
