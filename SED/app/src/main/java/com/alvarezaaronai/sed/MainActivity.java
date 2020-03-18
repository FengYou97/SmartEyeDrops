package com.alvarezaaronai.sed;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.mbientlab.metawear.android.BtleService;

public class MainActivity extends AppCompatActivity implements ServiceConnection {
    //Variables
    /*
        MetaWear
     */
    private BtleService.LocalBinder serviceBinder;
    /*
        Main Activity
     */
    
    /*
        Log Tags
     */
    private static final String TAG = "HomeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG, "onCreate: Binding Service");
        /*
           Bind a Connection Service / Disconnect when App is close
         */
        // Bind the service when the activity is created
        getApplicationContext().bindService(new Intent(this, BtleService.class),
                this, Context.BIND_AUTO_CREATE);
        Log.i(TAG, "onCreate: Finished Binding Service");
        /*
            All Sensor Data Wil Be updated to AWS
         */
        //Use this to send to Server: replace "t1" with the data in String format
        //place this part of the code in the area where data is collected so that it can be sent
//        String t1 = "time,x,y,z,force\n" +
//                "10,0.2,0.2,0.2,100";
//        new Client().execute(t1);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: Destroyed Binding");
        // Unbind the service when the activity is destroyed
        getApplicationContext().unbindService(this);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        // Typecast the binder to the service's LocalBinder class
        serviceBinder = (BtleService.LocalBinder) service;
        Log.i(TAG, "onServiceConnected: Service Connected : " + service.toString());
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) { }


}
