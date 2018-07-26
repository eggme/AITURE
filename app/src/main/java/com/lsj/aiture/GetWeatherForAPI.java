package com.lsj.aiture;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by LSJ on 2018-07-10.
 */
public class GetWeatherForAPI extends AsyncTask<Void,Void,Document> {

    private String url;

    public GetWeatherForAPI(int x , int y){
        url = "http://www.kma.go.kr/wid/queryDFS.jsp?";
        String gridx = "gridx="+x+"&";
        String gridy = "gridy="+y;
        url += gridx + gridy;
    }
    /*

        기상청 OpenAPI를 이용해 날씨정보를 구함
        http://www.kma.go.kr/wid/queryDFS.jsp?gridx=59&gridy=75
    */

    /*
           위도와 경도를 통해 날씨 정보 파싱
           GPSController -> GPS를 사용하여 현재 위도와 경도를 제공
     */
    @Override
    protected Document doInBackground(Void... voids) {
        Document doc = null;
        try{
             DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
             DocumentBuilder builder = builderFactory.newDocumentBuilder();

             URL parsingURL = new URL(url);
             doc = builder.parse(new InputSource(parsingURL.openStream()));
             doc.getDocumentElement().normalize();


        } catch (Exception e) {
            Log.i("GetWeatherForAPI", e.getMessage());
        }
        return doc;
    }
}
