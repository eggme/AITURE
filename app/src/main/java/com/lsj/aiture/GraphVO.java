package com.lsj.aiture;

import java.util.List;

/**
 * Created by kyyet on 2018-07-23.
 */

public class GraphVO extends GraphSetting{

    private GraphAnimation animation = null;
    private Graph graph = null;
    private int graphBG = -1;
    public final int INCREMENT = 10;
    private boolean isDrawRegion = false;

    public GraphVO(int paddingBottom, int paddingTop, int paddingLeft, int paddingRight, int marginTop, int marginRight, Graph graph) {
        super(paddingBottom, paddingTop, paddingLeft, paddingRight, marginTop, marginRight);
        this.graph = graph;
    }

    public Graph getGraph() {
        return this.graph;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
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
