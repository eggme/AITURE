package com.lsj.aiture;

import java.util.ArrayList;

/**
 * Created by kyyet on 2018-07-23.
 */

public class GraphData {

    ArrayList<WeatherDTO> list = null;
    private int[] temp = null;
    private int[] time = null;

    public GraphData(ArrayList<WeatherDTO> list){
        this.list = list;
        for(int i=0;i<6;i++){
            temp[i] = Integer.parseInt(list.get(i).getTEMP());
            time[i] = Integer.parseInt(list.get(i).getTIME());
        }
    }

    public int[] getTime(){


        return time;
    }

    public int[] getTemp(){


        return temp;
    }


}
