package com.lsj.aiture;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

public class CircularOutlineGraph extends View {

    public CircularOutlineGraph(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        final float ZERO = -90f;
        final float DOTONE = 72f;
        float score = 3.8f; //점수. 하드코딩으로 넣었지만 나중에 변경 예정

        float degree = score * DOTONE;

        Paint p = new Paint();
        p.setAntiAlias(true);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(5);
        p.setAlpha(0x00);
        p.setColor(0xfff3b255);

        RectF rectF = new RectF(0, 0, 250, 250);

        canvas.drawArc(rectF, ZERO, degree, false, p);
    }
}
