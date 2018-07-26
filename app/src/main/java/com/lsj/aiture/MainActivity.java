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
    private RelativeLayout graph;
    private TextView temp;
    private TextView weather_kor;
    private TextView pop_data;
    private ArrayList<WeatherDTO> list;
    private GPSController controller;
    private ConvertCoordinate convertCoordinate;
    private LatXLngY latXLngY;
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
        weather_kor = (TextView)findViewById(R.id.weather_kor);
        pop_data = (TextView)findViewById(R.id.pop_data);
        startSystem();
    }

    private void startSystem(){
        convertCoordinate = new ConvertCoordinate();
        if(!isPermission){
            callPermission();
            return;
        }
        controller = new GPSController(getApplicationContext());
        if(controller.isGetLocation()){
            latXLngY =  convertCoordinate.convertGRID_GPS(0, controller.getLatitude(), controller.getLongtitude());
            Log.i("GPSInfo", controller.getLatitude() + " : " + controller.getLongtitude() + " : " + latXLngY.getX() + " : " + latXLngY.getY());
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
        GetWeatherForAPI getWeatherForAPI = new GetWeatherForAPI();
        getWeatherForAPI.execute(((int)latXLngY.getX()), ((int)latXLngY.getY()));
        try {
            weatherParser = new WeatherParser(getWeatherForAPI.get());
        }catch (Exception e){
            Log.i("MainActivity = ", e.getMessage());
        }
        drowGraph();
        weatherSetting();
    }

    private void weatherSetting() {
        temp.setText(((int)Float.parseFloat(list.get(0).getTEMP())) + "");
        weather_kor.setText(list.get(0).getWKKOR());
        pop_data.setText(list.get(0).getPop());
    }

    private void drowGraph(){
        list = weatherParser.getWeatherData();
        GraphManager manager = new GraphManager(list);
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
