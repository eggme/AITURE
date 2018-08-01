package com.lsj.aiture;

/**
 * Created by kyyet on 2018-07-19.
 */

public class WeatherVO {

    private String LATITUDE; // 위도
    private String LONGITUDE;  // 경도
    private String HOUR;  // 기준 시간
    private String TIME;  // 가져온 시간
    private String TEMP;  // 온도
    private String pop;  // 강수확률
    private String TMX;  // 최고온도
    private String TMN;  // 최저온도
    private String WKKOR;  // 날씨 한글데이터
    private String REH;  // 습도

    public String getPop() {
        return pop;
    }

    public void setPop(String pop) {
        this.pop = pop;
    }

    public String getLATITUDE() {
        return LATITUDE;
    }

    public void setLATITUDE(String LATITUDE) {
        this.LATITUDE = LATITUDE;
    }

    public String getLONGITUDE() {
        return LONGITUDE;
    }

    public void setLONGITUDE(String LONGITUDE) {
        this.LONGITUDE = LONGITUDE;
    }

    public String getHOUR() {
        return HOUR;
    }

    public void setHOUR(String HOUR) {
        this.HOUR = HOUR;
    }

    public String getTIME() {
        return TIME;
    }

    public void setTIME(String TIME) {
        this.TIME = TIME;
    }

    public String getTEMP() {
        return TEMP;
    }

    public void setTEMP(String TEMP) {
        this.TEMP = TEMP;
    }

    public String getTMX() {
        return TMX;
    }

    public void setTMX(String TMX) {
        this.TMX = TMX;
    }

    public String getTMN() {
        return TMN;
    }

    public void setTMN(String TMN) {
        this.TMN = TMN;
    }

    public String getWKKOR() {
        return WKKOR;
    }

    public void setWKKOR(String WKKOR) {
        this.WKKOR = WKKOR;
    }

    public String getREH() {
        return REH;
    }

    public void setREH(String REH) {
        this.REH = REH;
    }
}
