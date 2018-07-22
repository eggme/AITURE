package com.lsj.aiture;

/**
 * Created by kyyet on 2018-07-23.
 */

public class GraphSetting {
    public static final int DEFAULT_PADDING = 0;
    public static final int DEFAULT_MARGIN_TOP = 0;
    public static final int DEFAULT_MARGIN_RIGHT = 0;
    public static final int DEFAULT_MAX_VALUE = 100;
    public static final int DEFAULT_MIN_VALUE = 50;
    public static final int DEFAULT_INCREMENT = 10;
    private int paddingBottom = 100;
    private int paddingTop = 100;
    private int paddingLeft = 120;
    private int paddingRight = 100;
    private int marginTop = 10;
    private int marginRight = 100;
    private GraphNameBox graphNameBox = null;

    public GraphSetting() {
    }

    public GraphSetting(int paddingBottom, int paddingTop, int paddingLeft, int paddingRight, int marginTop, int marginRight) {
        this.paddingBottom = paddingBottom;
        this.paddingTop = paddingTop;
        this.paddingLeft = paddingLeft;
        this.paddingRight = paddingRight;
        this.marginTop = marginTop;
        this.marginRight = marginRight;
    }

    public int getPaddingBottom() {
        return this.paddingBottom;
    }

    public void setPaddingBottom(int paddingBottom) {
        this.paddingBottom = paddingBottom;
    }

    public int getPaddingTop() {
        return this.paddingTop;
    }

    public void setPaddingTop(int paddingTop) {
        this.paddingTop = paddingTop;
    }

    public int getPaddingLeft() {
        return this.paddingLeft;
    }

    public void setPaddingLeft(int paddingLeft) {
        this.paddingLeft = paddingLeft;
    }

    public int getPaddingRight() {
        return this.paddingRight;
    }

    public void setPaddingRight(int paddingRight) {
        this.paddingRight = paddingRight;
    }

    public int getMarginTop() {
        return this.marginTop;
    }

    public void setMarginTop(int marginTop) {
        this.marginTop = marginTop;
    }

    public int getMarginRight() {
        return this.marginRight;
    }

    public void setMarginRight(int marginRight) {
        this.marginRight = marginRight;
    }

    public GraphNameBox getGraphNameBox() {
        return this.graphNameBox;
    }

    public void setGraphNameBox(GraphNameBox graphNameBox) {
        this.graphNameBox = graphNameBox;
    }
}
