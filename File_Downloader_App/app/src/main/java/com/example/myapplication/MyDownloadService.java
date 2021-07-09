package com.example.myapplication;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.os.Environment;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class MyDownloadService extends IntentService {

    private int result = Activity.RESULT_CANCELED;
    public static final String URL = "urlpath";
    public static final String FILENAME = "filename";
    public static final String FILEPATH = "filepath";
    public static final String RESULT = "result";
    public static final String NOTIFICATION = "service receiver";

    public MyDownloadService() {
        super("DownloadService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String urlPath = intent.getStringExtra(URL);
        String fileName = intent.getStringExtra(FILENAME);

        File output = new File(Environment.getExternalStorageDirectory(), fileName); //This helps in identifying the full path the to the SDCard

        if (output.exists()) {
            output.delete();
        }

        InputStream stream = null;
        FileOutputStream fos = null;

        try {
            java.net.URL url = new URL(urlPath);
            stream = url.openConnection().getInputStream(); //The connection to the resource is established here
            InputStreamReader reader = new InputStreamReader(stream);
            fos = new FileOutputStream(output.getPath());

            int next = -1;
            while ((next = reader.read()) != -1) {
                fos.write(next);
            }

            result = Activity.RESULT_OK;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        publishResults(output.getAbsolutePath(), result);
    }

    private void publishResults(String outputpath, int result) {
        Intent resultIntent = new Intent(NOTIFICATION);
        resultIntent.putExtra(FILEPATH, outputpath);
        resultIntent.putExtra(RESULT, result);
        sendBroadcast(resultIntent);
    }
}
