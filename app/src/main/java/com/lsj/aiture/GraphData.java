package com.lsj.aiture;

import java.util.ArrayList;

/**
 * Created by kyyet on 2018-07-23.
 */

public class GraphData {

    ArrayList<WeatherDTO> list = null;
    private int[] temp = new int[6];
    private int[] time = new int[6];

    public GraphData(ArrayList<WeatherDTO> list){
        this.list = list;
        for(int i=0;i<6;i++){
            temp[i] = (int)Float.parseFloat(list.get(i).getTEMP());
            time[i] = Integer.parseInt(list.get(i).getHOUR());
        }
    }

    public int[] getTime(){
        return time;
    }

    public int[] getTemp(){
        return temp;
    }


}
