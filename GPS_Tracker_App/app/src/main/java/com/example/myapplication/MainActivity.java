package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.os.Handler;

public class MainActivity extends AppCompatActivity implements ServiceConnection {
    private static String TAG = MainActivity.class.getCanonicalName();
    private IMyService myServiceProxy = null;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        String messageFromService;
        handler.post(runnableCode);

        Intent i = new Intent(this, MyService.class);
        bindService(i, this, BIND_AUTO_CREATE);
    }

    private Runnable runnableCode = new Runnable() {
        @Override
        public void run() {
            String messageFromService;
            if(myServiceProxy != null){
                try {
                    messageFromService = myServiceProxy.ping("magno");
                    TextView gyroTextView = findViewById(R.id.textView3);
                    gyroTextView.setText(messageFromService);

                    messageFromService = myServiceProxy.ping("accelero");
                    TextView acceleroTextView = findViewById(R.id.textView6);
                    acceleroTextView.setText(messageFromService);

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

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        Log.i(TAG, "Service connected");
        myServiceProxy = IMyService.Stub.asInterface(service);

    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        Log.i(TAG, "Service Disconnected");
        myServiceProxy = null;

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(myServiceProxy != null){
            unbindService(this);
        }
    }


}
