package com.alvarezaaronai.sed;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;

import com.alvarezaaronai.sed.Models.Data;
import com.alvarezaaronai.sed.Models.SensorSession;
import com.mbientlab.metawear.ForcedDataProducer;
import com.mbientlab.metawear.MetaWearBoard;
import com.mbientlab.metawear.Route;
import com.mbientlab.metawear.Subscriber;
import com.mbientlab.metawear.android.BtleService;
import com.mbientlab.metawear.builder.RouteBuilder;
import com.mbientlab.metawear.builder.RouteComponent;
import com.mbientlab.metawear.data.Acceleration;
import com.mbientlab.metawear.module.Accelerometer;
import com.mbientlab.metawear.module.Gpio;

import bolts.Continuation;
import bolts.Task;

public class MainActivity extends AppCompatActivity implements ServiceConnection {
    //Variables
    /*
        MetaWear
     */
    private BtleService.LocalBinder serviceBinder;
    private MetaWearBoard board;
    private Accelerometer accel;
    private Gpio gpio;
    private TextView mAccel;
    private Handler mHandler = new Handler();
    private StringBuilder mTempData;
    /*
        Main Activity
     */
    private final String MW_MAC_ADDRESS = "F5:64:B2:18:F2:09";
    //If you change the Mac Address, reset Branch,
    //Only change it to test your own device.
    private SensorSession sensorData = new SensorSession();
    private Data mData ;


    /*
        Log Tags
     */
    private static final String TAG = "HomeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*
            Import Member Variables
         */
        mAccel = findViewById(R.id.homeactivity_textview_accel);
        //Clear Text View
        mAccel.setText("");
        mTempData = new StringBuilder();
        Log.i(TAG, "onCreate: Binding Service");
        /*
           Bind a Connection Service / Disconnect when App is close
         */
        // Bind the service when the activity is created
        getApplicationContext().bindService(new Intent(this, BtleService.class),
                this, Context.BIND_AUTO_CREATE);
        Log.i(TAG, "onCreate: Finished Binding Service");
        mHandler.postDelayed(mAccelRun,5000);
        mHandler.postDelayed(mSetAccel,15000);
        /*
            All Sensor Data Wil Be updated to AWS
         */
        //Use this to send to Server: replace "t1" with the data in String format
        //place this part of the code in the area where data is collected so that it can be sent
//        String t1 = "time,x,y,z,force\n" +
//                "10,0.2,0.2,0.2,100";
//
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: Destroyed Binding");
        Log.i(TAG, "onDestroy: Stoping Accel");
        accel.stop();
        accel.acceleration().stop();
        // Unbind the service when the activity is destroyed
        getApplicationContext().unbindService(this);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        // Typecast the binder to the service's LocalBinder class
        serviceBinder = (BtleService.LocalBinder) service;
        Log.i(TAG, "onServiceConnected: Service Connected : " + service.toString());
        retrieveBoard();
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
    }

    /*
     *Methods HomeActivity
     */
    public void retrieveBoard() {
        final BluetoothManager btManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        final BluetoothDevice remoteDevice =
                btManager.getAdapter().getRemoteDevice(MW_MAC_ADDRESS);
        Log.i(TAG, "retrieveBoard: Trying to connect to : " + MW_MAC_ADDRESS);
        // Create a MetaWear board object for the Bluetooth Device
        board = serviceBinder.getMetaWearBoard(remoteDevice);
        board.connectAsync().onSuccessTask(new Continuation<Void, Task<Route>>() {
            @Override
            public Task<Route> then(Task<Void> task) throws Exception {

                Log.i(TAG, "connected to: " + MW_MAC_ADDRESS);

                accel = board.getModule(Accelerometer.class);
                accel.configure()
                        .odr(30f)       // Set sampling frequency to 30Hz, or closest valid ODR
                        .range(4f)      // Set data range to +/-4g, or closet valid range
                        .commit();

                gpio = board.getModule(Gpio.class);
                ForcedDataProducer adc = gpio.pin((byte) 1).analogAdc();
                adc.addRouteAsync(new RouteBuilder() {
                    @Override
                    public void configure(RouteComponent source) {
                        source.stream(new Subscriber() {
                            @Override
                            public void apply(com.mbientlab.metawear.Data data, Object... env) {
                                Log.i(TAG, "adc = " + data.value(Short.class));

                            }
                        });
                    }
                }).continueWith(new Continuation<Route, Void>() {
                    @Override
                    public Void then(Task<Route> task) throws Exception {
                        if (task.isFaulted()) {
                            Log.w(TAG, "Failed to configure app", task.getError());
                        } else {
                            Log.i(TAG, "app configured");
                        }
                        return null;
                    }
                });

                return accel.acceleration().addRouteAsync(new RouteBuilder() {
                    @Override
                    public void configure(RouteComponent source) {
                        source.stream(new Subscriber() {
                            @Override
                            public void apply(com.mbientlab.metawear.Data data, Object... env) {
                                String tempTimeStamp = data.formattedTimestamp();
                                Float tempX = data.value(Acceleration.class).x();
                                Float tempY = data.value(Acceleration.class).y();
                                Float tempZ = data.value(Acceleration.class).z();
                                Float tempForce = new Float(Math.random());
                                mData = new Data(tempTimeStamp, tempX, tempY,tempZ, tempForce);
                                //Add Data to Sensor
                                sensorData.addData(mData);
                                //Append Temporary Data to StringBuilder
                                Log.i(TAG, "apply: -----");
                                Log.i(TAG, ""+sensorData.getDataList().size());
                                Log.i(TAG, "apply: -----");
                            }

                        });
                    }
                });
            }
        }).continueWith(new Continuation<Route, Void>() {
            @Override
            public Void then(Task<Route> task) throws Exception {
                if (task.isFaulted()) {
                    Log.w(TAG, "Failed to configure app", task.getError());
                } else {
                    Log.i(TAG, "app configured");
                }
                return null;
            }
        });

    }
    
    private Runnable mAccelRun = new Runnable() {
        @Override
        public void run() {
            Log.i(TAG, "run: Start Accel");
            accel.acceleration().start();
            accel.start();
        }
    };
    private Runnable mSetAccel = new Runnable() {
        @Override
        public void run() {
            Log.i(TAG, "run2: Destroyed Binding");
            Log.i(TAG, "run2: Stopping Accel");
            accel.stop();
            accel.acceleration().stop();
            String tempData = sensorData.getDataString().toString();
            new Client().execute(tempData);
            mAccel.setText(tempData);
        }
    };
}
