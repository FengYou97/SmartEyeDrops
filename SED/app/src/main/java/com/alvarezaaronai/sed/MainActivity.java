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
import android.view.View;
import android.widget.ProgressBar;
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
    private Handler mHandler = new Handler();
    private StringBuilder mTempData;
    /*
        View Variables
     */
    private TextView mAccel;
    private TextView mGpio;
    private ProgressBar mProgressBar;
    /*
        Main Activity
     */
    //private final String MW_MAC_ADDRESS = "F5:64:B2:18:F2:09"; //Aaron Sensor
    private final String MW_MAC_ADDRESS = "CF:95:7C:47:C6:60"; //Feng Sensor
    //If you change the Mac Address, reset Branch,
    //Only change it to test your own device.
    private SensorSession sensorData = new SensorSession();
    private Data mData;
    private Float mTempForce = new Float(-1);
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
        mGpio = findViewById(R.id.homeactivity_textview_gpio);
        mProgressBar = findViewById(R.id.homeactivity_progressBar_loading);
        //Clear Text View
        mAccel.setText("");
        mGpio.setText("");
        mTempData = new StringBuilder();
        Log.i(TAG, "onCreate: Binding Service");
        /*
           Bind a Connection Service / Disconnect when App is close
         */
        // Bind the service when the activity is created
        getApplicationContext().bindService(new Intent(this, BtleService.class),
                this, Context.BIND_AUTO_CREATE);
        Log.i(TAG, "onCreate: Finished Binding Service");
        //Gather Data
        mProgressBar.setVisibility(View.VISIBLE);
        mHandler.postDelayed(mAccelRun, 15000);
        mHandler.postDelayed(mSetAccel, 30000);
        mHandler.postDelayed(mGpioRun, 15000);
        mHandler.postDelayed(mSetGpio, 30000);
        /*
            All Sensor Data Wil Be updated to AWS
         */

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: Destroyed Binding : Again");
        // Unbind the service when the activity is destroyed
        //Todo Unbind when Destroyed
        getApplicationContext().unbindService(this);

    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        // Typecast the binder to the service's LocalBinder class
        serviceBinder = (BtleService.LocalBinder) service;
        Log.i(TAG, "onServiceConnected: Service Connected : " + service.toString());
        retrieveBoard();
        retrieveBoard2();
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        Log.i(TAG, "onServiceDisconnected: Service Disconnected");
    }

    /*
     *Methods HomeActivity
     */
    //Retrieve GPIO
    public void retrieveBoard2() {
        final BluetoothManager btManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        final BluetoothDevice remoteDevice =
                btManager.getAdapter().getRemoteDevice(MW_MAC_ADDRESS);
        Log.i(TAG, "retrieveBoard: Trying to connect to : " + MW_MAC_ADDRESS);
        // Create a MetaWear board object for the Bluetooth Device
        board = serviceBinder.getMetaWearBoard(remoteDevice);
        board.connectAsync().onSuccessTask(task -> {
            gpio = board.getModule(Gpio.class);
            ForcedDataProducer adc = gpio.pin((byte) 1).analogAdc();

            Log.i(TAG, "retrieveBoard2: ADC");
            return adc.addRouteAsync(source -> source.stream(new Subscriber() {
                @Override
                public void apply(com.mbientlab.metawear.Data data, Object... env) {
                    mTempForce = data.value(Short.class).floatValue();
                    Log.i(TAG, "adc = " + mTempForce); // Track : GPIO

                }
            }));
        }).continueWith((Continuation<Route, Void>) task -> {
            if (task.isFaulted()) {
                Log.w(TAG, "Failed to configure app : GPIO", task.getError());
            } else {
                Log.i(TAG, "app configured : GPIO");
            }
            return null;
        });
    }

    //Retrieve Accel
    public void retrieveBoard() {
        final BluetoothManager btManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        final BluetoothDevice remoteDevice =
                btManager.getAdapter().getRemoteDevice(MW_MAC_ADDRESS);
        Log.i(TAG, "retrieveBoard: Trying to connect to : " + MW_MAC_ADDRESS);
        // Create a MetaWear board object for the Bluetooth Device
        board = serviceBinder.getMetaWearBoard(remoteDevice);
        board.connectAsync().onSuccessTask(task -> {

            Log.i(TAG, "connected to: " + MW_MAC_ADDRESS);

            accel = board.getModule(Accelerometer.class);
            accel.configure()
                    .odr(30f)       // Set sampling frequency to 30Hz, or closest valid ODR
                    .range(4f)      // Set data range to +/-4g, or closet valid range
                    .commit();

            return accel.acceleration().addRouteAsync(source -> source.stream(new Subscriber() {
                @Override
                public void apply(com.mbientlab.metawear.Data data, Object... env) {
                    String tempTimeStamp = data.formattedTimestamp();
                    Float tempX = data.value(Acceleration.class).x();
                    Float tempY = data.value(Acceleration.class).y();
                    Float tempZ = data.value(Acceleration.class).z();
                    Float tempForce = mTempForce; //mTempForce if = -1, Data is Invalid
                    mData = new Data(tempTimeStamp, tempX, tempY, tempZ, tempForce);
                    //Add Data to Sensor
                    sensorData.addData(mData);
                    Log.d(TAG, "apply() returned: " + mData.toString()); // Track : Accel
                }

            }));
        }).continueWith((Continuation<Route, Void>) task -> {
            if (task.isFaulted()) {
                Log.w(TAG, "Failed to configure app : Accel", task.getError());
            } else {
                Log.i(TAG, "app configured : Accel");
            }
            return null;
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
            mProgressBar.setVisibility(View.GONE);
            Log.v(TAG, "run: Data Size : \n " + sensorData.getDataList().size());
            Log.i(TAG, "run2: Destroyed Binding");
            Log.i(TAG, "run2: Stopping Accel");

            accel.stop();
            accel.acceleration().stop();
            String tempData = sensorData.getDataString().toString();
            //Set TextView Data
            mAccel.setText(tempData);
            new Client().execute(tempData); // TODO : Send Data to Cloud
        }
    };
    private Runnable mGpioRun = new Runnable() {
        @Override
        public void run() {
            Log.i(TAG, "run: Starting Pin GPIO");
            gpio.pin((byte) 1).monitor().start();

        }
    };
    private Runnable mSetGpio = new Runnable() {
        @Override
        public void run() {

            Log.i(TAG, "run: Stoping Pin GPIO");
            Log.i(TAG, "run: Unbinding ");
            gpio.pin((byte) 1).monitor().stop();
            //Unbind After X Amount of time
            //This Unbinds all sensors / Stops all sensors
            getApplicationContext().unbindService(MainActivity.this);


        }
    };

}
