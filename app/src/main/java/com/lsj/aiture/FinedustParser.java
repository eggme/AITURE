package com.lsj.aiture;

import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

public class FinedustParser {

    private Document document = null;
    private ArrayList<FinedustVO> list = null;

    public  FinedustParser(Document document){
        this.document = document;
    }

    /*
            파싱한 결과를 리스트에 담음
     */
    public ArrayList getFinedustData(){
        list = new ArrayList<>();
        try {
            NodeList nodeList = document.getElementsByTagName("item");
            Log.i("FinedustParser", nodeList.getLength()+ " : dd");
            for(int i = 0;i< nodeList.getLength(); i++){

                Node node = nodeList.item(i);
                Element fstElmnt = (Element) node;

                FinedustVO finedustVO = new FinedustVO();

                // 시간
                NodeList cityNameNode = fstElmnt.getElementsByTagName("cityName");
                finedustVO.setCityName(cityNameNode.item(0).getChildNodes().item(0).getNodeValue());

                // 현재 기온
                NodeList pm10ValueNode = fstElmnt.getElementsByTagName("pm10Value");
                finedustVO.setPm10Value(pm10ValueNode.item(0).getChildNodes().item(0).getNodeValue());

                list.add(finedustVO);
            }
        }catch (NullPointerException e){
            Log.i("GetWeatherForAPI", "Null Point Exception");
        }
        Log.i("FinedustParser", list.size()+" ");
        return list;
    }

}
