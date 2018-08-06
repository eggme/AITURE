package com.lsj.aiture;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
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

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NoActionBar{

    private WeatherParser weatherParser = null;
    private FinedustParser finedustParser = null;
    private BluetoothService btService;
    private final int REQUEST_ENABLE_BLUETOOTH = 1;

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

    private BluetoothAdapter bluetoothAdapter;
    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String result = msg.getData().getString("result");
            Toast.makeText(getApplicationContext(), result , Toast.LENGTH_LONG).show();
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
        setContentView(R.layout.activity_main);
        graph = (RelativeLayout)findViewById(R.id.graph);
        wrapper = (RelativeLayout)findViewById(R.id.wrapper);
        circularchart = (LinearLayout) findViewById(R.id.circularchart);
        finedust = (RelativeLayout)findViewById(R.id.finedust);
        humidity = (RelativeLayout)findViewById(R.id.humidity);
        precipitation = (RelativeLayout)findViewById(R.id.precipitation);
        temp = (TextView)findViewById(R.id.temp);
        weather_kor = (TextView)findViewById(R.id.weather_kor);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        btService = new BluetoothService(bluetoothAdapter ,handler);
        startSystem();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_ENABLE_BLUETOOTH :
                if(resultCode == Activity.RESULT_OK){

                }else{
                    Toast.makeText(getApplicationContext(), "블루투스를 지원해 주세요.", Toast.LENGTH_LONG).show();
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
    protected void onResume() {
        super.onResume();
        startSystem();
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
