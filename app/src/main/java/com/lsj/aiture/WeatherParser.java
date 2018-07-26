package com.lsj.aiture;

import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

/**
 * Created by kyyet on 2018-07-23.
 */

public class WeatherParser {

    private Document document = null;
    private ArrayList<WeatherVO> list = null;

    public  WeatherParser(Document document){
        this.document = document;
    }

    /*
            파싱한 결과를 리스트에 담음
     */
    public ArrayList getWeatherData(){

        list = new ArrayList<>();
        try {

            NodeList headerList = document.getElementsByTagName("header");

            Node tm = headerList.item(0);
            Element tmElement = (Element) tm;

            //시간
            NodeList TimeNode = tmElement.getElementsByTagName("tm");
            String time = TimeNode.item(0).getChildNodes().item(0).getNodeValue();

            // 위도
            NodeList latitudeNode = tmElement.getElementsByTagName("x");
            String latitude = latitudeNode.item(0).getChildNodes().item(0).getNodeValue();

            // 경도
            NodeList longitudeNode = tmElement.getElementsByTagName("y");
            String longitude = longitudeNode.item(0).getChildNodes().item(0).getNodeValue();

            NodeList nodeList = document.getElementsByTagName("data");

            for(int i = 0;i< nodeList.getLength(); i++){

                Node node = nodeList.item(i);
                Element fstElmnt = (Element) node;

                WeatherVO weatherVO = new WeatherVO();

                // 위도
                weatherVO.setLATITUDE(latitude);

                // 경도
                weatherVO.setLONGITUDE(longitude);

                // 시간
                weatherVO.setTIME(time);

                // 시간
                NodeList hourNode = fstElmnt.getElementsByTagName("hour");
                weatherVO.setHOUR(hourNode.item(0).getChildNodes().item(0).getNodeValue());

                // 현재 기온
                NodeList tempNode = fstElmnt.getElementsByTagName("temp");
                weatherVO.setTEMP(tempNode.item(0).getChildNodes().item(0).getNodeValue());

                NodeList popNode = fstElmnt.getElementsByTagName("pop");
                weatherVO.setPop(popNode.item(0).getChildNodes().item(0).getNodeValue());

                // 최고 기온
                NodeList tmxNode = fstElmnt.getElementsByTagName("tmx");
                weatherVO.setTMX(tmxNode.item(0).getChildNodes().item(0).getNodeValue());

                // 최저기온
                NodeList tmnNode = fstElmnt.getElementsByTagName("tmn");
                weatherVO.setTMN(tmnNode.item(0).getChildNodes().item(0).getNodeValue());

                // 날씨
                NodeList wfKorNode = fstElmnt.getElementsByTagName("wfKor");
                weatherVO.setWKKOR(wfKorNode.item(0).getChildNodes().item(0).getNodeValue());

                // 습도
                NodeList rehList = fstElmnt.getElementsByTagName("reh");
                weatherVO.setREH(rehList.item(0).getChildNodes().item(0).getNodeValue());

                list.add(weatherVO);
            }
        }catch (NullPointerException e){
            Log.i("GetWeatherForAPI", "Null Point Exception");
        }
        Log.i("WeatherParser", list.size()+" ");
        return list;
    }
}
