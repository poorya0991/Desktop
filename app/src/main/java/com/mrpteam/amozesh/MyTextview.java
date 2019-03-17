package com.mrpteam.amozesh;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class MyTextview extends android.support.v7.widget.AppCompatTextView {


    public MyTextview(Context context) {
        super(context);
        Typeface face=Typeface.createFromAsset(context.getAssets(), "font/sans.ttf");
        this.setTypeface(face);
    }

    public MyTextview(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface face=Typeface.createFromAsset(context.getAssets(), "font/sans.ttf");
        this.setTypeface(face);
    }

    public MyTextview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Typeface face=Typeface.createFromAsset(context.getAssets(), "font/sans.ttf");
        this.setTypeface(face);
    }

    protected void onDraw (Canvas canvas) {
        super.onDraw(canvas);


    }

}