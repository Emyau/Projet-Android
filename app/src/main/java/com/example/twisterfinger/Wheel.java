package com.example.twisterfinger;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.widget.Switch;

import java.util.ArrayList;
import java.util.Random;

public class Wheel {
    private enum State {
        SPINNING,
        IDLE
    }

    private Integer index = 0;
    private Random random = new Random();
    private ArrayList<Integer> list;
    private final Integer MAXInt = 3;
    private final Integer x = 1000;
    private final Integer y = 0;
    private State state = State.IDLE;
    private MicroHandler microHandler;
    private Context context;
    private Paint paint;

    public Wheel(Context context){
        paint = new Paint();
        this.context = context;
    }

    public Integer draw(Canvas canvas){
        switch (state){
            case IDLE:
                paint.setColor(Color.RED);
                canvas.drawRect( x, y, x + 100, 100, paint);
                break;
            case SPINNING:
                for(int i = 0; i < MAXInt; i++){
                    index = random.nextInt(MAXInt);
                    paint.setColor(Color.BLUE);
                    canvas.drawRect( x, y, x + 100, 100, paint);
                }
                break;
        }
        return index;
    }

    public void trigger(){
        microHandler = new MicroHandler(context);
        Thread wheelThread = new Thread(() ->{
            microHandler.startRecording();
            while( state == State.IDLE ) {
                if( microHandler.getAudioLevel() >= -10 ){
                    state = State.SPINNING;
                }
            }
            microHandler.stopRecording();
            Thread.currentThread().interrupt();
        });
        wheelThread.start();
    }

    public Integer getNewMouve(MicroHandler microHandler){
        return index;
    }



}
