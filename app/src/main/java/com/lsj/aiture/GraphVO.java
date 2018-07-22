package com.lsj.aiture;

import java.util.List;

/**
 * Created by kyyet on 2018-07-23.
 */

public class GraphVO extends GraphSetting{
    private int maxValue = 100;
    private int minValue = 50;
    private int increment = 10;
    private GraphAnimation animation = null;
    private String[] legendArr = null;
    private List<Graph> arrGraph = null;
    private int graphBG = -1;
    private boolean isDrawRegion = false;

    public GraphVO(String[] legendArr, List<Graph> arrGraph) {
        this.setLegendArr(legendArr);
        this.arrGraph = arrGraph;
    }

    public GraphVO(String[] legendArr, List<Graph> arrGraph, int graphBG) {
        this.setLegendArr(legendArr);
        this.arrGraph = arrGraph;
        this.setGraphBG(graphBG);
    }

    public GraphVO(int paddingBottom, int paddingTop, int paddingLeft, int paddingRight, int marginTop, int marginRight, int maxValue, int increment, String[] legendArr, List<Graph> arrGraph) {
        super(paddingBottom, paddingTop, paddingLeft, paddingRight, marginTop, marginRight);
        this.maxValue = maxValue;
        this.increment = increment;
        this.setLegendArr(legendArr);
        this.arrGraph = arrGraph;
    } // ���� ȣ���ϴ� �����ڴ� �̰���

    public GraphVO(int paddingBottom, int paddingTop, int paddingLeft, int paddingRight, int marginTop, int marginRight, int maxValue, int increment, String[] legendArr, List<Graph> arrGraph, int graphBG) {
        super(paddingBottom, paddingTop, paddingLeft, paddingRight, marginTop, marginRight);
        this.maxValue = maxValue;
        this.increment = increment;
        this.setLegendArr(legendArr);
        this.arrGraph = arrGraph;
        this.setGraphBG(graphBG);
    }

    public int getMaxValue() {
        return this.maxValue;
    }

    public int getMinValue() {
        return this.minValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public int getIncrement() {
        return this.increment;
    }

    public void setIncrement(int increment) {
        this.increment = increment;
    }

    public String[] getLegendArr() {
        return this.legendArr;
    }

    public void setLegendArr(String[] legendArr) {
        this.legendArr = legendArr;
    }

    public List<Graph> getArrGraph() {
        return this.arrGraph;
    }

    public void setArrGraph(List<Graph> arrGraph) {
        this.arrGraph = arrGraph;
    }

    public int getGraphBG() {
        return this.graphBG;
    }

    public void setGraphBG(int graphBG) {
        this.graphBG = graphBG;
    }

    public GraphAnimation getAnimation() {
        return this.animation;
    }

    public void setAnimation(GraphAnimation animation) {
        this.animation = animation;
    }

    public boolean isDrawRegion() {
        return this.isDrawRegion;
    }

    public void setDrawRegion(boolean isDrawRegion) {
        this.isDrawRegion = isDrawRegion;
    }

}
