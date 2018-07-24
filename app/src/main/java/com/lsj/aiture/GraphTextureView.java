package com.lsj.aiture;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.SurfaceTexture;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;

/**
 * Created by kyyet on 2018-07-23.
 */

public class GraphTextureView extends TextureView implements TextureView.SurfaceTextureListener {

    public DrawThread drawthread;
    private GraphVO graphVO = null;
    private Context context;
    private static final Object touchLock = new Object();
    private int width;
    private int height;
    private Object mLock = new Object();
    private SurfaceTexture mSurfaceTexture;
    boolean isRun = true;
    int avg = 0;

    public GraphTextureView(Context context, GraphVO vo) {
        super(context);
        this.context = context;
        this.graphVO = vo;
        int sum = 0;
        for(int i=0;i<graphVO.getGraph().getTemp().length;i++){
            sum += graphVO.getGraph().getTemp()[i];
        }
        avg = sum / graphVO.getGraph().getTemp().length;
        init();
    }

    private void init() {
        this.setSurfaceTextureListener(this);
    }


    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        this.width = width;
        this.height = height;
        isRun = true;
        synchronized (mLock) {
            mSurfaceTexture = surface;
            if (mSurfaceTexture != null)
                mLock.notify();
        }
        drawthread = new DrawThread();
        drawthread.start();
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        synchronized (mLock) {
            try {
                isRun = false;
                drawthread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
    }

    class DrawThread extends Thread {
        boolean isDirty = true;
        int xLength;
        int yLength;
        int chartXLength;
        int chartYLength;
        Paint p;
        Paint pCircle;
        Paint miniCircle;
        Paint pLine;
        Paint pBaseLine;
        Paint pBaseLineX;
        Paint pBaseLineY;
        Paint pMarkText;
        float anim;
        boolean isAnimation;
        boolean isDrawRegion;
        long animStartTime;
        Bitmap bg;
        Canvas canvas;
        Context mCtx;

        public DrawThread() {
            this.xLength = width - (GraphTextureView.this.graphVO.getPaddingLeft() + GraphTextureView.this.graphVO.getPaddingRight() + GraphTextureView.this.graphVO.getMarginRight());
            this.yLength = height - (GraphTextureView.this.graphVO.getPaddingBottom() + GraphTextureView.this.graphVO.getPaddingTop() + GraphTextureView.this.graphVO.getMarginTop());
            this.chartXLength = width - (GraphTextureView.this.graphVO.getPaddingLeft() + GraphTextureView.this.graphVO.getPaddingRight());
            this.chartYLength = height - (GraphTextureView.this.graphVO.getPaddingBottom() + GraphTextureView.this.graphVO.getPaddingTop());
            this.p = new Paint();
            this.pCircle = new Paint();
            this.miniCircle = new Paint();
            this.pLine = new Paint();
            this.pBaseLine = new Paint();
            this.pBaseLineX = new Paint();
            this.pBaseLineY = new Paint();
            this.pMarkText = new Paint();
            this.anim = 0.0F;
            this.isAnimation = false;
            this.isDrawRegion = false;
            this.animStartTime = -1L;
            this.bg = null;
            this.mCtx = context;
            int size = GraphTextureView.this.graphVO.getGraph().getTemp().length;
        }

        @Override
        public void run() {
            // Latch the SurfaceTexture when it becomes available.  We have to wait for
            // the TextureView to create it.
            GraphCanvasWrapper graphCanvasWrapper = null;
            this.setPaint();
            this.isAnimation();
            this.isDrawRegion();
            this.animStartTime = System.currentTimeMillis();
            while (isRun) { // isRun Default true
                if (!this.isDirty) { // isDirty Default true
                    try {
                        Thread.sleep(100L);
                    } catch (InterruptedException var13) {
                        var13.printStackTrace();
                    }
                } else {
                    Surface surface = null;
                    SurfaceTexture surfaceTexture = mSurfaceTexture;
                    if (surfaceTexture == null) {
                        Log.d("Graph", "ST null on entry");
                        return;
                    }
                    surface = new Surface(surfaceTexture);
                    Rect dirty = new Rect(0, 0, width, height);
                    canvas = surface.lockCanvas(dirty);
                    graphCanvasWrapper = new GraphCanvasWrapper(canvas, width, height, GraphTextureView.this.graphVO.getPaddingLeft(), GraphTextureView.this.graphVO.getPaddingBottom());
                    synchronized (GraphTextureView.touchLock) {
                        try {
                            canvas.drawColor(getResources().getColor(R.color.graphBG));
                            if (this.bg != null) {
                              //  canvas.drawColor(getResources().getColor(R.color.graphBG));
                               //canvas.drawColor(Color.argb(9,255,255,255));
                            }
                            //this.drawBaseLine(graphCanvasWrapper);
                            //graphCanvasWrapper.drawLine(0.0F, 0.0F, 0.0F, (float) this.chartYLength, this.pBaseLine);
                            //graphCanvasWrapper.drawLine(0.0F, 0.0F, (float) this.chartXLength, 0.0F, this.pBaseLine);
                            this.drawXText(graphCanvasWrapper);
                            this.drawTempText(graphCanvasWrapper);
                            //this.drawGraphRegion(graphCanvasWrapper);
                            this.drawGraph(graphCanvasWrapper);
                        } catch (Exception var15) {
                            var15.printStackTrace();
                        } finally {
                            if (surface != null) {
                                surface.unlockCanvasAndPost(canvas);
                            }
                        }
                    }
                    try {
                        Thread.sleep(0L);
                    } catch (InterruptedException var14) {
                        var14.printStackTrace();
                    }

                    this.calcTimePass();
                    surface.release();
                }
            }
        }

        private void setPaint() {
            this.p = new Paint();
            this.p.setFlags(1);
            this.p.setAntiAlias(true);
            this.p.setFilterBitmap(true);
            this.p.setColor(0x30FFFFFF);
            this.p.setStrokeWidth(5.0F);
            this.p.setStyle(Paint.Style.STROKE);
            this.pCircle = new Paint();
            this.pCircle.setFlags(1);
            this.pCircle.setAntiAlias(true);
            this.pCircle.setFilterBitmap(true);
            this.pCircle.setColor(Color.WHITE); //
            this.pCircle.setStrokeWidth(3.0F);
            this.pCircle.setStyle(Paint.Style.STROKE);
            this.miniCircle = new Paint();
            this.miniCircle.setFlags(2);
            this.miniCircle.setAntiAlias(true);
            this.miniCircle.setFilterBitmap(true);
            this.miniCircle.setColor(Color.WHITE);
            this.miniCircle.setStrokeWidth(6.0F);
            this.miniCircle.setStyle(Paint.Style.FILL);
            this.pLine = new Paint();
            this.pLine.setFlags(1);
            this.pLine.setAntiAlias(true);
            this.pLine.setFilterBitmap(true);
            this.pLine.setShader(new LinearGradient(0.0F, 300.0F, 0.0F, 0.0F, -16777216, -1, Shader.TileMode.MIRROR));
            this.pBaseLine = new Paint();
            this.pBaseLine.setFlags(1);
            this.pBaseLine.setAntiAlias(true);
            this.pBaseLine.setFilterBitmap(true);
            this.pBaseLine.setColor(getResources().getColor(R.color.graphLineColor));
            this.pBaseLine.setStrokeWidth(3.0F);
            this.pBaseLineX = new Paint();
            this.pBaseLineX.setFlags(1);
            this.pBaseLineX.setAntiAlias(true);
            this.pBaseLineX.setFilterBitmap(true);
            this.pBaseLineX.setColor(getResources().getColor(R.color.graphLineColor));
            this.pBaseLineX.setStrokeWidth(2.0F);
            this.pBaseLineX.setStyle(Paint.Style.STROKE);
            // this.pBaseLineX.setPathEffect(new DashPathEffect(new float[]{10.0F, 5.0F}, 0.0F));
            this.pBaseLineY = new Paint();
            this.pBaseLineY.setFlags(1);
            this.pBaseLineY.setAntiAlias(true);
            this.pBaseLineY.setFilterBitmap(true);
            this.pBaseLineY.setColor(getResources().getColor(R.color.graphLineColor));
            this.pBaseLineY.setStrokeWidth(2.0F);
            this.pBaseLineY.setStyle(Paint.Style.STROKE);
            // this.pBaseLineY.setPathEffect(new DashPathEffect(new float[]{10.0F, 5.0F}, 0.0F));
            this.pMarkText = new Paint();
            this.pMarkText.setFlags(1);
            this.pMarkText.setAntiAlias(true);
            this.pMarkText.setColor(Color.WHITE);
            this.pMarkText.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
        }


        private void calcTimePass() {
            if (this.isAnimation) {
                long curTime = System.currentTimeMillis();
                long gapTime = curTime - this.animStartTime;
                long animDuration = (long) GraphTextureView.this.graphVO.getAnimation().getDuration();
                if (gapTime >= animDuration) {
                    gapTime = animDuration;
                    this.isDirty = false;
                }
                this.anim = (float) (GraphTextureView.this.graphVO.getGraph().getTime().length * (float) gapTime / (float) animDuration);
            } else {
                this.isDirty = false;
            }

        }

        private void isAnimation() {
            if (GraphTextureView.this.graphVO.getAnimation() != null) { // vo.setAnimation(new GraphAnimation(GraphAnimation.LINEAR_ANIMATION, GraphAnimation.DEFAULT_DURATION));
                this.isAnimation = true;
            } else {
                this.isAnimation = false;
            }

        }

        private void isDrawRegion() {
            if (GraphTextureView.this.graphVO.isDrawRegion()) { // vo.setDrawRegion(true);
                this.isDrawRegion = true;
            } else {
                this.isDrawRegion = false;
            }

        }


        private void drawGraphRegion(GraphCanvasWrapper graphCanvas) {
            if (this.isDrawRegion) {
                if (this.isAnimation) {
                    this.drawGraphRegionWithAnimation(graphCanvas);
                } else {
                    this.drawGraphRegionWithoutAnimation(graphCanvas);
                }
            }

        }

        private void drawGraph(GraphCanvasWrapper graphCanvas) {
            if (this.isAnimation) {
                this.drawGraphWithAnimation(graphCanvas);
            } else {
                this.drawGraphWithoutAnimation(graphCanvas);
            }

        }

        private void drawGraphRegionWithoutAnimation(GraphCanvasWrapper graphCanvas) throws NullPointerException {
            boolean isDrawRegion = GraphTextureView.this.graphVO.isDrawRegion();
            for (int i = 0; i < GraphTextureView.this.graphVO.getGraph().getTemp().length; ++i) {
                GraphPath regionPath = new GraphPath(width, height, GraphTextureView.this.graphVO.getPaddingLeft(), GraphTextureView.this.graphVO.getPaddingBottom());
                boolean firstSet = false;
                float x = 0.0F;
                float y = 0.0F;
                this.p.setColor(GraphTextureView.this.graphVO.getGraph().getColor());  // 그래프선의 색을 지정해줌
                this.pCircle.setColor(GraphTextureView.this.graphVO.getGraph().getColor());    // 동그라미 포인트 색깔 지정하는거
                float xGap = (float) (this.xLength / 7);

                for (int pBg = 0; pBg < 6; ++pBg) {
                    if (pBg < GraphTextureView.this.graphVO.getGraph().getTemp().length) {
                        if (!firstSet) {// 처음 선을 그림
                            x = xGap * (float) pBg;
                            //y = (float) this.yLength * ((Graph) GraphTextureView.this.graphVO.getArrGraph().get(i)).getCoordinateArr()[pBg] / (float) GraphTextureView.this.graphVO.getMaxValue();
                            y = (float) (this.yLength / 2) + (( GraphTextureView.this.graphVO.getGraph().getTemp()[i] - avg ) * GraphTextureView.this.graphVO.INCREMENT);
                            /*
                                기준 = (화면 높이 / 2)  + ((데이터값 - 데이터값 배열의 평균값) * 증가량)
                             */
                            regionPath.moveTo(x, 0.0F);
                            regionPath.lineTo(x, y);
                            firstSet = true;
                        } else { // 그다음은 다음 포인트를 찾아서 애니메이션으로 그림
                            x = xGap * (float) pBg;
                            //y = (float) this.yLength * ((Graph) GraphTextureView.this.graphVO.getArrGraph().get(i)).getCoordinateArr()[pBg] / (float) GraphTextureView.this.graphVO.getMaxValue();
                            y = (float) (this.yLength / 2) + (( GraphTextureView.this.graphVO.getGraph().getTemp()[i] - avg ) * GraphTextureView.this.graphVO.INCREMENT);
                            regionPath.lineTo(x, y);
                            // http://baramziny.tistory.com/75 넘어려워시발머야
                        }
                    }
                }

                if (isDrawRegion) {
                    regionPath.lineTo(x, 0.0F);
                    regionPath.lineTo(0.0F, 0.0F);
                    Paint var10 = new Paint();
                    var10.setFlags(1);
                    var10.setAntiAlias(true);
                    var10.setFilterBitmap(true);
                    var10.setStyle(Paint.Style.FILL);
                    var10.setColor(GraphTextureView.this.graphVO.getGraph().getColor());
                    graphCanvas.getCanvas().drawPath(regionPath, var10);
                }
            }

        }

        /*
                애니메이션을 넣어 그림을 그리는 메소드
         */

        private void drawGraphRegionWithAnimation(GraphCanvasWrapper graphCanvas) {
            Log.i("Graph", " drawGraphRegionWithAnimation");
            float prev_x = 0.0F; // 처음 좌표
            float prev_y = 0.0F;
            float next_x = 0.0F; // 다음 좌표
            float next_y = 0.0F;
            boolean value = false;
            float mode = 0.0F;
            boolean isDrawRegion = GraphTextureView.this.graphVO.isDrawRegion();

            for (int i = 0; i < GraphTextureView.this.graphVO.getGraph().getTime().length; ++i) {
                GraphPath regionPath = new GraphPath(width, height, GraphTextureView.this.graphVO.getPaddingLeft(), GraphTextureView.this.graphVO.getPaddingBottom());
                boolean firstSet = false; // 처음선은 제자리에서 동그라미가 찍히기 떄문에 걸러줘야함
                float x = 0.0F;
                float y = 0.0F;
                this.p.setColor(GraphTextureView.this.graphVO.getGraph().getColor()); // 선 색깔
                this.pCircle.setColor(GraphTextureView.this.graphVO.getGraph().getColor()); // 동그라미 포인트 색깔
                float xGap = (float) (this.xLength / 7);//(float)(this.xLength / 5);//(float)(this.xLength / (((MyLineGraph)MyLineGraphView.this.graphVO.getArrGraph().get(i)).getCoordinateArr().length - 1)); // 그려야할 공간 width
                int var18 = (int) (this.anim / 1.0F);
                mode = this.anim % 1.0F;
                boolean isFinish = false;

                for (int x_bg = 0; x_bg <= var18 + 1; ++x_bg) {
                    if (x_bg < GraphTextureView.this.graphVO.getGraph().getTime().length) {
                        if (!firstSet) {
                            x = xGap * (float) x_bg;
                            y = (float) (this.yLength / 2) + (( GraphTextureView.this.graphVO.getGraph().getTemp()[x_bg] - avg ) * GraphTextureView.this.graphVO.INCREMENT) + 30;
                            regionPath.moveTo(x, 0.0F);
                            regionPath.lineTo(x, y);
                            firstSet = true;
                        } else {
                            x = xGap * (float) x_bg;
                            y = (float) (this.yLength / 2) + (( GraphTextureView.this.graphVO.getGraph().getTemp()[x_bg] - avg ) * GraphTextureView.this.graphVO.INCREMENT) + 30;
                            if (x_bg > var18) {
                                next_x = x - prev_x;
                                next_y = y - prev_y;
                                regionPath.lineTo(prev_x + next_x * mode, prev_y + next_y * mode);
                            } else {
                                regionPath.lineTo(x, y);
                            }
                        }
                        prev_x = x;
                        prev_y = y;
                    }
                }

                isFinish = true;
                if (isDrawRegion) {
                    float var19 = prev_x + next_x * mode;
                    if (var19 >= (float) this.xLength) {
                        var19 = (float) this.xLength;
                    }

                    regionPath.lineTo(var19, 0.0F);
                    regionPath.lineTo(0.0F, 0.0F);
                    Paint pBg = new Paint();
                    pBg.setFlags(1);
                    pBg.setAntiAlias(true);
                    pBg.setFilterBitmap(true);
                    pBg.setStyle(Paint.Style.FILL);
                    pBg.setColor(GraphTextureView.this.graphVO.getGraph().getColor());
                    graphCanvas.getCanvas().drawPath(regionPath, pBg);
                }
            }

        }

        private void drawGraphWithoutAnimation(GraphCanvasWrapper graphCanvas) {
            Log.i("Graph", " drawGraphWithoutAnimation");
            for (int i = 0; i < GraphTextureView.this.graphVO.getGraph().getTime().length; ++i) {
                GraphPath linePath = new GraphPath(width, height, GraphTextureView.this.graphVO.getPaddingLeft(), GraphTextureView.this.graphVO.getPaddingBottom());
                new GraphPath(width, height, GraphTextureView.this.graphVO.getPaddingLeft(), GraphTextureView.this.graphVO.getPaddingBottom());
                boolean firstSet = false;
                float x = 0.0F;
                float y = 0.0F;
                this.p.setColor(GraphTextureView.this.graphVO.getGraph().getColor());
                this.pCircle.setColor(GraphTextureView.this.graphVO.getGraph().getColor());
                float xGap = (float) (this.xLength / 6);
                for (int j = 0; j < 6; ++j) {
                    if (j < GraphTextureView.this.graphVO.getGraph().getTemp().length) {
                        if (!firstSet) {
                            x = xGap * (float) j;
                            y = (float) (this.yLength / 2) + (( GraphTextureView.this.graphVO.getGraph().getTemp()[j] - avg ) * GraphTextureView.this.graphVO.INCREMENT) + 30;
                            linePath.moveTo(x, y);
                            firstSet = true;
                        } else {
                            x = xGap * (float) j;
                            y = (float) (this.yLength / 2) + (( GraphTextureView.this.graphVO.getGraph().getTemp()[j] - avg ) * GraphTextureView.this.graphVO.INCREMENT) + 30;
                            linePath.lineTo(x, y);
                        }
                        this.pCircle.setColor(GraphTextureView.this.graphVO.getGraph().getColor());
                        this.miniCircle.setColor(GraphTextureView.this.graphVO.getGraph().getColor());
                        graphCanvas.drawCircle(x, y, 15.0F, this.pCircle); // miniCircle
                        graphCanvas.drawCircle(x, y, 10.0F, this.miniCircle);
                    }
                }

                graphCanvas.getCanvas().drawPath(linePath, this.p);
            }

        }
        /*
            실제로 실행되는 메소드
         */
        private void drawGraphWithAnimation(GraphCanvasWrapper graphCanvas) {

            float prev_x = 0.0F;
            float prev_y = 0.0F;
            float next_x = 0.0F;
            float next_y = 0.0F;
            float value = 0.0F;
            float mode = 0.0F;
            for (int i = 0; i < GraphTextureView.this.graphVO.getGraph().getTime().length; ++i) {
                GraphPath linePath = new GraphPath(width, height, GraphTextureView.this.graphVO.getPaddingLeft(), GraphTextureView.this.graphVO.getPaddingBottom());
                new GraphPath(width, height, GraphTextureView.this.graphVO.getPaddingLeft(), GraphTextureView.this.graphVO.getPaddingBottom());
                boolean firstSet = false;
                float x = 0.0F;
                float y = 0.0F;
                this.p.setColor(GraphTextureView.this.graphVO.getGraph().getColor());
                this.pCircle.setColor(GraphTextureView.this.graphVO.getGraph().getColor());
                float xGap = (float) (this.xLength / 6);
                value = this.anim / 1.0F;
                mode = this.anim % 1.0F;

                for (int j = 0; (float) j < value + 1.0F; ++j) {
                    if (j < GraphTextureView.this.graphVO.getGraph().getTemp().length) {
                        if (!firstSet) {
                            x = xGap * (float) j;
                            y = (float) (this.yLength / 2) + (( GraphTextureView.this.graphVO.getGraph().getTemp()[j] - avg ) * GraphTextureView.this.graphVO.INCREMENT) + 30;
                            Log.i("Graph", "y길이 = "+this.yLength+ " , y/2 = "+ (this.yLength / 2) + " 평균값 =  "+ avg +" 온도 = "+GraphTextureView.this.graphVO.getGraph().getTemp()[j] +"증가량 = "+GraphTextureView.this.graphVO.INCREMENT + " y값" +y);
                            linePath.moveTo(x, y);
                            firstSet = true;
                        } else {
                            x = xGap * (float) j;
                            y = (float) (this.yLength / 2) + (( GraphTextureView.this.graphVO.getGraph().getTemp()[j] - avg ) * GraphTextureView.this.graphVO.INCREMENT) + 30;
                            if ((float) j > value && mode != 0.0F) {
                                next_x = x - prev_x;
                                next_y = y - prev_y;
                                linePath.lineTo(prev_x + next_x * mode, prev_y + next_y * mode);
                            } else {
                                linePath.lineTo(x, y);
                            }
                        }

                        this.pCircle.setColor(GraphTextureView.this.graphVO.getGraph().getColor());
                        this.miniCircle.setColor(GraphTextureView.this.graphVO.getGraph().getColor());
                        graphCanvas.drawCircle(x, y, 15.0F, this.pCircle);
                        graphCanvas.drawCircle(x, y, 10.0F, this.miniCircle);
                        prev_x = x;
                        prev_y = y;
                    }
                }

                graphCanvas.getCanvas().drawPath(linePath, this.p);
            }

        }
/*

        private void drawXMark(GraphCanvasWrapper graphCanvas) {
            float x = 0.0F;
            float xGap = (float) (this.xLength / 5);//(float)(this.xLength / (((MyLineGraph)MyLineGraphView.this.graphVO.getArrGraph().get(0)).getCoordinateArr().length-1)); // ���׸��� 5���ϱ� View / 4
            float yGap = (float) (this.yLength);
            if (GraphTextureView.this.graphVO.getArrGraph().size() > 0) {
                for (int i = 0; i <= ((Graph) GraphTextureView.this.graphVO.getArrGraph().get(0)).getCoordinateArr().length; i++) { //((MyLineGraph)MyLineGraphView.this.graphVO.getArrGraph().get(0)).getCoordinateArr().length;
                    x = xGap * (float) i;
                    graphCanvas.drawLine(x, yGap, x, yGap + 30.0F, this.pBaseLine);
                }
            }
        }

        private void drawYMark(GraphCanvasWrapper canvas) {
            for (int i = 0; (GraphTextureView.this.graphVO.getIncrement() * i) <= GraphTextureView.this.graphVO.getMaxValue(); ++i) {
                float y = (float) (this.yLength * (GraphTextureView.this.graphVO.getIncrement() * i) / (GraphTextureView.this.graphVO.getMaxValue() - GraphTextureView.this.graphVO.getMinValue()));
                canvas.drawLine(0.0F, y, -40.0F, y, this.pBaseLine);
            }

        }

        */
        /*
                그래프 하단 텍스트
         */
        private void drawXText(GraphCanvasWrapper graphCanvas) {
            float x = 0.0F;
            float y = 0.0F;
            float xGap = (float) (this.xLength / 6);
            float yGap = 30f;
            for (int i = 0; i < GraphTextureView.this.graphVO.getGraph().getTime().length; i++) {
                x = xGap * (float) i;
                String text = (GraphTextureView.this.graphVO.getGraph().getTime()[i])+"시";
                this.pMarkText.measureText(text);
                this.pMarkText.setTextSize(30.0F);
                Rect rect = new Rect();
                this.pMarkText.getTextBounds(text, 0, text.length(), rect);
                graphCanvas.drawText(text, x - (float) (rect.width() / 2), yGap, this.pMarkText);

            }

        }

        private void drawTempText(GraphCanvasWrapper graphCanvas) {
            float x = 0.0F;
            float y = 0.0F;
            float xGap = (float) (this.xLength / 6);
            float yGap = 30f;
            for (int i = 0; i < GraphTextureView.this.graphVO.getGraph().getTime().length; i++) {
                x = xGap * (float) i;
                y = (float) (this.yLength / 2) + (( GraphTextureView.this.graphVO.getGraph().getTemp()[i] - avg ) * GraphTextureView.this.graphVO.INCREMENT) + 60;
                String text = (GraphTextureView.this.graphVO.getGraph().getTemp()[i])+"º";
                this.pMarkText.measureText(text);
                this.pMarkText.setTextSize(25.0F);
                Rect rect = new Rect();
                this.pMarkText.getTextBounds(text, 0, text.length(), rect);
                graphCanvas.drawText(text, x - (float) (rect.width() / 2), y, this.pMarkText);

            }

        }
    }
}
