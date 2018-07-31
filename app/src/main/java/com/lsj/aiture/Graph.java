package com.lsj.aiture;

/**
 * Created by Jun on 2015-05-12.
 */
public class Graph {
    private String name = null;
    private int color = 0xfff3b255;
    private int[] temp = null;
    private int[] time = null;
    private int bitmapResource = -1;

    public Graph(int[] temp, int[] time) {
        this.temp = temp;
        this.time = time;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getColor() {
        return this.color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int[] getTemp() {
        return temp;
    }

    public void setTemp(int[] temp) {
        this.temp = temp;
    }

    public int[] getTime() {
        return time;
    }

    public void setTime(int[] time) {
        this.time = time;
    }

    public int getBitmapResource() {
        return this.bitmapResource;
    }

    public void setBitmapResource(int bitmapResource) {
        this.bitmapResource = bitmapResource;
    }
}
