package com.lsj.aiture;

import java.util.ArrayList;

public class Weather {

    /*
          날씨에따라 이미지 변경 흐림 -> 흐림이미지
     */

    private int[] img_resource = {R.drawable.main_setting} ;


    public String getMinTemp(ArrayList<WeatherVO> vo){
        int min = 100;
        for(int i=0;i<vo.size();i++){
            int temp = (int)(Float.parseFloat(vo.get(i).getTEMP()));
            if(min > temp){
                min = temp;
            }
        }
        return min+"";
    }

    public String getMaxTemp(ArrayList<WeatherVO> vo){
        int max = 0;
        for(int i=0;i<vo.size();i++){
            int temp = (int)(Float.parseFloat(vo.get(i).getTEMP()));
            if(max < temp){
                max = temp;
            }
        }
        return max+"";
    }


}
