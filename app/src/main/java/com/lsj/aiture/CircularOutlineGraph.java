package com.lsj.aiture;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.view.View;

public class CircularOutlineGraph extends View {

    private final float ZERO = -90f;
    private float max;
    private float value;
    private String text;
    private String title;
    private final String FONTPATH = "NanumSquareL.otf";
    private Typeface mTypeface;
    private Context context;

    public CircularOutlineGraph(Context context , float max, float value, String text, String title) {
        super(context);
        this.context = context;
        this.max = max;
        this.value = value;
        this.text = text;
        this.title = title;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // if MAX = 100, value = 85
        float degree = value * ( 360 / max );

        mTypeface = Typeface.createFromAsset(context.getAssets() , FONTPATH);

        Paint p1 = new Paint(); // 배경 회색 페인트
        p1.setAntiAlias(true);
        p1.setStyle(Paint.Style.STROKE);
        p1.setStrokeWidth(5);
        p1.setAlpha(0x00);
        p1.setColor(0xff636668);

        Paint p2 = new Paint(); // 실제 데이터값 주황 페인트
        p2.setAntiAlias(true);
        p2.setStyle(Paint.Style.STROKE);
        p2.setStrokeWidth(5);
        p2.setAlpha(0x00);
        p2.setColor(0xfff3b255);

        RectF rectF1 = new RectF(5, 5, 200, 200); // 배경회색원

        canvas.drawArc(rectF1, ZERO, 360f, false, p1);

        RectF rectF2 = new RectF(5, 5, 200, 200); // 데이터주황원

        canvas.drawArc(rectF2, ZERO, degree, false, p2);

        drawCircular(canvas);

    }

    protected void drawCircular(Canvas canvas){
        setValue(canvas);

        Paint bText = new Paint();
        bText.measureText(title);
        bText.setColor(0xffffffff);
        bText.setTextSize(30.0F);
        Rect brect = new Rect();
        bText.getTextBounds(title,0, title.length(), brect);
        int bvx = 40;
        if(title.length() == 2){
            bvx = 70;
        }
        if(title.equals("강수확률")){
            bvx = 50;
        }
        canvas.drawText(title, bvx, 270, bText);
    }

    public void setValue(Canvas canvas) {
        switch (title){
            case "미세먼지" :
                Paint cText = new Paint();
                cText.measureText(((int)value)+"");
                cText.setColor(0xffffffff);
                cText.setTextSize(70.0F);
                cText.setTypeface(mTypeface);
                Rect crect = new Rect();
                String cTextV = ((int)value)+"";
                int cvx = 55;
                int cvy = 115;
                cText.getTextBounds(cTextV, 0, cTextV.length(), crect);
                if(cTextV.length() == 1){
                    cvx = 75;
                }
                canvas.drawText(cTextV, cvx, cvy, cText);
                // Additional
                Paint cTextA = new Paint();
                String cTextAV = "㎍/㎥";
                cTextA.measureText(cTextAV);
                cTextA.setColor(0xffffffff);
                cTextA.setTextSize(30.0F);
                Rect cArect = new Rect();
                int cAvx = 65;
                int cAvy = 155;
                cTextA.getTextBounds(cTextAV, 0, cTextAV.length(), cArect);
                canvas.drawText(cTextAV, cAvx, cAvy, cTextA);
                break;
            case "습도" :
            case "강수확률" :
                String pTextV = ((int)value)+"";
                Paint pText = new Paint();
                pText.measureText(((int)value)+"");
                pText.setColor(0xffffffff);
                pText.setTextSize(70.0F);
                pText.setTypeface(mTypeface);
                Rect prect = new Rect();
                int pvx = 40;
                int pvy = 125;
                pText.getTextBounds(pTextV, 0, pTextV.length(), prect);
                if(pTextV.length() == 1){
                    pvx = 75;
                }
                canvas.drawText(pTextV, pvx, pvy, pText);
                // Additional
                Paint pTextA = new Paint();
                String pTextAV = "%";
                pTextA.measureText(pTextAV);
                pTextA.setColor(0xffffffff);
                pTextA.setTextSize(50.0F);
                Rect pArect = new Rect();
                int pAvx = 125;
                int pAvy = 125;
                pTextA.getTextBounds(pTextAV, 0, pTextAV.length(), pArect);
                canvas.drawText(pTextAV, pAvx, pAvy, pTextA);

                break;
        }
    }
}
