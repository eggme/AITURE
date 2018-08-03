package com.lsj.aiture;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class BluetoothService {

    private final String TAG = "BluetoothService";
    private final int REQUEST_ENABLE_BLUETOOTH = 1;
    private BluetoothAdapter mbBluetoothAdapter;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB ");

    private BluetoothAdapter btAdapter;
    private Activity activity;
    private Handler handler;

    public BluetoothService(Activity activity){
        this.activity = activity;
        btAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public void checkBluetooth(){
        if(mbBluetoothAdapter == null){
            Toast.makeText(activity, "블루투스를 지원하지 않습니다.", Toast.LENGTH_LONG).show();
        }else{
            if(mbBluetoothAdapter.isEnabled()){
                Intent in = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                activity.startActivityForResult(in, REQUEST_ENABLE_BLUETOOTH);
            }
        }
    }

    public void findDevice(){
        final Set<BluetoothDevice> pairedDevices = mbBluetoothAdapter.getBondedDevices();
        if(pairedDevices.size() > 0){
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle("블루투스 장치 선택");

            List<String> listitems = new ArrayList<>();
            for(BluetoothDevice device : pairedDevices){
                listitems.add(device.getName());
            }
            listitems.add("취소");

            final CharSequence[] items = listitems.toArray(new CharSequence[listitems.size()]);

            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(which == pairedDevices.size()){
                        return;
                    }else{
                        connectToSelectedDevices(items[which].toString());
                    }
                }
            });

            builder.setCancelable(false);
            AlertDialog alert = builder.create();
            alert.show();

        }else{
            Toast.makeText(activity, "페어링된 장치가 없습니다.", Toast.LENGTH_LONG).show();
        }
    }

    public void connectToSelectedDevices(String deviceName){

    }
}
