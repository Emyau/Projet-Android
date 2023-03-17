package com.example.twisterfinger.activities.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.twisterfinger.Wheel;
import com.example.twisterfinger.activities.views.objects.Couleur;
import com.example.twisterfinger.activities.views.objects.TwisterCircle;
import com.example.twisterfinger.engine.GameEngine;
import com.example.twisterfinger.engine.RandomGenerator;

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
    private final Wheel wheel;

    private float ambiantLight;

    SharedPreferences prefs;

    private final SensorEventListener listenerLight = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            ambiantLight = sensorEvent.values[0];
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {
            //not used
        }
    };
    private GameEngine engine;

    public GameView(Context context) {
        super(context);

        prefs = getContext().getSharedPreferences("appPrefs", Context.MODE_PRIVATE);

        twisterCircleList = new ArrayList<>();
        int nbElement = NB_COLLUMN * NB_ROW;
        for (int i = 0; i < nbElement; i++) {
            int col = i % NB_COLLUMN;
            if (col == 0) {
                twisterCircleList.add(new TwisterCircle(Couleur.ROUGE_F));
            } else if (col == 1) {
                twisterCircleList.add(new TwisterCircle(Couleur.VERT_F));
            } else if (col == 2) {
                twisterCircleList.add(new TwisterCircle(Couleur.BLEU_F));
            } else {
                twisterCircleList.add(new TwisterCircle(Couleur.VIOLET_F));
            }
        }

        onDrawRunnable = this::invalidate;
        handler = new Handler();
        engine = new GameEngine(context);
        RandomGenerator randomFinger = new RandomGenerator(context);
        wheel = new Wheel(context);

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        setupListener();

        int circleRadius = (int) (getWidth() * 0.07);

        for (int i = 0; i < NB_ROW; i++) {
            for (int j = 0; j < NB_COLLUMN; j++) {
                // 0.8 is the game size, we keep 20% for the margin
                int cx = (int) (j * ((getWidth() * 0.8) / (NB_COLLUMN - 1)) + getWidth() * 0.1);
                int cy = (int) (i * ((getHeight() * 0.8) / (NB_ROW)) + getHeight() * 0.1);

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

        float coefLumi = prefs.getFloat("coefLumi", 0);

        switch (engine.getState()) {
            case WHEEL:
                switch (wheel.getState()) {
                    case WAIT_TO_BLOW:
                    case SPINNING:
                        break;
                    case IDLE:
                        engine.setrCouleur(wheel.getCouleur());
                        engine.setRfinger(wheel.getFinger());
                        Log.d("DEV", String.format("Couleur %s, doigt %s", engine.getrCouleur().name(), engine.getRfinger().name()));
                        wheel.valueConsumed();
                        engine.nextState();
                        break;
                }
                break;
            case FINGER:
                break;
            case FREEZE:
                break;
            case GAME_OVER:
                Log.d("DEV", "onDraw: LOOSER");
                engine.gameover();
                break;
        }

        Paint p = new Paint();
        p.setColor(Color.WHITE);
        canvas.drawText(Float.toString(coefLumi), 50, 50, p);

        drawCircles(canvas, ambiantLight, coefLumi);
        wheel.draw(canvas);

        long stopTime = System.nanoTime();
        long timeElapsed = (stopTime - startTime) / 1000000;
        if (timeElapsed > UPDATE_TIME) {
            Log.d("DEV", "Low framerate !");
            handler.post(onDrawRunnable);
        } else {
            handler.postDelayed(onDrawRunnable, UPDATE_TIME - timeElapsed);
        }
    }

    private void drawCircles(Canvas canvas, float ambiantLight, float coefLumi) {
        twisterCircleList.forEach(twisterCircle -> twisterCircle.draw(canvas, ambiantLight, coefLumi));
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN || event.getActionMasked() == MotionEvent.ACTION_POINTER_DOWN) {
            switch (engine.getState()) {
                case WHEEL:
                    break;
                case FINGER:
                    int index = event.getActionIndex();
                    TwisterCircle circleTouched = getCircleTouched(event.getX(index), event.getY(index));
                    if (circleTouched != null) {
                        if (engine.checkGoodCircle(circleTouched)) {
                            wheel.trigger();
                        }
                    }
                    break;
                case FREEZE:
                    break;
                case GAME_OVER:
                    engine.gameover();
                    break;
            }
            return true;
        } else if (event.getAction() == MotionEvent.ACTION_UP || event.getActionMasked() == MotionEvent.ACTION_POINTER_UP) {
            switch (engine.getState()) {
                case WHEEL:
                    break;
                case FINGER:
                    int index = event.getActionIndex();
                    TwisterCircle circleTouched = getCircleTouched(event.getX(index), event.getY(index));
                    if (circleTouched != null) {
                        engine.checkAllowedActionUp(circleTouched);
                    }
                    break;
                case FREEZE:
                    break;
                case GAME_OVER:
                    break;
            }

        }
        return false;
    }

    private void setupListener() {
        SensorManager sm = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        Sensor sensorLight = sm.getDefaultSensor(Sensor.TYPE_LIGHT);
        sm.registerListener(listenerLight, sensorLight, SensorManager.SENSOR_DELAY_NORMAL);
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
