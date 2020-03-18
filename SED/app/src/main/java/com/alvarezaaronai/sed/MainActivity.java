package com.alvarezaaronai.sed;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

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

    /*
        Main Activity
     */
    private final String MW_MAC_ADDRESS= "F5:64:B2:18:F2:09";
                                        //If you change the Mac Address, reset Branch,
                                        //Only change it to test your own device.
    private SensorSession sensorData = new SensorSession();

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
        retrieveBoard();
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) { }

    /*
     *Methods HomeActivity
     */
    public void retrieveBoard() {
        final BluetoothManager btManager=
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        final BluetoothDevice remoteDevice=
                btManager.getAdapter().getRemoteDevice(MW_MAC_ADDRESS);
        Log.i(TAG, "retrieveBoard: Trying to connect to : " + MW_MAC_ADDRESS );
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
                            public void apply(Data data, Object ... env) {
                                Log.i(TAG, "adc = " + data.value(Short.class));
                            }
                        });
                    }
                }).continueWith(new Continuation<Route, Void>() {
                    @Override
                    public Void then(Task<Route> task) throws Exception {
                        if(task.isFaulted()) {
                            Log.w("freefall GPIO", "Failed to configure app", task.getError());
                        } else {
                            Log.i("freefall GPIO: ", "app configured");
                        }
                        return null;
                    }
                });

                return accel.acceleration().addRouteAsync(new RouteBuilder() {
                    @Override
                    public void configure(RouteComponent source) {
                        source.stream(new Subscriber() {
                            @Override
                            public void apply(Data data, Object... env) {
                                Log.i("MainActivity", data.value(Acceleration.class).toString());
                            }
                        });
                    }
                });
            }
        }).continueWith(new Continuation<Route, Void>() {
            @Override
            public Void then(Task<Route> task) throws Exception {
                if(task.isFaulted()) {
                    Log.w(TAG, "Failed to configure app", task.getError());
                } else {
                    Log.i(TAG, "app configured");
                }
                return null;
            }
        });
    }

}
