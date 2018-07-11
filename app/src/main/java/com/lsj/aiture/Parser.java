package com.lsj.aiture;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by LSJ on 2018-07-10.
 */
public class Parser extends AsyncTask<String,Void,ArrayList<String>> {

    private Elements elements;
    private final String SICODE = "2900000000";
    private final String GuCODE = "2917000000";
    private final String DONGCODE = "2917061500";

    @Override
    protected void onPostExecute(ArrayList<String> strings) {
        super.onPostExecute(strings);
    }

    @Override
    protected ArrayList<String> doInBackground(String... strings) {
        try {
            Document doc = Jsoup.connect("http://www.weather.go.kr/weather/main.jsp").get();
            elements = doc.select("");
            elements.toString();
        }catch (IOException e){
            Log.i("Parser", "IOExcetion = "+ e.getMessage());
        }
        return null;
    }
}
