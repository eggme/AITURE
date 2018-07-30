package com.lsj.aiture;

import android.app.Activity;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

public class BluetoothService extends Service {

    private final String TAG = "BluetoothService";
    private final int REQUEST_ENABLE_BLUETOOTH = 1;

    private BluetoothAdapter btAdapter;
    private Activity activity;
    private Handler handler;

    public BluetoothService(Activity activity, Handler handler){
        this.activity = activity;
        this.handler = handler;
        btAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public boolean getDeviceState(){
        Log.i(TAG,"Check the Bluetooth Support");

        if(btAdapter == null){
            Log.i(TAG,"Bluetooth is not available");
            return false;
        }else{
            Log.i(TAG,"Bluetooth is available");
            return true;
        }
    }

    public void enableBluetooth(){
        Log.i(TAG,"Check the enable Bluetooth");

        if(btAdapter.isEnabled()){
            Log.i(TAG, "Bluetooth Enable Now");
        }else{
            Log.i(TAG, "Bluetooth Enable Request");

            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(intent, REQUEST_ENABLE_BLUETOOTH);
        }
    }



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
