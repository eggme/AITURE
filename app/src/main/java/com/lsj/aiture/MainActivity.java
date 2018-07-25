package com.lsj.aiture;

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
    private ArrayList<WeatherDTO> list;

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
        getWeather();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWeather();
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
        getWeatherForAPI.execute();
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
    }

    private void drowGraph(){
        list = weatherParser.getWeatherData();
        GraphManager manager = new GraphManager(list);
        GraphVO vo = manager.getGraph(getApplicationContext());
        graph.addView(new GraphTextureView(getApplicationContext(), vo));
    }
}
