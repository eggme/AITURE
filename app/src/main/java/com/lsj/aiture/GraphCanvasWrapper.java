package com.lsj.aiture;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * Created by kyyet on 2018-07-23.
 */

public class GraphCanvasWrapper {
    private MatrixTranslator mMt;
    private Canvas mCanvas;

    public GraphCanvasWrapper(Canvas canvas, int width, int height, int paddingLeft, int paddingBottom) {
        this.mMt = new MatrixTranslator(width, height, paddingLeft, paddingBottom);
        this.mCanvas = canvas;
    }

    public Canvas getCanvas() {
        return this.mCanvas;
    }

    public void drawCircle(float cx, float cy, float radius, Paint paint) {
        this.mCanvas.drawCircle(this.mMt.calcX(cx), this.mMt.calcY(cy), radius, paint);
    }

    public void drawArc(RectF oval, float startAngle, float sweepAngle, boolean useCenter, Paint paint) {
        if(this.mCanvas != null) {
            this.mCanvas.drawArc(oval, startAngle, sweepAngle, true, paint);
        }

    }

    public void drawLine(float startX, float startY, float stopX, float stopY, Paint paint) {
        this.mCanvas.drawLine(this.mMt.calcX(startX), this.mMt.calcY(startY), this.mMt.calcX(stopX), this.mMt.calcY(stopY), paint);
    }

    public void drawText(String text, float x, float y, Paint paint) {
        this.mCanvas.drawText(text, this.mMt.calcX(x), this.mMt.calcY(y), paint);
    }

    public void drawBitmapIcon(Bitmap bitmap, float left, float top, Paint paint) {
        this.mCanvas.drawBitmap(bitmap, this.mMt.calcBitmapCenterX(bitmap, left), this.mMt.calcBitmapCenterY(bitmap, top), paint);
    }
}
