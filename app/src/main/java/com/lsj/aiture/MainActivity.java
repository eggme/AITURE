package com.lsj.aiture;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NoActionBar{

    private WeatherParser weatherParser = null;
    private FinedustParser finedustParser = null;

    private RelativeLayout graph;
    private TextView temp;
    private TextView min_temp;
    private TextView max_temp;
    private TextView weather_kor;
    private TextView pop_data;
    private TextView reh_data;
    private TextView finedust_data;

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
        temp = (TextView)findViewById(R.id.temp);
        min_temp = (TextView)findViewById(R.id.min_temp);
        max_temp = (TextView)findViewById(R.id.max_temp);
        weather_kor = (TextView)findViewById(R.id.weather_kor);
        pop_data = (TextView)findViewById(R.id.pop_data);
        reh_data = (TextView)findViewById(R.id.reh_data);
        finedust_data = (TextView)findViewById(R.id.finedust_data);
        startSystem();
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
        min_temp.setText(weather.getMinTemp(weatherList));
        max_temp.setText(weather.getMaxTemp(weatherList));
        weather_kor.setText(weatherList.get(0).getWKKOR());
        pop_data.setText(weatherList.get(0).getPop());
        reh_data.setText(weatherList.get(0).getREH());
        int index = findGu(guName);
        if(index <= 0){
            index = 0;
        }
        FinedustDistinction finedustDistinction = new FinedustDistinction(Integer.parseInt(finedustList.get(index).getPm10Value()));
        finedust_data.setText(finedustDistinction.getFinedust());
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
}
