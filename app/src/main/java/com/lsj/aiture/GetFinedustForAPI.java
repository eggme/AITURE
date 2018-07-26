package com.lsj.aiture;

import android.os.AsyncTask;
import android.util.Log;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class GetFinedustForAPI extends AsyncTask<Void,Void,Document> {

    private String url;

    public GetFinedustForAPI(String sidoName, String goName){
        url = "http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/getCtprvnMesureSidoLIst?";
        String serviceKey = "serviceKey=KTpbaJSfa7K95ztc%2Fc%2Fhq5x65gdxXysXwu1D8%2FQOh3aLn%2BEs2iiEvLuaC6EUNFDCScKPRPQvHbTZbq%2BKAPxZyQ%3D%3D";
        String numOfRows = "&numOfRows=5";
        String pageSize = "&pageSize=1";
        String pageNo = "&pageNo=1";
        String startPage = "&startPage=1";
        String contentText2 = "";
        try{
            contentText2 = java.net.URLEncoder.encode(new String(sidoName.getBytes("UTF-8")));
        }catch (Exception e){}
        String sidoNames = "&sidoName="+contentText2;
        String searchCodndition = "&searchCondition=DAILY";
        url += serviceKey + numOfRows + pageSize + pageNo + startPage + sidoNames + searchCodndition;
    }

    @Override
    protected Document doInBackground(Void... voids) {
        Document doc = null;
        try{
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            URL parsingURL = new URL(url);
            InputSource source = new InputSource(parsingURL.openStream());
            doc = builder.parse(source);
            doc.getDocumentElement().normalize();
        } catch (Exception e) {
            Log.i("GetFinedustForAPI", e.getMessage());
        }
        return doc;
    }
}
