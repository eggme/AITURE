package com.lsj.aiture;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kyyet on 2018-07-23.
 */

public class GraphManager {

    private ArrayList<WeatherDTO> list = null;

    public GraphManager(ArrayList<WeatherDTO> list){
        this.list = list;
    }


    private GraphData graphData;

    public GraphVO getGraph(Context context){
        graphData = new GraphData(list);
        GraphVO vo;
        int paddingBottom = GraphVO.DEFAULT_PADDING + 30;
        int paddingTop = GraphVO.DEFAULT_PADDING + 110;
        int paddingLeft = GraphVO.DEFAULT_PADDING + 130;
        int paddingRight = GraphVO.DEFAULT_PADDING + 30;
        int marginTop = GraphVO.DEFAULT_MARGIN_TOP;
        int marginRight = GraphVO.DEFAULT_MARGIN_RIGHT;
        Graph graph = new Graph(graphData.getTemp(), graphData.getTime());

        vo = new GraphVO(
                paddingBottom, paddingTop, paddingLeft, paddingRight,
                marginTop, marginRight, graph);
        vo.setAnimation(new GraphAnimation(GraphAnimation.LINEAR_ANIMATION, GraphAnimation.DEFAULT_DURATION));
        return vo;
    }
}
