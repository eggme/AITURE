package com.lsj.aiture;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class GetMarketServerData extends AsyncTask<Void, Void, ArrayList<MarketVO>> {

    private final String SERVER_URL = "http://192.168.43.41:52273/";
    private ArrayList<MarketVO> list = new ArrayList<>();

    @Override
    protected ArrayList<MarketVO> doInBackground(Void... voids) {
        StringBuffer sb = new StringBuffer();
        try{
            Document doc = Jsoup.connect(SERVER_URL).get();
            Elements content = doc.select("td.data");
            int cnt =1;
            MarketVO vo = new MarketVO();
            for (Element element : content){
                switch (cnt){
                    case 1:
                        vo.setTitle(element.text());
                        break;
                    case 2:
                        vo.setImage(element.text());
                        break;
                    case 3:
                        vo.setContent(element.text());
                        cnt = 0;
                        list.add(vo);
                        vo = new MarketVO();
                        break;
                }
                cnt++;
            }
        }catch (IOException e){
            Log.i("asdasd", "IOException 발생");
        }
        return list;
    }
}
