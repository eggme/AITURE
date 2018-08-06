package com.lsj.aiture;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tsengvn.typekit.TypekitContextWrapper;

import java.util.Set;

public class SplashActivity extends AppCompatActivity implements NoActionBar{

    private ImageView img;
    private BluetoothAdapter adapter;


    private ArrayAdapter pariedDevice;
    private ArrayAdapter newDevice;
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if(BluetoothDevice.ACTION_FOUND.equals(action)){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if(device.getBondState() != BluetoothDevice.BOND_BONDED){
                    newDevice.add(device.getName() + "\n" + device.getAddress());
                }
            }else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
                setProgressBarIndeterminateVisibility(false);
                setTitle("디바이스 선택");
                if(newDevice.getCount() == 0){
                    newDevice.add("디바이스가 없습니다.");
                }
            }

        }
    };

    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        HideActionbar();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initialized();

        pariedDevice = new ArrayAdapter(this, R.layout.layout_name);
        newDevice = new ArrayAdapter(this, R.layout.layout_name);

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(receiver, filter);


        adapter = BluetoothAdapter.getDefaultAdapter();

        Set<BluetoothDevice> devices = adapter.getBondedDevices();

        if(devices != null){
            if(devices.size() > 0){
                for(BluetoothDevice device : devices){
                    pariedDevice.add(device.getName() + "\n"+ device.getAddress());
                }
            }else{
                pariedDevice.add("디바이스 없음");
            }
        }

        AlertDialog.Builder aBuilder = new AlertDialog.Builder(SplashActivity.this);
        aBuilder.setTitle("연결할 디바이스를 고르세요.");
        aBuilder.setAdapter(pariedDevice, (DialogInterface.OnClickListener) clickListener);
        Click();
    }

    private AdapterView.OnItemClickListener clickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            adapter.cancelDiscovery();

            String info = ((TextView)view).getText().toString();
            String address = info.substring(info.length() - 17);

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra(BluetoothState.EXTRA_DEVICE_ADDRESS, address);

            setResult(Activity.RESULT_OK, intent);
            startActivity(intent);
            finish();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(adapter != null){
            adapter.cancelDiscovery();
        }
        this.unregisterReceiver(receiver);
    }

    @Override
    public void HideActionbar(){
        View view = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        view.setSystemUiVisibility(uiOptions);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }

    private void doDiscovery(){
        setProgressBarIndeterminateVisibility(true);
        setTitle("디바이스 검색중");
        if(adapter.isDiscovering()){
            adapter.cancelDiscovery();
        }
        adapter.startDiscovery();
    }

    private void initialized(){
        img = (ImageView)findViewById(R.id.register);
    }

    private void Click(){
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doDiscovery();

            }
        });
    }

    // 폰트
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }
}
