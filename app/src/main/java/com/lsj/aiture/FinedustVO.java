package com.lsj.aiture;

public class FinedustVO {

    private String cityName; // 도시이름
    private String pm10Value; // 미세먼지 농도

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getPm10Value() {
        return pm10Value;
    }

    public void setPm10Value(String pm10Value) {
        this.pm10Value = pm10Value;
    }
}
