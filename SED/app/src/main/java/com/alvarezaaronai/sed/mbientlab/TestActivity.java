package com.alvarezaaronai.sed.mbientlab;

import androidx.appcompat.app.AppCompatActivity;
import bolts.Continuation;
import bolts.Task;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

import com.alvarezaaronai.sed.R;
import com.mbientlab.metawear.Data;
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

public class TestActivity extends AppCompatActivity implements ServiceConnection {
    private BtleService.LocalBinder serviceBinder;

    private MetaWearBoard board;

    private Accelerometer accelerometer;
    private Gpio gpio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        // Bind the service when the activity is created
        getApplicationContext().bindService(new Intent(this, BtleService.class),
                this, Context.BIND_AUTO_CREATE);

        findViewById(R.id.start_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accelerometer.acceleration().start();
                accelerometer.start();
            }
        });

        findViewById(R.id.stop_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accelerometer.stop();
                accelerometer.acceleration().stop();
            }
        });

    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        // Typecast the binder to the service's LocalBinder class
        serviceBinder = (BtleService.LocalBinder) service;
        retrieveBoard("D3:46:95:59:B1:29");
    }

    public void retrieveBoard(final String macAddr) {
        final BluetoothManager btManager=
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        assert btManager != null;
        final BluetoothDevice remoteDevice=
                btManager.getAdapter().getRemoteDevice(macAddr);

        // Create a MetaWear board object for the Bluetooth Device
        board = serviceBinder.getMetaWearBoard(remoteDevice);

        // COMMENNTTSOJDNFSODJFSODFN
        board.connectAsync().onSuccessTask(new Continuation<Void, Task<Route>>() {
            @Override
            public Task<Route> then(Task<Void> task) throws Exception {

                Log.i("Freefall:", "connected to: " + macAddr);

                accelerometer = board.getModule(Accelerometer.class);
                accelerometer.configure()
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
                                Log.i("MainActivity", "adc = " + data.value(Short.class));
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

                return accelerometer.acceleration().addRouteAsync(new RouteBuilder() {
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
                    Log.w("freefall", "Failed to configure app", task.getError());
                } else {
                    Log.i("freefall: ", "app configured");
                }
                return null;
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // Unbind the service when the activity is destroyed
        getApplicationContext().unbindService(this);
    }



    @Override
    public void onServiceDisconnected(ComponentName componentName) { }
}
