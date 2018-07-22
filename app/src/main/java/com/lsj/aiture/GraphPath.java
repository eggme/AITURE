package com.lsj.aiture;

import android.graphics.Path;
import android.graphics.PointF;

/**
 * Created by kyyet on 2018-07-23.
 */

public class GraphPath extends Path {
    private MatrixTranslator mMt;

    public GraphPath(int width, int height, int paddingLeft, int paddingBottom) {
        this.mMt = new MatrixTranslator(width, height, paddingLeft, paddingBottom);
    }

    public void moveTo(float x, float y) {
        super.moveTo(this.mMt.calcX(x), this.mMt.calcY(y));
    }

    public void moveTo(PointF point) {
        super.moveTo(this.mMt.calcX(point.x), this.mMt.calcY(point.y));
    }

    public void lineTo(float x, float y) {
        super.lineTo(this.mMt.calcX(x), this.mMt.calcY(y));
    }

    public void lineTo(PointF point) {
        super.lineTo(this.mMt.calcX(point.x), this.mMt.calcY(point.y));
    }
}
