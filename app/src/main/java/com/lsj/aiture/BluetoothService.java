package com.lsj.aiture;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class BluetoothService {

    private final String TAG = "BluetoothService";
    public final int BLUETOOTHSERVICE_CONNECTING = 1;
    private final int REQUEST_ENABLE_BLUETOOTH = 1;
    private Set<BluetoothDevice> mDevices = null;
    private int mPairedDeviceCount;
    private BluetoothAdapter btAdapter;
    private BluetoothDevice mRemoteDevice;
    private Activity activity;
    private BluetoothSocket mSocket;
    private InputStream mInputStream;
    private OutputStream mOutputStream;
    private Thread mWorkerThread;
    private int bytes;
    private Handler handler;


    public BluetoothService(Activity activity , BluetoothAdapter btAdapter , Handler handler){
        this.activity = activity;
        this.btAdapter = btAdapter;
        this.handler = handler;
    }

    public void checkBluetooth(){
        if(btAdapter == null){
            Toast.makeText(activity, "블루투스를 지원하지 않습니다.", Toast.LENGTH_LONG).show();
        }else{
            if(btAdapter.isEnabled()){
                Intent in = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                activity.startActivityForResult(in, REQUEST_ENABLE_BLUETOOTH);
            }else{
                findDevice();
            }
        }
    }

    public void findDevice(){

        mDevices = btAdapter.getBondedDevices();
        mPairedDeviceCount = mDevices.size();

        if(mPairedDeviceCount != 0){
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle("블루투스 장치 선택");

            List<String> listitems = new ArrayList<>();
            for(BluetoothDevice device : mDevices){
                listitems.add(device.getName());
            }
            listitems.add("취소");

            final CharSequence[] items = listitems.toArray(new CharSequence[listitems.size()]);

            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(which == mPairedDeviceCount){
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

    public BluetoothDevice getDeviceFromDondedList(String deviceName){
        BluetoothDevice selectedDevice = null;
        for(BluetoothDevice device : mDevices){
            if(deviceName.equals(device.getName())){
                selectedDevice = device;
            }
        }
        return selectedDevice;
    }

    public void connectToSelectedDevices(String deviceName){
        mRemoteDevice = getDeviceFromDondedList(deviceName);
        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
        Log.i(TAG, "ㅎㅇㅎㅇ");
        try{
            Log.i(TAG, "ㅎㅇㅎㅇ");
            mSocket = mRemoteDevice.createInsecureRfcommSocketToServiceRecord(uuid);
            Log.i(TAG, mSocket.isConnected()+"");
            mSocket.connect();
            Log.i(TAG, "ㅎㅇㅎㅇ");
            mOutputStream = mSocket.getOutputStream();
            mInputStream = mSocket.getInputStream();
            Log.i(TAG, "ㅎㅇㅎㅇ");
            sendData("aaa");
        }catch (IOException e){
            Toast.makeText(activity.getApplicationContext(), "IO Exception 발생", Toast.LENGTH_LONG).show();
            Log.i(TAG, e.getMessage());
            e.printStackTrace();
        }
    }

    public void sendData(String data){
        try{
            mOutputStream.write(data.getBytes());
        }catch (Exception e){}
    }

    String datas;
    private void beginListenForData() {
        mWorkerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                    try{
                        while ((bytes = mInputStream.read()) != -1){
                            datas += bytes;
                        }
                        Message msg = new Message();
                        Bundle b = new Bundle();
                        b.putString("result", datas);
                        msg.setData(b);
                        handler.sendMessage(msg);
                    }catch (IOException e){}
                }
        });
        mWorkerThread.start();
    }
}
