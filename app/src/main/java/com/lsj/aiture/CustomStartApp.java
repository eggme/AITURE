package com.lsj.aiture;

import android.app.Application;

import com.tsengvn.typekit.Typekit;

public class CustomStartApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Typekit.getInstance()
                .addNormal(Typekit.createFromAsset(this, "NanumSquareR.otf"))
                .addBold(Typekit.createFromAsset(this, "NanumSquareB.otf"))
                .addCustom1(Typekit.createFromAsset(this, "NanumSquareL.otf"));
    }
}