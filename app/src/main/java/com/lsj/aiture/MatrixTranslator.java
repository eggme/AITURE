package com.lsj.aiture;

import android.graphics.Bitmap;

/**
 * Created by kyyet on 2018-07-23.
 */

public class MatrixTranslator {
    private int mWidth;
    private int mHeight;
    private int mPaddingLeft;
    private int mPaddingBottom;

    public MatrixTranslator(int width, int height, int paddingLeft, int paddingBottom) {
        this.mWidth = width;
        this.mHeight = height;
        this.mPaddingLeft = paddingLeft;
        this.mPaddingBottom = paddingBottom;
    }

    public float calcX(float x) {
        return x + (float)this.mPaddingLeft;
    }

    public float calcY(float y) {
        return (float)this.mHeight - (y + (float)this.mPaddingBottom);
    }

    public float calcBitmapCenterX(Bitmap bitmap, float x) {
        return x + (float)this.mPaddingLeft - (float)(bitmap.getWidth() / 2);
    }

    public float calcBitmapCenterY(Bitmap bitmap, float y) {
        return (float)this.mHeight - (y + (float)this.mPaddingBottom) - (float)(bitmap.getHeight() / 2);
    }
}