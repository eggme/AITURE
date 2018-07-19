package com.lsj.aiture;

import android.os.AsyncTask;
import android.util.Log;

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
public class Parser extends AsyncTask<Integer,Void,Document> {

    /*

        기상청 OpenAPI를 이용해 날씨정보를 구함

    */

    private String url = "http://www.kma.go.kr/wid/queryDFS.jsp?";
    private String gridx = "gridx=";
    private String gridy = "gridy=";

    /*
            파싱한 결과를 리스트에 담음
     */
    @Override
    protected void onPostExecute(Document doc) {
        ArrayList<WeatherDTO> weatherList = new ArrayList<WeatherDTO>();
        try {

            NodeList headerList = doc.getElementsByTagName("header");

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

            NodeList nodeList = doc.getElementsByTagName("data");

            for(int i = 0;i< nodeList.getLength(); i++){

                Node node = nodeList.item(i);
                Element fstElmnt = (Element) node;

                WeatherDTO weatherDTO = new WeatherDTO();

                // 위도
                weatherDTO.setLATITUDE(latitude);

                // 경도
                weatherDTO.setLONGITUDE(longitude);

                // 시간
                weatherDTO.setTIME(time);

                // 시간
                NodeList hourNode = fstElmnt.getElementsByTagName("hour");
                weatherDTO.setHOUR(hourNode.item(0).getChildNodes().item(0).getNodeValue());

                // 현재 기온
                NodeList tempNode = fstElmnt.getElementsByTagName("temp");
                weatherDTO.setTEMP(tempNode.item(0).getChildNodes().item(0).getNodeValue());

                // 최고 기온
                NodeList tmxNode = fstElmnt.getElementsByTagName("tmx");
                weatherDTO.setTMX(tmxNode.item(0).getChildNodes().item(0).getNodeValue());

                // 최저기온
                NodeList tmnNode = fstElmnt.getElementsByTagName("tmn");
                weatherDTO.setTMN(tmnNode.item(0).getChildNodes().item(0).getNodeValue());

                // 날씨
                NodeList wfKorNode = fstElmnt.getElementsByTagName("wfKor");
                weatherDTO.setWKKOR(wfKorNode.item(0).getChildNodes().item(0).getNodeValue());

                // 습도
                NodeList rehList = fstElmnt.getElementsByTagName("reh");
                weatherDTO.setREH(rehList.item(0).getChildNodes().item(0).getNodeValue());

                weatherList.add(weatherDTO);
            }
        }catch (NullPointerException e){
            Log.i("Parser value = ", "Null Point Exception");
        }

        // 그래프 그리기
        drawGraph(weatherList);

        super.onPostExecute(doc);
    }

    /*
            파싱된 결과를 통해 그래프를 뷰에 그림
     */
    private void drawGraph(ArrayList<WeatherDTO> list) {

        Log.i("Parser value = ", list.size()+ " " + list.get(2).getWKKOR());

    }


    /*
           위도와 경도를 통해 날씨 정보 파싱
     */
    @Override
    protected Document doInBackground(Integer... integers) {
        Document doc = null;
        url += gridx+integers[0]+"&"+gridy+integers[1];
        try{
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();

            URL parsingURL = new URL(url);
            doc = builder.parse(new InputSource(parsingURL.openStream()));
            doc.getDocumentElement().normalize();


        } catch (Exception e) {
            Log.i("Parser class error by =", e.getMessage());
        }

        return doc;
    }
}
