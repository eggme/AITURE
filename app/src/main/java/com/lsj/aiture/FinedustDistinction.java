package com.lsj.aiture;

public class FinedustDistinction {

    private int pm10Value;
    public final float MAX = 200f;

    public FinedustDistinction(int pm10Value){
        this.pm10Value = pm10Value;
    }

    public String getFinedust(){
        String result = "";
        switch (pm10Value/10){
            case 1 :
            case 2 :
            case 3 :
                result = "좋음";
                break;
            case 4 :
            case 5 :
            case 6 :
            case 7 :
            case 8 :
                result = "보통";
                break;
            case 9 :
            case 10 :
            case 11 :
            case 12 :
            case 13 :
            case 14 :
            case 15 :
                result = "나쁨";
                break;
            default :
                result = "매우나쁨";
                break;
        }
        return result;
    }


}
