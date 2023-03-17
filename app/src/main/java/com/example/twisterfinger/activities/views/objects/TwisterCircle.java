package com.example.twisterfinger.activities.views.objects;

import android.graphics.Canvas;
import android.graphics.Paint;

public class TwisterCircle {

    public static final int MARGIN = 50;

    private int cx, cy;
    private final Paint paint;
    private int radius;

    public TwisterCircle(int color) {
        this.cx = 0;
        this.cy = 0;

        paint = new Paint();
        paint.setColor(color);
    }

    public void draw(Canvas canvas) {
        canvas.drawCircle(cx, cy, radius, paint);
    }

    public static boolean isInsideTheCircle(TwisterCircle circle, float px, float py) {
        return Math.sqrt(Math.abs(Math.pow(px - circle.cx, 2)) + Math.abs(Math.pow(py - circle.cy, 2))) <= circle.radius;
    }

    public void setColor(int color) {
        paint.setColor(color);
    }

    public void setCx(int cx) {
        this.cx = cx;
    }

    public void setCy(int cy) {
        this.cy = cy;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int getRadius() {
        return radius;
    }
}
