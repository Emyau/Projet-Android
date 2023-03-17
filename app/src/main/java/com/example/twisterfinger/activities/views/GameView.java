package com.example.twisterfinger.activities.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.twisterfinger.activities.views.objects.TwisterCircle;

import java.util.ArrayList;
import java.util.List;

public class GameView extends View {

    private static final int NB_ROW = 7;
    private static final int NB_COLLUMN = 4;
    private static final long UPDATE_TIME = 1000 / 60;

    // 0 if no finger
    // then 1 to 10 starting with left hand to right
    private final List<TwisterCircle> twisterCircleList;

    private final Runnable onDrawRunnable;
    private final Handler handler;

    public GameView(Context context) {
        super(context);
        twisterCircleList = new ArrayList<>();
        int nbElement = NB_COLLUMN * NB_ROW;
        for (int i = 0; i < nbElement; i++) {
            int col = i % NB_COLLUMN;
            if (col == 0) {
                twisterCircleList.add(new TwisterCircle(Color.RED));
            } else if (col == 1) {
                twisterCircleList.add(new TwisterCircle(Color.YELLOW));
            } else if (col == 2) {
                twisterCircleList.add(new TwisterCircle(Color.GREEN));
            } else {
                twisterCircleList.add(new TwisterCircle(Color.rgb(138, 43, 226)));
            }
        }

        onDrawRunnable = this::invalidate;
        handler = new Handler();

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        int circleRadius = (int) (getWidth() * 0.07);

        for (int i = 0; i < NB_ROW; i++) {
            for (int j = 0; j < NB_COLLUMN; j++) {
                // 0.8 is the game size, we keep 20% for the margin
                int cx = (int) (j * ((getWidth() * 0.8) / (NB_COLLUMN - 1)) + getWidth() * 0.1);
                int cy = (int) (i * ((getHeight() * 0.8) / (NB_ROW - 1)) + getHeight() * 0.1);

                int index = i * NB_COLLUMN + j;

                twisterCircleList.get(index).setCx(cx);
                twisterCircleList.get(index).setCy(cy);
                twisterCircleList.get(index).setRadius(circleRadius);
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        long startTime = System.nanoTime();

        drawCircles(canvas);

        long stopTime = System.nanoTime();
        long timeElapsed = (stopTime - startTime) / 1000000;
        if (timeElapsed > UPDATE_TIME) {
            Log.d("DEV", "Low framerate !");
            handler.post(onDrawRunnable);
        } else {
            handler.postDelayed(onDrawRunnable, UPDATE_TIME - timeElapsed);
        }
    }

    private void drawCircles(Canvas canvas) {
        twisterCircleList.forEach(twisterCircle -> twisterCircle.draw(canvas));
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN || event.getActionMasked() == MotionEvent.ACTION_POINTER_DOWN) {
            Log.d("DEV", String.format("onTouchEvent: Touch at %f %f", event.getX(), event.getY()));
            int index = event.getActionIndex();
            TwisterCircle circleTouched = getCircleTouched(event.getX(index), event.getY(index));
            if (circleTouched != null) {
                // Do something when circle is touched here
                circleTouched.setColor(Color.BLUE);
            }
            return true;
        }
        return false;
    }

    private TwisterCircle getCircleTouched(float x, float y) {
        for (TwisterCircle c : twisterCircleList) {
            if (TwisterCircle.isInsideTheCircle(c, x, y)) {
                return c;
            }
        }
        return null;
    }
}
