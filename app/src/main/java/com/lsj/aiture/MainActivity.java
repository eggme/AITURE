package com.lsj.aiture;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tsengvn.typekit.TypekitContextWrapper;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NoActionBar{

    private WeatherParser weatherParser = null;
    private FinedustParser finedustParser = null;

    private RelativeLayout wrapper;
    private RelativeLayout graph;
    private LinearLayout circularchart;
    private RelativeLayout finedust;
    private RelativeLayout humidity;
    private RelativeLayout precipitation;
    private ImageView setting;
    private TextView temp;
    private TextView weather_kor;
    private CircularOutlineGraph circularOutlineGraph;

    private ArrayList<WeatherVO> weatherList;
    private ArrayList<FinedustVO> finedustList;

    private GPSController controller;
    private ConvertCoordinate convertCoordinate;
    private LatXLngY latXLngY;
    private Weather weather;

    private final int PERMISSIONS_ACCESS_FINE_LOCATION = 1000;
    private final int PERMISSIONS_ACCESS_COARSE_LOCATION = 1001;
    private boolean isPermission = false;
    private boolean isAccessFineLocation = false;
    private boolean isAccessCoarseLocation = false;

    private BluetoothAdapter adapter;
    private BluetoothService service;
    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.i("asdasd", "message!!");
            switch (msg.what){
                case BluetoothState.BLUETOOTH_MESSAGE_CHANGE :
                    switch (msg.arg1){
                        case BluetoothState.STATE_CONNECTED :
                            Toast.makeText(getApplicationContext(), "연결 성공", Toast.LENGTH_SHORT).show();
                            break;
                        case BluetoothState.STATE_CONNECTING :
                            Log.i("asdasd", "Connecting");
                            break;
                        case BluetoothState.STATE_NONE :
                            Log.i("asdasd", "None");
                            break;
                    }
                    break;
                case BluetoothState.EXTRA_DEVICE_NUMBER :
                    Message message = (Message) msg.obj;
                    Bundle bundle = message.getData();
                    String address = bundle.getString(BluetoothState.EXTRA_DEVICE_ADDRESS);
                    Toast.makeText(getApplicationContext(), "페어링된 디바이스 : "+ address, Toast.LENGTH_LONG).show();
                    BluetoothDevice device = adapter.getRemoteDevice(address);
                    service.connect(device);
                    break;
                case BluetoothState.BLUETOOTH_MESSAGE_READ :
                    // 메세지 읽을 때
                    byte[] readBuf = (byte[]) msg.obj;
                    String readMsg = new String(readBuf, 0 , msg.arg1);
                    Log.i("asdasd", readMsg);
                    Toast.makeText(getApplicationContext(), readMsg, Toast.LENGTH_LONG).show();
                    break;
                case BluetoothState.BLUETOOTH_MESSAGE_WRITE :
                    Log.i("asdasd", "BLUETOOTH_MESSAGE_WRITE");
                    Message msg1 = (Message) msg.obj;
                    Bundle bd = msg1.getData();
                    String add = bd.getString(BluetoothState.EXTRA_DATA);
                    Log.i("asdasd", add);
                    Toast.makeText(getApplicationContext(), add, Toast.LENGTH_LONG).show();
                    service.write(add.getBytes());
                    break;
                case BluetoothState.BLUETOOTH_MESSAGE_WRITE_SUCCESS :
                    Log.i("asdasd", "BLUETOOTH_MESSAGE_WRITE_SUCCESS");
                    byte[] bytes = (byte[]) msg.obj;
                    String adds = new String(bytes, StandardCharsets.UTF_8);
                    Log.i("asdasd", adds);
                    Toast.makeText(getApplicationContext(), adds + " Write Success", Toast.LENGTH_LONG).show();
                        break;
                case BluetoothState.BLUETOOTH_MESSAGE_DEVICE_NAME :
                    String deviceName = msg.getData().getString(BluetoothState.DEVICE_NAME);
                    Toast.makeText(getApplicationContext(), deviceName, Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        HideActionbar();
        if(!adapter.isEnabled()){
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, BluetoothState.REQUEST_ENABLE_BT);
        }else{
            setUpBT();
        }
    }

    private void setUpBT(){
        service = new BluetoothService(adapter, handler);
        if(service != null){
            Log.i("asdasd", "service not null");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adapter = BluetoothAdapter.getDefaultAdapter();
        setting = (ImageView)findViewById(R.id.setting);
        graph = (RelativeLayout)findViewById(R.id.graph);
        wrapper = (RelativeLayout)findViewById(R.id.wrapper);
        circularchart = (LinearLayout) findViewById(R.id.circularchart);
        finedust = (RelativeLayout)findViewById(R.id.finedust);
        humidity = (RelativeLayout)findViewById(R.id.humidity);
        precipitation = (RelativeLayout)findViewById(R.id.precipitation);
        temp = (TextView)findViewById(R.id.temp);
        weather_kor = (TextView)findViewById(R.id.weather_kor);
        String address = getIntent().getExtras().getString(BluetoothState.EXTRA_DEVICE_ADDRESS);
        Log.i("asdasd", address);
        if(address != null){
            Bundle b = new Bundle();
            b.putString(BluetoothState.EXTRA_DEVICE_ADDRESS,address);
            Message m = new Message();
            m.setData(b);
            handler.obtainMessage(BluetoothState.EXTRA_DEVICE_NUMBER , m).sendToTarget();
        }
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message msg = new Message();
                Bundle b = new Bundle();
                Log.i("asdasd", "Welcome to Bluetooth for AITURE!");
                b.putString(BluetoothState.EXTRA_DATA , "Welcome to Bluetooth for AITURE!");
                msg.setData(b);
                handler.obtainMessage(BluetoothState.BLUETOOTH_MESSAGE_WRITE, msg).sendToTarget();
            }
        });
        startSystem();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case BluetoothState.REQUEST_ENABLE_BT :
                if(resultCode == Activity.RESULT_OK){
                    setUpBT();
                }
                break;
        }

    }


    private void startSystem(){
        weather = new Weather();
        convertCoordinate = new ConvertCoordinate();
        if(!isPermission){
            callPermission();
            return;
        }
        controller = new GPSController(getApplicationContext());
        if(controller.isGetLocation()){
            latXLngY =  convertCoordinate.convertGRID_GPS(0, controller.getLatitude(), controller.getLongtitude());
            getWeather();
        }else{
            controller.showSettingsAlert();
        }
    }

    @Override
    protected synchronized void onResume() {
        super.onResume();
        startSystem();
        if(service != null){
            if(service.getState() == BluetoothState.STATE_NONE){
                service.start();
            }
        }
    }

    @Override
    public void HideActionbar() {
        View view = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        view.setSystemUiVisibility(uiOptions);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }

    public void getWeather() {
        GetWeatherForAPI getWeatherForAPI = new GetWeatherForAPI(((int)latXLngY.getX()), ((int)latXLngY.getY()));
        ConvertAddress convertAddress = new ConvertAddress(getApplicationContext());
        String address = convertAddress.getAddress(controller.getLatitude(), controller.getLongtitude());
        try {
            getWeatherForAPI.execute();
            weatherParser = new WeatherParser(getWeatherForAPI.get());
        }catch (Exception e){
            Log.i("WeatherParser", e.getMessage());
        }finally {
            getWeatherForAPI.cancel(true);
        }
        drowGraph();
        String[] data = address.split(",");
        GetFinedustForAPI getFinedustForAPI = new GetFinedustForAPI(data[0], data[1]);
        try{
            getFinedustForAPI.execute();
            finedustParser = new FinedustParser(getFinedustForAPI.get());
        }catch (Exception e){
            Log.i("FinedustParser", e.getMessage());
        }finally {
            getFinedustForAPI.cancel(true);
        }
        finedustList = finedustParser.getFinedustData();
        weatherSetting(data[1]);

    }

    private void weatherSetting(String guName) {
        temp.setText(((int)Float.parseFloat(weatherList.get(0).getTEMP())) + "");
        weather_kor.setText(weatherList.get(0).getWKKOR());
        wrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("MainActivity" , "Click");
                if(graph.getVisibility() == View.GONE) {
                    Log.i("MainActivity" , "VISIBLE");
                    graph.setVisibility(View.VISIBLE);
                    circularchart.setVisibility(View.VISIBLE);
                }else{
                    Log.i("MainActivity" , "GONE");
                    graph.setVisibility(View.GONE);
                    circularchart.setVisibility(View.GONE);
                }
            }
        });
        int index = findGu(guName);
        if(index <= 0){
            index = 0;
        }
        FinedustDistinction finedustDistinction = new FinedustDistinction(Integer.parseInt(finedustList.get(index).getPm10Value()));

        float finedustValue = Float.parseFloat(finedustList.get(index).getPm10Value());
        circularOutlineGraph = new CircularOutlineGraph(getApplicationContext()  ,finedustDistinction.MAX, finedustValue , finedustValue+"" , "미세먼지");
        finedust.addView(circularOutlineGraph);

        float humidityValue = Float.parseFloat(weatherList.get(0).getREH());
        circularOutlineGraph = new CircularOutlineGraph(getApplicationContext(), 100, humidityValue , humidityValue+"" , "습도");
        humidity.addView(circularOutlineGraph);

        float precipitationValue = Float.parseFloat(weatherList.get(0).getPop());
        circularOutlineGraph = new CircularOutlineGraph(getApplicationContext() , 100, precipitationValue, precipitationValue+"" , "강수확률");
        precipitation.addView(circularOutlineGraph);
    }

    private int findGu(String guName) {
        for(int i=0;i<finedustList.size();i++){
            if(guName.equals(finedustList.get(i))){
                return i;
            }
        }
        return -1;
    }

    private void drowGraph(){
        weatherList = weatherParser.getWeatherData();
        GraphManager manager = new GraphManager(weatherList);
        GraphVO vo = manager.getGraph(getApplicationContext());
        graph.addView(new GraphTextureView(getApplicationContext(), vo));
    }

    private void callPermission() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_ACCESS_FINE_LOCATION);

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){

            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSIONS_ACCESS_COARSE_LOCATION);
        } else {
            isPermission = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_ACCESS_FINE_LOCATION
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            isAccessFineLocation = true;
        } else if (requestCode == PERMISSIONS_ACCESS_COARSE_LOCATION
                && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            isAccessCoarseLocation = true;
        }
        if (isAccessFineLocation && isAccessCoarseLocation) {
            isPermission = true;
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }
}
